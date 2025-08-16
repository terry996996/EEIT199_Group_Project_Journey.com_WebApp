package journey.enums;

import java.util.Arrays;

public enum CommentReportStatusEnum {
    PENDING("待處理"),
    RESOLVED("已處理"),
    REJECTED("已駁回");

    private final String label;

    CommentReportStatusEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static CommentReportStatusEnum fromString(String dbstr) {
        return Arrays
                .stream(CommentReportStatusEnum.values())
                .filter(e -> e.name().equalsIgnoreCase(dbstr))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown status: " + dbstr));
    }
}