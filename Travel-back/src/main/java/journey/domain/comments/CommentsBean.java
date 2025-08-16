package journey.domain.comments;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import journey.domain.attractiontickets.AttractionTicketsBean;
import journey.domain.lodgings.LodgingsBean;
import journey.domain.lodgings.RoomTypesBean;
import journey.domain.plans.TripPlanBean;
import journey.domain.traffictickets.TrafficTicketsBean;
import journey.domain.users.AllUserBean;

/**
 * 留言評論實體類別
 * 
 * 功能說明：
 * - 支援多種評論目標：住宿、房型、交通票券、景點票券、行程規劃
 * - 支援主留言和回覆留言的層級結構
 * - 支援評分系統（僅主留言）
 * - 支援圖片上傳和按讚功能
 * - 支援軟刪除和檢舉機制
 * 
 * 權限規則：
 * - USER: 可以發表主留言和回覆留言
 * - VENDOR: 可以回覆留言（針對自己的住宿/票券）
 * - ADMIN: 可以回覆留言和管理所有留言
 * 
 * @author Journey Team
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "comments")
public class CommentsBean {
    /** 留言ID - 主鍵 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 評論目標：住宿 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lodging_id")
    @JsonBackReference
    private LodgingsBean lodging;

    /** 評論目標：房型 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id")
    @JsonBackReference
    private RoomTypesBean roomType;

    /** 評論目標：交通票券 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "traffic_ticket_id")
    @JsonBackReference
    private TrafficTicketsBean trafficTicketId;

    /** 評論目標：景點票券 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attraction_ticket_id")
    @JsonBackReference
    private AttractionTicketsBean attractionTicketId;

    /** 評論目標：行程規劃 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_plan_id")
    @JsonBackReference
    private TripPlanBean tripPlanId;

    /** 留言作者 - 必填 */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private AllUserBean user;

    /** 留言內容 - 必填，最大500字 */
    @Column(name = "content", nullable = false, length = 500)
    private String content;

    /** 父留言（用於回覆功能） */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    @JsonIgnoreProperties({ "parentComment", "commentImages", "commentFeedbacks", "commentReports" })
    private CommentsBean parentComment;

    /** 評分（1-5分，僅主留言有評分） */
    @Column(name = "rating")
    private Byte rating;

    /** 是否已驗證（管理員驗證） */
    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    /** 是否啟用 */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    /** 刪除時間（軟刪除） */
    @Column(name = "deleted_time")
    private LocalDateTime deletedTime;

    /** 刪除狀態：1=正常，0=已刪除 */
    @Column(name = "delete_status", nullable = false)
    private Integer deleteStatus = 1;

    /** 建立時間 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** 更新時間 */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** 留言圖片列表 */
    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("sortOrder ASC") // 按照 sortOrder 升序排列
    @JsonManagedReference
    private List<CommentImagesBean> commentImages;

    /** 按讚記錄列表 */
    @OneToMany(mappedBy = "comment")
    @JsonManagedReference
    private Set<CommentFeedbacksBean> commentFeedbacks;

    /** 檢舉記錄列表 */
    @OneToMany(mappedBy = "comment")
    @JsonManagedReference
    private Set<CommentReportsBean> commentReports;

    /**
     * 預設建構函數
     */
    public CommentsBean() {
    }

    /**
     * 完整參數建構函數
     * 
     * @param id                 留言ID
     * @param lodging            住宿評論目標
     * @param roomType           房型評論目標
     * @param trafficTicketId    交通票券評論目標
     * @param attractionTicketId 景點票券評論目標
     * @param tripPlanId         行程規劃評論目標
     * @param user               留言作者
     * @param content            留言內容
     * @param parentComment      父留言
     * @param rating             評分
     * @param isVerified         是否已驗證
     * @param isActive           是否啟用
     * @param deletedTime        刪除時間
     * @param deleteStatus       刪除狀態
     * @param createdAt          建立時間
     * @param updatedAt          更新時間
     * @param commentImages      留言圖片列表
     * @param commentFeedbacks   按讚記錄列表
     * @param commentReports     檢舉記錄列表
     */
    public CommentsBean(Integer id, LodgingsBean lodging, RoomTypesBean roomType, TrafficTicketsBean trafficTicketId,
            AttractionTicketsBean attractionTicketId, TripPlanBean tripPlanId, AllUserBean user, String content,
            CommentsBean parentComment,
            Byte rating, Boolean isVerified, Boolean isActive, LocalDateTime deletedTime, Integer deleteStatus,
            LocalDateTime createdAt, LocalDateTime updatedAt, List<CommentImagesBean> commentImages,
            Set<CommentFeedbacksBean> commentFeedbacks, Set<CommentReportsBean> commentReports) {
        this.id = id;
        this.lodging = lodging;
        this.roomType = roomType;
        this.trafficTicketId = trafficTicketId;
        this.attractionTicketId = attractionTicketId;
        this.tripPlanId = tripPlanId;
        this.user = user;
        this.content = content;
        this.parentComment = parentComment;
        this.rating = rating;
        this.isVerified = isVerified;
        this.isActive = isActive;
        this.deletedTime = deletedTime;
        this.deleteStatus = deleteStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.commentImages = commentImages;
        this.commentFeedbacks = commentFeedbacks;
        this.commentReports = commentReports;
    }

    // Getters and setters for all fields
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LodgingsBean getLodging() {
        return lodging;
    }

    public void setLodging(LodgingsBean lodging) {
        this.lodging = lodging;
    }

    public RoomTypesBean getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypesBean roomType) {
        this.roomType = roomType;
    }

    public TrafficTicketsBean getTrafficTicketId() {
        return trafficTicketId;
    }

    public void setTrafficTicketId(TrafficTicketsBean trafficTicketId) {
        this.trafficTicketId = trafficTicketId;
    }

    public AttractionTicketsBean getAttractionTicketId() {
        return attractionTicketId;
    }

    public void setAttractionTicketId(AttractionTicketsBean attractionTicketId) {
        this.attractionTicketId = attractionTicketId;
    }

    public TripPlanBean getTripPlanId() {
        return tripPlanId;
    }

    public void setTripPlanId(TripPlanBean tripPlanId) {
        this.tripPlanId = tripPlanId;
    }

    public AllUserBean getUser() {
        return user;
    }

    public void setUser(AllUserBean user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CommentsBean getParentComment() {
        return parentComment;
    }

    public void setParentComment(CommentsBean parentComment) {
        this.parentComment = parentComment;
    }

    public Byte getRating() {
        return rating;
    }

    public void setRating(Byte rating) {
        this.rating = rating;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getDeletedTime() {
        return deletedTime;
    }

    public void setDeletedTime(LocalDateTime deletedTime) {
        this.deletedTime = deletedTime;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<CommentImagesBean> getCommentImages() {
        return commentImages;
    }

    public void setCommentImages(List<CommentImagesBean> commentImages) {
        this.commentImages = commentImages;
    }

    public Set<CommentFeedbacksBean> getCommentFeedbacks() {
        return commentFeedbacks;
    }

    public void setCommentFeedbacks(Set<CommentFeedbacksBean> commentFeedbacks) {
        this.commentFeedbacks = commentFeedbacks;
    }

    public Set<CommentReportsBean> getCommentReports() {
        return commentReports;
    }

    public void setCommentReports(Set<CommentReportsBean> commentReports) {
        this.commentReports = commentReports;
    }

    /**
     * 建立前自動設定時間戳記
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null)
            this.createdAt = now;
        if (this.updatedAt == null)
            this.updatedAt = now;
    }

    /**
     * 更新前自動更新時間戳記
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}