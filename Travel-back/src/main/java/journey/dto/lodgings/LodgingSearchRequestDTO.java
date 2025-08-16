package journey.dto.lodgings;

import java.time.LocalDate;

/**
 * 旅宿搜尋請求資料傳輸物件（DTO）
 * 用於接收前端傳來的旅宿搜尋條件。
 * 
 * @author FAN
 * @since 250704
 */
public class LodgingSearchRequestDTO {

    private String cityName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer guestCount;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

}
