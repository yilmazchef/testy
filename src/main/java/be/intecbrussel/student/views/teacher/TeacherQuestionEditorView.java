package be.intecbrussel.student.views.teacher;


import be.intecbrussel.student.data.dto.QuestionDto;
import be.intecbrussel.student.data.dto.TaskDto;
import be.intecbrussel.student.data.dto.UserDto;
import be.intecbrussel.student.data.entity.UserEntity;
import be.intecbrussel.student.security.AuthenticatedUser;
import be.intecbrussel.student.service.IQuestionService;
import be.intecbrussel.student.service.ITeacherService;
import be.intecbrussel.student.util.QuestionBatchImporter;
import be.intecbrussel.student.views.AbstractView;
import be.intecbrussel.student.views.DefaultNotification;
import be.intecbrussel.student.views.Priority;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@PageTitle( TeacherQuestionEditorView.TITLE )
@Route( value = TeacherQuestionEditorView.ROUTE )
@RolesAllowed( { "ROLE_TEACHER", "ROLE_MANAGER" } )
public class TeacherQuestionEditorView extends AbstractView {

	public static final String TITLE = "Exam Designer";
	public static final String ROUTE = "teacher/question";

	private final VaadinSession currentSession = VaadinSession.getCurrent();
	private final VStepper stepper = new VStepper();

	private final IQuestionService questionService;
	private final ITeacherService teacherService;
	private final QuestionBatchImporter batchImporter;
    private final AuthenticatedUser authenticatedUser;


	public TeacherQuestionEditorView( IQuestionService questionService, ITeacherService teacherService,
                                      QuestionBatchImporter batchImporter, final AuthenticatedUser authenticatedUser ) {

		this.questionService = questionService;
		this.teacherService = teacherService;
		this.batchImporter = batchImporter;
        this.authenticatedUser = authenticatedUser;

        initParentComponentStyle();

        Optional< UserEntity > oUser = Optional.empty();
        try {
            oUser = this.authenticatedUser.get();
        } catch ( SQLException e ) {
            e.printStackTrace();
        }

        if ( oUser.isPresent() ) {
            final var oTeacher = teacherService.fetchTeacherById( oUser.get().getId() );
            oTeacher.ifPresent( this::initBatchImportLayout );
        }

	}


	private void initBatchImportLayout( UserDto teacher ) {

		final var batchImportLayout = new VerticalLayout();
		batchImportLayout.setPadding( false );
		batchImportLayout.setAlignItems( Alignment.CENTER );
		batchImportLayout.setAlignSelf( Alignment.CENTER );

		final var buffer = new MemoryBuffer();
		final var uploadQuestion = new Upload( buffer );
		uploadQuestion.setMaxFiles( 1 );
		uploadQuestion.setDropLabel( new Label( "Upload a 10MB file in .yaml format" ) );
		uploadQuestion.setMaxFileSize( 10 * 1024 * 1024 );

		uploadQuestion.addSucceededListener( onSucceeded -> {

			final var inputStream = buffer.getInputStream();
			final var fileName = onSucceeded.getFileName();

			final var yamlFactory = new YAMLFactory();
			final var yamlMapper = new ObjectMapper( yamlFactory );
			try {

				final var beans = Arrays.asList( yamlMapper.readValue( inputStream, QuestionDto[].class ) );
				for ( QuestionDto question : beans ) {
					question.setTeacher( teacher );
					stepper.addStep( "Question", initQuestionTemplateLayout( question, question.getTasks() ) );
				}

				stepper.getFinish().addClickListener( importQuestionsEvent( beans ) );

				final var uploadMessage = beans.size() + " new questions with tasks from " + fileName;
				getNotifications().add( new DefaultNotification( "FILE UPLOAD", uploadMessage ) );

			} catch ( IOException ioException ) {
				getNotifications()
						.add( new DefaultNotification( "ERROR: FILE READ", ioException.getMessage(), Priority.ERROR ) );
			}

		} );

		uploadQuestion.addFileRejectedListener( onRejected -> getNotifications()
				.add( new DefaultNotification( "ERROR: FILE UPLOAD", onRejected.getErrorMessage() ) ) );

		batchImportLayout.add( uploadQuestion );

		stepper.setSizeFull();
		stepper.getNext().setIcon( VaadinIcon.ARROW_RIGHT.create() );
		stepper.getBack().setIcon( VaadinIcon.ARROW_LEFT.create() );
		stepper.getFinish().addThemeVariants( ButtonVariant.LUMO_PRIMARY );
		stepper.addStep( batchImportLayout );

		add( stepper );
	}


