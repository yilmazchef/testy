package be.intecbrussel.student.views.student;


import be.intecbrussel.student.data.dto.ExamDto;
import be.intecbrussel.student.data.dto.QuestionDto;
import be.intecbrussel.student.data.dto.TaskDto;
import be.intecbrussel.student.data.dto.UserDto;
import be.intecbrussel.student.data.entity.UserEntity;
import be.intecbrussel.student.security.AuthenticatedUser;
import be.intecbrussel.student.service.IExamService;
import be.intecbrussel.student.service.IQuestionService;
import be.intecbrussel.student.service.IStudentService;
import be.intecbrussel.student.util.QuestionBatchImporter;
import be.intecbrussel.student.views.AbstractView;
import be.intecbrussel.student.views.DefaultNotification;
import be.intecbrussel.student.views.Priority;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.http.HttpEntity;

import javax.annotation.security.PermitAll;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@PageTitle( StudentQuestionImporterView.TITLE )
@Route( value = StudentQuestionImporterView.ROUTE )
@PermitAll
public class StudentQuestionImporterView extends AbstractView {

	public static final String TITLE = "Student Exam Loader from External Resource(s)";
	public static final String ROUTE = "student/load";

	private final VaadinSession currentSession = VaadinSession.getCurrent();

	private final IExamService examService;
	private final IQuestionService questionService;
	private final IStudentService studentService;
	private final QuestionBatchImporter batchImporter;
	private final AuthenticatedUser authenticatedUser;


	public StudentQuestionImporterView( IExamService examService, IQuestionService questionService, IStudentService studentService,
	                                    QuestionBatchImporter batchImporter, final AuthenticatedUser authenticatedUser ) {

		this.examService = examService;
		this.questionService = questionService;
		this.studentService = studentService;
		this.batchImporter = batchImporter;
		this.authenticatedUser = authenticatedUser;

		initParentComponentStyle();

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
			final var fetchedStudent = studentService.fetchStudentByUserName( userBeingSearched.userId );
			add( initBatchImporterLayout( fetchedStudent.orElseGet( () -> new UserDto().withAnonymous( true ) ) ) );

		}


	}


	private VerticalLayout initBatchImporterLayout( UserDto student ) {

		final var importLayout = new VerticalLayout();
		final var buffer = new MemoryBuffer();
		final var upload = new Upload( buffer );
		final var uploadStatusDiv = new Div();

		upload.addSucceededListener( onSucceeded -> {

			final var inputStream = buffer.getInputStream();
			final var fileName = onSucceeded.getFileName();

			final var yamlFactory = new YAMLFactory();
			final var yamlMapper = new ObjectMapper( yamlFactory );
			try {

				// TODO: student can only import existing questions ..
				// a new batchImporter must be created to make sure all questions exist in the DB
				// all tasks will have no weight but when import is done, questions will retrieve
				// other required data from DB to make sure tasks have weights..

				final var beans = Arrays.asList( yamlMapper.readValue( inputStream, QuestionDto[].class ) );
				final var countResponse = examService.countByQuestions(
						beans.stream().map( QuestionDto::getId ).collect( Collectors.toUnmodifiableSet() ),
						currentSession.getSession().getId() );

				if ( countResponse.hasBody() ) {
					for ( QuestionDto bean : beans ) {
						bean.setTeacher( new UserDto().withAnonymous( true ).withId( UUID.randomUUID().toString() ) );
					}

					final var importedQuestions = batchImporter.batchImportQuestions( beans.toArray( QuestionDto[]::new ) );
					final var importedTasks = importedQuestions
							.stream()
							.map( task -> batchImporter.batchImportTasks( task.getId(), task.getTasks().toArray( TaskDto[]::new ) ) )
							.collect( Collectors.toUnmodifiableList() );

					uploadStatusDiv.setText( importedQuestions.size() + " new questions with " + importedTasks.size() + " new tasks in total." );

					final var code = fileName.replace( ".yaml", "" );
					final var examsInitialized = initExams( student, code, importedQuestions );

					final var questionImportMessage = "Question(s) from " + fileName + " is/are imported: ";
					getNotifications().add( new DefaultNotification( "Question Batch Import", questionImportMessage ) );

					uploadStatusDiv.removeAll();

					if ( examsInitialized ) {
						final var examsGeneratedMessage = "New exams based on the questions imported are generated..";
						getNotifications().add( new DefaultNotification( "New Exam", examsGeneratedMessage ) );
						UI.getCurrent().navigate( StudentNewExamView.class, new RouteParameters( "code", code ) );
					}
				}

			} catch ( IOException ioException ) {
				getNotifications().add( new DefaultNotification( "ERROR", ioException.getMessage(), Priority.ERROR ) );
			}

		} );

		upload.addFileRejectedListener( onRejected -> {
			uploadStatusDiv.removeAll();
			new Notification( onRejected.getErrorMessage(), 2000, Notification.Position.BOTTOM_CENTER ).open();
		} );

		upload.getElement().addEventListener( "file-remove", onRemoved -> uploadStatusDiv.removeAll() );

		importLayout.add( upload, uploadStatusDiv );

		add( importLayout );

		return importLayout;
	}


	private boolean initExams( final UserDto student, final String examCode, final List< QuestionDto > questions ) {

		final var examsCount = questions
				.stream()
				.flatMap( questionDto -> questionDto.getTasks().stream() )
				.map( taskDto -> newExam( examCode, taskDto, student ) )
				.map( examService::create )
				.filter( HttpEntity::hasBody )
				.count();

		final var message = "An exam with " + examCode + " and with " + examsCount + ( examsCount == 1 ? "question" : "questions" ) + " is generated.";
		getNotifications().add( new DefaultNotification( "New Exam is Ready", message ) );

		return examsCount > 0;
	}


	private ExamDto newExam( final String examCode, final TaskDto task, UserDto student ) {

		return new ExamDto()
				.withCode( examCode )
				.withSession( currentSession.getSession().getId() )
				.withStartTime( Timestamp.valueOf( LocalDateTime.now() ) )
				.withEndTime( Timestamp.valueOf( LocalDateTime.now() ) )
				.withStudent( student )
				.withTask( task )
				.withScore( task.getWeight() );
	}


	private void initParentComponentStyle() {

		setWidthFull();
		setMargin( false );
		setPadding( false );
		setHorizontalComponentAlignment( Alignment.CENTER );
	}


	@Override
	public String getViewName() {

		return StudentQuestionImporterView.ROUTE;
	}

}
