package journey.service.comments;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import journey.domain.comments.CommentImagesBean;
import journey.domain.comments.CommentReasonsBean;
import journey.domain.comments.CommentReportsBean;
import journey.domain.comments.CommentsBean;
import journey.domain.users.AdminBean;
import journey.dto.AuthorInfoDTO;
import journey.dto.Comments.CommentReportRequestDTO;
import journey.dto.Comments.CommentReportResponseDTO;
import journey.dto.Comments.CommentReportUpdateDTO;
import journey.dto.Comments.CommentResponseDTO;
import journey.enums.CommentReportStatusEnum;
import journey.enums.UserTypeEnum;
import journey.repository.comments.CommentReasonsRepository;
import journey.repository.comments.CommentReportsRepository;
import journey.repository.comments.CommentsRepository;
import journey.repository.users.AdminRepository;
import journey.repository.users.AllUserRepository;

@Service
public class CommentReportsService {
    private final CommentReportsRepository reportRepo;
    private final CommentsRepository commentRepo;
    private final CommentReasonsRepository reasonRepo;
    private final AllUserRepository userRepo;
    private final AdminRepository adminRepo;
    private final CommentService commentService;

    public CommentReportsService(CommentReportsRepository reportRepo,
            CommentsRepository commentRepo,
            CommentReasonsRepository reasonRepo,
            AllUserRepository userRepo,
            AdminRepository adminRepo,
            CommentService commentService) {
        this.reportRepo = reportRepo;
        this.commentRepo = commentRepo;
        this.reasonRepo = reasonRepo;
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
        this.commentService = commentService;
    }

    @Transactional
    public void create(CommentReportRequestDTO req, Integer userId, UserTypeEnum role) {

        /* ─── 2. 取得留言與檢查作者 ─── */
        CommentsBean comment = commentRepo.findById(req.commentId())
                .orElseThrow(() -> new IllegalArgumentException("留言不存在"));

        if (comment.getUser().getId().equals(userId)) {
            System.out.println("不能檢舉自己留言");
            throw new IllegalStateException("不能檢舉自己留言");
        }

        /* ─── 3. 檢查是否重複檢舉 ─── */
        if (reportRepo.existsByCommentIdAndCreatedById(req.commentId(), userId)) {
            System.out.println("您已檢舉過此留言");
            throw new IllegalStateException("您已檢舉過此留言");
        }

        /* ─── 4. 建立檢舉 ─── */
        Integer reasonId = req.reasonIds().get(0);
        CommentReasonsBean reason = reasonRepo.getReferenceById(reasonId);

        CommentReportsBean report = new CommentReportsBean();
        report.setComment(comment);
        report.setReason(reason);
        report.setCreatedBy(userRepo.getReferenceById(userId));
        // status 預設 PENDING, createdAt 在 @PrePersist
        reportRepo.save(report);
    }

