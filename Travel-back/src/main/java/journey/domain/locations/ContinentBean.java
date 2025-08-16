package journey.domain.locations;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "continents")
public class ContinentBean {
    // ------------ actual columns ------------ //
    @Id
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "name_zh", unique = true, nullable = false)
    private String nameZh;
    // ------------ actual columns ------------ //

    // ------------ mappings ------------//
    @OneToMany(mappedBy = "continent")
    @JsonManagedReference
    private Set<CountryBean> countryBeans;
    // ------------ mappings ------------//

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    @Override
    public String toString() {
        return "ContinentBean [name=" + name + ", nameZh=" + nameZh + "]";
    }

    public Set<CountryBean> getCountryBeans() {
        return countryBeans;
    }

    public void setCountryBeans(Set<CountryBean> countryBeans) {
        this.countryBeans = countryBeans;
    }

}
