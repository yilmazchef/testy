package be.intecbrussel.student.data.entity;

import java.util.Objects;

public class SolutionEntity extends AEntity {

    private String examId;
    private String teacherId;
    private String content;
    private Integer likes;
    private Integer dislikes;

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public SolutionEntity withExamId(String examId) {
        this.setExamId(examId);
        return this;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public SolutionEntity withTeacherId(String teacherId) {
        this.setTeacherId(teacherId);
        return this;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SolutionEntity withContent(String content, boolean replace) {
        this.setContent(replace ? content : this.getContent().concat(content));
        return this;
    }

    public SolutionEntity withContent(String content) {
        return this.withContent(content, false);
    }

    public SolutionEntity withCode(String code) {
        this.setContent(
                this.getContent().concat("<code>").concat(code).concat("</code>")
        );
        return this;
    }

    public Integer getLikes() {
        return likes;
    }

    public void addLike() {
        this.setLikes(this.getLikes() + 1);
    }

    public void removeLike() {
        this.setLikes(this.getLikes() - 1);
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getDisLikes() {
        return this.dislikes;
    }

    public void addDisLike() {
        this.setDisLikes(this.getDisLikes() + 1);
    }

    public void removeDislike() {
        this.setDisLikes(this.getDisLikes() - 1);
    }

    public void setDisLikes(Integer dislikes) {
        this.dislikes = dislikes;
    }

    public SolutionEntity withLike() {
        this.addLike();
        return this;
    }

    public SolutionEntity withLikes(Integer likes) {
        this.setLikes(likes);
        return this;
    }

    public SolutionEntity withDisLike() {
        this.addDisLike();
        return this;
    }

    public SolutionEntity withDisLikes(Integer dislikes) {
        this.setDisLikes(dislikes);
        return this;
    }

    @Override
    public SolutionEntity withId(String id) {
        return null;
    }

    @Override
    public SolutionEntity withActive(Boolean active) {
        return null;
    }

    @Override
    public String toString() {
        return this.getContent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SolutionEntity)) return false;
        if (!super.equals(o)) return false;
        SolutionEntity that = (SolutionEntity) o;
        return getExamId().equals(that.getExamId()) && Objects.equals(getTeacherId(), that.getTeacherId()) && getContent().equals(that.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getExamId(), getTeacherId(), getContent());
    }
}
