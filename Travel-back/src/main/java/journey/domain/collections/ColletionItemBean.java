package journey.domain.collections;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import journey.domain.payments.TypeEnumBean;

@Entity
@Table(name = "collection_items")
public class ColletionItemBean {
    // ------------ actual columns ------------ //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "item_id", nullable = false)
    private Integer itemId;
    @Column(name = "added_time", nullable = false)
    private LocalDateTime addedTime;
    @Column(name = "delete_time")
    private LocalDateTime deleteTime;
    @Column(name = "delete_status", nullable = false)
    private Integer deleteStatus = 1;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private CollectionBean collectionId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_type", nullable = false)
    @JsonBackReference
    private TypeEnumBean itemType;
    // ------------ actual columns ------------ //

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public LocalDateTime getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(LocalDateTime addedTime) {
        this.addedTime = addedTime;
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

    public CollectionBean getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(CollectionBean collectionId) {
        this.collectionId = collectionId;
    }

    public TypeEnumBean getItemType() {
        return itemType;
    }

    public void setItemType(TypeEnumBean itemType) {
        this.itemType = itemType;
    }

    @Override
    public String toString() {
        return "ColletionItemBean [id=" + id + ", itemId=" + itemId + ", addedTime=" + addedTime + ", deleteTime="
                + deleteTime + ", deleteStatus=" + deleteStatus + ", collectionId=" + collectionId + ", itemType="
                + itemType.getTypeName() + "]";
    }

    @PrePersist
    public void perPersist() {
        if (this.addedTime == null)
            this.addedTime = LocalDateTime.now();
        if (this.deleteStatus == null)
            this.deleteStatus = 1;
    }
}
