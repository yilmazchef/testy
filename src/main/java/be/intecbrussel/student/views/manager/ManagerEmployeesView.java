package be.intecbrussel.student.views.manager;


import be.intecbrussel.student.data.dto.UserDto;
import be.intecbrussel.student.security.AuthenticatedUser;
import be.intecbrussel.student.service.IManagerService;
import be.intecbrussel.student.service.IStudentService;
import be.intecbrussel.student.service.ITeacherService;
import be.intecbrussel.student.views.AbstractView;
import be.intecbrussel.student.views.MainAppLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.sql.SQLException;
import java.util.List;

import static be.intecbrussel.student.views.manager.ManagerEmployeesView.ROUTE;
import static be.intecbrussel.student.views.manager.ManagerEmployeesView.TITLE;

@PageTitle( value = TITLE )
@Route( value = ROUTE, layout = MainAppLayout.class )
public class ManagerEmployeesView extends AbstractView {

	public static final String ROUTE = "manager/employees";
	public static final String TITLE = "Employees by Department";

	private final VaadinSession currentSession = VaadinSession.getCurrent();

	private final MainAppLayout appLayout;
	private final VerticalLayout employeesLayout;

	private final AuthenticatedUser authenticatedUser;
	private final IManagerService managerService;
	private final ITeacherService teacherService;
	private final IStudentService studentService;


	public ManagerEmployeesView( final MainAppLayout appLayout,
	                             final AuthenticatedUser authenticatedUser,
	                             final IManagerService managerService, final ITeacherService teacherService, final IStudentService studentService ) {

		this.appLayout = appLayout;
		this.authenticatedUser = authenticatedUser;
		this.managerService = managerService;
		this.teacherService = teacherService;
		this.studentService = studentService;
		this.employeesLayout = new VerticalLayout();

		final var managerBeingSearched = new Object() {
			String username = null;
		};
		try {
			final var oUser = authenticatedUser.get();
			oUser.ifPresent( userEntity -> managerBeingSearched.username = userEntity.getUsername() );
		} catch ( SQLException sqlException ) {
			Notification.show( sqlException.getMessage(), 3000, Notification.Position.BOTTOM_CENTER ).open();
		}

		final var fetchedManager = this.managerService.fetchManagerByUserName( managerBeingSearched.username );
		final var teacher = fetchedManager.orElseGet( () -> new UserDto().withAnonymous( true ) );

		final var managerLabel = new Label( "Managers" );
		final var managerProvider = initManagers( this.managerService );
		final var managersGrid = new Grid< UserDto >();
		managersGrid.setDataProvider( managerProvider );
		managersGrid.addColumn( managerDto -> managerDto.getUsername() ).setHeader( "User" );
		managersGrid.addColumn( manager -> manager.getFirstName() + " " + manager.getLastName() ).setHeader( "Manager" );
		managersGrid.addColumn( managerDto -> String.join( ",", managerDto.getRoles() ) ).setHeader( "Roles" );

		final var teacherLabel = new Label( "Teachers" );
		final var teacherProvider = initTeachers( this.teacherService );
		final var teachersGrid = new Grid< UserDto >();
		teachersGrid.setDataProvider( teacherProvider );
		teachersGrid.addColumn( teacherDto -> teacherDto.getUsername() ).setHeader( "User" );
		teachersGrid.addColumn( manager -> manager.getFirstName() + " " + manager.getLastName() ).setHeader( "Teacher" );
		teachersGrid.addColumn( teacherDto -> String.join( ",", teacherDto.getRoles() ) ).setHeader( "Roles" );

		final var studentLabel = new Label( "Students" );
		final var studentProvider = initStudents( this.studentService );
		final var studentsGrid = new Grid< UserDto >();
		studentsGrid.setDataProvider( studentProvider );
		studentsGrid.addColumn( studentDto -> studentDto.getUsername() ).setHeader( "User" );
		studentsGrid.addColumn( manager -> manager.getFirstName() + " " + manager.getLastName() ).setHeader( "Student" );

		this.employeesLayout.add(
				managerLabel, managersGrid,
				teacherLabel, teachersGrid,
				studentLabel, studentsGrid
		);
		add( this.employeesLayout );
	}


	private DataProvider< UserDto, Void > initManagers( IManagerService managerService ) {

		return DataProvider.fromCallbacks(
				// First callback fetches items based on a query
				query -> {
					// The index of the first item to load
					int offset = query.getOffset();
					// The number of items to load
					int limit = query.getLimit();

					List< UserDto > teachers = managerService.fetchManagers( offset, limit );

					return teachers.stream();
				},
				// Second callback fetches the total number of items currently in the Grid.
				// The grid can then use it to properly adjust the scrollbars.
				query -> managerService.getManagersCount()
		);
	}


	private DataProvider< UserDto, Void > initTeachers( ITeacherService teacherService ) {

		return DataProvider.fromCallbacks(
				// First callback fetches items based on a query
				query -> {
					// The index of the first item to load
					int offset = query.getOffset();
					// The number of items to load
					int limit = query.getLimit();

					List< UserDto > teachers = teacherService.fetchTeachers( offset, limit );

					return teachers.stream();
				},
				// Second callback fetches the total number of items currently in the Grid.
				// The grid can then use it to properly adjust the scrollbars.
				query -> teacherService.getTeachersCount()
		);
	}


	private DataProvider< UserDto, Void > initStudents( IStudentService studentService ) {

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


	@Override
	public String getViewName() {

		return TITLE;
	}

}