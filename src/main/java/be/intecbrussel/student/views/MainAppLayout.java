package be.intecbrussel.student.views;

import be.intecbrussel.student.security.SecurityUtils;
import be.intecbrussel.student.views.anonymous.AnonymousExamAnalyticsView;
import be.intecbrussel.student.views.anonymous.AnonymousNewExamView;
import be.intecbrussel.student.views.commons.ContinueOnAnotherDeviceView;
import be.intecbrussel.student.views.commons.PluginsView;
import be.intecbrussel.student.views.manager.ManagerEmployeesView;
import be.intecbrussel.student.views.student.StudentExamAnalyticsView;
import be.intecbrussel.student.views.student.StudentNewExamView;
import be.intecbrussel.student.views.student.StudentQuestionImporterView;
import be.intecbrussel.student.views.teacher.TeacherExamGeneratorView;
import be.intecbrussel.student.views.teacher.TeacherQuestionEditorView;
import be.intecbrussel.student.views.teacher.TeacherQuestionTemplateGeneratorView;
import be.intecbrussel.student.views.teacher.TeacherStudentsView;
import be.intecbrussel.student.views.user.LoginView;
import be.intecbrussel.student.views.user.RegisterView;
import com.github.appreciated.app.layout.addons.notification.DefaultNotificationHolder;
import com.github.appreciated.app.layout.addons.notification.component.NotificationButton;
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder;
import com.github.appreciated.app.layout.component.applayout.LeftLayouts;
import com.github.appreciated.app.layout.component.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftSubMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftClickableItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftHeaderItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.component.router.AppLayoutRouterLayout;
import com.github.appreciated.app.layout.entity.DefaultBadgeHolder;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static com.github.appreciated.app.layout.entity.Section.FOOTER;
import static com.github.appreciated.app.layout.entity.Section.HEADER;

@Push
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@PWA(name = "Intec Brussel Online Test Platform", shortName = "Testy", iconPath = "icons/icon.png")
@Component
@UIScope // optional but useful; allows access to this instance from views
@CssImport("./styles/views/main/main-view.css")
public class MainAppLayout extends AppLayoutRouterLayout<LeftLayouts.LeftResponsive> {

    private final DefaultNotificationHolder notifications = new DefaultNotificationHolder();
    private final DefaultBadgeHolder badge = new DefaultBadgeHolder(5);
    private final VaadinSession currentSession = VaadinSession.getCurrent();

    public MainAppLayout() {

        notifications.addClickListener(notification -> {/* ... */});

        final var menuEntry = new LeftNavigationItem("Menu", VaadinIcon.MENU.create(), MenuView.class);
        badge.bind(menuEntry.getBadge());

        final var anonymousExamRoute = new LeftNavigationItem("Exam", VaadinIcon.TASKS.create(), AnonymousNewExamView.class);
        final var anonymousExamAnalyticsRoute = new LeftNavigationItem("Analytics", VaadinIcon.PIE_BAR_CHART.create(), AnonymousExamAnalyticsView.class);
        final var anonymousContinueExam = new LeftNavigationItem("Move", VaadinIcon.MOBILE_BROWSER.create(), ContinueOnAnotherDeviceView.class);

        final var userRegisterRoute = new LeftNavigationItem("Register", VaadinIcon.USER_CARD.create(), RegisterView.class);
        final var userLoginRoute = new LeftNavigationItem("Login", VaadinIcon.USER_CHECK.create(), LoginView.class);
        final var userLogoutRoute = new LeftClickableItem("Logout", VaadinIcon.SIGN_OUT.create(), onClick -> UI.getCurrent().navigate("/logout"));

        final var studentNewExamRoute = new LeftNavigationItem("Exam", VaadinIcon.TASKS.create(), StudentNewExamView.class);
        final var studentImportExamRoute = new LeftNavigationItem("Import", VaadinIcon.FILE_ADD.create(), StudentQuestionImporterView.class);
        final var studentExamAnalyticsRoute = new LeftNavigationItem("Analytics", VaadinIcon.PIE_BAR_CHART.create(), StudentExamAnalyticsView.class);
        final var studentContinueExam = new LeftNavigationItem("Move", VaadinIcon.MOBILE_BROWSER.create(), ContinueOnAnotherDeviceView.class);

        final var teacherQuestionEditorRoute = new LeftNavigationItem("Question", VaadinIcon.QUESTION.create(), TeacherQuestionEditorView.class);
        final var teacherExamGeneratorRoute = new LeftNavigationItem("Exam", VaadinIcon.FILE_TREE.create(), TeacherExamGeneratorView.class);
        final var teacherGenerateExamFileRoute = new LeftNavigationItem("Generate", VaadinIcon.TERMINAL.create(), TeacherQuestionTemplateGeneratorView.class);
        final var teacherClassRoute = new LeftNavigationItem("Students", VaadinIcon.ACADEMY_CAP.create(), TeacherStudentsView.class);

        final var managerEmployeesRoute = new LeftNavigationItem("Employees", VaadinIcon.GROUP.create(), ManagerEmployeesView.class);

        final var themeChangeEvent = new LeftClickableItem("Theme", VaadinIcon.MOON.create(), click -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();
            if (themeList.contains(Lumo.DARK)) themeList.remove(Lumo.DARK);
            else themeList.add(Lumo.DARK);
        });

