package be.intecbrussel.student.data.dto;

import be.intecbrussel.student.data.entity.AEntity;

import java.util.Objects;

public class SolutionDto extends AEntity {

    private ExamDto exam;
    private UserDto teacher;
    private String content;
    private Integer likes;
    private Integer dislikes;

    public ExamDto getExam() {
        return exam;
    }

    public void setExam(ExamDto exam) {
        this.exam = exam;
    }

    public void setExamId(String examId) {
        this.setExam((this.getExam() == null) ? new ExamDto().withId(examId) : this.getExam().withId(examId));
    }

    public SolutionDto withExamId(String examId) {
        this.setExamId(examId);
        return this;
    }

    public SolutionDto withExam(ExamDto exam) {
        this.setExam(exam);
        return this;
    }

    public UserDto getTeacher() {
        return teacher;
    }

    public void setTeacher(UserDto teacher) {
        this.teacher = teacher;
    }

    public void setTeacherId(String teacherId) {
        this.setTeacher((this.getTeacher() == null) ? new UserDto().withId(teacherId) : this.getTeacher().withId(teacherId));
    }

    public SolutionDto withTeacher(UserDto teacher) {
        this.setTeacher(teacher);
        return this;
    }

    public SolutionDto withTeacherId(String teacherId) {
        this.setTeacherId(teacherId);
        return this;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SolutionDto withContent(String content, boolean replace) {
        this.setContent(replace ? content : this.getContent().concat(content));
        return this;
    }

    public SolutionDto withContent(String content) {
        return this.withContent(content, false);
    }

    public SolutionDto withCode(String code) {
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

    public SolutionDto withLike() {
        this.addLike();
        return this;
    }

    public SolutionDto withLikes(Integer likes) {
        this.setLikes(likes);
        return this;
    }

    public SolutionDto withDisLike() {
        this.addDisLike();
        return this;
    }

    public SolutionDto withDisLikes(Integer dislikes) {
        this.setDisLikes(dislikes);
        return this;
    }

    @Override
    public SolutionDto withId( String id) {
        return null;
    }

    @Override
    public SolutionDto withActive(Boolean active) {
        return null;
    }

    @Override
    public String toString() {
        return this.getContent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SolutionDto)) return false;
        if (!super.equals(o)) return false;
        SolutionDto that = (SolutionDto) o;
        return getExam().equals(that.getExam()) && getContent().equals(that.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getExam(), getContent());
    }
}
