package journey.domain.consultationmessageboard;

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
import jakarta.persistence.Table;
import journey.domain.users.AdminBean;

@Entity
@Table(name = "reply")
public class ReplyBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Integer replyId;

    @Column(name = "reply_by", nullable = false)
    private String replyBy;

    @Column(name = "reply_time", nullable = false)
    private LocalDateTime replyTime = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consult_id", nullable = false)
    @JsonBackReference
    private ConsultsBean consult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    @JsonBackReference
    private AdminBean admin;

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public String getReplyBy() {
        return replyBy;
    }

    public void setReplyBy(String replyBy) {
        this.replyBy = replyBy;
    }

    public LocalDateTime getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(LocalDateTime replyTime) {
        this.replyTime = replyTime;
    }

    public ConsultsBean getConsult() {
        return consult;
    }

    public void setConsult(ConsultsBean consult) {
        this.consult = consult;
    }

    public AdminBean getAdmin() {
        return admin;
    }

    public void setAdmin(AdminBean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "ReplyBean [replyId=" + replyId + ", replyBy=" + replyBy + ", replyTime=" + replyTime + ", consult="
                + consult + ", admin=" + admin + "]";
    }

}
