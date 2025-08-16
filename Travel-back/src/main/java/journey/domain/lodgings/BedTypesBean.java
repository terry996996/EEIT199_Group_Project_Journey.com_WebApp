package journey.domain.lodgings;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "bed_types")
public class BedTypesBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "bedType")
    @JsonManagedReference
    private Set<RoomTypeBedsBean> roomTypeBeds;

    public BedTypesBean() {
        // 給 JPA 和 Jackson 使用
    }

    public BedTypesBean(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    // getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RoomTypeBedsBean> getRoomTypeBeds() {
        return roomTypeBeds;
    }

    public void setRoomTypeBeds(Set<RoomTypeBedsBean> roomTypeBeds) {
        this.roomTypeBeds = roomTypeBeds;
    }

}