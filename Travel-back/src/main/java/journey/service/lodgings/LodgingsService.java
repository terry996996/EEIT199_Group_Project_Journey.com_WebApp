package journey.service.lodgings;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import journey.domain.lodgings.RoomTypesBean;
import journey.dto.lodgings.BedTypeDTO;
import journey.dto.lodgings.FacilityDTO;
import journey.dto.lodgings.FilterOptionsDTO;
import journey.dto.lodgings.ImageDTO;
import journey.dto.lodgings.LodgingHotSearchResponseDTO;
import journey.dto.lodgings.LodgingRoomTypeDetailDTO;
import journey.dto.lodgings.LodgingSearchRequestDTO;
import journey.dto.lodgings.LodgingSearchResponseDTO;
import journey.dto.lodgings.LodgingTypeDTO;
import journey.dto.lodgings.SearchResponseDTO;
import journey.dto.lodgings.SimpleImageDTO;
import journey.repository.lodgings.LodgingsRepository;
import journey.repository.lodgings.RoomTypesRepository;

/**
 * 住宿搜尋服務類
 * 提供一般用戶的住宿搜尋功能，包含搜尋條件篩選、熱門住宿查詢等。
 * 支援按城市、日期、人數等條件搜尋可用房型，並提供篩選選項統計。
 * 
 * @author FAN
 * @since 250704
 */
@Service
public class LodgingsService {

    @Autowired
    private LodgingsRepository lodgingsRepository;

    @Autowired
    private RoomTypesRepository roomTypesRepository;

    @Autowired
    private UnifiedRatingService unifiedRatingService;

    @Autowired
    private journey.service.comments.CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    // private final ObjectMapper objectMapper = new ObjectMapper();

    /* ---------- 熱門榜 ---------- */

    /**
     * 獲取熱門住宿列表
     * 根據近1個月評分和評論數量排序，返回最受歡迎的住宿。
     * 使用 Spring Cache 進行快取，提高查詢效能。
     * 
     * @param topN 返回前 N 名的住宿數量
     * @return List<LodgingHotSearchResponseDTO> 熱門住宿列表
     */
    @Cacheable(value = "hotLodgings", key = "#topN")
    public List<LodgingHotSearchResponseDTO> getHotLodgings(int topN) {
        return lodgingsRepository.findTopRatedLodgings(topN);
    }

    /* ---------- 搜尋功能 ---------- */

