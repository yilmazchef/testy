package it.vkod.testy.views.anonymous;

import it.vkod.testy.data.dto.ExamDto;
import it.vkod.testy.data.dto.TaskDto;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.List;
import java.util.stream.Collectors;

@AnonymousAllowed
public class AnonymousExamResultsGrid extends Grid<ExamDto> {

    public AnonymousExamResultsGrid(List<ExamDto> exams) {

        addThemeVariants(GridVariant.LUMO_COMPACT);
        setSelectionMode(Grid.SelectionMode.NONE);
        removeAllColumns();
        addColumn(examDto -> {
            final var content = String.join("\n", examDto.getTask().getQuestion().getContent());
            return content.length() > 100 ? content.substring(0, 100) : content;
        }).setHeader("Question").setKey("question");
        addColumn(examDto -> {
            final var task = examDto.getTask();
            return task.getValue();
        }).setHeader("Task").setKey("task");
        addColumn(examDto -> {
            final var score = examDto.getScore();
            final var type = examDto.getTask().getType();
            if (type == TaskDto.Type.CHOICE) {
                return score == 1.00 ? "CORRECT" : "WRONG";
            } else if (type == TaskDto.Type.TODO) {
                return score > 0.00 ? "COMPLETED" : "NOT COMPLETED";
            } else {
                return score;
            }
        }).setHeader("Score").setKey("score");

        setItems(exams.stream().filter(ExamDto::getSelected).collect(Collectors.toUnmodifiableList()));

    }
}