    /**
     * 管理員查詢檢舉列表
     * 
     * @param status 狀態篩選（可選）
     * @param page   頁碼
     * @param size   每頁大小
     * @return 檢舉列表
     */
    public List<CommentReportResponseDTO> getReportsForAdmin(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentReportsBean> reportsPage;

        if (status != null && !status.trim().isEmpty()) {
            try {
                // 使用 enum 的 fromString 方法，支援大小寫不敏感轉換
                CommentReportStatusEnum statusEnum = CommentReportStatusEnum.fromString(status);
                reportsPage = reportRepo.findByStatusOrderByCreatedAtDesc(statusEnum, pageable);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("無效的狀態值: " + status);
            }
        } else {
            reportsPage = reportRepo.findAllByOrderByCreatedAtDesc(pageable);
        }

        return reportsPage.getContent().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * 管理員處理檢舉
     * 
     * @param reportId  檢舉 ID
     * @param updateDto 更新資料
     * @param adminId   管理員 ID
     */
    @Transactional
    public void processReport(Integer reportId, CommentReportUpdateDTO updateDto, Integer adminId) {
        System.out.println("🔍 開始處理檢舉 - 檢舉ID: " + reportId + ", 管理員ID: " + adminId);

        // 查找檢舉記錄
        CommentReportsBean report = reportRepo.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("檢舉記錄不存在"));

        // 檢查檢舉狀態
        if (report.getStatus() != CommentReportStatusEnum.PENDING) {
            throw new IllegalArgumentException("只能處理待處理的檢舉");
        }

        // 驗證狀態值
        CommentReportStatusEnum newStatus;
        try {
            // 使用 enum 的 fromString 方法，支援大小寫不敏感轉換
            newStatus = CommentReportStatusEnum.fromString(updateDto.status());
            if (newStatus == CommentReportStatusEnum.PENDING) {
                throw new IllegalArgumentException("不能將狀態設為待處理");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("無效的狀態值: " + updateDto.status());
        }

        // 獲取管理員資訊
        System.out.println("🔍 查找管理員 - ID: " + adminId);
        AdminBean admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("管理員不存在 (ID: " + adminId + ")"));
        System.out.println("✅ 找到管理員: " + admin.getAdminId() + " - " + admin.getRole());

        // 更新檢舉狀態
        report.setStatus(newStatus);
        report.setReviewedBy(admin);
        report.setReviewedAt(LocalDateTime.now());
        report.setNote(updateDto.note());

        // 儲存檢舉更新
        reportRepo.save(report);

        // 處理被檢舉的留言
        CommentsBean reportedComment = report.getComment();
        System.out.println("🔍 處理被檢舉留言 - ID: " + reportedComment.getId() + ", 內容: " + reportedComment.getContent());

        if (newStatus == CommentReportStatusEnum.RESOLVED) {
            // 確認檢舉：系統下架留言
            reportedComment.setIsActive(false);
            reportedComment.setDeleteStatus(1); // 1 = 系統下架
            reportedComment.setDeletedTime(LocalDateTime.now());
            reportedComment.setUpdatedAt(LocalDateTime.now());

            System.out.println("🚫 留言已系統下架 - ID: " + reportedComment.getId());

            // 同步處理留言圖片
            if (reportedComment.getCommentImages() != null && !reportedComment.getCommentImages().isEmpty()) {
                System.out.println("🖼️ 同步處理留言圖片，共 " + reportedComment.getCommentImages().size() + " 張");

                for (CommentImagesBean image : reportedComment.getCommentImages()) {
                    if (image.getIsActive() && image.getDeleteStatus() == 1) {
                        // 系統下架圖片
                        image.setIsActive(false);
                        image.setDeleteStatus(1); // 1 = 系統下架
                        image.setDeletedTime(LocalDateTime.now());

                        System.out.println("🚫 圖片已系統下架 - ID: " + image.getId() + ", URL: " + image.getImageUrl());
                    }
                }
            }

        } else if (newStatus == CommentReportStatusEnum.REJECTED) {
            // 駁回檢舉：保持留言正常狀態
            System.out.println("✅ 檢舉已駁回，留言保持正常狀態 - ID: " + reportedComment.getId());
        }

        // 儲存留言更新（包含圖片更新）
        commentRepo.save(reportedComment);

        System.out.println("✅ 檢舉處理完成 - ID: " + reportId + ", 新狀態: " + newStatus + ", 留言ID: " + reportedComment.getId());
    }

    /**
     * 將 CommentReportsBean 轉換為 CommentReportResponseDTO
     */
    private CommentReportResponseDTO convertToResponseDTO(CommentReportsBean report) {
        // 轉換檢舉者資訊
        AuthorInfoDTO reporter = new AuthorInfoDTO();
        reporter.setId(report.getCreatedBy().getId());
        reporter.setUsername(report.getCreatedBy().getUsername());

        // 轉換被檢舉的留言資訊
        CommentResponseDTO reportedComment = commentService.getCommentById(report.getComment().getId());

        // 轉換處理者資訊（如果有）
        AuthorInfoDTO reviewer = null;
        if (report.getReviewedBy() != null) {
            reviewer = new AuthorInfoDTO();
            reviewer.setId(report.getReviewedBy().getAdminId());
            reviewer.setUsername(report.getReviewedBy().getAllUserBean().getUsername());
        }

        return new CommentReportResponseDTO(
                report.getId(),
                report.getStatus().name(),
                report.getStatus().getLabel(),
                report.getCreatedAt(),
                report.getReviewedAt(),
                report.getNote(),
                report.getReason().getId(),
                report.getReason().getReasonText(),
                reporter,
                reportedComment,
                reviewer);
    }
}