package be.intecbrussel.student.data.dto;


import java.util.Objects;

public class TaskDto extends ADto {

    public enum Type {
        UNDEFINED,
        TODO,
        CHOICE,
        CHALLENGE,
        UML2CODE,
        INFO,
        WEIGHTLESS
    }

    private String value;
    private Integer orderNo;
    private Double weight;
    private Type type;
    private QuestionDto question;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type = Type.valueOf(type);
    }

    public QuestionDto getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDto question) {
        this.question = question;
    }

    public void setQuestionId(String questionId) {
        this.setQuestion(new QuestionDto().withId(questionId));
    }

    public TaskDto withValue(String value) {
        this.setValue(value);
        return this;
    }

    public TaskDto withOrderNo(Integer orderNo) {
        this.setOrderNo(orderNo);
        return this;
    }

    public TaskDto withWeight(Double weight) {
        this.setWeight(weight);
        return this;
    }

    public TaskDto withType(String type) {
        this.setType(type);
        return this;
    }

    public TaskDto withType(Type type) {
        this.setType(type);
        return this;
    }

    public TaskDto withQuestionId(String questionId) {
        this.setQuestion(new QuestionDto().withId(questionId));
        return this;
    }

    public TaskDto withQuestion(QuestionDto question) {
        this.setQuestion(question);
        return this;
    }

    @Override
    public TaskDto withId(String id) {
        super.setId(id);
        return this;
    }

    @Override
    public TaskDto withActive(Boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskDto)) return false;
        if (!super.equals(o)) return false;
        TaskDto that = (TaskDto) o;
        return getValue().equals(that.getValue()) && getType() == that.getType() && getQuestion().equals(that.getQuestion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getValue(), getType(), getQuestion());
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
