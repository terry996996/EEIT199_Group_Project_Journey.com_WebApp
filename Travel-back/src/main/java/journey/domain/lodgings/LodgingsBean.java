package journey.domain.lodgings;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
import journey.domain.comments.CommentsBean;
import journey.domain.locations.CityBean;
import journey.domain.locations.CountryBean;
import journey.domain.users.VendorBean;

/**
 * 住宿實體類別
 * 
 * 功能說明：
 * - 儲存住宿基本資訊（名稱、描述、地址、聯絡方式等）
 * - 支援地理位置資訊（經緯度）
 * - 支援多種住宿類型（飯店、民宿、旅館等）
 * - 支援圖片管理和房型管理
 * - 支援評論系統
 * - 支援軟刪除機制
 * 
 * 權限規則：
 * - VENDOR: 可以建立和管理自己的住宿
 * - ADMIN: 可以管理所有住宿
 * - USER: 可以查看和評論住宿
 * 
 * @author Journey Team
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "lodgings")
public class LodgingsBean {
    /** 住宿ID - 主鍵 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 住宿名稱 - 最大100字 */
    @Column(name = "lodging_name", length = 100)
    private String lodgingName;

    /** 住宿描述 - 必填，最大255字 */
    @Column(name = "description", nullable = false, length = 255)
    private String description;

    /** 住宿地址 - 必填，最大100字 */
    @Column(name = "address", nullable = false, length = 100)
    private String address;

    /** 緯度 - 必填，精確到小數點後4位 */
    @Column(name = "latitude", nullable = false, precision = 9, scale = 4)
    private BigDecimal latitude;

    /** 經度 - 必填，精確到小數點後4位 */
    @Column(name = "longitude", nullable = false, precision = 9, scale = 4)
    private BigDecimal longitude;

    /** 住宿電話 - 必填，最大20字 */
    @Column(name = "lodging_tel", nullable = false, length = 20)
    private String lodgingTel;

    /** 電子郵件 - 必填，最大100字 */
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    /** 是否啟用 - 預設true */
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

    /** 供應商 - 必填 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private VendorBean vendor;

    /** 住宿類型 - 必填（飯店、民宿、旅館等） */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lodging_type", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private LodgingsTypeBean lodgingType;

    /** 所在城市 - 必填 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private CityBean city;

    /** 所在國家 - 必填 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private CountryBean country;

    /** 更新人 - 必填 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private VendorBean updatedBy;

    /** 房型列表 */
    @OneToMany(mappedBy = "lodging")
    @JsonManagedReference
    private Set<RoomTypesBean> roomTypes;

    /** 住宿圖片列表 */
    @OneToMany(mappedBy = "lodging")
    @OrderBy("sortOrder ASC") // 按照 sortOrder 升序排列
    @JsonManagedReference
    private Set<LodgingImagesBean> lodgingImages;

    /** 住宿評論列表 */
    @OneToMany(mappedBy = "lodging")
    @JsonManagedReference
    private Set<CommentsBean> comments;

    // 无参构造函数，供 JPA 和 Jackson 使用
    public LodgingsBean() {
        // 給 JPA 和 Jackson 使用
    }

    public LodgingsBean(Integer id, String lodgingName, String description, String address, BigDecimal latitude,
            BigDecimal longitude, String lodgingTel, String email, Boolean isActive, LocalDateTime deletedTime,
            Integer deleteStatus, LocalDateTime createdAt, LocalDateTime updatedAt, VendorBean vendor,
            LodgingsTypeBean lodgingType, CityBean city, CountryBean country, VendorBean updatedBy) {
        this.id = id;
        this.lodgingName = lodgingName;
        this.description = description;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lodgingTel = lodgingTel;
        this.email = email;
        this.isActive = isActive;
        this.deletedTime = deletedTime;
        this.deleteStatus = deleteStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.vendor = vendor;
        this.lodgingType = lodgingType;
        this.city = city;
        this.country = country;
        this.updatedBy = updatedBy;
    }

    // getters and setters
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

    public VendorBean getVendor() {
        return vendor;
    }

    public void setVendor(VendorBean vendor) {
        this.vendor = vendor;
    }

    public LodgingsTypeBean getLodgingType() {
        return lodgingType;
    }

    public void setLodgingType(LodgingsTypeBean lodgingType) {
        this.lodgingType = lodgingType;
    }

    public CityBean getCity() {
        return city;
    }

    public void setCity(CityBean city) {
        this.city = city;
    }

    public CountryBean getCountry() {
        return country;
    }

    public void setCountry(CountryBean country) {
        this.country = country;
    }

    public VendorBean getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(VendorBean updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Set<RoomTypesBean> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(Set<RoomTypesBean> roomTypes) {
        this.roomTypes = roomTypes;
    }

    public Set<LodgingImagesBean> getLodgingImages() {
        return lodgingImages;
    }

    public void setLodgingImages(Set<LodgingImagesBean> lodgingImages) {
        this.lodgingImages = lodgingImages;
    }

    public Set<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(Set<CommentsBean> comments) {
        this.comments = comments;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null)
            this.createdAt = now;
        if (this.updatedAt == null)
            this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}