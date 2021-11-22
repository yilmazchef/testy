package be.intecbrussel.student.views.anonymous;


import be.intecbrussel.student.data.dto.ExamDto;
import be.intecbrussel.student.data.dto.QuestionDto;
import be.intecbrussel.student.data.dto.TaskDto;
import be.intecbrussel.student.service.IExamService;
import be.intecbrussel.student.views.AbstractView;
import be.intecbrussel.student.views.DefaultNotification;
import be.intecbrussel.student.views.Priority;
import be.intecbrussel.student.views.student.StudentExamAnalyticsView;
import com.mlottmann.vstepper.VStepper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@PageTitle(AnonymousNewExamView.TITLE)
@Route("")
@RouteAlias(value = AnonymousNewExamView.ROUTE)
@AnonymousAllowed
public class AnonymousNewExamView extends AbstractView implements HasUrlParameter<String> {

    private static final Logger ANONYMOUS_VIEW_LOGGER = LoggerFactory.getLogger(AnonymousNewExamView.class);

    public static final String TITLE = "Anonymous Exam";
    public static final String ROUTE = "anonymous/exam";
    private final VaadinSession currentSession = UI.getCurrent().getSession();

    private final Label examCodeLabel = new Label();
    private final VStepper examStepper = new VStepper();

    private final IExamService examService;

    public AnonymousNewExamView(IExamService examService) {
        this.examService = examService;

        initParentComponentStyle();

        examStepper.setSizeFull();
        examStepper.getNext().setIcon(VaadinIcon.ARROW_RIGHT.create());
        examStepper.getNext().setVisible(false);
        examStepper.getBack().setIcon(VaadinIcon.ARROW_LEFT.create());
        examStepper.getFinish().setIcon(VaadinIcon.CHECK.create());

        final var examStarterLayout = new VerticalLayout();
        examStarterLayout.setWidthFull();
        examStarterLayout.setAlignItems(Alignment.CENTER);
        examStarterLayout.setPadding(false);

        final var examCodeField = new TextField();
        final var startExamButton = new Button("Start Exam", onClick -> {
            final var code = examCodeField.getValue();
            final var patchResponses = examService.patchSession(code, currentSession.getSession().getId());
            if (patchResponses.hasBody() && patchResponses.getBody() != null) {
                final var examsResponse = examService.selectAllByCode(code);
                if (examsResponse.hasBody() && examsResponse.getBody() != null) {
                    examStepper.getNext().setVisible(true);
                    add(initSingleExamStepperLayout(code, examsResponse.getBody()));
                }
            }
        });
        startExamButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        examStarterLayout.add(examCodeLabel, examCodeField, startExamButton);
        examStepper.addStep(examStarterLayout);

        examStepper.addFinishListener(onFinishClick -> getUI().ifPresent(ui -> ui.navigate(StudentExamAnalyticsView.class)));

        add(examStepper);
    }

    private VStepper initSingleExamStepperLayout(final String examCode, final List<ExamDto> exams) {

        examCodeLabel.setText("Exam: " + examCode);

        exams
                .stream()
                .collect(Collectors.groupingBy(examDto -> examDto.getTask().getQuestion()))
                .forEach((questionDto, examDtos) -> examStepper.addStep(
                        "Question", initSingleQuestionLayout(
                                questionDto, examDtos.stream().map(ExamDto::getTask).collect(Collectors.toUnmodifiableList()))
                        )
                );

        return examStepper;
    }

    private VerticalLayout initSingleQuestionLayout(QuestionDto question, List<TaskDto> tasks) {

        final var layout = new VerticalLayout();
        layout.setId(String.valueOf((int) System.currentTimeMillis()));

        layout.add(new H3(question.getHeader()));
        question.getContent().stream().map(Paragraph::new).forEach(layout::add);

        final var choicesCheckBoxGroup = initTaskLayout("Choices", question, tasks.stream().filter(taskDto -> taskDto.getType() == TaskDto.Type.CHOICE).collect(Collectors.toUnmodifiableList()));
        final var todosCheckBoxGroup = initTaskLayout("Task(s)", question, tasks.stream().filter(taskDto -> taskDto.getType() == TaskDto.Type.TODO).collect(Collectors.toUnmodifiableList()));
        layout.add(choicesCheckBoxGroup, todosCheckBoxGroup);

        final var submitButton = new Button("Submit", onClick -> {

            if (!todosCheckBoxGroup.isEmpty() && !todosCheckBoxGroup.getSelectedItems().isEmpty()) {
                final var selectedTodos = todosCheckBoxGroup.getValue();
                todosCheckBoxGroup.setEnabled(false);

                final var patchCounter = new AtomicInteger(0);
                for (final var selectedTodo : selectedTodos) {
                    final var examPatchResponse = examService.patchTask(selectedTodo.getId(), currentSession.getSession().getId(), true);
                    if (examPatchResponse.hasBody() && examPatchResponse.getBody() != null) {
                        patchCounter.getAndIncrement();
                    }
                }

                getNotifications().add(new DefaultNotification(
                        "Question Submitted", "Question with " + patchCounter.get() + (patchCounter.get() == 1 ? " todo" : " todos") + " submitted"));
            }

            if (!choicesCheckBoxGroup.isEmpty() && !choicesCheckBoxGroup.getSelectedItems().isEmpty()) {
                final var selectedChoices = choicesCheckBoxGroup.getValue();
                choicesCheckBoxGroup.setEnabled(false);

                final var patchCounter = new AtomicInteger(0);
                for (final var selectedChoice : selectedChoices) {
                    final var examPatchResponse = examService.patchTask(selectedChoice.getId(), currentSession.getSession().getId(), true);
                    if (examPatchResponse.hasBody() && examPatchResponse.getBody() != null) {
                        patchCounter.getAndIncrement();
                    }
                }

                getNotifications().add(new DefaultNotification(
                        "Question Submitted", "Question with " + patchCounter.get() + (patchCounter.get() == 1 ? " choice" : " choices") + " submitted"));
            }

            // disable button after submission ..
            onClick.getSource().setEnabled(false);

        });
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        layout.add(submitButton);
        return layout;

    }

    private CheckboxGroup<TaskDto> initTaskLayout(String labelText, QuestionDto question, List<TaskDto> tasks) {

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
        checkboxGroup.addSelectionListener(onSelect -> {
            int noOfSelectedTodos = onSelect.getAllSelectedItems().size();
            if (noOfSelectedTodos > correctTasksCount) {
                final var notificationMsg = "You cannot select more than " + correctTasksCount + " " + labelText + ".";
                checkboxGroup.deselect(onSelect.getAddedSelection());
                getNotifications().add(new DefaultNotification("Warning", notificationMsg, Priority.LOW));
            }
        });

        return checkboxGroup;

    }

    private void initParentComponentStyle() {
        setWidthFull();
        setMargin(false);
        setPadding(false);
        setHorizontalComponentAlignment(Alignment.CENTER);
    }

    @Override
    public String getViewName() {
        return AnonymousNewExamView.ROUTE;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null) {
            final var location = event.getLocation();
            final var queryParameters = location.getQueryParameters();

            final var parametersMap = queryParameters.getParameters();
            parametersMap.forEach((k, v) -> {
                ANONYMOUS_VIEW_LOGGER.info("Location: " + location.getPath() + "Parameter Key: " + k + ", Parameter Values: " + v.stream().collect(Collectors.joining(",")));
            });

        }
    }
}
