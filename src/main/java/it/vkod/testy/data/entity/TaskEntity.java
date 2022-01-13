package it.vkod.testy.data.entity;


import java.util.Objects;

public class TaskEntity extends AEntity {

    private String value;
    private Integer orderNo;
    private Double weight;
    private String type;
    private String questionId;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public TaskEntity withValue(String value) {
        this.setValue(value);
        return this;
    }

    public TaskEntity withOrderNo(Integer orderNo) {
        this.setOrderNo(orderNo);
        return this;
    }

    public TaskEntity withWeight(Double weight) {
        this.setWeight(weight);
        return this;
    }

    public TaskEntity withType(String type) {
        this.setType(type);
        return this;
    }

    public TaskEntity withQuestionId(String questionId) {
        this.setQuestionId(questionId);
        return this;
    }

    @Override
    public TaskEntity withId(String id) {
        super.setId(id);
        return this;
    }

    @Override
    public TaskEntity withActive(Boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskEntity)) return false;
        if (!super.equals(o)) return false;
        TaskEntity that = (TaskEntity) o;
        return getValue().equals(that.getValue()) && getType().equals(that.getType()) && getQuestionId().equals(that.getQuestionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getValue(), getType(), getQuestionId());
    }
}
