package journey.dto.lodgings;

import java.util.List;

/**
 * 旅宿搜尋回應資料傳輸物件（DTO）
 * 用於包裝旅宿搜尋結果與篩選條件。
 * 
 * @author FAN
 * @since 250704
 */
public class SearchResponseDTO {
    private List<LodgingSearchResponseDTO> results;
    private FilterOptionsDTO filterOptions;

    public SearchResponseDTO(List<LodgingSearchResponseDTO> results, FilterOptionsDTO filterOptions) {
        this.results = results;
        this.filterOptions = filterOptions;
    }

    public List<LodgingSearchResponseDTO> getResults() {
        return results;
    }

    public FilterOptionsDTO getFilterOptions() {
        return filterOptions;
    }

    public void setResults(List<LodgingSearchResponseDTO> results) {
        this.results = results;
    }

    public void setFilterOptions(FilterOptionsDTO filterOptions) {
        this.filterOptions = filterOptions;
    }
}