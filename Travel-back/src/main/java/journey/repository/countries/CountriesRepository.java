package journey.repository.countries;

import org.springframework.data.jpa.repository.JpaRepository;

import journey.domain.locations.CountryBean;

public interface CountriesRepository extends JpaRepository<CountryBean, Integer> {

}
