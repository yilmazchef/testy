package be.intecbrussel.student.views.teacher;


import be.intecbrussel.student.data.dto.ExamDto;
import be.intecbrussel.student.data.dto.QuestionDto;
import be.intecbrussel.student.data.dto.TaskDto;
import be.intecbrussel.student.data.dto.UserDto;
import be.intecbrussel.student.data.entity.UserEntity;
import be.intecbrussel.student.security.AuthenticatedUser;
import be.intecbrussel.student.service.IExamService;
import be.intecbrussel.student.service.IQuestionService;
import be.intecbrussel.student.service.ITeacherService;
import be.intecbrussel.student.views.AbstractView;
import be.intecbrussel.student.views.DefaultNotification;
import be.intecbrussel.student.views.Priority;
import be.intecbrussel.student.views.student.StudentExamAnalyticsView;
import com.mlottmann.vstepper.VStepper;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@PageTitle( TeacherExamGeneratorView.TITLE )
@Route( value = TeacherExamGeneratorView.ROUTE )
@RolesAllowed( { "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_TEACHER" } )
// <- Should match one of the user's roles (case-sensitive)
public class TeacherExamGeneratorView extends AbstractView {

	public static final String TITLE = "Exam Generator";
	public static final String ROUTE = "teacher/exam";

	private final IQuestionService questionService;
	private final ITeacherService teacherService;
	private final IExamService examService;
	private final AuthenticatedUser authenticatedUser;


	public TeacherExamGeneratorView( IQuestionService questionService, ITeacherService teacherService, IExamService examService,
	                                 final AuthenticatedUser authenticatedUser ) {

		this.questionService = questionService;
		this.teacherService = teacherService;
		this.examService = examService;
		this.authenticatedUser = authenticatedUser;

		initParentComponentStyle();

		Optional< UserEntity > oUser = Optional.empty();
		try {
			oUser = authenticatedUser.get();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}

		if ( oUser.isPresent() ) {
			final var oTeacher = teacherService.fetchTeacherById( oUser.get().getId() );
			oTeacher.ifPresent( this::initExamsLayout );
		}
	}


	private ComponentEventListener< ClickEvent< Button > > generateExamEvent( final String examCode, final List< QuestionDto > questions ) {

		return onFinishClick -> {
			final var examsCount = questions
					.stream()
					.flatMap( questionDto -> questionDto.getTasks().stream() )
					.map( taskDto -> newExam( examCode, taskDto ) )
					.map( examService::create )
					.filter( savedId -> !savedId.equalsIgnoreCase( "-1" ) )
					.count();

			final var message = "An exam with " + examCode + " and with " + examsCount + ( examsCount == 1 ? "question" : "questions" ) + " is generated.";
			getNotifications().add( new DefaultNotification( "New Exam is Ready", message ) );

		};
	}


	private ExamDto newExam( final String examCode, final TaskDto task ) {

		return new ExamDto()
				.withCode( examCode )
				.withSession( "ANON" )
				.withStartTime( Timestamp.valueOf( LocalDateTime.now() ) )
				.withEndTime( Timestamp.valueOf( LocalDateTime.now() ) )
				.withStudent( new UserDto().withId( UUID.randomUUID().toString() ).withAnonymous( true ) )
				.withTask( task )
				.withScore( task.getWeight() );
	}


	private void initExamsLayout( final UserDto teacher ) {

		final var examResponse = examService.selectAllBySession();

		if ( examResponse != null && !examResponse.isEmpty() ) {

			final var examsListBox = new ListBox< String >();

			examResponse
					.stream()
					.collect( Collectors.groupingBy( ExamDto::getCode ) )
					.forEach( ( examCode, examData ) ->
							examsListBox.add( new Button( "Edit Exam", onClick -> initSingleExamStepperLayout( examCode, examData, teacher ) ) ) );

			add( examsListBox );
		}

		try {
			final var questions = questionService.getAllQuestions();
			if ( !questions.isEmpty() ) {
				initExamGeneratorLayout( questions );
			}

		} catch ( SQLException sqlException ) {
			getNotifications().add( new DefaultNotification( "ERROR: LOADING QUESTIONS", sqlException.getMessage(), Priority.ERROR ) );
		}

	}


	private void initExamGeneratorLayout( List< QuestionDto > questions ) {

		final var questionsListBox = new MultiSelectListBox< QuestionDto >();
		questionsListBox.setItems( questions );
		final var examCodeLabel = new Label( "Exam Code" );
		final var examCodeField = new TextField();

		final var generateExamButton = new Button(
				"Generate an exam with selected questions",
				generateExamEvent( examCodeField.getValue(), questionsListBox.getSelectedItems().stream().collect( Collectors.toUnmodifiableList() ) ) );

		add( examCodeLabel, examCodeField, questionsListBox, generateExamButton );
	}