        final var pluginsRoute = new LeftNavigationItem("Plugins", VaadinIcon.PLUG.create(), PluginsView.class);
        final var invalidateSessionEvent = new LeftClickableItem("Reset All", VaadinIcon.HAMMER.create(), onClick -> currentSession.getSession().invalidate());

        final var userSubMenuWithLogout = LeftSubMenuBuilder
                .get("User", VaadinIcon.USER.create())
                .add(userRegisterRoute, userLogoutRoute)
                .build();

        final var userSubMenuWithLogin = LeftSubMenuBuilder
                .get("User", VaadinIcon.USER.create())
                .add(userLoginRoute, userRegisterRoute)
                .build();

        final var anonymousSubMenu = LeftSubMenuBuilder
                .get("Anonymous", VaadinIcon.USERS.create())
                .add(anonymousExamRoute, anonymousExamAnalyticsRoute, anonymousContinueExam)
                .build();

        final var studentSubMenu = LeftSubMenuBuilder
                .get("Student", VaadinIcon.ACADEMY_CAP.create())
                .add(studentNewExamRoute, studentExamAnalyticsRoute, studentImportExamRoute, studentContinueExam)
                .build();

        final var teacherSubMenu = LeftSubMenuBuilder
                .get("Teacher", VaadinIcon.BRIEFCASE.create())
                .add(teacherQuestionEditorRoute, teacherExamGeneratorRoute, teacherGenerateExamFileRoute, teacherClassRoute)
                .build();

        final var managerSubMenu = LeftSubMenuBuilder
                .get("Manager", VaadinIcon.WORKPLACE.create())
                .add(managerEmployeesRoute)
                .build();

        final var menu = LeftAppMenuBuilder.get().addToSection(HEADER, new LeftHeaderItem("Online Test Platform", "Version 1.0.0", "images/logo.png"));

        final var title = new StringBuilder();

        if (SecurityUtils.isUserLoggedIn()) {
            final var authenticatedUser = SecurityUtils.getAuthenticatedUser();

            final var authorities = authenticatedUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

            final var studentRole = "STUDENT_ROLE";
            final var teacherRole = "TEACHER_ROLE";
            final var managerRole = "MANAGER_ROLE";

            menu.add(userSubMenuWithLogout);

            if (authenticatedUser.isAuthenticated()
                    && authorities.contains(studentRole)
                    && !authorities.contains(teacherRole)
                    && !authorities.contains(managerRole)
            ) {
                menu.add(studentSubMenu);
            } else if (authenticatedUser.isAuthenticated()
                    && authorities.contains(teacherRole)
                    && !authorities.contains(managerRole)
            ) {
                menu.add(teacherSubMenu);
            } else if (authenticatedUser.isAuthenticated()
                    && authorities.contains(managerRole)
            ) {
                menu.add(managerSubMenu, teacherSubMenu, studentSubMenu);
            }

            title.append(authenticatedUser.getName());

        } else {
            menu.add(userSubMenuWithLogin);
            menu.add(anonymousSubMenu);
            title.append("anonymous");
        }

        menu.add(pluginsRoute, invalidateSessionEvent);
        menu.addToSection(FOOTER, themeChangeEvent);

        init(AppLayoutBuilder
                .get(LeftLayouts.LeftResponsive.class).withTitle(title.toString())
                .withAppBar(AppBarBuilder.get().add(new NotificationButton<>(VaadinIcon.BELL, notifications)).build())
                .withAppMenu(menu.build())
                .build());

    }

    public DefaultNotificationHolder getNotifications() {
        return notifications;
    }

    public DefaultBadgeHolder getBadge() {
        return badge;
    }
}
