package it.vkod.testy.data.dto;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class QuestionDto extends ADto {

    private Integer orderNo;
    private String header;
    private List<String> content = new ArrayList<>();
    private Double weight;
    private UserDto teacher;

    private final List<TaskDto> tasks = new ArrayList<>();
    private final List<TagDto> tags = new ArrayList<>();

    public List<TaskDto> getTasks() {
        return tasks;
    }

    public void addTask(TaskDto task) {
        this.tasks.add(task);
    }

    public void removeTask(TaskDto task) {
        this.tasks.remove(task);
    }

    public QuestionDto withTask(TaskDto task) {
        this.tasks.add(task);
        return this;
    }

    public QuestionDto withTasks(TaskDto... tasks) {
        Collections.addAll(this.tasks, tasks);
        return this;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public QuestionDto withTag(TagDto tag) {
        this.tags.add(tag);
        return this;
    }

    public QuestionDto withTags(TagDto... tags) {
        Collections.addAll(this.tags, tags);
        return this;
    }

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

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public void addContent(String content) {
        this.getContent().add(content);
    }

    public void removeContent(Integer paragraphNo) {
        this.getContent().remove(Math.abs(paragraphNo) - 1);
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public UserDto getTeacher() {
        return teacher;
    }

    public void setTeacher(UserDto teacher) {
        this.teacher = teacher;
    }

    public void setTeacherId(String teacher) {
        this.teacher = new UserDto().withId(teacher);
    }

    public QuestionDto withOrderNo(Integer orderNo) {
        this.setOrderNo(orderNo);
        return this;
    }

    public QuestionDto withHeader(String header) {
        this.setHeader(header);
        return this;
    }

    public QuestionDto withContent(String content) {
        this.addContent(content);
        return this;
    }

    public QuestionDto withoutContent(String content) {
        if (this.getContent().contains(content))
            this.removeContent(this.getContent().indexOf(content));
        return this;
    }

    public QuestionDto withWeight(Double weight) {
        this.setWeight(weight);
        return this;
    }

    public QuestionDto withTeacherId(String teacherId) {
        this.setTeacher(new UserDto().withId(teacherId));
        return this;
    }

    public QuestionDto withTeacher(UserDto teacher) {
        this.setTeacher(teacher);
        return this;
    }

    @Override
    public QuestionDto withId(String id) {
        super.setId(id);
        return this;
    }

    @Override
    public QuestionDto withActive(Boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionDto)) return false;
        if (!super.equals(o)) return false;
        QuestionDto that = (QuestionDto) o;
        return getHeader().equals(that.getHeader()) && getContent().equals(that.getContent()) && Objects.equals(getTasks(), that.getTasks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getHeader(), getContent(), getTasks());
    }

    @Override
    public String toString() {
        final var contentBuilder = new StringBuilder();
        final var paragraphs = this.content.toArray(String[]::new);
        for (String paragraph : paragraphs) {
            if (paragraph.startsWith("<code>"))
                contentBuilder.append("\n").append("<b>").append(paragraph).append("</b>").append("\n");
            else
                contentBuilder.append(paragraph);
        }

        return contentBuilder.toString();
    }
}
