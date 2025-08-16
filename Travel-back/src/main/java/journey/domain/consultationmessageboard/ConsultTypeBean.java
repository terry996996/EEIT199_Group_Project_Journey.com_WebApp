package journey.domain.consultationmessageboard;

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
@Table(name = "consult_type")
public class ConsultTypeBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consult_type_id")
    private Integer consultTypeId;

    @Column(name = "consult_type_name", nullable = false, unique = true, length = 500)
    private String consultTypeName;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "consultType")
    @JsonManagedReference
    private Set<ConsultsBean> consults;

    public Integer getConsultTypeId() {
        return consultTypeId;
    }

    public void setConsultTypeId(Integer consultTypeId) {
        this.consultTypeId = consultTypeId;
    }

    public String getConsultTypeName() {
        return consultTypeName;
    }

    public void setConsultTypeName(String consultTypeName) {
        this.consultTypeName = consultTypeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ConsultsBean> getConsults() {
        return consults;
    }

    public void setConsults(Set<ConsultsBean> consults) {
        this.consults = consults;
    }

    @Override
    public String toString() {
        return "ConsultTypeBean [consultTypeId=" + consultTypeId + ", consultTypeName=" + consultTypeName
                + ", description=" + description + ", consults=" + consults + "]";
    }

}
