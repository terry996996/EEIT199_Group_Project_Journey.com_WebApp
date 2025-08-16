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

// 付款方式資料表 (payments)
@Entity
@Table(name = "payments")
public class PaymentsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_tool_id")
    private Integer paymentToolId;

    @Column(name = "payment_tool", nullable = false, unique = true, length = 500)
    private String paymentTool;

    @OneToMany(mappedBy = "paymentTool")
    @JsonManagedReference
    private Set<PaymentsRecordBean> payments;

    public Integer getPaymentToolId() {
        return paymentToolId;
    }

    public void setPaymentToolId(Integer paymentToolId) {
        this.paymentToolId = paymentToolId;
    }

    public String getPaymentTool() {
        return paymentTool;
    }

    public void setPaymentTool(String paymentTool) {
        this.paymentTool = paymentTool;
    }

    public Set<PaymentsRecordBean> getPayments() {
        return payments;
    }

    public void setPayments(Set<PaymentsRecordBean> payments) {
        this.payments = payments;
    }

    @Override
    public String toString() {
        return "PaymentsBean [paymentToolId=" + paymentToolId + ", paymentTool=" + paymentTool + ", payments="
                + payments + "]";
    }

}
