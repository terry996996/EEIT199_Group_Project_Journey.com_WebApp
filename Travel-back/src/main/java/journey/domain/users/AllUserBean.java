package journey.domain.users;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import journey.domain.comments.CommentFeedbacksBean;
import journey.domain.comments.CommentReportsBean;
import journey.domain.comments.CommentsBean;
import journey.mapping.UserPlanCollectionAuditable;

@Entity
@Table(name = "all_users")
public class AllUserBean extends UserPlanCollectionAuditable {
    // ------------ actual columns ------------ //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "icon", nullable = false)
    private String icon;
    @Column(name = "icon_type", nullable = false)
    private String iconType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_type", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private UserTypeBean userType;
    // ------------ actual columns ------------ //

    // ------------ mappings ------------//
    @OneToOne(mappedBy = "allUserBean")
    @JsonBackReference
    private UserBean user;

    @OneToOne(mappedBy = "allUserBean")
    @JsonBackReference
    private AdminBean admin;

    @OneToOne(mappedBy = "allUserBean")
    @JsonBackReference
    private VendorBean vendor;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<CommentsBean> comments;

    @OneToMany(mappedBy = "createdBy")
    @JsonManagedReference
    private Set<CommentReportsBean> commentReports;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<CommentFeedbacksBean> commentFeedbacks;
    // ------------ mappings ------------//

    @Override
    public String toString() {
        return "AllUserBean [id=" + id + ", username=" + username + ", passwordHash=" + passwordHash
                + ", email=" + email + ", createdAt=" + createdAt
                + ", lastModified=" + lastModified + ", userType=" + userType
                + ", icon=" + icon + ", iconType=" + iconType + "]";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconType() {
        return iconType;
    }

    public void setIconType(String iconType) {
        this.iconType = iconType;
    }

    public UserTypeBean getUserType() {
        return userType;
    }

    public void setUserType(UserTypeBean userTypeBean) {
        this.userType = userTypeBean;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public AdminBean getAdmin() {
        return admin;
    }

    public void setAdmin(AdminBean admin) {
        this.admin = admin;
    }

    public VendorBean getVendor() {
        return vendor;
    }

    public void setVendor(VendorBean vendor) {
        this.vendor = vendor;
    }

    public Set<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(Set<CommentsBean> comments) {
        this.comments = comments;
    }

    public Set<CommentReportsBean> getCommentReports() {
        return commentReports;
    }

    public void setCommentReports(Set<CommentReportsBean> commentReports) {
        this.commentReports = commentReports;
    }

    public Set<CommentFeedbacksBean> getCommentFeedbacks() {
        return commentFeedbacks;
    }

    public void setCommentFeedbacks(Set<CommentFeedbacksBean> commentFeedbacks) {
        this.commentFeedbacks = commentFeedbacks;
    }

}
