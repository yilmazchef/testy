package it.vkod.testy.views.student;


import it.vkod.testy.data.dto.ExamDto;
import it.vkod.testy.data.dto.QuestionDto;
import it.vkod.testy.data.dto.TaskDto;
import it.vkod.testy.data.dto.UserDto;
import it.vkod.testy.data.entity.UserEntity;
import it.vkod.testy.security.AuthenticatedUser;
import it.vkod.testy.service.IExamService;
import it.vkod.testy.service.IQuestionService;
import it.vkod.testy.service.IStudentService;
import it.vkod.testy.util.QuestionBatchImporter;
import it.vkod.testy.views.AbstractView;
import it.vkod.testy.views.DefaultNotification;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.VaadinSession;

import javax.annotation.security.PermitAll;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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


	public StudentQuestionImporterView( final IExamService examService, final IQuestionService questionService, final IStudentService studentService,
	final QuestionBatchImporter batchImporter, final AuthenticatedUser authenticatedUser ) {

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
			final var oStudent = studentService.fetchStudentById( userBeingSearched.userId );

			oStudent.ifPresent( userDto -> {
				Notification.show( userDto.getEmail() ).open();
				add( initBatchImporterLayout( userDto ) );
			} );
		}


	}


	private VerticalLayout initBatchImporterLayout( UserDto student ) {

		final var importLayout = new VerticalLayout();
		final var teacherCode = new TextField();
		teacherCode.setRequired( true );
		teacherCode.setPattern( "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}" );

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
			} catch ( JsonMappingException e ) {


			} catch ( JsonParseException e ) {
				e.printStackTrace();
			} catch ( IOException e ) {
				e.printStackTrace();
			}


		} );

		upload.addFileRejectedListener( onRejected -> {
			uploadStatusDiv.removeAll();
			new Notification( onRejected.getErrorMessage(), 2000, Notification.Position.BOTTOM_CENTER ).open();
		} );

		upload.getElement().addEventListener( "file-remove", onRemoved -> uploadStatusDiv.removeAll() );

		importLayout.add( upload, uploadStatusDiv, teacherCode );

		add( importLayout );

		return importLayout;
	}


	private boolean initExams( final UserDto student, final String examCode, final List< QuestionDto > questions ) {

		final var examsCount = questions
				.stream()
				.flatMap( questionDto -> questionDto.getTasks().stream() )
				.map( taskDto -> newExam( examCode, taskDto, student ) )
				.map( examService::create )
				.filter( savedId -> !savedId.equalsIgnoreCase( "-1" ) )
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
