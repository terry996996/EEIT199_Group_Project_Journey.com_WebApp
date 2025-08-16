package journey.domain.consultationmessageboard;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import journey.domain.users.UserBean;

@Entity
@Table(name = "consults")
public class ConsultsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consult_id")
    private Integer consultId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "gender", nullable = false)
    private Integer gender;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "consult_content", nullable = false)
    private String consultContent;

    @Column(name = "consult_time", nullable = false)
    private LocalDateTime consultTime = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private UserBean user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consult_type_id", nullable = false)
    @JsonBackReference
    private ConsultTypeBean consultType;

    @OneToMany(mappedBy = "consult")
    @JsonManagedReference
    private Set<ReplyBean> replies;

    public Integer getConsultId() {
        return consultId;
    }

    public void setConsultId(Integer consultId) {
        this.consultId = consultId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getConsultContent() {
        return consultContent;
    }

    public void setConsultContent(String consultContent) {
        this.consultContent = consultContent;
    }

    public LocalDateTime getConsultTime() {
        return consultTime;
    }

    public void setConsultTime(LocalDateTime consultTime) {
        this.consultTime = consultTime;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public ConsultTypeBean getConsultType() {
        return consultType;
    }

    public void setConsultType(ConsultTypeBean consultType) {
        this.consultType = consultType;
    }

    public Set<ReplyBean> getReplies() {
        return replies;
    }

    public void setReplies(Set<ReplyBean> replies) {
        this.replies = replies;
    }

    @Override
    public String toString() {
        return "ConsultsBean [consultId=" + consultId + ", name=" + name + ", gender=" + gender + ", email=" + email
                + ", phone=" + phone + ", consultContent=" + consultContent + ", consultTime=" + consultTime + ", user="
                + user + ", consultType=" + consultType + ", replies=" + replies + "]";
    }

}
