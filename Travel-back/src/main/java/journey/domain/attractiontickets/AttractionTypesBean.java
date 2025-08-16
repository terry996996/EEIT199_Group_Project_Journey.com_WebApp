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
@Table(name = "attraction_types")

public class AttractionTypesBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String type;

    @ManyToMany(mappedBy = "attractionTypes")
    @JsonBackReference
    private Set<AttractionTicketsBean> attractionTickets;

    @ManyToMany(mappedBy = "types")
    @JsonBackReference
    private Set<AttractionsBean> attractions;

    public AttractionTypesBean() {
    }

    public AttractionTypesBean(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<AttractionTicketsBean> getAttractionTickets() {
        return attractionTickets;
    }

    public void setAttractionTickets(Set<AttractionTicketsBean> attractionTickets) {
        this.attractionTickets = attractionTickets;
    }

    public Set<AttractionsBean> getAttractions() {
        return attractions;
    }

    public void setAttractions(Set<AttractionsBean> attractions) {
        this.attractions = attractions;
    }
}
