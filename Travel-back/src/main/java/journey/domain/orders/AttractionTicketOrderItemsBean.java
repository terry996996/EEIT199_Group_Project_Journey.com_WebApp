package journey.domain.orders;

import jakarta.persistence.*;
import journey.domain.attractiontickets.AttractionTicketsBean;
import journey.domain.attractiontickets.TicketPackagesBean;
import journey.domain.attractiontickets.TicketTypesBean;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "attraction_ticket_order_items")
public class AttractionTicketOrderItemsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private AttractionTicketsOrderBean order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attraction_id", nullable = false)
    @JsonBackReference
    private AttractionTicketsBean attraction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_packages_id", nullable = false)
    @JsonBackReference
    private TicketPackagesBean ticketPackage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    @JsonBackReference
    private TicketTypesBean option;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "use_date", nullable = false)
    private LocalDateTime useDate;

    @Column(name = "unit_price", nullable = false)
    private Integer unitPrice;

    public AttractionTicketOrderItemsBean() {
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public AttractionTicketsOrderBean getOrder() {
        return order;
    }

    public void setOrder(AttractionTicketsOrderBean order) {
        this.order = order;
    }

    public AttractionTicketsBean getAttraction() {
        return attraction;
    }

    public void setAttraction(AttractionTicketsBean attraction) {
        this.attraction = attraction;
    }

    public TicketPackagesBean getTicketPackage() {
        return ticketPackage;
    }

    public void setTicketPackage(TicketPackagesBean ticketPackage) {
        this.ticketPackage = ticketPackage;
    }

    public TicketTypesBean getOption() {
        return option;
    }

    public void setOption(TicketTypesBean option) {
        this.option = option;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getUseDate() {
        return useDate;
    }

    public void setUseDate(LocalDateTime useDate) {
        this.useDate = useDate;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

}
