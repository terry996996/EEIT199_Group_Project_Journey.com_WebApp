package journey.service.lodgings;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import journey.domain.lodgings.BedTypesBean;
import journey.domain.lodgings.FacilitiesBean;
import journey.domain.lodgings.LodgingsBean;
import journey.domain.lodgings.RoomFacilitiesBean;
import journey.domain.lodgings.RoomTypeBedsBean;
import journey.domain.lodgings.RoomTypesBean;
import journey.domain.users.VendorBean;
import journey.dto.BaseImageDTO;
import journey.dto.lodgings.BedTypeDTO;
import journey.dto.lodgings.FacilityDTO;
import journey.dto.lodgings.ImageDTO;
import journey.dto.lodgings.RoomTypeCreateRequestDTO;
import journey.dto.lodgings.RoomTypeCreateResponseDTO;
import journey.dto.lodgings.RoomTypeDetailDTO;
import journey.dto.lodgings.RoomTypeListDTO;
import journey.dto.lodgings.RoomTypeUpdateRequestDTO;
import journey.dto.lodgings.SimpleImageDTO;
import journey.repository.lodgings.BedTypesRepository;
import journey.repository.lodgings.FacilitiesRepository;
import journey.repository.lodgings.LodgingsRepository;
import journey.repository.lodgings.RoomFacilitiesRepository;
import journey.repository.lodgings.RoomTypeBedsRepository;
import journey.repository.lodgings.RoomTypesRepository;
import journey.repository.users.VendorRepository;
import journey.service.UnifiedImageService;

/**
 * 商家房型管理服務
 * 提供商家對房型的 CRUD 操作，包含圖片管理。
 * 
 * 功能說明：
 * - 創建、查詢、更新、刪除房型
 * - 支援床型和設施管理
 * - 整合統一圖片服務
 * - 支援軟刪除機制
 * 
 * @author Journey Team
 * @version 2.0 - 整合 UnifiedImageService
 * @since 2025-07-08
 */
@Service
public class VendorRoomTypeService {

        @Autowired
        private VendorRepository vendorRepository;

        @Autowired
        private RoomTypesRepository roomTypesRepository;

        @Autowired
        private LodgingsRepository lodgingsRepository;

        @Autowired
        private RoomTypeBedsRepository roomTypeBedsRepository;

        @Autowired
        private RoomFacilitiesRepository roomFacilitiesRepository;

        @Autowired
        private BedTypesRepository bedTypesRepository;

        @Autowired
        private FacilitiesRepository facilitiesRepository;

        @Autowired
        private UnifiedImageService unifiedImageService;

