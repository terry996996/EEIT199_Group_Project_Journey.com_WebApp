/**
 * 廠商旅宿列表資料傳輸物件（DTO）
 * 用於描述廠商可管理的旅宿列表。
 * @author FAN
 * @since 250704
 */
package journey.dto.lodgings;

public class VendorLodgingListDTO {

    private Integer id;
    private String lodgingName;
    private String countryName;
    private String cityName;
    private String address;
    private Boolean isActive;
    private Integer deleteStatus;
    private String statusText;

    // 無參構造函數
    public VendorLodgingListDTO() {
    }

    // 構造函數
    public VendorLodgingListDTO(Integer id, String lodgingName, String countryName,
            String cityName, String address, Boolean isActive) {
        this.id = id;
        this.lodgingName = lodgingName;
        this.countryName = countryName;
        this.cityName = cityName;
        this.address = address;
        this.isActive = isActive;
        this.deleteStatus = 1; // 預設值
        this.statusText = isActive ? "在售" : "停業";
    }

    // 完整構造函數
    public VendorLodgingListDTO(Integer id, String lodgingName, String countryName,
            String cityName, String address, Boolean isActive, Integer deleteStatus) {
        this.id = id;
        this.lodgingName = lodgingName;
        this.countryName = countryName;
        this.cityName = cityName;
        this.address = address;
        this.isActive = isActive;
        this.deleteStatus = deleteStatus;
        this.statusText = determineStatusText(isActive, deleteStatus);
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLodgingName() {
        return lodgingName;
    }

    public void setLodgingName(String lodgingName) {
        this.lodgingName = lodgingName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    // 根據 isActive 和 deleteStatus 判斷狀態文字
    private static String determineStatusText(Boolean isActive, Integer deleteStatus) {
        if (deleteStatus == null) {
            return "未知";
        }

        switch (deleteStatus) {
            case 1:
                return isActive != null && isActive ? "在售" : "停業";
            case 0:
                return "已下架";
            case -1:
                return "已刪除";
            default:
                return "未知";
        }
    }
}