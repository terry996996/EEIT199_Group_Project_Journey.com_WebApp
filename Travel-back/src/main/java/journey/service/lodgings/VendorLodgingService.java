package journey.service.lodgings;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import journey.domain.locations.CityBean;
import journey.domain.locations.CountryBean;
import journey.domain.lodgings.LodgingImagesBean;
import journey.domain.lodgings.LodgingsBean;
import journey.domain.lodgings.LodgingsTypeBean;
import journey.domain.users.VendorBean;
import journey.dto.lodgings.ImageDTO;
import journey.dto.lodgings.LodgingRequestDTO;
import journey.dto.lodgings.LodgingResponseDTO;
import journey.dto.lodgings.VendorLodgingDetailDTO;
import journey.dto.lodgings.VendorLodgingListDTO;
import journey.repository.locations.CityRepository;
import journey.repository.locations.CountryRepository;
import journey.repository.lodgings.LodgingsRepository;
import journey.repository.lodgings.LodgingsTypeRepository;
import journey.repository.users.VendorRepository;

/**
 * 商家住宿管理服務類
 * 提供商家後台的住宿管理功能，包含住宿的創建、查詢、更新、刪除等完整 CRUD 操作。
 * 支援住宿狀態管理（在售、停業、已下架）、軟刪除、圖片管理等，並提供商家權限驗證。
 * 
 * @author FAN
 * @since 250704
 */
@Service
public class VendorLodgingService {

        @Autowired
        private LodgingsRepository lodgingsRepository;

        @Autowired
        private ImageService imageService;

        @Autowired
        private LodgingsTypeRepository lodgingsTypeRepository;

        @Autowired
        private CityRepository cityRepository;

        @Autowired
        private CountryRepository countryRepository;

        @Autowired
        private VendorRepository vendorRepository;

        /* ---------- 商家後台旅宿管理 ---------- */

        /**
         * 創建新住宿
         * 根據請求 DTO 創建新的住宿實體，包含基本資訊、關聯資料和圖片管理。
         * 新創建的住宿預設為停業狀態，需要後續手動啟用。
         * 
         * @param request 住宿創建請求 DTO
         * @return LodgingResponseDTO 創建成功的住宿回應 DTO
         * @throws IllegalArgumentException 當關聯資料（住宿類型、城市、國家、供應商）不存在時拋出
         */
        @Transactional
        public LodgingResponseDTO createLodging(LodgingRequestDTO request, Integer vendorId) {
                // 查詢關聯資料
                LodgingsTypeBean lodgingType = lodgingsTypeRepository.findById(request.getLodgingTypeId())
                                .orElseThrow(() -> new IllegalArgumentException("住宿類型不存在"));

                CityBean city = cityRepository.findById(request.getCityId())
                                .orElseThrow(() -> new IllegalArgumentException("城市不存在"));

                CountryBean country = countryRepository.findById(request.getCountryId())
                                .orElseThrow(() -> new IllegalArgumentException("國家不存在"));

                // 取得 vendor
                Integer useVendorId = vendorId != null ? vendorId : 3;
                VendorBean vendor = vendorRepository.findById(useVendorId)
                                .orElseThrow(() -> new IllegalArgumentException("供應商不存在"));

                // 取得 created_by (allUserBean)
                Integer createdByUserId = vendor.getAllUserBean() != null ? vendor.getAllUserBean().getId() : 2002;
                journey.domain.users.AllUserBean createdBy = vendor.getAllUserBean();
                if (createdBy == null) {
                        throw new IllegalArgumentException("Vendor 對應不到 allUserBean，請檢查資料庫");
                }

                // 創建住宿實體
                LodgingsBean lodging = new LodgingsBean();
                lodging.setLodgingName(request.getLodgingName());
                lodging.setDescription(request.getDescription());
                lodging.setAddress(request.getAddress());
                lodging.setLatitude(request.getLatitude());
                lodging.setLongitude(request.getLongitude());
                lodging.setLodgingTel(request.getLodgingTel());
                lodging.setEmail(request.getEmail());
                lodging.setIsActive(false); // 強制使用預設值：停業狀態
                lodging.setDeleteStatus(1);
                lodging.setVendor(vendor);
                lodging.setLodgingType(lodgingType);
                lodging.setCity(city);
                lodging.setCountry(country);
                lodging.setUpdatedBy(vendor); // 暫時設為同一個 vendor
                // lodging.setCreatedBy(createdBy); // <--- 已移除 created_by 欄位，這行要刪除或註解

                // 儲存住宿
                LodgingsBean savedLodging = lodgingsRepository.save(lodging);

                // 批次儲存圖片
                List<ImageDTO> savedImages = new ArrayList<>();
                if (request.getImages() != null && !request.getImages().isEmpty()) {
                        // 轉換為通用 ImageDTO
                        List<ImageDTO> imageDtos = request.getImages().stream()
                                        .map(imageDto -> new ImageDTO(
                                                        imageDto.getImageId(),
                                                        imageDto.getImageType(),
                                                        savedLodging.getId(), // lodgingId
                                                        imageDto.getRoomTypeId(), // roomTypeId
                                                        imageDto.getImageUrl(),
                                                        imageDto.getMimeType(),
                                                        imageDto.getSortOrder(),
                                                        imageDto.getIsActive()))
                                        .collect(Collectors.toList());

                        // 驗證圖片數量
                        imageService.validateImageCount(imageDtos, 5);

                        // 使用 ImageService 儲存圖片
                        savedImages = imageService.saveImages(imageDtos);
                }

                // 創建響應 DTO
                return new LodgingResponseDTO(
                                savedLodging.getId(),
                                savedLodging.getLodgingName(),
                                savedLodging.getDescription(),
                                savedLodging.getAddress(),
                                savedLodging.getLatitude(),
                                savedLodging.getLongitude(),
                                savedLodging.getLodgingTel(),
                                savedLodging.getEmail(),
                                savedLodging.getIsActive(),
                                savedLodging.getCreatedAt(),
                                savedLodging.getUpdatedAt(),
                                savedLodging.getLodgingType().getId(),
                                savedLodging.getLodgingType().getTypeText(),
                                savedLodging.getCity().getId(),
                                savedLodging.getCity().getNameZh() != null ? savedLodging.getCity().getNameZh()
                                                : savedLodging.getCity().getName(),
                                savedLodging.getCountry().getId(),
                                savedLodging.getCountry().getNameZh() != null ? savedLodging.getCountry().getNameZh()
                                                : savedLodging.getCountry().getName(),
                                savedImages);
        }

