package journey.service.lodgings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import journey.domain.lodgings.LodgingImagesBean;
import journey.domain.lodgings.LodgingsBean;
import journey.domain.lodgings.RoomTypesBean;
import journey.dto.lodgings.ImageDTO;
import journey.repository.lodgings.LodgingImagesRepository;
import journey.repository.lodgings.LodgingsRepository;
import journey.repository.lodgings.RoomTypesRepository;

/**
 * 圖片管理服務類
 * 統一處理住宿和房型圖片的業務邏輯，包含圖片的上傳、更新、刪除和查詢功能。
 * 支援圖片數量驗證、資料驗證、批次操作等，並提供 DTO 與 Bean 之間的轉換。
 * 
 * @author FAN
 * @since 250704
 */
@Service
public class ImageService {

    @Autowired
    private LodgingImagesRepository lodgingImagesRepository;

    @Autowired
    private LodgingsRepository lodgingsRepository;

    @Autowired
    private RoomTypesRepository roomTypesRepository;

    /**
     * 驗證圖片數量限制
     * 檢查圖片列表數量是否超過最大限制，超過則拋出異常。
     * 
     * @param images   圖片 DTO 列表
     * @param maxCount 最大允許的圖片數量
     * @throws IllegalArgumentException 當圖片數量超過限制時拋出
     */
    public void validateImageCount(List<ImageDTO> images, int maxCount) {
        if (images != null && images.size() > maxCount) {
            throw new IllegalArgumentException("圖片數量不能超過" + maxCount + "張");
        }
    }

    /**
     * 驗證圖片數量限制（使用 BaseImageDTO）
     * 檢查圖片列表數量是否超過最大限制，超過則拋出異常。
     * 
     * @param images   圖片 DTO 列表
     * @param maxCount 最大允許的圖片數量
     * @param targetType 目標類型
     * @throws IllegalArgumentException 當圖片數量超過限制時拋出
     */
    public void validateImageCount(List<journey.dto.BaseImageDTO> images, int maxCount, String targetType) {
        if (images != null && images.size() > maxCount) {
            throw new IllegalArgumentException(targetType + "圖片數量不能超過" + maxCount + "張");
        }
    }

    /**
     * 驗證圖片資料完整性
     * 檢查圖片 DTO 的必要欄位和關聯資料是否存在。
     * 
     * @param imageDto 圖片 DTO
     * @throws IllegalArgumentException 當住宿不存在或房型不存在時拋出
     */
    public void validateImageData(ImageDTO imageDto) {
        if (imageDto.getLodgingId() == null) {
            throw new IllegalArgumentException("住宿ID不能為空");
        }

        // 驗證住宿是否存在
        if (!lodgingsRepository.existsById(imageDto.getLodgingId())) {
            throw new IllegalArgumentException("住宿不存在");
        }

        // 如果是房型圖片，驗證房型是否存在
        if (imageDto.getRoomTypeId() != null) {
            if (!roomTypesRepository.existsById(imageDto.getRoomTypeId())) {
                throw new IllegalArgumentException("房型不存在");
            }
        }
    }

    /**
     * 將圖片 DTO 轉換為資料庫實體
     * 建立 ImageDTO 與 LodgingImagesBean 之間的轉換，包含關聯資料設定。
     * 
     * @param imageDto 圖片 DTO
     * @return LodgingImagesBean 圖片資料庫實體
     * @throws IllegalArgumentException 當關聯的住宿或房型不存在時拋出
     */
    public LodgingImagesBean convertToBean(ImageDTO imageDto) {
        LodgingImagesBean image = new LodgingImagesBean();

        if (imageDto.getImageId() != null) {
            image.setImageId(imageDto.getImageId());
        }

        image.setImageType(imageDto.getImageType());
        image.setImageUrl(imageDto.getImageUrl());
        image.setMimeType(imageDto.getMimeType());
        image.setSortOrder(imageDto.getSortOrder());
        image.setIsActive(imageDto.getIsActive() != null ? imageDto.getIsActive() : true);
        image.setDeleteStatus(1);

        // 設置住宿關聯
        LodgingsBean lodging = lodgingsRepository.findById(imageDto.getLodgingId())
                .orElseThrow(() -> new IllegalArgumentException("住宿不存在"));
        image.setLodging(lodging);

        // 設置房型關聯（如果有的話）
        if (imageDto.getRoomTypeId() != null) {
            RoomTypesBean roomType = roomTypesRepository.findById(imageDto.getRoomTypeId())
                    .orElseThrow(() -> new IllegalArgumentException("房型不存在"));
            image.setRoomType(roomType);
        }

        return image;
    }

