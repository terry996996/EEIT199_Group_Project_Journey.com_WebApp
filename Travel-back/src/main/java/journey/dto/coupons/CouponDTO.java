package journey.dto.coupons;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class CouponDTO {
    private UUID id; // 折價券流水號
    private String title; // 折價券名稱
    private String code; // 折價券代碼
    private BigDecimal value; // 折扣金額或百分比
    private String type; // 折扣類型 {fixed金額, percentage百分比}
    private BigDecimal minSpend; // 低銷金額
    private BigDecimal maxDiscount; // 最高折扣金額
    private LocalDateTime issuedTime; // 發放日期
    private LocalDateTime startTime; // 生效日期
    private LocalDateTime useAt; // 使用日期
    private LocalDateTime expiryDate; // 到期日
    private String status; // 折價券當前狀態，{"available", "used", "expired"} => 有效、已使用、過期
    private String detail; // 折價券類型說明文字
    private Set<String> applicableProductTypes; // 適用商品類型

    // Constructors
    public CouponDTO() {
    }

    public CouponDTO(UUID id, String title, String code, BigDecimal value, String type, BigDecimal minSpend,
            BigDecimal maxDiscount, LocalDateTime expiryDate, String status,
            LocalDateTime issuedTime, LocalDateTime startTime, LocalDateTime useAt, String detail,
            Set<String> applicableProductTypes) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.value = value;
        this.type = type;
        this.minSpend = minSpend;
        this.maxDiscount = maxDiscount;
        this.expiryDate = expiryDate;
        this.status = status;
        this.issuedTime = issuedTime;
        this.startTime = startTime;
        this.useAt = useAt;
        this.detail = detail;
        this.applicableProductTypes = applicableProductTypes;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getMinSpend() {
        return minSpend;
    }

    public void setMinSpend(BigDecimal minSpend) {
        this.minSpend = minSpend;
    }

    public BigDecimal getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(BigDecimal maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getIssuedTime() {
        return issuedTime;
    }

    public void setIssuedTime(LocalDateTime issuedTime) {
        this.issuedTime = issuedTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getUseAt() {
        return useAt;
    }

    public void setUseAt(LocalDateTime useAt) {
        this.useAt = useAt;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Set<String> getApplicableProductTypes() {
        return applicableProductTypes;
    }

    public void setApplicableProductTypes(Set<String> applicableProductTypes) {
        this.applicableProductTypes = applicableProductTypes;
    }

}