        /**
         * 創建房型
         * 根據請求 DTO 創建新的房型，包含基本資料、床型、設施和圖片管理。
         * 
         * @param request 房型創建請求 DTO
         * @return RoomTypeCreateResponseDTO 創建成功的房型回應 DTO
         * @throws IllegalArgumentException 當關聯資料不存在時拋出
         */
        @Transactional
        public RoomTypeCreateResponseDTO createRoomType(RoomTypeCreateRequestDTO request) {
                // TODO: 暫時使用固定的 vendor_id，後續再處理多人合作程式
                Integer vendorId = 3;

                // 驗證關聯資料
                LodgingsBean lodging = lodgingsRepository.findById(request.getLodgingId())
                                .orElseThrow(() -> new IllegalArgumentException("住宿不存在"));

                VendorBean vendor = vendorRepository.findById(vendorId)
                                .orElseThrow(() -> new IllegalArgumentException("商家不存在"));

                // 創建房型實體
                RoomTypesBean roomType = new RoomTypesBean();
                roomType.setName(request.getRoomTypeName());
                roomType.setDescription(request.getDescription());
                roomType.setPricePerNight(request.getPricePerNight());
                roomType.setMaxGuests(request.getMaxGuests());
                roomType.setIsActive(true);
                roomType.setDeleteStatus(1);
                roomType.setLodging(lodging);
                // roomType.setCreatedBy(vendor); // 已移除 created_by 欄位
                roomType.setUpdatedBy(vendor);

                // 儲存房型
                RoomTypesBean savedRoomType = roomTypesRepository.save(roomType);

                // 批次儲存圖片 - 使用 UnifiedImageService
                List<BaseImageDTO> savedImages = new ArrayList<>();
                if (request.getImages() != null && !request.getImages().isEmpty()) {
                        // 轉換為 BaseImageDTO
                        List<BaseImageDTO> baseImageDtos = request.getImages().stream()
                                        .map(imageDto -> {
                                                BaseImageDTO baseDto = new BaseImageDTO();
                                                baseDto.setImageId(imageDto.getImageId());
                                                baseDto.setImageType(imageDto.getImageType());
                                                baseDto.setTargetId(savedRoomType.getId());
                                                baseDto.setTargetType("room_type");
                                                baseDto.setImageUrl(imageDto.getImageUrl());
                                                baseDto.setMimeType(imageDto.getMimeType());
                                                baseDto.setSortOrder(imageDto.getSortOrder());
                                                baseDto.setIsActive(imageDto.getIsActive());
                                                // 向後相容欄位
                                                baseDto.setLodgingId(request.getLodgingId());
                                                baseDto.setRoomTypeId(savedRoomType.getId());
                                                return baseDto;
                                        })
                                        .collect(Collectors.toList());

                        // 驗證圖片數量
                        unifiedImageService.validateImageCount(baseImageDtos, 5, "room_type");

                        // 使用 UnifiedImageService 儲存圖片
                        savedImages = unifiedImageService.saveImages(baseImageDtos, "room_type");
                }

                return new RoomTypeCreateResponseDTO(
                                savedRoomType.getId(),
                                savedRoomType.getCreatedAt().toString(),
                                savedRoomType.getUpdatedAt().toString(),
                                savedRoomType.getName(),
                                savedRoomType.getDescription(),
                                savedRoomType.getPricePerNight(),
                                savedRoomType.getMaxGuests(),
                                savedRoomType.getIsActive(),
                                request.getLodgingId(),
                                lodging.getLodgingName(),
                                vendor.getId(),
                                vendor.getVendorName(),
                                vendor.getId(),
                                vendor.getVendorName(),
                                request.getBeds(),
                                request.getFacilityIds(),
                                // 轉換回 ImageDTO 以保持向後相容
                                savedImages.stream()
                                                .map(baseDto -> {
                                                        ImageDTO imageDto = new ImageDTO();
                                                        imageDto.setImageId(baseDto.getImageId());
                                                        imageDto.setImageType(baseDto.getImageType());
                                                        imageDto.setLodgingId(baseDto.getLodgingId());
                                                        imageDto.setRoomTypeId(baseDto.getRoomTypeId());
                                                        imageDto.setImageUrl(baseDto.getImageUrl());
                                                        imageDto.setMimeType(baseDto.getMimeType());
                                                        imageDto.setSortOrder(baseDto.getSortOrder());
                                                        imageDto.setIsActive(baseDto.getIsActive());
                                                        imageDto.setUploadedAt(baseDto.getUploadedAt());
                                                        return imageDto;
                                                })
                                                .collect(Collectors.toList()));
        }

        /**
         * 查詢飯店全部房型簡歷
         * 根據住宿ID查詢該住宿的所有房型基本資訊。
         * 
         * @param lodgingId 住宿ID
         * @return List<RoomTypeListDTO> 房型列表
         * @throws IllegalArgumentException 當住宿不存在時拋出
         */
        public List<RoomTypeListDTO> getRoomTypes(Integer lodgingId) {
                // 驗證住宿是否存在
                LodgingsBean lodging = lodgingsRepository.findById(lodgingId)
                                .orElseThrow(() -> new IllegalArgumentException("住宿不存在"));

                List<RoomTypesBean> roomTypes = roomTypesRepository.findByLodgingId(lodgingId);

                return roomTypes.stream()
                                .map(roomType -> {
                                        // 轉換圖片為 SimpleImageDTO
                                        List<SimpleImageDTO> images = roomType.getLodgingImages().stream()
                                                        .filter(img -> img.getDeleteStatus() == 1)
                                                        .map(img -> new SimpleImageDTO(
                                                                        img.getImageType(),
                                                                        img.getImageUrl(),
                                                                        img.getSortOrder()))
                                                        .collect(Collectors.toList());

                                        return new RoomTypeListDTO(
                                                        roomType.getId(),
                                                        roomType.getName(),
                                                        roomType.getDescription(),
                                                        roomType.getPricePerNight(),
                                                        roomType.getMaxGuests(),
                                                        roomType.getIsActive(),
                                                        roomType.getCreatedAt(),
                                                        roomType.getUpdatedAt(),
                                                        lodgingId,
                                                        lodging.getLodgingName(),
                                                        images);
                                })
                                .collect(Collectors.toList());
        }

