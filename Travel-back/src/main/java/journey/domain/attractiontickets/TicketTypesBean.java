package journey.domain.attractiontickets;

import jakarta.persistence.*;
import journey.domain.orders.AttractionTicketOrderItemsBean;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "ticket_types")
public class TicketTypesBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    @JsonBackReference
    private TicketPackagesBean ticketPackage;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "ticket_name", nullable = false, length = 100)
    private String ticketName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private LocalDateTime date;

    @OneToMany(mappedBy = "option")
    @JsonManagedReference
    private Set<AttractionTicketOrderItemsBean> attractionTicketOrderItems;

    public TicketTypesBean() {
    }

    public TicketTypesBean(Integer id, TicketPackagesBean ticketPackage, Integer price, String ticketName,
            Integer quantity, LocalDateTime date) {
        this.id = id;
        this.ticketPackage = ticketPackage;
        this.price = price;
        this.ticketName = ticketName;
        this.quantity = quantity;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TicketPackagesBean getTicketPackage() {
        return ticketPackage;
    }

    public void setTicketPackage(TicketPackagesBean ticketPackage) {
        this.ticketPackage = ticketPackage;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Set<AttractionTicketOrderItemsBean> getAttractionTicketOrderItems() {
        return attractionTicketOrderItems;
    }

    public void setAttractionTicketOrderItems(Set<AttractionTicketOrderItemsBean> attractionTicketOrderItems) {
        this.attractionTicketOrderItems = attractionTicketOrderItems;
    }
}