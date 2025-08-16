package journey.converter;

import java.util.Arrays;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import journey.enums.CommentReportStatusEnum;

@Converter(autoApply = true)
public class CommentReportStatusEnumConverter implements AttributeConverter<CommentReportStatusEnum, String> {

    @Override
    public String convertToDatabaseColumn(CommentReportStatusEnum attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public CommentReportStatusEnum convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
        
        try {
            // 先嘗試直接轉換（大小寫敏感）
            return CommentReportStatusEnum.valueOf(dbData);
        } catch (IllegalArgumentException ex) {
            // 如果失敗，再嘗試不分大小寫轉換
            return Arrays.stream(CommentReportStatusEnum.values())
                    .filter(enumValue -> enumValue.name().equalsIgnoreCase(dbData.trim()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid status: " + dbData));
        }
    }
}