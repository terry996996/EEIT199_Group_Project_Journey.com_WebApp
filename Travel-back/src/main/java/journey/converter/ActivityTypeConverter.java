package journey.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import journey.enums.ActivityTypeEnum;

@Converter(autoApply = true)
public class ActivityTypeConverter implements AttributeConverter<ActivityTypeEnum, String> {

    @Override
    public String convertToDatabaseColumn(ActivityTypeEnum attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public ActivityTypeEnum convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ActivityTypeEnum.valueOf(dbData);
    }

}
