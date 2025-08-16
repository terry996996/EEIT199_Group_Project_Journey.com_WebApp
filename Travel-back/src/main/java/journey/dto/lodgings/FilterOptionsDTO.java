package journey.dto.lodgings;

import java.util.List;

/**
 * 旅宿搜尋篩選條件資料傳輸物件（DTO）
 * 用於描述搜尋旅宿時可用的篩選條件。
 * 
 * @author FAN
 * @since 250704
 */
public class FilterOptionsDTO {
    private List<BedTypeDTO> bedTypes;
    private List<FacilityDTO> facilities;
    private List<LodgingTypeDTO> lodgingsTypes;

    public FilterOptionsDTO(List<BedTypeDTO> bedTypes, List<FacilityDTO> facilities,
            List<LodgingTypeDTO> lodgingsTypes) {
        this.bedTypes = bedTypes;
        this.facilities = facilities;
        this.lodgingsTypes = lodgingsTypes;
    }

    public List<BedTypeDTO> getBedTypes() {
        return bedTypes;
    }

    public List<FacilityDTO> getFacilities() {
        return facilities;
    }

    public List<LodgingTypeDTO> getLodgingsTypes() {
        return lodgingsTypes;
    }

    public void setBedTypes(List<BedTypeDTO> bedTypes) {
        this.bedTypes = bedTypes;
    }

    public void setFacilities(List<FacilityDTO> facilities) {
        this.facilities = facilities;
    }

    public void setLodgingsTypes(List<LodgingTypeDTO> lodgingsTypes) {
        this.lodgingsTypes = lodgingsTypes;
    }
}