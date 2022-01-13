package it.vkod.testy.data.entity;

import java.util.Objects;

public class TagEntity extends AEntity {

    private String categoryId;
    private String questionId;
    private Double weight;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public TagEntity withCategoryId(String categoryId) {
        this.setCategoryId(categoryId);
        return this;
    }

    public TagEntity withQuestionId(String questionId) {
        this.setQuestionId(questionId);
        return this;
    }

    public TagEntity withWeight(Double weight) {
        this.setWeight(weight);
        return this;
    }

    @Override
    public TagEntity withId(String id) {
        super.setId(id);
        return this;
    }

    @Override
    public TagEntity withActive(Boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagEntity)) return false;
        if (!super.equals(o)) return false;
        TagEntity tagEntity = (TagEntity) o;
        return getCategoryId().equals(tagEntity.getCategoryId()) && getQuestionId().equals(tagEntity.getQuestionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCategoryId(), getQuestionId());
    }
}
