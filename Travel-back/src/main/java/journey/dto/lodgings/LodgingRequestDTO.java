package journey.dto.lodgings;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 旅宿資料傳輸物件（DTO）
 * 用於接收建立或更新旅宿時的請求資料。
 * 
 * @author FAN
 * @since 250704
 */
public class LodgingRequestDTO {

    @NotBlank(message = "住宿名稱不能為空")
    @Size(max = 100, message = "住宿名稱不能超過100個字符")
    private String lodgingName;

    @NotNull(message = "住宿類型ID不能為空")
    private Integer lodgingTypeId;

    @NotBlank(message = "描述不能為空")
    @Size(max = 255, message = "描述不能超過255個字符")
    private String description;

    @NotNull(message = "國家ID不能為空")
    private Integer countryId;

    @NotNull(message = "城市ID不能為空")
    private Integer cityId;

    @NotBlank(message = "地址不能為空")
    @Size(max = 100, message = "地址不能超過100個字符")
    private String address;

    @NotNull(message = "緯度不能為空")
    private BigDecimal latitude;

    @NotNull(message = "經度不能為空")
    private BigDecimal longitude;

    @NotBlank(message = "住宿電話不能為空")
    @Size(max = 20, message = "住宿電話不能超過20個字符")
    private String lodgingTel;

    @NotBlank(message = "電子郵件不能為空")
    @Email(message = "電子郵件格式不正確")
    @Size(max = 100, message = "電子郵件不能超過100個字符")
    private String email;

    private Boolean isActive;

    @Size(max = 5, message = "圖片數量不能超過5張")
    private List<ImageDTO> images;

    // 無參構造函數
    public LodgingRequestDTO() {
    }

    // 構造函數
    public LodgingRequestDTO(String lodgingName, Integer lodgingTypeId, String description,
            String address, Integer cityId, Integer countryId,
            BigDecimal latitude, BigDecimal longitude, String lodgingTel,
            String email, Boolean isActive, List<ImageDTO> images) {
        this.lodgingName = lodgingName;
        this.lodgingTypeId = lodgingTypeId;
        this.description = description;
        this.address = address;
        this.cityId = cityId;
        this.countryId = countryId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lodgingTel = lodgingTel;
        this.email = email;
        this.isActive = isActive;
        this.images = images;
    }

    // Getters and Setters
    public String getLodgingName() {
        return lodgingName;
    }

    public void setLodgingName(String lodgingName) {
        this.lodgingName = lodgingName;
    }

    public Integer getLodgingTypeId() {
        return lodgingTypeId;
    }

    public void setLodgingTypeId(Integer lodgingTypeId) {
        this.lodgingTypeId = lodgingTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getLodgingTel() {
        return lodgingTel;
    }

    public void setLodgingTel(String lodgingTel) {
        this.lodgingTel = lodgingTel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<ImageDTO> getImages() {
        return images;
    }

    public void setImages(List<ImageDTO> images) {
        this.images = images;
    }

}