        /**
         * 批次建立住宿（支援 Excel 多筆匯入）
         * @author Max
         * @since 2024-07-14
         * @param dtos 住宿請求 DTO 清單
         * @param vendorId 商家ID
         * @return 每筆匯入結果訊息
         */
        public List<String> createLodgings(List<LodgingRequestDTO> dtos, Integer vendorId) {
                List<String> results = new ArrayList<>();
                for (LodgingRequestDTO dto : dtos) {
                        try {
                                createLodging(dto, vendorId);
                                results.add("住宿: " + dto.getLodgingName() + " 匯入成功");
                        } catch (Exception e) {
                                results.add("住宿: " + dto.getLodgingName() + " 匯入失敗: " + e.getMessage());
                        }
                }
                return results;
        }

        /**
         * 獲取商家的旅宿列表（支援狀態篩選）
         * 根據商家ID和狀態參數查詢對應的住宿列表。
         * 支援多種狀態篩選：在售、停業、已下架、全部。
         * 
         * @param vendorId 商家ID
         * @param status   狀態篩選：active(在售)、inactive(停業)、deleted(已下架)、all(全部)
         * @return List<VendorLodgingListDTO> 商家住宿列表 DTO
         */
        public List<VendorLodgingListDTO> getVendorLodgings(Integer vendorId, String status) {
                // TODO: 暫時使用固定的 vendor_id，後續再處理多人合作程式
                vendorId = 3;

                List<LodgingsBean> lodgings;

                switch (status != null ? status.toLowerCase() : "active") {
                        case "active":
                                // 在售：is_active = true, delete_status = 1
                                System.out.println("查詢在售旅宿 - vendor_id: " + vendorId
                                                + ", is_active: true, delete_status: 1");
                                lodgings = lodgingsRepository.findByVendorIdAndStatusWithJoins(vendorId, true, 1);
                                break;

                        case "inactive":
                                // 停業：is_active = false, delete_status = 1
                                System.out.println("查詢停業旅宿 - vendor_id: " + vendorId
                                                + ", is_active: false, delete_status: 1");
                                lodgings = lodgingsRepository.findByVendorIdAndStatusWithJoins(vendorId, false, 1);
                                break;

                        case "deleted":
                                // 已下架：is_active = false, delete_status = 0
                                System.out.println("查詢已下架旅宿 - vendor_id: " + vendorId
                                                + ", is_active: false, delete_status: 0");
                                lodgings = lodgingsRepository.findByVendorIdAndStatusWithJoins(vendorId, false, 0);
                                break;

                        case "all":
                                // 全部：不分狀態
                                System.out.println("查詢全部旅宿 - vendor_id: " + vendorId);
                                lodgings = lodgingsRepository.findByVendorIdWithJoins(vendorId);
                                break;

                        default:
                                // 預設查詢在售
                                System.out.println("查詢在售旅宿（預設） - vendor_id: " + vendorId
                                                + ", is_active: true, delete_status: 1");
                                lodgings = lodgingsRepository.findByVendorIdAndStatusWithJoins(vendorId, true, 1);
                                break;
                }

                System.out.println("查詢到 " + lodgings.size() + " 筆住宿資料");

                if (!lodgings.isEmpty()) {
                        System.out.println("第一筆資料: " + lodgings.get(0).getLodgingName());
                }

                return lodgings.stream()
                                .map(lodging -> {
                                        try {
                                                return new VendorLodgingListDTO(
                                                                lodging.getId(),
                                                                lodging.getLodgingName(),
                                                                lodging.getCountry().getNameZh() != null
                                                                                ? lodging.getCountry().getNameZh()
                                                                                : lodging.getCountry().getName(),
                                                                lodging.getCity().getNameZh() != null
                                                                                ? lodging.getCity().getNameZh()
                                                                                : lodging.getCity().getName(),
                                                                lodging.getAddress(),
                                                                lodging.getIsActive(),
                                                                lodging.getDeleteStatus());
                                        } catch (Exception e) {
                                                System.err.println("映射住宿資料時發生錯誤: " + e.getMessage());
                                                e.printStackTrace();
                                                return null;
                                        }
                                })
                                .filter(dto -> dto != null)
                                .collect(Collectors.toList());
        }