    /**
     * 搜尋住宿並提供篩選選項
     * 根據城市、日期、人數等條件搜尋可用的住宿和房型，並統計篩選選項。
     * 
     * @param req 搜尋請求 DTO，包含城市名稱、入住日期、退房日期、人數等條件
     * @return SearchResponseDTO 包含搜尋結果和篩選選項
     */
    public SearchResponseDTO searchLodgingsWithFilterOptions(LodgingSearchRequestDTO req) {
        LocalDate checkIn = req.getCheckInDate();
        LocalDate checkOut = req.getCheckOutDate();
        List<Object[]> results = lodgingsRepository.searchAvailableRoomTypes(
                req.getCityName(),
                checkIn,
                checkOut,
                req.getGuestCount());
        List<LodgingSearchResponseDTO> lodgingResults = results.stream()
                .map(this::mapToLodgingSearchResponseDTO)
                .collect(Collectors.toList());
        // 彙整 filter options
        var bedTypeSet = new java.util.LinkedHashSet<BedTypeDTO>();
        var facilitySet = new java.util.LinkedHashSet<FacilityDTO>();
        var lodgingTypeSet = new java.util.LinkedHashSet<LodgingTypeDTO>();
        for (LodgingSearchResponseDTO dto : lodgingResults) {
            if (dto.bedTypes() != null)
                bedTypeSet.addAll(dto.bedTypes());
            if (dto.facilities() != null)
                facilitySet.addAll(dto.facilities());
            if (dto.lodgingTypeId() != null && dto.lodgingType() != null) {
                lodgingTypeSet.add(new LodgingTypeDTO(dto.lodgingTypeId(), dto.lodgingType()));
            }
        }
        FilterOptionsDTO filterOptions = new FilterOptionsDTO(
                bedTypeSet.stream().toList(),
                facilitySet.stream().toList(),
                lodgingTypeSet.stream().toList());
        // 統計每種 lodgingTypeId 出現次數
        var lodgingTypeCount = new java.util.LinkedHashMap<Integer, Integer>();
        var lodgingTypeNameMap = new java.util.LinkedHashMap<Integer, String>();
        for (LodgingSearchResponseDTO dto : lodgingResults) {
            if (dto.lodgingTypeId() != null && dto.lodgingType() != null) {
                lodgingTypeCount.put(dto.lodgingTypeId(), lodgingTypeCount.getOrDefault(dto.lodgingTypeId(), 0) + 1);
                lodgingTypeNameMap.put(dto.lodgingTypeId(), dto.lodgingType());
            }
        }

        // 統計每種床型出現次數
        var bedTypeCount = new java.util.LinkedHashMap<String, Integer>();
        for (LodgingSearchResponseDTO dto : lodgingResults) {
            if (dto.bedTypes() != null) {
                for (BedTypeDTO bedType : dto.bedTypes()) {
                    String bedTypeName = bedType.name();
                    bedTypeCount.put(bedTypeName, bedTypeCount.getOrDefault(bedTypeName, 0) + 1);
                }
            }
        }

        // 統計每種設施出現次數
        var facilityCount = new java.util.LinkedHashMap<String, Integer>();
        for (LodgingSearchResponseDTO dto : lodgingResults) {
            if (dto.facilities() != null) {
                for (FacilityDTO facility : dto.facilities()) {
                    String facilityName = facility.name();
                    facilityCount.put(facilityName, facilityCount.getOrDefault(facilityName, 0) + 1);
                }
            }
        }

        // 輸出除錯資訊
        System.out.println("=== 符合條件總共 " + lodgingResults.size() + " 間 ===");
        for (var entry : lodgingTypeCount.entrySet()) {
            Integer id = entry.getKey();
            String name = lodgingTypeNameMap.get(id);
            Integer count = entry.getValue();
            System.out.println("lodgingTypeId=" + id + " (" + name + ") : " + count + " 間");
        }

        // 輸出床型統計資訊
        System.out.println("=== 床型統計 ===");
        for (var entry : bedTypeCount.entrySet()) {
            String bedTypeName = entry.getKey();
            Integer count = entry.getValue();
            System.out.println("床型: " + bedTypeName + " : " + count + " 間");
        }

        // 輸出設施統計資訊
        System.out.println("=== 設施統計 ===");
        for (var entry : facilityCount.entrySet()) {
            String facilityName = entry.getKey();
            Integer count = entry.getValue();
            System.out.println("設施: " + facilityName + " : " + count + " 間");
        }

        return new SearchResponseDTO(lodgingResults, filterOptions);
    }

    /**
     * 將資料庫查詢結果映射為 LodgingSearchResponseDTO
     * 將 Object[] 陣列轉換為結構化的 DTO 物件。
     * 
     * @param row 資料庫查詢結果陣列
     * @return LodgingSearchResponseDTO 住宿搜尋回應 DTO
     */
    private LodgingSearchResponseDTO mapToLodgingSearchResponseDTO(Object[] row) {
        return new LodgingSearchResponseDTO(
                (Integer) row[0], // lodgingId
                (String) row[1], // lodgingName
                row[2] != null ? ((BigDecimal) row[2]).doubleValue() : 0.0, // avgRating
                (Long) row[3], // reviewCount
                (Integer) row[4], // roomTypeId
                (String) row[5], // roomTypeName
                (String) row[6], // roomTypeDescription
                (Integer) row[7], // maxGuests
                row[8] != null ? new BigDecimal(((Integer) row[8]).toString()) : BigDecimal.ZERO, // pricePerNight
                (String) row[9], // imageUrl
                (Integer) row[10], // lodgingTypeId
                (String) row[11], // lodgingType
                parseJsonToBedTypes((String) row[12]), // bedTypes
                parseJsonToFacilities((String) row[13]) // facilities
        );
    }

