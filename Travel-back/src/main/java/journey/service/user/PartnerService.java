package journey.service.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import journey.domain.users.PartnerBean;
import journey.domain.users.UserBean;
import journey.dto.user.PartnerListDTO;
import journey.dto.user.ProfileDTO;
import journey.enums.GenderEnum;
import journey.repository.users.PartnerRepository;
import journey.service.locations.CountryService;

/**
 * partner related services
 * 
 * @author 乃文
 * @version 1.0
 * @since 07/03/2025
 */
@Service
@Transactional
public class PartnerService {
    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private CountryService countryService;

    /**
     * add new partner (requires a profile dto)
     * 
     * @param user    the user whom this relates
     * @param profile dto recieved from frontend
     * @return new partner entity if succeeds
     */
    public PartnerBean newPartner(UserBean user, ProfileDTO profile) {
        if (user != null && profile != null) {
            PartnerBean bean = new PartnerBean();

            // map dto to entity
            dto2Bean(bean, profile);

            // add relationship
            bean.setRelatesTo(user);

            return partnerRepository.save(bean);
        }
        return null;
    }

    /**
     * find a partner by id
     * 
     * @return partner entity if exists (delete status == 1)
     */
    public PartnerBean findPartner(Integer id) {
        if (id != null) {
            Optional<PartnerBean> opt = partnerRepository.findById(id);
            if (opt.isPresent() && opt != null) {
                PartnerBean bean = opt.get();

                // only if status not -1 will reutrn
                if (bean.getDeleteStatus() >= 0) {
                    return opt.get();
                }
            }
        }
        return null;
    }

    /**
     * find the partners related to the given user
     * filtered by delete_status > 0
     */
    public List<PartnerListDTO> findAllPartnersByUser(UserBean user) {
        if (user != null) {
            List<PartnerBean> partners = partnerRepository.findByRelatesTo(user);
            if (partners != null && !partners.isEmpty()) {
                return partners
                        .stream()
                        .filter(partner -> partner.getDeleteStatus() >= 0)
                        .map(this::bean2Dto)
                        .toList();
            }
        }
        // return empty list
        return new ArrayList<PartnerListDTO>();
    }

    /**
     * update the partner profile
     * 
     * @param id      partner id
     * @param profile dto from frontend
     * @return the updated partner entity if succeeds
     */
    public PartnerBean updateProfile(Integer id, ProfileDTO profile) {
        if (id != null && profile != null) {
            PartnerBean bean = findPartner(id);
            if (bean != null) {
                dto2Bean(bean, profile);
                return partnerRepository.save(bean);
            }
        }
        return null;
    }

    /**
     * soft deletion (set delete status to 0)
     * 
     * @param id id of the partner entity
     * @return true if delete status change from active to soft deleted
     */
    public PartnerBean removePartner(Integer id) {
        if (id != null) {
            PartnerBean bean = findPartner(id);
            if (bean != null) {
                bean.setDeleteStatus(0);
                bean.setDeleteTime(LocalDateTime.now());
                return partnerRepository.save(bean);
            }
        }
        return null;
    }

    public PartnerBean recoverPartner(Integer id) {
        if (id != null) {
            PartnerBean bean = findPartner(id);
            if (bean != null) {
                bean.setDeleteStatus(1);
                return partnerRepository.save(bean);
            }
        }
        return null;
    }

    /**
     * a method that maps the dto to the partner entity
     * 
     * @param bean    target partner entity
     * @param profile source dto
     */
    private void dto2Bean(PartnerBean bean, ProfileDTO profile) {
        if (bean != null && profile != null) {
            BeanUtils.copyProperties(profile, bean,
                    "gender", "nationality", "residence", "phoneCode");
            bean.setGender(GenderEnum.fromCode(profile.getGender()));
            bean.setNationality(countryService.findCountry(profile.getNationality()));
            bean.setResidence(countryService.findCountry(profile.getResidence()));
        }
    }

    private PartnerListDTO bean2Dto(PartnerBean bean) {
        if (bean != null) {
            ProfileDTO profile = new ProfileDTO();
            BeanUtils.copyProperties(bean, profile, "gender", "nationality", "residence", "phoneCode");
            profile.setGender(bean.getGender().getCode());
            profile.setNationality(bean.getNationality().getId());
            profile.setResidence(bean.getResidence().getId());
            return new PartnerListDTO(bean.getId(), profile, bean.getDeleteStatus());
        }
        return null;
    }
}
