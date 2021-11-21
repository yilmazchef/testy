package be.intecbrussel.student.views.teacher;


import be.intecbrussel.student.data.dto.UserDto;
import be.intecbrussel.student.data.entity.UserEntity;
import be.intecbrussel.student.data.index.UserFilter;
import be.intecbrussel.student.security.AuthenticatedUser;
import be.intecbrussel.student.service.IStudentService;
import be.intecbrussel.student.service.ITeacherService;
import be.intecbrussel.student.views.AbstractView;
import be.intecbrussel.student.views.MainAppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import javax.annotation.security.RolesAllowed;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static be.intecbrussel.student.views.teacher.TeacherStudentsView.ROUTE;
import static be.intecbrussel.student.views.teacher.TeacherStudentsView.TITLE;

@PageTitle( value = TITLE )
@Route( value = ROUTE, layout = MainAppLayout.class )
@RolesAllowed( { "TEACHER", "MANAGER" } )
public class TeacherStudentsView extends AbstractView {

	public static final String ROUTE = "teacher/students";
	public static final String TITLE = "Students by Classes";

	private final VaadinSession currentSession = VaadinSession.getCurrent();

	private final ITeacherService teacherService;
	private final IStudentService studentService;

	private final MainAppLayout appLayout;
	private final AuthenticatedUser authenticatedUser;


	public TeacherStudentsView( ITeacherService teacherService, IStudentService studentService, MainAppLayout appLayout, final AuthenticatedUser authenticatedUser ) {

		this.appLayout = appLayout;
		this.authenticatedUser = authenticatedUser;
		this.teacherService = teacherService;
		this.studentService = studentService;

		final var studentsLayout = new VerticalLayout();

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
			final var oTeacher = teacherService.fetchTeacherByUserName( user.getUsername() );
			oTeacher.ifPresent( userDto -> userBeingSearched.userId = userDto.getId() );
		}

		final var fetchedTeacher = this.teacherService.fetchTeacherById( userBeingSearched.userId );
		final var teacher = fetchedTeacher.orElseGet( () -> new UserDto().withAnonymous( true ) );


		final var searchLayout = new HorizontalLayout();
		searchLayout.setAlignItems( Alignment.CENTER );
		searchLayout.setJustifyContentMode( JustifyContentMode.CENTER );
		final var firstNameLabel = new Label( "First Name" );
		final var firstNameField = new TextField();
		final var lastNameLabel = new Label( "Last Name" );
		final var lastNameField = new TextField();
		final var classNameLabel = new Label( "Class Name" );
		final var classNameField = new TextField();
		final var userNameLabel = new Label( "User Name" );
		final var userNameField = new TextField();


		final var studentLabel = new Label( "Students" );
		final var studentsGrid = new Grid< UserDto >();
		final var studentProvider = initStudents();
		studentsGrid.setDataProvider( studentProvider );
		studentsGrid.addColumn( studentDto -> studentDto.getUsername() ).setHeader( "User" );
		studentsGrid.addColumn( manager -> manager.getFirstName() + " " + manager.getLastName() ).setHeader( "Student" );

		final var searchButton = new Button( "Search", onClick -> {
			final var students = this.studentService.fetchStudents(
					1, 25,
					new UserFilter()
							.withFirstName( firstNameField.getValue() )
							.withLastName( lastNameField.getValue() )
							.withClassName( classNameField.getValue() )
							.withUserName( userNameField.getValue() )
			);

			studentsGrid.setItems( students );
		} );

		studentsLayout.add( studentLabel, studentsGrid );

		searchLayout.add(
				firstNameLabel, firstNameField,
				lastNameLabel, lastNameField,
				classNameLabel, classNameField,
				userNameLabel, userNameField,
				searchButton
		);

		studentsLayout.add( searchLayout );

		add( studentsLayout );
	}


	private DataProvider< UserDto, Void > initStudents() {

		return DataProvider.fromCallbacks(
				// First callback fetches items based on a query
				query -> {
					// The index of the first item to load
					int offset = query.getOffset();
					// The number of items to load
					int limit = query.getLimit();

					List< UserDto > persons = studentService.fetchStudents( offset, limit );

					return persons.stream();
				},
				// Second callback fetches the total number of items currently in the Grid.
				// The grid can then use it to properly adjust the scrollbars.
				query -> studentService.getStudentsCount()
		);
	}


	private DataProvider< UserDto, String > initStudentsWithFilter() {

		return DataProvider.fromFilteringCallbacks(
				// First callback fetches items based on a query
				query -> {
					final var filter = query.getFilter().orElse( "" );
					// The index of the first item to load
					int offset = query.getOffset();
					// The number of items to load
					int limit = query.getLimit();

					List< UserDto > persons = filter.equals( "" ) ?
							studentService.fetchStudents( offset, limit, filter ) :
							studentService.fetchStudents( offset, limit );

					return persons.stream();
				},
				// Second callback fetches the total number of items currently in the Grid.
				// The grid can then use it to properly adjust the scrollbars.
				query -> studentService.getStudentsCount()
		);
	}


	@Override
	public String getViewName() {

		return TITLE;
	}

}