    /**
     * 將資料庫實體轉換為圖片 DTO
     * 建立 LodgingImagesBean 與 ImageDTO 之間的轉換。
     * 
     * @param image 圖片資料庫實體
     * @return ImageDTO 圖片 DTO
     */
    public ImageDTO convertToDTO(LodgingImagesBean image) {
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
     * 批次儲存圖片
     * 將圖片 DTO 列表轉換為實體並批次儲存到資料庫。
     * 跳過標記為非啟用的圖片，並進行資料驗證。
     * 
     * @param imageDtos 圖片 DTO 列表
     * @return List<ImageDTO> 儲存成功的圖片 DTO 列表
     */
    @Transactional
    public List<ImageDTO> saveImages(List<ImageDTO> imageDtos) {
        if (imageDtos == null || imageDtos.isEmpty()) {
            return new ArrayList<>();
        }

        List<LodgingImagesBean> imagesToSave = new ArrayList<>();

        for (ImageDTO imageDto : imageDtos) {
            // 跳過軟刪除的圖片
            if (imageDto.getIsActive() != null && !imageDto.getIsActive()) {
                continue;
            }

            validateImageData(imageDto);
            LodgingImagesBean image = convertToBean(imageDto);
            imagesToSave.add(image);
        }

        // 批次儲存
        List<LodgingImagesBean> savedImages = lodgingImagesRepository.saveAll(imagesToSave);

        // 轉換為 DTO
        List<ImageDTO> result = new ArrayList<>();
        for (LodgingImagesBean savedImage : savedImages) {
            result.add(convertToDTO(savedImage));
        }

        return result;
    }

    /**
     * 更新圖片資料
     * 支援新增、更新和軟刪除圖片操作。
     * 根據是否有 imageId 來判斷是新增還是更新操作。
     * 
     * @param imageDtos 圖片 DTO 列表
     * @return List<ImageDTO> 更新後的圖片 DTO 列表（僅包含啟用的圖片）
     */
    @Transactional
    public List<ImageDTO> updateImages(List<ImageDTO> imageDtos) {
        if (imageDtos == null || imageDtos.isEmpty()) {
            return new ArrayList<>();
        }

        List<LodgingImagesBean> imagesToUpdate = new ArrayList<>();
        List<LodgingImagesBean> imagesToSave = new ArrayList<>();

        for (ImageDTO imageDto : imageDtos) {
            if (imageDto.getImageId() != null) {
                // 更新現有圖片
                Optional<LodgingImagesBean> existingImage = lodgingImagesRepository.findById(imageDto.getImageId());
                if (existingImage.isPresent()) {
                    LodgingImagesBean image = existingImage.get();

                    if (imageDto.getIsActive() != null && !imageDto.getIsActive()) {
                        // 軟刪除圖片
                        image.setIsActive(false);
                        image.setDeleteStatus(0);
                    } else {
                        // 更新圖片資料
                        image.setImageType(imageDto.getImageType());
                        image.setImageUrl(imageDto.getImageUrl());
                        image.setMimeType(imageDto.getMimeType());
                        image.setSortOrder(imageDto.getSortOrder());
                        image.setIsActive(imageDto.getIsActive() != null ? imageDto.getIsActive() : true);
                    }
                    imagesToUpdate.add(image);
                }
            } else {
                // 新增圖片
                if (imageDto.getIsActive() != null && !imageDto.getIsActive()) {
                    continue; // 跳過軟刪除的新圖片
                }
                validateImageData(imageDto);
                LodgingImagesBean image = convertToBean(imageDto);
                imagesToSave.add(image);
            }
        }

        // 批次儲存和更新
        List<LodgingImagesBean> savedNewImages = lodgingImagesRepository.saveAll(imagesToSave);
        List<LodgingImagesBean> savedUpdatedImages = lodgingImagesRepository.saveAll(imagesToUpdate);

        // 轉換為 DTO
        List<ImageDTO> result = new ArrayList<>();
        for (LodgingImagesBean savedImage : savedNewImages) {
            result.add(convertToDTO(savedImage));
        }
        for (LodgingImagesBean savedImage : savedUpdatedImages) {
            if (savedImage.getIsActive()) {
                result.add(convertToDTO(savedImage));
            }
        }

        return result;
    }

    /**
     * 根據住宿ID獲取圖片列表
     * 查詢指定住宿的所有啟用且未刪除的圖片。
     * 
     * @param lodgingId 住宿ID
     * @return List<ImageDTO> 圖片 DTO 列表
     */
    public List<ImageDTO> getImagesByLodgingId(Integer lodgingId) {
        List<LodgingImagesBean> images = lodgingImagesRepository
                .findByLodgingIdAndIsActiveTrueAndDeleteStatus(lodgingId, 1);
        return images.stream()
                .map(this::convertToDTO)
                .toList();
    }

    /**
     * 根據房型ID獲取圖片列表
     * 查詢指定房型的所有啟用且未刪除的圖片。
     * 
     * @param roomTypeId 房型ID
     * @return List<ImageDTO> 圖片 DTO 列表
     */
    public List<ImageDTO> getImagesByRoomTypeId(Integer roomTypeId) {
        List<LodgingImagesBean> images = lodgingImagesRepository
                .findByRoomTypeIdAndIsActiveTrueAndDeleteStatus(roomTypeId, 1);
        return images.stream()
                .map(this::convertToDTO)
                .toList();
    }
}