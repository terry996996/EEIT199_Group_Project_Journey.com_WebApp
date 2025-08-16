package journey.domain.payments;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import journey.domain.collections.ColletionItemBean;
import journey.domain.onlinecustomerservice.CustomerServiceChatroomBean;
import journey.domain.onlinecustomerservice.CustomerServiceQuestionBean;

@Entity
@Table(name = "type_enum")
public class TypeEnumBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "type_name", nullable = false, unique = true, length = 500)
    private String typeName;

    @OneToMany(mappedBy = "type")
    @JsonManagedReference
    private Set<PaymentsRecordBean> payments;

    @OneToMany(mappedBy = "type")
    @JsonManagedReference
    private Set<CustomerServiceChatroomBean> chatrooms;

    @OneToMany(mappedBy = "type")
    @JsonManagedReference
    private Set<CustomerServiceQuestionBean> questions;

    @OneToMany(mappedBy = "itemType")
    @JsonManagedReference
    private Set<ColletionItemBean> colletionItems;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Set<PaymentsRecordBean> getPayments() {
        return payments;
    }

    public void setPayments(Set<PaymentsRecordBean> payments) {
        this.payments = payments;
    }

    public Set<CustomerServiceChatroomBean> getChatrooms() {
        return chatrooms;
    }

    public void setChatrooms(Set<CustomerServiceChatroomBean> chatrooms) {
        this.chatrooms = chatrooms;
    }

    public Set<CustomerServiceQuestionBean> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<CustomerServiceQuestionBean> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "TypeEnumBean [typeId=" + typeId + ", typeName=" + typeName + ", payments=" + payments + ", chatrooms="
                + chatrooms + ", questions=" + questions + "]";
    }

    public Set<ColletionItemBean> getColletionItems() {
        return colletionItems;
    }

    public void setColletionItems(Set<ColletionItemBean> colletionItems) {
        this.colletionItems = colletionItems;
    }

}
