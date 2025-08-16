package journey.dto;

import jakarta.validation.constraints.NotNull;

public class ChatroomRequestDTO {
    @NotNull(message = "Type ID cannot be null")
    private Integer typeId;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
}