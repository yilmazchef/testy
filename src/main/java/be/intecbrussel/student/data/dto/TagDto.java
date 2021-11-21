package be.intecbrussel.student.data.dto;

import java.util.Objects;

public class TagDto extends ADto {

    private CategoryDto category;
    private QuestionDto question;
    private Double weight;

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public QuestionDto getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDto question) {
        this.question = question;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public TagDto withCategoryId(String categoryId) {
        this.setCategory((CategoryDto) new CategoryDto().withId(categoryId));
        return this;
    }

    public TagDto withCategory(CategoryDto category) {
        this.setCategory(category);
        return this;
    }

    public TagDto withQuestionId(String questionId) {
        this.setQuestion((QuestionDto) new QuestionDto().withId(questionId));
        return this;
    }

    public TagDto withQuestion(QuestionDto question) {
        this.setQuestion(question);
        return this;
    }

    public TagDto withWeight(Double weight) {
        this.setWeight(weight);
        return this;
    }

    @Override
    public TagDto withId(String id) {
        super.setId(id);
        return this;
    }

    @Override
    public TagDto withActive(Boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagDto)) return false;
        if (!super.equals(o)) return false;
        TagDto that = (TagDto) o;
        return getCategory().equals(that.getCategory()) && getQuestion().equals(that.getQuestion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCategory(), getQuestion());
    }

    @Override
    public String toString() {
        return this.getCategory() + ":" + this.getQuestion();
    }
}
