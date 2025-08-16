package journey.dto.attractions;

public class TicketContentDto {

    private Integer id; // 主鍵 ID（修改時用）
    private String title; // 區塊標題
    private String subtitle; // 副標
    private String description; // 編輯器 HTML 內文
    private Integer ticketId; // 所屬票券 ID

    // ---------- Getter / Setter ----------
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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }
}
