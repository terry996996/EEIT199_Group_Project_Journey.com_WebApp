package journey.repository.attractions;

import journey.domain.attractiontickets.AttractionsBean;
import journey.domain.attractiontickets.AttractionImagesBean;
import journey.domain.attractiontickets.AttractionContentsBean;

import java.util.List;

/**
 * DTO：用來封裝「新增完整景點」的資料結構
 * 包含：景點主體 + 圖片列表 + 內文列表
 * 
 * 專門用於前端傳來一筆複合資料時使用，例如：
 * {
 *   "attraction": {...},
 *   "images": [{...}, {...}],
 *   "contents": [{...}]
 * }
 */
public class AttractionCreateRequest {

    /**
     * 景點主體資料，例如名稱、地址、主圖、國家城市關聯等
     * 對應表格：attraction
     */
    private AttractionsBean attraction;

    /**
     * 該景點所附的多筆圖片資料
     * 對應表格：attraction_images（@OneToMany）
     */
    private List<AttractionImagesBean> images;

    /**
     * 該景點所附的多筆介紹內容，例如段落說明、標題、副標題等
     * 對應表格：attraction_contents（@OneToMany）
     */
    private List<AttractionContentsBean> contents;

    // === Getter / Setter 區 ===

    /**
     * 取得景點主體資料
     */
    public AttractionsBean getAttraction() {
        return attraction;
    }

    /**
     * 設定景點主體資料
     */
    public void setAttraction(AttractionsBean attraction) {
        this.attraction = attraction;
    }

    /**
     * 取得所有圖片清單
     */
    public List<AttractionImagesBean> getImages() {
        return images;
    }

    /**
     * 設定圖片清單
     */
    public void setImages(List<AttractionImagesBean> images) {
        this.images = images;
    }

    /**
     * 取得所有介紹內文清單
     */
    public List<AttractionContentsBean> getContents() {
        return contents;
    }

    /**
     * 設定介紹內文清單
     */
    public void setContents(List<AttractionContentsBean> contents) {
        this.contents = contents;
    }
}