package be.intecbrussel.student.views.student;


import be.intecbrussel.student.data.dto.ExamDto;
import be.intecbrussel.student.data.dto.QuestionDto;
import be.intecbrussel.student.data.dto.TaskDto;
import be.intecbrussel.student.data.entity.UserEntity;
import be.intecbrussel.student.security.AuthenticatedUser;
import be.intecbrussel.student.service.IExamService;
import be.intecbrussel.student.service.IStudentService;
import be.intecbrussel.student.views.AbstractView;
import be.intecbrussel.student.views.DefaultNotification;
import be.intecbrussel.student.views.Priority;
import com.flowingcode.vaadin.addons.simpletimer.SimpleTimer;
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

import javax.annotation.security.PermitAll;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@PageTitle( StudentNewExamView.TITLE )
@Route( value = StudentNewExamView.ROUTE )
@PermitAll
public class StudentNewExamView extends AbstractView implements HasUrlParameter< String > {

	private static final Logger logger = LoggerFactory.getLogger( StudentNewExamView.class );

	public static final String TITLE = "Student Exam";
	public static final String ROUTE = "student/exam";
	private final VaadinSession currentSession = UI.getCurrent().getSession();

	private final IExamService examService;
	private final IStudentService studentService;
	private final AuthenticatedUser authenticatedUser;

	private final VStepper stepper;
	private final VerticalLayout examStarterLayout;
	private final Label examCodeLabel;
	private final TextField examCodeField;
	private final Label examTimerLabel;
	private final SimpleTimer examTimer;
	private final Button startExamButton;


	public StudentNewExamView( IExamService examService, IStudentService studentService, final AuthenticatedUser authenticatedUser ) {

		this.examService = examService;
		this.studentService = studentService;
		this.authenticatedUser = authenticatedUser;

		this.stepper = new VStepper();
		this.examStarterLayout = new VerticalLayout();
		this.examCodeLabel = new Label( "Exam Code" );
		this.examCodeField = new TextField();
		this.examTimerLabel = new Label( "Exam timer will be started as soon as you click on 'Start Exam' button.." );
		this.examTimer = new SimpleTimer();
		this.startExamButton = new Button( "Start Exam" );

		this.examTimer.getStyle().set( "font-size", "24pt" );
		this.examTimer.setMinutes( true );
		this.examTimer.setFractions( false );
		this.examTimer.addTimerEndEvent( onTimeOutEvent -> stopExamAndDisableSubmissions( this.examCodeField.getValue(), currentSession.getSession().getId() ) );

		initParentStyle();

		final var userBeingSearched = new Object() {
			String userId = null;
		};

		Optional< UserEntity > oUser = Optional.empty();
		try {
			oUser = authenticatedUser.get();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}

		if ( oUser.isPresent() ) {
			final var user = oUser.get();
			userBeingSearched.userId = user.getId();
		}

		final var fetchedStudent = studentService.fetchStudentById( userBeingSearched.userId );

		this.startExamButton.addClickListener( onClick -> {
			this.startExamButton.setText( onClick.getClickCount() % 2 == 0 ? "Stop" : "Start" );
			startExamEvent();
		} );

		initStepperStyle();

		this.examStarterLayout.add( this.examCodeLabel, this.examCodeField, this.startExamButton, this.examTimerLabel, this.examTimer );
		this.stepper.addStep( this.examStarterLayout );
		add( this.stepper );
	}


	private void initStepperStyle() {

		stepper.setSizeFull();
		stepper.getNext().setIcon( VaadinIcon.ARROW_RIGHT.create() );
		stepper.getBack().setIcon( VaadinIcon.ARROW_LEFT.create() );
		stepper.getFinish().setIcon( VaadinIcon.CHECK.create() );

		examStarterLayout.setWidthFull();
		examStarterLayout.setJustifyContentMode( JustifyContentMode.CENTER );

	}


	private void startExamEvent() {

		final var code = examCodeField.getValue();
		final var patchResponses = examService.patchSession( code, currentSession.getSession().getId() );
		if ( patchResponses != null && !patchResponses.isEmpty() ) {
			final var examsResponse = examService.selectAllByCode( code );
			if ( examsResponse != null && !examsResponse.isEmpty() ) {

				examsResponse
						.stream()
						.collect( Collectors.groupingBy( examDto -> examDto.getTask().getQuestion() ) )
						.forEach( ( questionDTO, examDTOs ) -> stepper.addStep(
										"Question", initSingleQuestionLayout(
												questionDTO, examDTOs.stream().map( ExamDto::getTask )
														.collect( Collectors.toUnmodifiableList() ) )
								)
						);

				examTimer.setStartTime(
						TimeUnit.MINUTES.toSeconds( 3 ) *
								examsResponse.stream().collect( Collectors.groupingBy( examDto -> examDto.getTask().getQuestion() ) ).keySet().size()
				);
				examTimer.start(); // START THE TIMER ..
			}
		}
	}


	private void stopExamAndDisableSubmissions( final String code, final String session ) {

		getNotifications().add(
				new DefaultNotification( "EXAM " + code + " TIME IS OVER", "Exam stopped due timeout.. Current Session: " + session, Priority.HIGH ) );
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
				for ( final var selectedTodo : selectedTodos ) {
					final var examPatchResponse = examService.patchTask( selectedTodo.getId(), currentSession.getSession().getId(), true );
					if ( examPatchResponse != null && !examPatchResponse.isEmpty() && !examPatchResponse.equalsIgnoreCase( "-1" ) ) {
						patchCounter.getAndIncrement();
					}
				}

				getNotifications().add( new DefaultNotification(
						"Question Submitted", "Question with " + patchCounter.get() + ( patchCounter.get() == 1 ? " todo" : " todos" ) + " submitted" ) );
			}

			if ( !choicesCheckBoxGroup.isEmpty() && !choicesCheckBoxGroup.getSelectedItems().isEmpty() ) {
				final var selectedChoices = choicesCheckBoxGroup.getValue();
				choicesCheckBoxGroup.setEnabled( false );

				final var patchCounter = new AtomicInteger( 0 );
				for ( final var selectedChoice : selectedChoices ) {
					final var examPatchResponse = examService.patchTask( selectedChoice.getId(), currentSession.getSession().getId(), true );
					if ( examPatchResponse != null && !examPatchResponse.isEmpty() && !examPatchResponse.equalsIgnoreCase( "-1" ) ) {
						patchCounter.getAndIncrement();
					}
				}

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
		checkboxGroup.addSelectionListener( onSelect -> {
			int noOfSelectedTodos = onSelect.getAllSelectedItems().size();
			if ( noOfSelectedTodos > correctTasksCount ) {
				final var notificationMsg = "You cannot select more than " + correctTasksCount + " " + labelText + ".";
				checkboxGroup.deselect( onSelect.getAddedSelection() );
				getNotifications().add( new DefaultNotification( "Warning", notificationMsg, Priority.LOW ) );
			}
		} );

		return checkboxGroup;

	}


	private void initParentStyle() {

		setSizeFull();
		setPadding( false );
		setJustifyContentMode( JustifyContentMode.CENTER );
	}


	@Override
	public String getViewName() {

		return StudentNewExamView.ROUTE;
	}


	@Override
	public void setParameter( BeforeEvent event, @OptionalParameter String parameter ) {

		if ( parameter != null ) {
			Location location = event.getLocation();
			QueryParameters queryParameters = location.getQueryParameters();

			final var code = queryParameters.getParameters().get( "code" ).get( 0 );
			this.examCodeField.setValue( code );
		}
	}

}
