package journey.repository.traffictickets;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import journey.domain.traffictickets.TrafficTicketsBean;

public interface TrafficTicketsRepository extends
                JpaRepository<TrafficTicketsBean, Integer> {

        @Query("SELECT t FROM TrafficTicketsBean t JOIN FETCH t.country")
        List<TrafficTicketsBean> findAllWithCountry();

        // 根据名称模糊查询，不区分大小写
        List<TrafficTicketsBean> findByNameContainingIgnoreCase(String name);

        @Query("SELECT t FROM TrafficTicketsBean t WHERE " +
                        "(:name IS NULL OR t.name LIKE %:name%) AND " +
                        "(:minPrice IS NULL OR t.price >= :minPrice) AND " +
                        "(:maxPrice IS NULL OR t.price <= :maxPrice) AND " +
                        "(COALESCE(:selectedTypes, NULL) IS NULL OR t.transportType IN :selectedTypes) AND " +
                        "(COALESCE(:selectedRegions, NULL) IS NULL OR t.area IN :selectedRegions)")
        List<TrafficTicketsBean> findByFilters(
                        @Param("name") String name,
                        @Param("selectedTypes") List<String> selectedTypes,
                        @Param("selectedRegions") List<String> selectedRegions,
                        @Param("minPrice") Integer minPrice,
                        @Param("maxPrice") Integer maxPrice);

}
