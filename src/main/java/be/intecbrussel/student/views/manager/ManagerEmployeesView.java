package be.intecbrussel.student.views.manager;

import be.intecbrussel.student.data.dto.*;
import be.intecbrussel.student.security.SecurityUtils;
import be.intecbrussel.student.service.IManagerService;
import be.intecbrussel.student.service.IStudentService;
import be.intecbrussel.student.service.ITeacherService;
import be.intecbrussel.student.views.AbstractView;
import be.intecbrussel.student.views.MainAppLayout;
import be.intecbrussel.student.views.user.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;

import static be.intecbrussel.student.views.manager.ManagerEmployeesView.ROUTE;
import static be.intecbrussel.student.views.manager.ManagerEmployeesView.TITLE;

@PageTitle(value = TITLE)
@Route(value = ROUTE, layout = MainAppLayout.class)
public class ManagerEmployeesView extends AbstractView {

    public static final String ROUTE = "manager/employees";
    public static final String TITLE = "Employees by Department";

    private final VaadinSession currentSession = VaadinSession.getCurrent();

    private final ITeacherService teacherService;
    private final IStudentService studentService;
    private final IManagerService managerService;

    private final MainAppLayout appLayout;
    private final VerticalLayout employeesLayout;

    public ManagerEmployeesView(ITeacherService teacherService, IStudentService studentService, IManagerService managerService, MainAppLayout appLayout) {

        this.appLayout = appLayout;
        this.employeesLayout = new VerticalLayout();

        this.teacherService = teacherService;
        this.studentService = studentService;
        this.managerService = managerService;

        if (!SecurityUtils.isUserLoggedIn()) {
            UI.getCurrent().navigate(LoginView.class, new RouteParameters("redirected", "manager"));
        } else {
            final var authenticatedUser = SecurityUtils.getAuthenticatedUser();
            final var fetchedManager = this.managerService.fetchManagerByUserName(authenticatedUser.getName());
            final var teacher = fetchedManager.orElseGet(() -> new ManagerDto().withAnonymous(true));

            final var managerLabel = new Label("Managers");
            final var managerProvider = initManagers(this.managerService);
            final var managersGrid = new Grid<ManagerDto>();
            managersGrid.setDataProvider(managerProvider);
            managersGrid.addColumn(managerDto -> managerDto.getUsername()).setHeader("User");
            managersGrid.addColumn(manager -> manager.getFirstName() + " " + manager.getLastName()).setHeader("Manager");
            managersGrid.addColumn(managerDto -> String.join(",", managerDto.getRoles())).setHeader("Roles");

            final var teacherLabel = new Label("Teachers");
            final var teacherProvider = initTeachers(this.teacherService);
            final var teachersGrid = new Grid<TeacherDto>();
            teachersGrid.setDataProvider(teacherProvider);
            teachersGrid.addColumn(teacherDto -> teacherDto.getUsername()).setHeader("User");
            teachersGrid.addColumn(manager -> manager.getFirstName() + " " + manager.getLastName()).setHeader("Teacher");
            teachersGrid.addColumn(teacherDto -> String.join(",", teacherDto.getRoles())).setHeader("Roles");

            final var studentLabel = new Label("Students");
            final var studentProvider = initStudents(this.studentService);
            final var studentsGrid = new Grid<StudentDto>();
            studentsGrid.setDataProvider(studentProvider);
            studentsGrid.addColumn(studentDto -> studentDto.getUsername()).setHeader("User");
            studentsGrid.addColumn(manager -> manager.getFirstName() + " " + manager.getLastName()).setHeader("Student");
            studentsGrid.addColumn(studentDto -> studentDto.getClassName()).setHeader("Class");

            this.employeesLayout.add(
                    managerLabel, managersGrid,
                    teacherLabel, teachersGrid,
                    studentLabel, studentsGrid
            );
            add(this.employeesLayout);
        }
    }

    private DataProvider<ManagerDto, Void> initManagers(IManagerService managerService) {
        return DataProvider.fromCallbacks(
                // First callback fetches items based on a query
                query -> {
                    // The index of the first item to load
                    int offset = query.getOffset();
                    // The number of items to load
                    int limit = query.getLimit();

                    List<ManagerDto> teachers = managerService.fetchManagers(offset, limit);

                    return teachers.stream();
                },
                // Second callback fetches the total number of items currently in the Grid.
                // The grid can then use it to properly adjust the scrollbars.
                query -> managerService.getManagersCount()
        );
    }

    private DataProvider<TeacherDto, Void> initTeachers(ITeacherService teacherService) {
        return DataProvider.fromCallbacks(
                // First callback fetches items based on a query
                query -> {
                    // The index of the first item to load
                    int offset = query.getOffset();
                    // The number of items to load
                    int limit = query.getLimit();

                    List<TeacherDto> teachers = teacherService.fetchTeachers(offset, limit);

                    return teachers.stream();
                },
                // Second callback fetches the total number of items currently in the Grid.
                // The grid can then use it to properly adjust the scrollbars.
                query -> teacherService.getTeachersCount()
        );
    }

    private DataProvider<StudentDto, Void> initStudents(IStudentService studentService) {
        return DataProvider.fromCallbacks(
                // First callback fetches items based on a query
                query -> {
                    // The index of the first item to load
                    int offset = query.getOffset();
                    // The number of items to load
                    int limit = query.getLimit();

                    List<StudentDto> persons = studentService.fetchStudents(offset, limit);

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