    /**
     * 解析 JSON 字串為床型列表
     * 將資料庫中的 JSON 格式床型資料轉換為 BedTypeDTO 列表。
     * 
     * @param json JSON 格式的床型資料字串
     * @return List<BedTypeDTO> 床型 DTO 列表，解析失敗時返回空列表
     */
    private List<BedTypeDTO> parseJsonToBedTypes(String json) {
        try {
            if (json == null || json.trim().isEmpty() || json.equals("[]")) {
                return List.of();
            }
            return objectMapper.readValue(json, new TypeReference<List<BedTypeDTO>>() {
            });
        } catch (Exception e) {
            return List.of();
        }
    }

    /**
     * 解析 JSON 字串為設施列表
     * 將資料庫中的 JSON 格式設施資料轉換為 FacilityDTO 列表。
     * 
     * @param json JSON 格式的設施資料字串
     * @return List<FacilityDTO> 設施 DTO 列表，解析失敗時返回空列表
     */
    private List<FacilityDTO> parseJsonToFacilities(String json) {
        try {
            if (json == null || json.trim().isEmpty() || json.equals("[]")) {
                return List.of();
            }
            return objectMapper.readValue(json, new TypeReference<List<FacilityDTO>>() {
            });
        } catch (Exception e) {
            return List.of();
        }
    }

    /* ---------- 旅館房型詳細資訊 ---------- */

