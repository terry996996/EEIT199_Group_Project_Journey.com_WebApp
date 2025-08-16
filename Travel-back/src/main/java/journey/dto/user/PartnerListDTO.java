package journey.dto.user;

public class PartnerListDTO {
    private Integer id;
    private ProfileDTO profile;
    private Integer deleteStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }

    public PartnerListDTO(Integer id, ProfileDTO profile, Integer deleteStatus) {
        this.id = id;
        this.profile = profile;
        this.deleteStatus = deleteStatus;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

}