        /**
         * 查詢單筆房型詳細
         * 根據住宿ID和房型ID查詢房型的完整詳細資訊。
         * 
         * @param lodgingId  住宿ID
         * @param roomTypeId 房型ID
         * @return RoomTypeDetailDTO 房型詳細資料
         * @throws IllegalArgumentException 當住宿或房型不存在時拋出
         */
        public RoomTypeDetailDTO getRoomTypeDetail(Integer lodgingId, Integer roomTypeId) {
                RoomTypesBean roomType = roomTypesRepository
                                .findByLodgingIdAndRoomTypeIdWithDetails(lodgingId, roomTypeId)
                                .orElseThrow(() -> new IllegalArgumentException("房型不存在"));

                LodgingsBean lodging = roomType.getLodging();

                // 轉換圖片為 SimpleImageDTO
                List<SimpleImageDTO> images = roomType.getLodgingImages().stream()
                                .filter(img -> img.getDeleteStatus() == 1)
                                .map(img -> new SimpleImageDTO(
                                                img.getImageType(),
                                                img.getImageUrl(),
                                                img.getSortOrder()))
                                .collect(Collectors.toList());

                // 轉換床型資料
                List<BedTypeDTO> beds = roomType.getRoomTypeBeds().stream()
                                .map(bed -> new BedTypeDTO(
                                                bed.getBedType().getId(),
                                                bed.getBedType().getName(),
                                                bed.getQuantity()))
                                .collect(Collectors.toList());

                // 轉換設施資料
                List<FacilityDTO> facilities = roomType.getRoomFacilities().stream()
                                .map(facility -> new FacilityDTO(
                                                facility.getFacility().getId(),
                                                facility.getFacility().getFacilityName()))
                                .collect(Collectors.toList());

                return new RoomTypeDetailDTO(
                                roomType.getId(),
                                roomType.getName(),
                                roomType.getDescription(),
                                roomType.getPricePerNight(),
                                roomType.getMaxGuests(),
                                roomType.getIsActive(),
                                roomType.getCreatedAt(),
                                roomType.getUpdatedAt(),
                                lodgingId,
                                lodging.getLodgingName(),
                                roomType.getCreatedBy().getId(),
                                roomType.getCreatedBy().getVendorName(),
                                roomType.getUpdatedBy().getId(),
                                roomType.getUpdatedBy().getVendorName(),
                                beds,
                                facilities,
                                images);
        }

