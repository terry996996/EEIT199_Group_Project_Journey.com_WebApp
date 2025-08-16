package journey.enums;

import java.util.Arrays;

public enum UserTypeEnum {
    USER,
    ADMIN,
    VENDOR;

    public static UserTypeEnum fromString(String dbstr) {
        return Arrays
                .stream(UserTypeEnum.values())
                .filter(e -> e.name().equalsIgnoreCase(dbstr))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid User type: " + dbstr));
    }
}
