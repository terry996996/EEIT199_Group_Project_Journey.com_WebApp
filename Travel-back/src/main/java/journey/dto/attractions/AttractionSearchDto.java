package journey.dto.attractions;

import java.util.List;

public class AttractionSearchDto {
    private Integer id;


    private String keyword;     // 關鍵字（支援模糊搜尋）
    private Integer countryId;  // 國家 ID
    private Integer cityId;     // 城市 ID
    private List<Integer> typeIds;
    private List<Integer> tagIds;
    private String order;       // 排序欄位（可為 "id", "name" 等）
    private Boolean dir;        // 排序方向：true=DESC，false=ASC
    private Integer start;      // 分頁起始筆數
    private Integer rows;       // 每頁筆數
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public Integer getCountryId() {
        return countryId;
    }
    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }
    public Integer getCityId() {
        return cityId;
    }
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
    public List<Integer> getTypeIds() {
        return typeIds;
    }
    public void setTypeIds(List<Integer> typeIds) {
        this.typeIds = typeIds;
    }
    public List<Integer> getTagIds() {
        return tagIds;
    }
    public void setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds;
    }
    public String getOrder() {
        return order;
    }
    public void setOrder(String order) {
        this.order = order;
    }
    public Boolean getDir() {
        return dir;
    }
    public void setDir(Boolean dir) {
        this.dir = dir;
    }
    public Integer getStart() {
        return start;
    }
    public void setStart(Integer start) {
        this.start = start;
    }
    public Integer getRows() {
        return rows;
    }
    public void setRows(Integer rows) {
        this.rows = rows;
    }

    // Getter & Setter 全部補上
    
}
