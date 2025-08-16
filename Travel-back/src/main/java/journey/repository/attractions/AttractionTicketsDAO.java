package journey.repository.attractions;

import journey.domain.attractiontickets.AttractionTicketsBean;
import journey.dto.attractions.AttractionSearchDto;

import java.util.List;

/**
 * 票券 DAO 介面：
 * 用於條件查詢 AttractionTicketsBean（支援模糊搜尋、分類、國家城市、狀態等）
 */
public interface AttractionTicketsDAO {

    /**
     * 多條件查詢票券清單（支援模糊搜尋、城市/國家篩選、狀態、排序與分頁）
     *
     * @param dto 查詢參數 DTO，包含：
     *            - keyword：模糊搜尋名稱
     *            - countryId：國家 ID
     *            - cityId：城市 ID
     *            - typeId / tagId：可選擴充
     *            - state：啟用狀態
     *            - order：排序欄位名稱，如 views、price、createdAt
     *            - dir：排序方向 true=DESC / false=ASC
     *            - start：分頁起點
     *            - rows：每頁筆數
     * @return 符合條件的票券清單
     */
    List<AttractionTicketsBean> find(AttractionSearchDto dto);

    /**
     * 計算符合條件的票券總筆數（用於分頁）
     *
     * @param dto 同上，查詢條件 DTO
     * @return 符合條件的總筆數
     */
    long count(AttractionSearchDto dto);
}
