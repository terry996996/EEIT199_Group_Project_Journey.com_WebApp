package journey.domain.comments;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import journey.domain.users.AllUserBean;

@Entity
@Table(name = "comment_feedbacks")
public class CommentFeedbacksBean {
    @EmbeddedId
    private CommentFeedbacksId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("commentId")
    @JoinColumn(name = "comment_id")
    @JsonBackReference
    private CommentsBean comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private AllUserBean user;

    @Column(name = "feedback", nullable = false)
    private Boolean feedback;

    public CommentFeedbacksBean() {
    }

    public CommentFeedbacksBean(CommentFeedbacksId id, CommentsBean comment, AllUserBean user, Boolean feedback) {
        this.id = id;
        this.comment = comment;
        this.user = user;
        this.feedback = feedback;
    }

    public CommentFeedbacksId getId() {
        return id;
    }

    public void setId(CommentFeedbacksId id) {
        this.id = id;
    }

    public CommentsBean getComment() {
        return comment;
    }

    public void setComment(CommentsBean comment) {
        this.comment = comment;
    }

    public AllUserBean getUser() {
        return user;
    }

    public void setUser(AllUserBean user) {
        this.user = user;
    }

    public Boolean getFeedback() {
        return feedback;
    }

    public void setFeedback(Boolean feedback) {
        this.feedback = feedback;
    }

    @Embeddable
    public static class CommentFeedbacksId implements Serializable {
        private Integer commentId;
        private Integer userId;

        public CommentFeedbacksId() {
        }

        public Integer getCommentId() {
            return commentId;
        }

        public void setCommentId(Integer commentId) {
            this.commentId = commentId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            CommentFeedbacksId that = (CommentFeedbacksId) o;
            return commentId.equals(that.commentId) && userId.equals(that.userId);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(commentId, userId);
        }
    }
}