	private void initSingleExamStepperLayout( final String examCode, final List< ExamDto > exams, final UserDto teacher ) {

		final var layout = new VerticalLayout();
		layout.setPadding( false );
		layout.setMargin( false );
		layout.setSpacing( false );
		layout.setAlignItems( Alignment.CENTER );

		final var examCodeLabel = new Label( "Exam: " + examCode );
		final var stepper = new VStepper();

		stepper.setSizeFull();
		stepper.getNext().setIcon( VaadinIcon.ARROW_RIGHT.create() );
		stepper.getBack().setIcon( VaadinIcon.ARROW_LEFT.create() );
		stepper.getFinish().setIcon( VaadinIcon.CHECK.create() );

		exams
				.stream()
				.map( examDto -> {
					examDto.getTask().getQuestion().setTeacher( teacher );
					return examDto;
				} )
				.collect( Collectors.groupingBy( examDto -> examDto.getTask().getQuestion() ) )
				.forEach( ( questionDto, examDtos ) -> stepper.addStep(
								"Question", initSingleQuestionLayout(
										questionDto, examDtos.stream().map( ExamDto::getTask ).collect( Collectors.toUnmodifiableList() ) )
						)
				);

		stepper.addFinishListener( onFinishClick -> getUI().ifPresent( ui -> ui.navigate( StudentExamAnalyticsView.class ) ) );

		layout.add( examCodeLabel, stepper );
		add( layout );
	}


	private VerticalLayout initSingleQuestionLayout( QuestionDto question, List< TaskDto > tasks ) {

		final var layout = new VerticalLayout();
		layout.setId( String.valueOf( ( int ) System.currentTimeMillis() ) );

		layout.add( new H3( question.getHeader() ) );
		question.getContent().stream().map( Paragraph::new ).forEach( layout::add );

		final var choicesCheckBoxGroup = initTaskLayout( "Choices", question, tasks.stream().filter( taskDto -> taskDto.getType() == TaskDto.Type.CHOICE ).collect( Collectors.toUnmodifiableList() ) );
		final var todosCheckBoxGroup = initTaskLayout( "Task(s)", question, tasks.stream().filter( taskDto -> taskDto.getType() == TaskDto.Type.TODO ).collect( Collectors.toUnmodifiableList() ) );
		layout.add( choicesCheckBoxGroup, todosCheckBoxGroup );

		final var submitButton = new Button( "Submit", onClick -> {

			if ( !todosCheckBoxGroup.isEmpty() && !todosCheckBoxGroup.getSelectedItems().isEmpty() ) {
				final var selectedTodos = todosCheckBoxGroup.getValue();
				todosCheckBoxGroup.setEnabled( false );

				final var patchCounter = new AtomicInteger( 0 );

				getNotifications().add( new DefaultNotification(
						"Question Submitted", "Question with " + patchCounter.get() + ( patchCounter.get() == 1 ? " todo" : " todos" ) + " submitted" ) );
			}

			if ( !choicesCheckBoxGroup.isEmpty() && !choicesCheckBoxGroup.getSelectedItems().isEmpty() ) {
				final var selectedChoices = choicesCheckBoxGroup.getValue();
				choicesCheckBoxGroup.setEnabled( false );

				final var patchCounter = new AtomicInteger( 0 );

				getNotifications().add( new DefaultNotification(
						"Question Submitted", "Question with " + patchCounter.get() + ( patchCounter.get() == 1 ? " choice" : " choices" ) + " submitted" ) );
			}

			// disable button after submission ..
			onClick.getSource().setEnabled( false );

		} );
		submitButton.addThemeVariants( ButtonVariant.LUMO_PRIMARY );

		layout.add( submitButton );
		return layout;

	}


	private CheckboxGroup< TaskDto > initTaskLayout( String labelText, QuestionDto question, List< TaskDto > tasks ) {

		final var checkboxGroup = new CheckboxGroup< TaskDto >();
		checkboxGroup.setThemeName( RadioGroupVariant.LUMO_VERTICAL.getVariantName() );
		checkboxGroup.setRequired( true );

		if ( tasks.isEmpty() ) {
			checkboxGroup.setVisible( false );
			return checkboxGroup;
		}

		checkboxGroup.setId( labelText + "CheckboxGroup_" + question.getId() );
		checkboxGroup.setItems( tasks );
		final var correctTasksCount = tasks.stream().filter( choice -> choice.getWeight() > 0.00 ).count();
		final var allTasksCount = tasks.size();
		checkboxGroup.setLabel(
				labelText.contains( "Choice" ) ?
						"Only" + correctTasksCount + " " + ( ( correctTasksCount <= 1 ) ? "choice is" : "choices are" ) + " correct from " + allTasksCount + " " + ( ( correctTasksCount <= 1 ) ? "choice" : "choices" ) :
						allTasksCount + " to complete."
		);
		return checkboxGroup;

	}


	private void initParentComponentStyle() {

		setWidthFull();
		setPadding( false );
		setAlignItems( Alignment.CENTER );
	}


	@Override
	public String getViewName() {

		return TeacherExamGeneratorView.ROUTE;
	}

}