    /**
     * 獲取旅館和房型的詳細資訊
     * 包含旅館基本資訊、房型詳細資訊、圖片、床型配置、設施配置、可用性等。
     * 
     * @param lodgingId  旅館ID
     * @param roomTypeId 房型ID
     * @return LodgingRoomTypeDetailDTO 旅館房型詳細資訊
     * @throws IllegalArgumentException 當旅館或房型不存在時拋出
     */
    public LodgingRoomTypeDetailDTO getLodgingRoomTypeDetail(Integer lodgingId, Integer roomTypeId) {
        System.out.println("=== 開始處理 lodgingId=" + lodgingId + ", roomTypeId=" + roomTypeId);

        // 查詢房型詳細資訊（包含關聯資料）
        RoomTypesBean roomType = roomTypesRepository.findByLodgingIdAndRoomTypeIdWithDetails(lodgingId, roomTypeId)
                .orElseThrow(() -> new IllegalArgumentException("房型不存在或已停用"));
        System.out.println("=== 房型查詢成功: " + roomType.getName());

        // 使用統一的評分計算
        UnifiedRatingService.RatingStats ratingStats = unifiedRatingService.calculateUnifiedRating(lodgingId);
        System.out.println("=== 統一評分計算成功: avgRating=" + ratingStats.getAvgRating() + ", reviewCount="
                + ratingStats.getReviewCount());

        System.out.println("=== 開始處理圖片資訊");
        // 轉換住宿圖片資訊（image_type = 'lodging'）
        List<SimpleImageDTO> lodgingImages = roomType.getLodging().getLodgingImages().stream()
                .filter(img -> img.getIsActive() && img.getDeleteStatus() == 1 && "lodging".equals(img.getImageType()))
                .map(this::mapToSimpleImageDTO)
                .sorted((a, b) -> Integer.compare(a.getSortOrder(), b.getSortOrder()))
                .collect(Collectors.toList());
        System.out.println("=== 住宿圖片處理完成，數量: " + lodgingImages.size());

        // 轉換房型圖片資訊（image_type = 'room'）
        List<SimpleImageDTO> roomTypeImages = roomType.getLodgingImages().stream()
                .filter(img -> img.getIsActive() && img.getDeleteStatus() == 1 && "room".equals(img.getImageType()))
                .map(this::mapToSimpleImageDTO)
                .sorted((a, b) -> Integer.compare(a.getSortOrder(), b.getSortOrder()))
                .collect(Collectors.toList());
        System.out.println("=== 房型圖片處理完成，數量: " + roomTypeImages.size());

        System.out.println("=== 開始處理床型配置");
        // 轉換床型配置
        List<BedTypeDTO> bedTypes = roomType.getRoomTypeBeds().stream()
                .map(rtb -> new BedTypeDTO(rtb.getBedType().getId(), rtb.getBedType().getName(), rtb.getQuantity()))
                .collect(Collectors.toList());
        System.out.println("=== 床型配置處理完成，數量: " + bedTypes.size());

        System.out.println("=== 開始處理設施配置");
        // 轉換設施配置
        List<FacilityDTO> facilities = roomType.getRoomFacilities().stream()
                .map(rf -> new FacilityDTO(rf.getFacility().getId(), rf.getFacility().getFacilityName()))
                .collect(Collectors.toList());
        System.out.println("=== 設施配置處理完成，數量: " + facilities.size());

        System.out.println("=== 開始處理留言資訊");
        // 獲取該住宿的所有留言（包含已刪除的，避免主留言刪除時回覆一起消失）
        // 不包含按讚狀態，因為這裡只需要顯示評論內容
        List<journey.dto.Comments.CommentResponseDTO> comments = commentService.getCommentsByTarget("LODGING",
                lodgingId, true, false, null);
        System.out.println("=== 留言處理完成，數量: " + comments.size());

        System.out.println("=== 開始構建回應DTO");
        // 構建回應DTO
        return new LodgingRoomTypeDetailDTO(
                roomType.getLodging().getId(),
                roomType.getLodging().getLodgingName(),
                roomType.getLodging().getDescription(),
                roomType.getLodging().getAddress(),
                roomType.getLodging().getLatitude(),
                roomType.getLodging().getLongitude(),
                roomType.getLodging().getLodgingTel(),
                roomType.getLodging().getEmail(),
                roomType.getLodging().getLodgingType().getId(),
                roomType.getLodging().getLodgingType().getTypeText(),
                roomType.getLodging().getCity().getId(),
                roomType.getLodging().getCity().getNameZh() != null ? roomType.getLodging().getCity().getNameZh()
                        : roomType.getLodging().getCity().getName(),
                roomType.getLodging().getCountry().getId(),
                roomType.getLodging().getCountry().getNameZh() != null ? roomType.getLodging().getCountry().getNameZh()
                        : roomType.getLodging().getCountry().getName(),
                roomType.getId(),
                roomType.getName(),
                roomType.getDescription(),
                roomType.getPricePerNight(),
                roomType.getMaxGuests(),
                ratingStats.getAvgRating(),
                ratingStats.getReviewCount(),
                lodgingImages,
                roomTypeImages,
                bedTypes,
                facilities,
                comments);
    }

    /**
     * 將圖片實體映射為DTO
     */
    private ImageDTO mapToImageDTO(journey.domain.lodgings.LodgingImagesBean image) {
        return new ImageDTO(
                image.getImageId(),
                image.getImageType(),
                image.getLodging() != null ? image.getLodging().getId() : null,
                image.getRoomType() != null ? image.getRoomType().getId() : null,
                image.getImageUrl(),
                image.getMimeType(),
                image.getSortOrder(),
                image.getIsActive(),
                image.getUploadedAt());
    }

    /**
     * 將圖片實體映射為簡化DTO
     * 只包含前端需要的圖片資訊
     */
    private SimpleImageDTO mapToSimpleImageDTO(journey.domain.lodgings.LodgingImagesBean image) {
        return new SimpleImageDTO(
                image.getImageType(),
                image.getImageUrl(),
                image.getSortOrder());
    }

    public RoomTypesBean findRoomTypeById(Integer id) {
        if (id != null) {
            Optional<RoomTypesBean> opt = roomTypesRepository.findById(id);
            if (opt != null && opt.isPresent()) {
                return opt.get();
            }
        }
        return null;
    }
}
