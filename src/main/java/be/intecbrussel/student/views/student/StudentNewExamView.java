package be.intecbrussel.student.views.student;

import be.intecbrussel.student.data.dto.ExamDto;
import be.intecbrussel.student.data.dto.QuestionDto;
import be.intecbrussel.student.data.dto.StudentDto;
import be.intecbrussel.student.data.dto.TaskDto;
import be.intecbrussel.student.security.SecurityUtils;
import be.intecbrussel.student.service.IExamService;
import be.intecbrussel.student.service.IStudentService;
import be.intecbrussel.student.views.AbstractView;
import be.intecbrussel.student.views.MainAppLayout;
import be.intecbrussel.student.views.user.LoginView;
import com.flowingcode.vaadin.addons.simpletimer.SimpleTimer;
import com.github.appreciated.app.layout.addons.notification.entity.DefaultNotification;
import com.github.appreciated.app.layout.addons.notification.entity.Priority;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@PageTitle(StudentNewExamView.TITLE)
@Route(value = StudentNewExamView.ROUTE, layout = MainAppLayout.class)
public class StudentNewExamView extends AbstractView implements HasUrlParameter<String> {

    private static final Logger logger = LoggerFactory.getLogger(StudentNewExamView.class);

    public static final String TITLE = "Student Exam";
    public static final String ROUTE = "student/exam";
    private final VaadinSession currentSession = UI.getCurrent().getSession();

    private final IExamService examService;
    private final MainAppLayout appLayout;
    private final IStudentService studentService;

    private final VStepper stepper;
    private final VerticalLayout examStarterLayout;
    private final Label examCodeLabel;
    private final TextField examCodeField;
    private final Label examTimerLabel;
    private final SimpleTimer examTimer;
    private final Button startExamButton;

    public StudentNewExamView(IExamService examService, MainAppLayout appLayout, IStudentService studentService) {
        this.examService = examService;
        this.appLayout = appLayout;
        this.studentService = studentService;

        this.stepper = new VStepper();
        this.examStarterLayout = new VerticalLayout();
        this.examCodeLabel = new Label("Exam Code");
        this.examCodeField = new TextField();
        this.examTimerLabel = new Label("Exam timer will be started as soon as you click on 'Start Exam' button..");
        this.examTimer = new SimpleTimer();
        this.startExamButton = new Button("Start Exam");

        this.examTimer.getStyle().set("font-size", "24pt");
        this.examTimer.setMinutes(true);
        this.examTimer.setFractions(false);
        this.examTimer.addTimerEndEvent(onTimeOutEvent -> stopExamAndDisableSubmissions(this.examCodeField.getValue(), currentSession.getSession().getId()));

        initParentStyle();

        if (!SecurityUtils.isUserLoggedIn()) {
            UI.getCurrent().navigate(LoginView.class, new RouteParameters("redirected", "student"));
        } else {
            final var authenticatedUser = SecurityUtils.getAuthenticatedUser();
            final var fetchedStudent = studentService.fetchStudentByUserName(authenticatedUser.getName());
            this.startExamButton.addClickListener(onClick -> startExamEvent());
        }

        initStepperStyle();

        this.examStarterLayout.add(this.examCodeLabel, this.examCodeField, this.startExamButton, this.examTimerLabel, this.examTimer);
        this.stepper.addStep(this.examStarterLayout);
        add(this.stepper);
    }

    private void initStepperStyle() {

        stepper.setSizeFull();
        stepper.getNext().setIcon(VaadinIcon.ARROW_RIGHT.create());
        stepper.getBack().setIcon(VaadinIcon.ARROW_LEFT.create());
        stepper.getFinish().setIcon(VaadinIcon.CHECK.create());

        examStarterLayout.setWidthFull();
        examStarterLayout.setJustifyContentMode(JustifyContentMode.CENTER);

    }

    private void startExamEvent() {
        final var code = examCodeField.getValue();
        final var patchResponses = examService.patchSession(code, currentSession.getSession().getId());
        if (patchResponses.hasBody() && patchResponses.getBody() != null) {
            final var examsResponse = examService.selectAllByCode(code);
            if (examsResponse.hasBody() && examsResponse.getBody() != null) {

                examsResponse.getBody()
                        .stream()
                        .collect(Collectors.groupingBy(examDto -> examDto.getTask().getQuestion()))
                        .forEach((questionDTO, examDTOs) -> stepper.addStep(
                                "Question", initSingleQuestionLayout(
                                        questionDTO, examDTOs.stream().map(ExamDto::getTask)
                                .collect(Collectors.toUnmodifiableList()))
                                )
                        );

                examTimer.setStartTime(
                        TimeUnit.MINUTES.toSeconds(3) *
                                examsResponse.getBody().stream().collect(Collectors.groupingBy(examDto -> examDto.getTask().getQuestion())).keySet().size()
                );
                examTimer.start(); // START THE TIMER ..
            }
        }
    }

    private void stopExamAndDisableSubmissions(final String code, final String session) {
        appLayout.getNotifications().add(
                new DefaultNotification("EXAM " + code + " TIME IS OVER", "Exam stopped due timeout.. Current Session: " + session, Priority.HIGH));
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

                appLayout.getNotifications().add(new DefaultNotification(
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

                appLayout.getNotifications().add(new DefaultNotification(
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
                appLayout.getNotifications().add(new DefaultNotification("Warning", notificationMsg, Priority.LOW));
            }
        });

        return checkboxGroup;

    }

    private void initParentStyle() {
        setSizeFull();
        setPadding(false);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    @Override
    public String getViewName() {
        return StudentNewExamView.ROUTE;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null) {
            Location location = event.getLocation();
            QueryParameters queryParameters = location.getQueryParameters();

            final var code = queryParameters.getParameters().get("code").get(0);
            this.examCodeField.setValue(code);
        }
    }
}