	private ComponentEventListener< ClickEvent< Button > > importQuestionsEvent( List< QuestionDto > questions ) {

		return onFinishClick -> {
			final var importedQuestions = batchImporter.batchImportQuestions( questions.toArray( QuestionDto[]::new ) );
			final var importedTasks = importedQuestions.stream().map( question -> batchImporter
							.batchImportTasks( question.getId(), question.getTasks().toArray( TaskDto[]::new ) ) )
					.collect( Collectors.toUnmodifiableSet() );

			final var message = importedQuestions.size() + " question(s) with " + importedTasks.size()
					+ " total tasks imported..";
			getNotifications().add( new DefaultNotification( "Question Batch Import", message ) );
		};
	}


	private VerticalLayout initQuestionTemplateLayout( QuestionDto question, List< TaskDto > tasks ) {

		final var layout = new VerticalLayout();
		layout.setId( String.valueOf( ( int ) System.currentTimeMillis() ) );

		layout.add( new H3( question.getHeader() ) );
		question.getContent().stream().map( Paragraph::new ).forEach( layout::add );

		final CheckboxGroup< TaskDto > choicesCheckBoxGroup = initTaskLayout( "Choices", question, tasks.stream()
				.filter( taskDto -> taskDto.getType() == TaskDto.Type.CHOICE ).collect( Collectors.toUnmodifiableList() ) );
		final CheckboxGroup< TaskDto > todosCheckBoxGroup = initTaskLayout( "Task(s)", question, tasks.stream()
				.filter( taskDto -> taskDto.getType() == TaskDto.Type.TODO ).collect( Collectors.toUnmodifiableList() ) );
		layout.add( choicesCheckBoxGroup, todosCheckBoxGroup );

		final var submitButton = new Button( "Update", updateQuestionEvent( choicesCheckBoxGroup, todosCheckBoxGroup ) );
		submitButton.addThemeVariants( ButtonVariant.LUMO_PRIMARY );

		final var singleImportButton = new Button( "Import", importQuestionsEvent( Collections.singletonList( question ) ) );
		singleImportButton.addThemeVariants( ButtonVariant.LUMO_PRIMARY );

		layout.add( submitButton, singleImportButton );
		return layout;

	}


	private ComponentEventListener< ClickEvent< Button > > updateQuestionEvent( CheckboxGroup< TaskDto > choicesCheckBoxGroup,
	                                                                            CheckboxGroup< TaskDto > todosCheckBoxGroup ) {

		return onClick -> {

			if ( !todosCheckBoxGroup.isEmpty() && !todosCheckBoxGroup.getSelectedItems().isEmpty() ) {
				final var selectedTodos = todosCheckBoxGroup.getValue();

				final var patchCounter = new AtomicInteger( 0 );
				for ( TaskDto selectedTodo : selectedTodos ) {
					patchCounter.getAndIncrement();
				}

				getNotifications().add( new DefaultNotification( "Question Updated !!", "Question with "
						+ patchCounter.get() + ( patchCounter.get() == 1 ? " todo" : " todos" ) + " updated.." ) );
			}

			if ( !choicesCheckBoxGroup.isEmpty() && !choicesCheckBoxGroup.getSelectedItems().isEmpty() ) {
				final var selectedChoices = choicesCheckBoxGroup.getValue();

				final var patchCounter = new AtomicInteger( 0 );
				for ( TaskDto selectedChoice : selectedChoices ) {
					patchCounter.getAndIncrement();
				}

				getNotifications().add( new DefaultNotification( "Question Updated !!", "Question with "
						+ patchCounter.get() + ( patchCounter.get() == 1 ? " choice" : " choices" ) + " updated.." ) );
			}

		};
	}


	private CheckboxGroup< TaskDto > initTaskLayout( String labelText, QuestionDto question, List< TaskDto > tasks ) {

		// TODO: for each task selection change event,
		// question data must be updated..
		// each task will have number field to update weight..

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
		checkboxGroup.setLabel( labelText.contains( "Choice" )
				? "Only" + correctTasksCount + " " + ( ( correctTasksCount <= 1 ) ? "choice is" : "choices are" )
				+ " correct from " + allTasksCount + " " + ( ( correctTasksCount <= 1 ) ? "choice" : "choices" )
				: allTasksCount + " to complete." );
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


	private void initParentComponentStyle() {

		setWidthFull();
		setPadding( false );
		setAlignItems( Alignment.CENTER );
	}


	@Override
	public String getViewName() {

		return TeacherQuestionEditorView.ROUTE;
	}

}
