package be.intecbrussel.student.views.student;

import be.intecbrussel.student.data.dto.ExamDto;
import be.intecbrussel.student.data.dto.QuestionDto;
import be.intecbrussel.student.data.dto.TaskDto;
import com.mlottmann.vstepper.VStepper;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;

import java.util.List;
import java.util.stream.Collectors;

public class StudentExamRecapStepper extends VStepper {

    public StudentExamRecapStepper(List<ExamDto> exams) {

        setSizeFull();
        getNext().setIcon(VaadinIcon.ARROW_RIGHT.create());
        getNext().setVisible(false);
        getBack().setIcon(VaadinIcon.ARROW_LEFT.create());
        getFinish().setIcon(VaadinIcon.CHECK.create());

        getNext().setVisible(true);
        exams
                .stream()
                .filter(ExamDto::getSubmitted)
                .collect(Collectors.groupingBy(examDto -> examDto.getTask().getQuestion()))
                .forEach((questionDto, examDtos) -> addStep(
                        "Question", initQuestionAnalyticsLayout(questionDto, examDtos.stream().map(ExamDto::getTask).collect(Collectors.toUnmodifiableList()))));

    }

    private VerticalLayout initQuestionAnalyticsLayout(QuestionDto question, List<TaskDto> tasks) {

        final var layout = new VerticalLayout();
        layout.setId(String.valueOf((int) System.currentTimeMillis()));

        layout.add(new H3(question.getHeader()));
        question.getContent().stream().map(Paragraph::new).forEach(layout::add);

        final CheckboxGroup<TaskDto> choicesCheckBoxGroup =
                initTaskAnalyticsLayout("Choices", question, tasks.stream().filter(taskDto -> taskDto.getType() == TaskDto.Type.CHOICE).collect(Collectors.toUnmodifiableList()));

        final CheckboxGroup<TaskDto> todosCheckBoxGroup =
                initTaskAnalyticsLayout("Task(s)", question, tasks.stream().filter(taskDto -> taskDto.getType() == TaskDto.Type.TODO).collect(Collectors.toUnmodifiableList()));
        layout.add(choicesCheckBoxGroup, todosCheckBoxGroup);

        return layout;

    }

    private CheckboxGroup<TaskDto> initTaskAnalyticsLayout(String labelText, QuestionDto question, List<TaskDto> tasks) {

        final var checkboxGroup = new CheckboxGroup<TaskDto>();
        checkboxGroup.setThemeName(RadioGroupVariant.LUMO_VERTICAL.getVariantName());
        checkboxGroup.setRequired(true);

        if (tasks.isEmpty()) {
            checkboxGroup.setVisible(false);
            return checkboxGroup;
        }

        checkboxGroup.setId(labelText + "CheckboxGroup_" + question.getId());
        checkboxGroup.setItems(tasks);
        final var correctTasksCount = tasks.stream().filter(choice -> choice.getWeight() > 0.00).count();
        final var allTasksCount = tasks.size();
        checkboxGroup.setLabel(
                labelText.contains("Choice") ?
                        "Only" + correctTasksCount + " " + ((correctTasksCount <= 1) ? "choice is" : "choices are") + " correct from " + allTasksCount + " " + ((correctTasksCount <= 1) ? "choice" : "choices") :
                        allTasksCount + " to complete."
        );

        final var hasValueTasks = tasks.stream().filter(taskDto -> taskDto.getWeight() > 0).collect(Collectors.toUnmodifiableList());
        checkboxGroup.select(hasValueTasks);
        return checkboxGroup;

    }
}