        /**
         * 獲取商家的所有旅宿列表（向後相容）
         * 預設查詢在售狀態的住宿列表。
         * 
         * @param vendorId 商家ID
         * @return List<VendorLodgingListDTO> 商家住宿列表 DTO
         */
        public List<VendorLodgingListDTO> getVendorLodgings(Integer vendorId) {
                return getVendorLodgings(vendorId, "active");
        }

        /**
         * 獲取單筆旅宿詳細資料
         * 查詢指定ID的住宿完整資訊，包含圖片列表。
         * 
         * @param id 住宿ID
         * @return VendorLodgingDetailDTO 住宿詳細資料 DTO
         * @throws IllegalArgumentException 當住宿不存在時拋出
         */
        public VendorLodgingDetailDTO getLodgingDetail(Integer id) {
                LodgingsBean lodging = lodgingsRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("旅宿不存在"));

                // 使用 ImageService 獲取圖片列表
                List<ImageDTO> imageDtos = imageService.getImagesByLodgingId(lodging.getId());

                // 直接使用 ImageDTO，無需轉換
                List<ImageDTO> images = imageDtos;

                return new VendorLodgingDetailDTO(
                                lodging.getId(),
                                lodging.getLodgingName(),
                                lodging.getDescription(),
                                lodging.getAddress(),
                                lodging.getLatitude(),
                                lodging.getLongitude(),
                                lodging.getLodgingTel(),
                                lodging.getEmail(),
                                lodging.getIsActive(),
                                lodging.getCreatedAt(),
                                lodging.getUpdatedAt(),
                                lodging.getUpdatedBy().getId(), // 暫時返回 ID，後續可擴展為完整資訊
                                lodging.getLodgingType().getId(),
                                lodging.getLodgingType().getTypeText(),
                                lodging.getCity().getId(),
                                lodging.getCity().getNameZh() != null ? lodging.getCity().getNameZh()
                                                : lodging.getCity().getName(),
                                lodging.getCountry().getId(),
                                lodging.getCountry().getNameZh() != null ? lodging.getCountry().getNameZh()
                                                : lodging.getCountry().getName(),
                                images);
        }

        /**
         * 更新旅宿資料
         * 根據請求 DTO 更新指定ID的住宿資訊，包含基本資料、關聯資料和圖片管理。
         * 支援部分更新，只更新提供的欄位。
         * 
         * @param id      住宿ID
         * @param request 住宿更新請求 DTO
         * @return LodgingResponseDTO 更新後的住宿回應 DTO
         * @throws IllegalArgumentException 當住宿或關聯資料不存在時拋出
         */
        @Transactional
        public LodgingResponseDTO updateLodging(Integer id, LodgingRequestDTO request) {

                // 查詢現有旅宿
                LodgingsBean lodging = lodgingsRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("旅宿不存在"));

                // 查詢關聯資料
                LodgingsTypeBean lodgingType = lodgingsTypeRepository.findById(request.getLodgingTypeId())
                                .orElseThrow(() -> new IllegalArgumentException("住宿類型不存在"));

                CityBean city = cityRepository.findById(request.getCityId())
                                .orElseThrow(() -> new IllegalArgumentException("城市不存在"));

                CountryBean country = countryRepository.findById(request.getCountryId())
                                .orElseThrow(() -> new IllegalArgumentException("國家不存在"));

                // 更新住宿資料
                lodging.setLodgingName(request.getLodgingName());
                lodging.setDescription(request.getDescription());
                lodging.setAddress(request.getAddress());
                lodging.setLatitude(request.getLatitude());
                lodging.setLongitude(request.getLongitude());
                lodging.setLodgingTel(request.getLodgingTel());
                lodging.setEmail(request.getEmail());
                if (request.getIsActive() != null) {
                        lodging.setIsActive(request.getIsActive());
                }
                lodging.setLodgingType(lodgingType);
                lodging.setCity(city);
                lodging.setCountry(country);

                // 儲存住宿
                LodgingsBean savedLodging = lodgingsRepository.save(lodging);

                // 處理圖片更新
                List<ImageDTO> savedImages = new ArrayList<>();
                if (request.getImages() != null && !request.getImages().isEmpty()) {
                        // 轉換為通用 ImageDTO
                        List<ImageDTO> imageDtos = request.getImages().stream()
                                        .map(imageDto -> new ImageDTO(
                                                        imageDto.getImageId(),
                                                        imageDto.getImageType(),
                                                        savedLodging.getId(), // lodgingId
                                                        imageDto.getRoomTypeId(), // roomTypeId
                                                        imageDto.getImageUrl(),
                                                        imageDto.getMimeType(),
                                                        imageDto.getSortOrder(),
                                                        imageDto.getIsActive()))
                                        .collect(Collectors.toList());

                        // 驗證圖片數量
                        imageService.validateImageCount(imageDtos, 5);

                        // 使用 ImageService 更新圖片
                        savedImages = imageService.updateImages(imageDtos);
                }

                // 創建響應 DTO
                return new LodgingResponseDTO(
                                savedLodging.getId(),
                                savedLodging.getLodgingName(),
                                savedLodging.getDescription(),
                                savedLodging.getAddress(),
                                savedLodging.getLatitude(),
                                savedLodging.getLongitude(),
                                savedLodging.getLodgingTel(),
                                savedLodging.getEmail(),
                                savedLodging.getIsActive(),
                                savedLodging.getCreatedAt(),
                                savedLodging.getUpdatedAt(),
                                savedLodging.getLodgingType().getId(),
                                savedLodging.getLodgingType().getTypeText(),
                                savedLodging.getCity().getId(),
                                savedLodging.getCity().getNameZh() != null ? savedLodging.getCity().getNameZh()
                                                : savedLodging.getCity().getName(),
                                savedLodging.getCountry().getId(),
                                savedLodging.getCountry().getNameZh() != null ? savedLodging.getCountry().getNameZh()
                                                : savedLodging.getCountry().getName(),
                                savedImages);
        }

        /**
         * 軟刪除旅宿
         * 將指定住宿標記為已刪除狀態，同時軟刪除相關的圖片。
         * 軟刪除的住宿不會從資料庫中移除，但不會在正常查詢中顯示。
         * 
         * @param id 住宿ID
         * @throws IllegalArgumentException 當住宿不存在或已經被刪除時拋出
         */
        @Transactional
        public void softDeleteLodging(Integer id) {
                // 查詢旅宿
                LodgingsBean lodging = lodgingsRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("旅宿不存在"));

                // TODO: 檢查權限 - 目前暫時跳過，後續實作身份驗證
                // 檢查是否為當前用戶的旅宿
                // if (!lodging.getVendor().getId().equals(getCurrentUserId())) {
                // throw new SecurityException("無權限刪除此旅宿");
                // }

                // 檢查是否已經被刪除
                if (lodging.getDeleteStatus() == 0) {
                        throw new IllegalArgumentException("旅宿已經被刪除");
                }

                // 軟刪除旅宿
                lodging.setIsActive(false);
                lodging.setDeleteStatus(0);
                lodging.setDeletedTime(LocalDateTime.now());

                // 同時軟刪除相關的圖片
                Set<LodgingImagesBean> images = lodging.getLodgingImages();
                for (LodgingImagesBean image : images) {
                        if (image.getDeleteStatus() == 1) {
                                image.setIsActive(false);
                                image.setDeleteStatus(0);
                                image.setDeletedTime(LocalDateTime.now());
                        }
                }

                // 儲存變更
                lodgingsRepository.save(lodging);
        }

        /**
         * 停業旅宿
         * 將指定住宿標記為停業狀態，停止對外銷售。
         * 需要商家權限驗證，確保只能操作自己的住宿。
         * 
         * @param id       住宿ID
         * @param vendorId 商家ID
         * @throws IllegalArgumentException 當住宿不存在或無權限操作時拋出
         */
        @Transactional
        public void deactivateLodging(Integer id, Integer vendorId) {
                // TODO: 暫時使用固定的 vendor_id，後續再處理多人合作程式
                vendorId = 3;

                LodgingsBean lodging = lodgingsRepository.findByIdAndVendorId(id, vendorId)
                                .orElseThrow(() -> new IllegalArgumentException("旅宿不存在或無權限操作"));

                lodging.setIsActive(false);
                lodging.setUpdatedBy(lodging.getVendor());
                lodging.setUpdatedAt(LocalDateTime.now());

                lodgingsRepository.save(lodging);
        }

        /**
         * 重新上架旅宿
         * 將停業的住宿重新標記為啟用狀態，恢復對外銷售。
         * 軟刪除的住宿無法重新上架，需要先還原。
         * 
         * @param id       住宿ID
         * @param vendorId 商家ID
         * @throws IllegalArgumentException 當住宿不存在、無權限操作或為軟刪除狀態時拋出
         */
        @Transactional
        public void activateLodging(Integer id, Integer vendorId) {
                // TODO: 暫時使用固定的 vendor_id，後續再處理多人合作程式
                vendorId = 3;

                LodgingsBean lodging = lodgingsRepository.findByIdAndVendorId(id, vendorId)
                                .orElseThrow(() -> new IllegalArgumentException("旅宿不存在或無權限操作"));

                // 檢查是否為軟刪除狀態，軟刪除的旅宿不能重新上架
                if (lodging.getDeleteStatus() == 0) {
                        throw new IllegalArgumentException("軟刪除的旅宿無法重新上架，請先還原旅宿");
                }

                lodging.setIsActive(true);
                lodging.setUpdatedBy(lodging.getVendor());
                lodging.setUpdatedAt(LocalDateTime.now());

                lodgingsRepository.save(lodging);
        }

        /**
         * 軟刪還原旅宿
         * 將軟刪除的住宿還原為正常狀態，恢復可操作權限。
         * 還原後的住宿預設為停業狀態，需要手動啟用。
         * 
         * @param id       住宿ID
         * @param vendorId 商家ID
         * @throws IllegalArgumentException 當住宿不存在或無權限操作時拋出
         */
        @Transactional
        public void restoreLodging(Integer id, Integer vendorId) {
                // TODO: 暫時使用固定的 vendor_id，後續再處理多人合作程式
                vendorId = 3;

                LodgingsBean lodging = lodgingsRepository.findByIdAndVendorId(id, vendorId)
                                .orElseThrow(() -> new IllegalArgumentException("旅宿不存在或無權限操作"));

                lodging.setDeleteStatus(1);
                lodging.setIsActive(false); // 還原後預設為停業狀態
                lodging.setDeletedTime(null);
                lodging.setUpdatedBy(lodging.getVendor());
                lodging.setUpdatedAt(LocalDateTime.now());

                lodgingsRepository.save(lodging);
        }
}