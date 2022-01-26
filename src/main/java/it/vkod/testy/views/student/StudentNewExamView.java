package it.vkod.testy.views.student;

import it.vkod.testy.data.dto.ExamDto;
import it.vkod.testy.data.dto.QuestionDto;
import it.vkod.testy.data.dto.TaskDto;
import it.vkod.testy.data.entity.UserEntity;
import it.vkod.testy.security.AuthenticatedUser;
import it.vkod.testy.service.IExamService;
import it.vkod.testy.service.IStudentService;
import it.vkod.testy.views.AbstractView;
import it.vkod.testy.views.DefaultNotification;
import it.vkod.testy.views.Priority;
import com.flowingcode.vaadin.addons.simpletimer.SimpleTimer;
import com.mlottmann.vstepper.VStepper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@PageTitle(StudentNewExamView.TITLE)
@Route(value = StudentNewExamView.ROUTE)
@PermitAll
public class StudentNewExamView extends AbstractView implements HasUrlParameter<String> {

	private static final Logger logger = LoggerFactory.getLogger(StudentNewExamView.class);

	public static final String TITLE = "Student Exam";
	public static final String ROUTE = "student/exam";
	public static final Long DURATION_IN_MINUTES_PER_QUESTION = 4L;

	private final IExamService examService;
	private final IStudentService studentService;
	private final AuthenticatedUser authenticatedUser;

	private final VStepper stepper;
	private final VerticalLayout examStarterLayout;
	private final SimpleTimer examTimer;
	private final Button startExamButton;
	private final Select<String> examSelection;

