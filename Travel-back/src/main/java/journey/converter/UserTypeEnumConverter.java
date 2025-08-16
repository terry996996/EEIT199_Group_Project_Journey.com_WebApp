package journey.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import journey.enums.UserTypeEnum;

@Converter(autoApply = true)
public class UserTypeEnumConverter implements AttributeConverter<UserTypeEnum, String> {

    @Override
    public String convertToDatabaseColumn(UserTypeEnum attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public UserTypeEnum convertToEntityAttribute(String dbData) {
        return dbData == null ? null : UserTypeEnum.fromString(dbData);
    }
}
