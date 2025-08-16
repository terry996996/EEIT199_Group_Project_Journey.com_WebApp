package journey.enums;

public enum GenderEnum {
    MALE(1),
    FEMALE(2),
    OTHER(3);

    private final int code;

    private GenderEnum(int code) {
        this.code = code;

    }

    public int getCode() {
        return code;
    }

    public static GenderEnum fromCode(Integer code) {
        if (code != null) {
            for (GenderEnum genderEnum : values()) {
                if (genderEnum.code == code) {
                    return genderEnum;
                }
            }
            throw new IllegalArgumentException("Invalid code: " + code);
        }
        return null;
    }
}