        /**
         * 更新房型資料
         * 根據住宿ID和房型ID更新房型的詳細資訊。
         * 
         * @param lodgingId  住宿ID
         * @param roomTypeId 房型ID
         * @param request    更新請求 DTO
         * @return RoomTypeDetailDTO 更新後的房型詳細資料
         * @throws IllegalArgumentException 當住宿或房型不存在時拋出
         */
        @Transactional
        public RoomTypeDetailDTO updateRoomType(Integer lodgingId, Integer roomTypeId,
                        RoomTypeUpdateRequestDTO request) {
                // TODO: 暫時使用固定的 vendor_id，後續再處理多人合作程式
                Integer vendorId = 3;

                RoomTypesBean roomType = roomTypesRepository
                                .findByLodgingIdAndRoomTypeIdAndVendorId(lodgingId, roomTypeId, vendorId)
                                .orElseThrow(() -> new IllegalArgumentException("房型不存在或無權限操作"));

                // 更新基本資訊
                roomType.setName(request.getRoomTypeName());
                roomType.setDescription(request.getDescription());
                roomType.setPricePerNight(request.getPricePerNight());
                roomType.setMaxGuests(request.getMaxGuests());
                if (request.getIsActive() != null) {
                        roomType.setIsActive(request.getIsActive());
                }
                roomType.setUpdatedBy(roomType.getCreatedBy()); // 暫時設為同一個 vendor
                roomType.setUpdatedAt(LocalDateTime.now());

                // 儲存房型
                RoomTypesBean savedRoomType = roomTypesRepository.save(roomType);

                // 更新圖片（如果需要）
                if (request.getImages() != null && !request.getImages().isEmpty()) {
                        // 轉換為 BaseImageDTO
                        List<BaseImageDTO> baseImageDtos = request.getImages().stream()
                                        .map(imageDto -> {
                                                BaseImageDTO baseDto = new BaseImageDTO();
                                                baseDto.setImageId(imageDto.getImageId());
                                                baseDto.setImageType(imageDto.getImageType());
                                                baseDto.setTargetId(roomTypeId);
                                                baseDto.setTargetType("room_type");
                                                baseDto.setImageUrl(imageDto.getImageUrl());
                                                baseDto.setMimeType(imageDto.getMimeType());
                                                baseDto.setSortOrder(imageDto.getSortOrder());
                                                baseDto.setIsActive(imageDto.getIsActive());
                                                baseDto.setLodgingId(lodgingId);
                                                baseDto.setRoomTypeId(roomTypeId);
                                                return baseDto;
                                        })
                                        .collect(Collectors.toList());

                        // 驗證圖片數量
                        unifiedImageService.validateImageCount(baseImageDtos, 5, "room_type");

                        // 使用 UnifiedImageService 更新圖片
                        unifiedImageService.updateImages(baseImageDtos, "room_type");
                }

                // 更新床型關聯
                if (request.getBeds() != null && !request.getBeds().isEmpty()) {
                        roomTypeBedsRepository.deleteByRoomTypeId(roomTypeId); // 先刪除舊的關聯
                        List<RoomTypeBedsBean> newBeds = request.getBeds().stream()
                                        .map(bed -> {
                                                BedTypesBean bedType = bedTypesRepository.findById(bed.id())
                                                                .orElseThrow(() -> new IllegalArgumentException(
                                                                                "床型不存在"));
                                                RoomTypeBedsBean roomTypeBed = new RoomTypeBedsBean();
                                                roomTypeBed.setRoomTypeId(roomTypeId);
                                                roomTypeBed.setBedTypeId(bed.id());
                                                roomTypeBed.setQuantity(bed.quantity());
                                                roomTypeBed.setRoomType(savedRoomType);
                                                roomTypeBed.setBedType(bedType);
                                                return roomTypeBed;
                                        })
                                        .collect(Collectors.toList());
                        roomTypeBedsRepository.saveAll(newBeds);
                }

                // 更新設施關聯
                if (request.getFacilityIds() != null && !request.getFacilityIds().isEmpty()) {
                        roomFacilitiesRepository.deleteByRoomTypeId(roomTypeId); // 先刪除舊的關聯
                        List<RoomFacilitiesBean> newFacilities = request.getFacilityIds().stream()
                                        .map(facilityId -> {
                                                FacilitiesBean facility = facilitiesRepository.findById(facilityId)
                                                                .orElseThrow(() -> new IllegalArgumentException(
                                                                                "設施不存在"));
                                                RoomFacilitiesBean roomFacility = new RoomFacilitiesBean();
                                                roomFacility.setRoomTypeId(roomTypeId);
                                                roomFacility.setFacilityId(facilityId);
                                                roomFacility.setRoomType(savedRoomType);
                                                roomFacility.setFacility(facility);
                                                return roomFacility;
                                        })
                                        .collect(Collectors.toList());
                        roomFacilitiesRepository.saveAll(newFacilities);
                }

                // 回傳更新後的詳細資料
                return getRoomTypeDetail(lodgingId, roomTypeId);
        }

        /**
         * 軟刪除房型
         * 將指定房型標記為已刪除狀態，停止對外銷售。
         * 需要商家權限驗證，確保只能操作自己的房型。
         * 
         * @param lodgingId  住宿ID
         * @param roomTypeId 房型ID
         * @throws IllegalArgumentException 當房型不存在或無權限操作時拋出
         */
        @Transactional
        public void softDeleteRoomType(Integer lodgingId, Integer roomTypeId) {
                // TODO: 暫時使用固定的 vendor_id，後續再處理多人合作程式
                Integer vendorId = 3;

                RoomTypesBean roomType = roomTypesRepository
                                .findByLodgingIdAndRoomTypeIdAndVendorId(lodgingId, roomTypeId, vendorId)
                                .orElseThrow(() -> new IllegalArgumentException("房型不存在或無權限操作"));

                // 檢查是否已經被刪除
                if (roomType.getDeleteStatus() == 0) {
                        throw new IllegalArgumentException("房型已經被刪除");
                }

                // 軟刪除房型
                roomType.setIsActive(false);
                roomType.setDeleteStatus(0);
                roomType.setDeletedTime(LocalDateTime.now());
                roomType.setUpdatedBy(roomType.getCreatedBy());
                roomType.setUpdatedAt(LocalDateTime.now());

                // 同時軟刪除相關的圖片
                roomType.getLodgingImages().stream()
                                .filter(img -> img.getDeleteStatus() == 1)
                                .forEach(img -> {
                                        img.setIsActive(false);
                                        img.setDeleteStatus(0);
                                        img.setDeletedTime(LocalDateTime.now());
                                });

                // 儲存變更
                roomTypesRepository.save(roomType);
        }
}
