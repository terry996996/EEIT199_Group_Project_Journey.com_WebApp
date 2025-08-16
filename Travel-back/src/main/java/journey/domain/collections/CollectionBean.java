package journey.domain.collections;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
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
import journey.mapping.UserPlanCollectionAuditable;

@Entity
@Table(name = "collections")
public class CollectionBean extends UserPlanCollectionAuditable {
    // ------------ actual columns ------------ //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "delete_time")
    private LocalDateTime deleteTime;
    @Column(name = "delete_status", nullable = false)
    private Integer deleteStatus = 1;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private UserBean userId;
    // ------------ actual columns ------------ //

    // ------------ mappings ------------ //
    @OneToMany(mappedBy = "collectionId", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private Set<ColletionItemBean> colletionItemBeans;
    // ------------ mappings ------------ //

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(LocalDateTime deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public UserBean getUserId() {
        return userId;
    }

    public void setUserId(UserBean userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "CollectionBean [id=" + id + ", title=" + title + ", description=" + description + ", createdAt="
                + createdAt + ", lastModified=" + lastModified + ", deleteTime=" + deleteTime + ", deleteStatus="
                + deleteStatus + ", userId=" + userId.getId() + "]";
    }

    public Set<ColletionItemBean> getColletionItemBeans() {
        return colletionItemBeans;
    }

    public void setColletionItemBeans(Set<ColletionItemBean> colletionItemBeans) {
        this.colletionItemBeans = colletionItemBeans;
    }

}
