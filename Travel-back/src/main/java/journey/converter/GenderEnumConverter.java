package journey.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import journey.enums.GenderEnum;

@Converter(autoApply = true)
public class GenderEnumConverter implements AttributeConverter<GenderEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(GenderEnum genderEnum) {
        if (genderEnum != null) {
            return genderEnum.getCode();
        }
        return null;
    }

    @Override
    public GenderEnum convertToEntityAttribute(Integer dbData) {
        if (dbData != null) {
            return GenderEnum.fromCode(dbData);
        }
        return null;
    }

}