	public StudentNewExamView(IExamService examService, IStudentService studentService,
			final AuthenticatedUser authenticatedUser) {

		this.examService = examService;
		this.studentService = studentService;
		this.authenticatedUser = authenticatedUser;

		this.stepper = new VStepper();
		this.examStarterLayout = new VerticalLayout();
		this.examTimer = new SimpleTimer();
		this.startExamButton = new Button("Start Exam");

		// FETCH ALL EXAMS BY STUDENT AND GROUP THEM BY EXAM_CODE

		final var codes = this.examService.selectAllExamsGroupedByCode();

		this.examSelection = new Select<>(codes.keySet().toArray(String[]::new));

		this.examTimer.getStyle().set("font-size", "24pt");
		this.examTimer.setMinutes(true);
		this.examTimer.setFractions(false);
		this.examTimer.addTimerEndEvent(onTimeOutEvent -> stopExamAndDisableSubmissions(this.examSelection.getValue(),
				getCurrentSession().getSession().getId()));

		initParentStyle();
		initStepperStyle();

		final var userBeingSearched = new Object() {
			String userId = null;
		};

		Optional<UserEntity> oUser = Optional.empty();
		try {
			oUser = this.authenticatedUser.get();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (oUser.isPresent()) {
			final var user = oUser.get();
			userBeingSearched.userId = user.getId();
		}

		final var fetchedStudent = this.studentService.fetchStudentById(userBeingSearched.userId);

		if (fetchedStudent.isEmpty()
				|| fetchedStudent.get().getCourse().name().equalsIgnoreCase(this.examSelection.getValue())) {
			getNotifications().add(new DefaultNotification(
					"This user does not exists OR not authorized to start the exam: " + this.examSelection.getValue(), Priority.ERROR));
		} else {
			this.startExamButton.addClickListener(onClick -> {
				this.startExamButton.setText(onClick.getClickCount() % 2 == 0 ? "Stop" : "Start");
				if (onClick.getClickCount() == 1) {
					startExamEvent(this.examSelection.getValue());
				} else {
					this.stepper.getNext().setEnabled(this.stepper.getNext().getElement().isEnabled());
				}
			});
		}

		this.examStarterLayout.add(this.examSelection, this.startExamButton, this.examTimer);
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

	private void startExamEvent(final String examCode) {

		final var patchResponses = examService.patchSession(examCode, getCurrentSession().getSession().getId());
		if (patchResponses != null && !patchResponses.isEmpty()) {
			final var examsResponse = examService.selectAllByCode(examCode);
			if (examsResponse != null && !examsResponse.isEmpty()) {

				examsResponse
						.stream()
						.collect(Collectors.groupingBy(examDto -> examDto.getTask().getQuestion()))
						.forEach((questionDTO, examDTOs) -> stepper.addStep(
								"Question", initSingleQuestionLayout(examCode,
										questionDTO, examDTOs.stream().map(ExamDto::getTask)
												.collect(Collectors.toUnmodifiableList()))));

				examTimer.setStartTime(
						TimeUnit.MINUTES.toSeconds(DURATION_IN_MINUTES_PER_QUESTION) *
								examsResponse.stream()
										.collect(Collectors.groupingBy(examDto -> examDto.getTask().getQuestion()))
										.keySet().size());
				examTimer.start(); // START THE TIMER ..
			}
		}
	}

	private void stopExamAndDisableSubmissions(final String code, final String session) {

		getNotifications().add(
				new DefaultNotification("EXAM " + code + " TIME IS OVER",
						"Exam stopped due timeout.. Current Session: " + session, Priority.HIGH));
		this.stepper.getNext().setEnabled(false);
	}

	private VerticalLayout initSingleQuestionLayout(String examCode, QuestionDto question, List<TaskDto> tasks) {

		final var layout = new VerticalLayout();
		layout.setId(String.valueOf((int) System.currentTimeMillis()));

		layout.add(new H3(question.getHeader()));
		question.getContent().stream().map(Paragraph::new).forEach(layout::add);

		final var choicesCheckBoxGroup = initTaskLayout("Choices", question, tasks.stream()
				.filter(taskDto -> taskDto.getType() == TaskDto.Type.CHOICE).collect(Collectors.toUnmodifiableList()));
		final var todosCheckBoxGroup = initTaskLayout("Task(s)", question, tasks.stream()
				.filter(taskDto -> taskDto.getType() == TaskDto.Type.TODO).collect(Collectors.toUnmodifiableList()));
		layout.add(choicesCheckBoxGroup, todosCheckBoxGroup);

		final var submitButton = new Button("Submit", onClick -> {

			if (!todosCheckBoxGroup.isEmpty() && !todosCheckBoxGroup.getSelectedItems().isEmpty()) {
				final var selectedTodos = todosCheckBoxGroup.getSelectedItems();
				todosCheckBoxGroup.setEnabled(false);

				final var patchCounter = new AtomicInteger(0);
				for (final var selectedTodo : selectedTodos) {
					final var examPatchResponse = examService.patchTask(examCode, selectedTodo.getId(),
							getCurrentSession().getSession().getId(), true);
					if (examPatchResponse != null && !examPatchResponse.isEmpty()
							&& !examPatchResponse.equalsIgnoreCase("-1")) {
						patchCounter.getAndIncrement();
					}
				}

				getNotifications().add(new DefaultNotification(
						"Question Submitted", "Question with " + patchCounter.get()
								+ (patchCounter.get() == 1 ? " todo" : " todos") + " submitted"));
			}

			if (!choicesCheckBoxGroup.isEmpty() && !choicesCheckBoxGroup.getSelectedItems().isEmpty()) {
				final var selectedChoices = choicesCheckBoxGroup.getValue();
				choicesCheckBoxGroup.setEnabled(false);

				final var patchCounter = new AtomicInteger(0);
				for (final var selectedChoice : selectedChoices) {
					final var examPatchResponse = examService.patchTask(examCode, selectedChoice.getId(),
							getCurrentSession().getSession().getId(), true);
					if (examPatchResponse != null && !examPatchResponse.isEmpty()
							&& !examPatchResponse.equalsIgnoreCase("-1")) {
						patchCounter.getAndIncrement();
					}
				}

				getNotifications().add(new DefaultNotification(
						"Question Submitted", "Question with " + patchCounter.get()
								+ (patchCounter.get() == 1 ? " choice" : " choices") + " submitted"));
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
				labelText.contains("Choice")
						? "Only" + correctTasksCount + " " + ((correctTasksCount <= 1) ? "choice is" : "choices are")
								+ " correct from " + allTasksCount + " "
								+ ((correctTasksCount <= 1) ? "choice" : "choices")
						: allTasksCount + " to complete.");
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

		}
	}

}
