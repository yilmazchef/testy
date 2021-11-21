package be.intecbrussel.student.data.entity;


import java.util.Objects;

public class QuestionEntity extends AEntity {

    private Integer orderNo;
    private String header;
    private String content;
    private Double weight;
    private String teacherId;

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public QuestionEntity withOrderNo(Integer orderNo) {
        this.setOrderNo(orderNo);
        return this;
    }

    public QuestionEntity withHeader(String header) {
        this.setHeader(header);
        return this;
    }

    public QuestionEntity withContent(String content) {
        this.setContent(content);
        return this;
    }

    public QuestionEntity withWeight(Double weight) {
        this.setWeight(weight);
        return this;
    }

    public QuestionEntity withTeacherId(String teacherId) {
        this.setTeacherId(teacherId);
        return this;
    }

    @Override
    public QuestionEntity withId(String id) {
        super.setId(id);
        return this;
    }

    @Override
    public QuestionEntity withActive(Boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionEntity)) return false;
        if (!super.equals(o)) return false;
        QuestionEntity that = (QuestionEntity) o;
        return getHeader().equals(that.getHeader()) && getContent().equals(that.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getHeader(), getContent());
    }
}
