package journey.dto.user;

public class RegisterUserDTO {
    private RegistrationFormDTO form;
    private ProfileDTO profile;

    public RegistrationFormDTO getForm() {
        return form;
    }

    public void setForm(RegistrationFormDTO registrationFormDTO) {
        this.form = registrationFormDTO;
    }

    public ProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileDTO profileDTO) {
        this.profile = profileDTO;
    }

}
