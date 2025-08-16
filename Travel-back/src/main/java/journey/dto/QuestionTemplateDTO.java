package journey.dto;

public class QuestionTemplateDTO {
    private Integer questionTemplateId;
    private String questionTemplate;
    private String replyTemplate;
    private TypeDTO type;

    public Integer getQuestionTemplateId() {
        return questionTemplateId;
    }

    public void setQuestionTemplateId(Integer questionTemplateId) {
        this.questionTemplateId = questionTemplateId;
    }

    public String getQuestionTemplate() {
        return questionTemplate;
    }

    public void setQuestionTemplate(String questionTemplate) {
        this.questionTemplate = questionTemplate;
    }

    public String getReplyTemplate() {
        return replyTemplate;
    }

    public void setReplyTemplate(String replyTemplate) {
        this.replyTemplate = replyTemplate;
    }

    public TypeDTO getType() {
        return type;
    }

    public void setType(TypeDTO type) {
        this.type = type;
    }

    public static class TypeDTO {
        private Integer typeId;
        private String typeName;

        public Integer getTypeId() {
            return typeId;
        }

        public void setTypeId(Integer typeId) {
            this.typeId = typeId;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }
}
