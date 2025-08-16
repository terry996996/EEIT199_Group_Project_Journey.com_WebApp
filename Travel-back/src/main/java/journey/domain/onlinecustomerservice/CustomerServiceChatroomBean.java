package journey.domain.onlinecustomerservice;

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
import journey.domain.payments.TypeEnumBean;
import journey.domain.users.UserBean;

@Entity
@Table(name = "customer_service_chatroom")
public class CustomerServiceChatroomBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Integer chatRoomId;

    @Column(name = "chat_room_name", nullable = false)
    private String chatRoomName;

    @Column(name = "chat_room_create_at", nullable = false)
    private LocalDateTime chatRoomCreateAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private UserBean user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    @JsonBackReference
    private TypeEnumBean type;

    @OneToMany(mappedBy = "chatRoom")
    @JsonManagedReference
    private Set<CustomerServiceMessageBean> messages;

    public Integer getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Integer chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    public LocalDateTime getChatRoomCreateAt() {
        return chatRoomCreateAt;
    }

    public void setChatRoomCreateAt(LocalDateTime chatRoomCreateAt) {
        this.chatRoomCreateAt = chatRoomCreateAt;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public TypeEnumBean getType() {
        return type;
    }

    public void setType(TypeEnumBean type) {
        this.type = type;
    }

    public Set<CustomerServiceMessageBean> getMessages() {
        return messages;
    }

    public void setMessages(Set<CustomerServiceMessageBean> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "CustomerServiceChatroomBean [chatRoomId=" + chatRoomId + ", chatRoomName=" + chatRoomName
                + ", chatRoomCreateAt=" + chatRoomCreateAt + ", user=" + user + ", type=" + type + ", messages="
                + messages + "]";
    }

}
