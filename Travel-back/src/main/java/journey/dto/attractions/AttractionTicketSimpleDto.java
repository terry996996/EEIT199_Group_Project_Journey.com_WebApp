package journey.dto.attractions;

public class AttractionTicketSimpleDto {

    private Integer id;
    private String name;
    private String imageUrl;
    private String address;
    private String description;
    private Integer minPrice; // 從票種中擷取的最低價格
    private Integer views;
    
    public AttractionTicketSimpleDto(Integer id, String name, String imageUrl, String address,
    String description, Integer minPrice, Integer views) {
this.id = id;
this.name = name;
this.imageUrl = imageUrl;
this.address = address;
this.description = description;
this.minPrice = minPrice;
this.views = views;
}

// Getter & Setter
public Integer getId() { return id; }
public void setId(Integer id) { this.id = id; }

public String getName() { return name; }
public void setName(String name) { this.name = name; }

public String getImageUrl() { return imageUrl; }
public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

public String getAddress() { return address; }
public void setAddress(String address) { this.address = address; }

public String getDescription() { return description; }
public void setDescription(String description) { this.description = description; }

public Integer getMinPrice() { return minPrice; }
public void setMinPrice(Integer minPrice) { this.minPrice = minPrice; }

public Integer getViews() { return views; }
public void setViews(Integer views) { this.views = views; }
}
    