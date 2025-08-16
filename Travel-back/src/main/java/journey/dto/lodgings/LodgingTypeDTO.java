package journey.dto.lodgings;

/**
 * 旅宿類型資料傳輸物件（DTO）
 * 用於描述旅宿的類型資訊。
 * 
 * @author FAN
 * @since 250704
 */
public class LodgingTypeDTO {
    private Integer id;
    private String name;

    public LodgingTypeDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LodgingTypeDTO that = (LodgingTypeDTO) o;
        return id.equals(that.id) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return id.hashCode() + name.hashCode();
    }
}