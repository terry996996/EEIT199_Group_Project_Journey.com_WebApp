package journey.domain.users;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import journey.converter.UserTypeEnumConverter;
import journey.enums.UserTypeEnum;

@Entity
@Table(name = "user_types")
public class UserTypeBean {
    // ------------ actual columns ------------ //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Convert(converter = UserTypeEnumConverter.class)
    @Column(name = "type", unique = true, nullable = false)
    private UserTypeEnum type;
    // ------------ actual columns ------------ //

    // ------------ mappings ------------ //
    @OneToMany(mappedBy = "userType")
    @JsonManagedReference
    private Set<AllUserBean> allUserBeans;
    // ------------ mappings ------------ //

    @Override
    public String toString() {
        return "UserTypeBean [id=" + id + ", type=" + type.toString() + "]";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserTypeEnum getType() {
        return type;
    }

    public void setType(UserTypeEnum type) {
        this.type = type;
    }

    public Set<AllUserBean> getAllUserBeans() {
        return allUserBeans;
    }

    public void setAllUserBeans(Set<AllUserBean> allUserBeans) {
        this.allUserBeans = allUserBeans;
    }

    public UserTypeEnum toEnum() { // 在 UserTypeBean 裡
        return this.type; // 這裡才是 String
    }
}