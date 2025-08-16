package journey.repository.attractions;

import journey.domain.attractiontickets.AttractionTicketsBean;
import journey.dto.attractions.AttractionSearchDto;
import journey.dto.attractions.AttractionTicketMainDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AttractionTicketMainDAOImpl implements AttractionTicketMainDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AttractionTicketMainDto> searchMainTickets(AttractionSearchDto dto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AttractionTicketsBean> cq = cb.createQuery(AttractionTicketsBean.class);
        Root<AttractionTicketsBean> root = cq.from(AttractionTicketsBean.class);
        List<Predicate> predicates = new ArrayList<>();

        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
            predicates.add(cb.like(root.get("name"), "%" + dto.getKeyword() + "%"));
        }

        if (dto.getCountryId() != null) {
            predicates.add(cb.equal(root.get("country").get("id"), dto.getCountryId()));
        }

        if (dto.getCityId() != null) {
            predicates.add(cb.equal(root.get("city").get("id"), dto.getCityId()));
        }

        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            Join<Object, Object> tagsJoin = root.join("attractionTags", JoinType.LEFT);
            predicates.add(tagsJoin.get("id").in(dto.getTagIds()));
            cq.distinct(true);
        }

        if (dto.getTypeIds() != null && !dto.getTypeIds().isEmpty()) {
            Join<Object, Object> typesJoin = root.join("attractionTypes", JoinType.LEFT);
            predicates.add(typesJoin.get("id").in(dto.getTypeIds()));
            cq.distinct(true);
        }

        cq.where(predicates.toArray(new Predicate[0]));

        List<AttractionTicketsBean> tickets = entityManager.createQuery(cq).getResultList();

        return tickets.stream().map(ticket -> {
            AttractionTicketMainDto result = new AttractionTicketMainDto();
            result.setId(ticket.getId());
            result.setName(ticket.getName());
            result.setDescription(ticket.getDescription());
            result.setAddress(ticket.getAddress());
            result.setImageUrl(ticket.getImageUrl());
            result.setViews(ticket.getViews());

            if (ticket.getCountry() != null) {
                result.setCountryName(ticket.getCountry().getName());
            }

            if (ticket.getCity() != null) {
                result.setCityName(ticket.getCity().getName());
            }

            if (ticket.getAttractionTags() != null) {
                result.setTagNames(
                    ticket.getAttractionTags().stream().map(tag -> tag.getTagName()).collect(Collectors.toList())
                );
            }

            if (ticket.getAttractionTypes() != null) {
                result.setTypeNames(
                    ticket.getAttractionTypes().stream().map(type -> type.getType()).collect(Collectors.toList())
                );
            }

            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AttractionTicketsBean> find(AttractionSearchDto dto) {
        return null; // 可選擇不實作
    }

    @Override
    public long count(AttractionSearchDto dto) {
        return 0; // 可選擇不實作
    }
    
}
