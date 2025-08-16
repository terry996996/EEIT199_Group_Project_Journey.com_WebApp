package journey.domain.attractiontickets;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "attraction_images")
public class AttractionImagesBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Integer imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attraction_id", nullable = false)
    @JsonBackReference
    private AttractionsBean attraction;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @Column(name = "image_type", nullable = false, length = 50)
    private String imageType;

    @Column(name = "alt_text", length = 255)
    private String altText;

    @Column(name = "sort_order")
    private Integer sortOrder;

    public AttractionImagesBean() {
    }

    public AttractionImagesBean(Integer imageId, AttractionsBean attraction, String imageUrl, String imageType,
            String altText,
            Integer sortOrder) {
        this.imageId = imageId;
        this.attraction = attraction;
        this.imageUrl = imageUrl;
        this.imageType = imageType;
        this.altText = altText;
        this.sortOrder = sortOrder;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public AttractionsBean getAttraction() {
        return attraction;
    }

    public void setAttraction(AttractionsBean attraction) {
        this.attraction = attraction;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

}
