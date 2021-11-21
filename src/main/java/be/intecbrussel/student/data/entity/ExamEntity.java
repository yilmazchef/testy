package be.intecbrussel.student.data.entity;


import java.sql.Timestamp;
import java.util.Objects;

public class ExamEntity extends AEntity {

    private String session;
    private String code; // TODO: add code to the DB ..
    private java.sql.Timestamp startTime;
    private java.sql.Timestamp endTime;
    private String studentId;
    private String taskId;
    private Double score;
    private Boolean selected;
    private Boolean submitted;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ExamEntity withCode(String code) {
        this.setCode(code);
        return this;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = Timestamp.valueOf(startTime);
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = Timestamp.valueOf(endTime);
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Boolean submitted) {
        this.submitted = submitted;
    }

    public ExamEntity withSubmitted(Boolean submitted) {
        this.setSubmitted(submitted);
        return this;
    }

    public ExamEntity withSelected(Boolean selected) {
        this.setSelected(selected);
        return this;
    }

    public ExamEntity withSession(String session) {
        this.setSession(session);
        return this;
    }

    public ExamEntity withStartTime(Timestamp startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public ExamEntity withEndTime(Timestamp endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public ExamEntity withStudentId(String studentId) {
        this.setStudentId(studentId);
        return this;
    }

    public ExamEntity withTaskId(String taskId) {
        this.setTaskId(taskId);
        return this;
    }

    public ExamEntity withScore(Double score) {
        this.setScore(score);
        return this;
    }

    @Override
    public ExamEntity withId(String id) {
        super.setId(id);
        return this;
    }

    @Override
    public ExamEntity withActive(Boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExamEntity)) return false;
        if (!super.equals(o)) return false;
        ExamEntity that = (ExamEntity) o;
        return Objects.equals(getSession(), that.getSession()) && getCode().equals(that.getCode()) && getStudentId().equals(that.getStudentId()) && getTaskId().equals(that.getTaskId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSession(), getCode(), getStudentId(), getTaskId());
    }
}
