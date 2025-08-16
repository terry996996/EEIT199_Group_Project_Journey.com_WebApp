package journey.domain.attractiontickets;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "attraction_tags")

public class AttractionTagsBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "tag_name", nullable = false, unique = true, length = 50)
    private String tagName;

    @ManyToMany(mappedBy = "attractionTags")
    @JsonBackReference
    private Set<AttractionTicketsBean> attractionTickets;

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    private Set<AttractionsBean> attractions;

    public AttractionTagsBean() {
    }

    public AttractionTagsBean(Integer id, String tagName) {
        this.id = id;
        this.tagName = tagName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Set<AttractionsBean> getAttractions() {
        return attractions;
    }

    public void setAttractions(Set<AttractionsBean> attractions) {
        this.attractions = attractions;
    }

    public Set<AttractionTicketsBean> getAttractionTickets() {
        return attractionTickets;
    }

    public void setAttractionTickets(Set<AttractionTicketsBean> attractionTickets) {
        this.attractionTickets = attractionTickets;
    }

}
