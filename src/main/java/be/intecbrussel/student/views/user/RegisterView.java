package be.intecbrussel.student.views.user;

import be.intecbrussel.student.security.SecurityUtils;
import be.intecbrussel.student.service.EmailService;
import be.intecbrussel.student.service.IManagerService;
import be.intecbrussel.student.service.IStudentService;
import be.intecbrussel.student.service.ITeacherService;
import be.intecbrussel.student.views.AbstractView;
import be.intecbrussel.student.views.MainAppLayout;
import be.intecbrussel.student.views.anonymous.AnonymousNewExamView;

import com.github.appreciated.app.layout.addons.notification.entity.DefaultNotification;
import com.vaadin.flow.component.Direction;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Objects;

import static be.intecbrussel.student.views.user.RegisterView.RegistryType.*;

@PageTitle(RegisterView.TITLE)
@Route(value = RegisterView.ROUTE, layout = MainAppLayout.class)
public class RegisterView extends AbstractView implements BeforeEnterObserver, LocaleChangeObserver {

    public static final String TITLE = "Register | Testy";
    public static final String ROUTE = "register";

    private final IStudentService studentService;
    private final ITeacherService teacherService;
    private final IManagerService managerService;

    private final MainAppLayout appLayout;
    private final VerticalLayout registryLayout;
    private final H2 registryFormHeader;

    private final StudentRegisterView studentRegisterView;
    private final TeacherRegisterView teacherRegisterView;
    private final ManagerRegisterView managerRegisterView;

    private final EmailService emailService;

    public enum RegistryType {
        Student, Teacher, Manager
    }

    public RegisterView(IStudentService studentService, ITeacherService teacherService, IManagerService managerService,
            MainAppLayout appLayout, EmailService emailService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.managerService = managerService;

        this.emailService = emailService;

        this.appLayout = appLayout;

        initParentStyle();

        if (SecurityUtils.isUserLoggedIn()) {
            appLayout.getNotifications().add(new DefaultNotification("ALREADY LOGGED IN",
                    "You have already logged, to register with another account please logout first.."));
        }

        this.registryLayout = new VerticalLayout();
        this.registryLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        
        this.registryFormHeader = new H2("Registry Form");

        this.studentRegisterView = new StudentRegisterView(studentService, appLayout, emailService);
        this.teacherRegisterView = new TeacherRegisterView(teacherService, appLayout, emailService);
        this.managerRegisterView = new ManagerRegisterView(managerService, appLayout, emailService);

        this.studentRegisterView.setVisible(false);
        this.teacherRegisterView.setVisible(false);
        this.managerRegisterView.setVisible(false);

        final var registryTypeComboBox = new ComboBox<RegistryType>();
        registryTypeComboBox.setItemLabelGenerator(RegistryType::name);
        registryTypeComboBox.setItems(Manager, Teacher, Student);
        registryTypeComboBox.addValueChangeListener(onChange -> {
            switch (onChange.getValue()) {
            case Student:
                changeRegistryForm("Student Registry Form", true, false, false);
                break;
            case Teacher:
                changeRegistryForm("Teacher Registry Form", false, true, false);
                break;
            case Manager:
                changeRegistryForm("Manager Registry Form", false, false, true);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + onChange.getValue());
            }
        });

        this.registryLayout.add(this.registryFormHeader, registryTypeComboBox, this.studentRegisterView,
                this.teacherRegisterView, this.managerRegisterView);

        add(this.registryLayout);

    }

    private void changeRegistryForm(final String header, final boolean isStudent, final boolean isTeacher,
            final boolean isManager) {
        this.registryFormHeader.setText(header);
        this.studentRegisterView.setVisible(isStudent);
        this.teacherRegisterView.setVisible(isTeacher);
        this.managerRegisterView.setVisible(isManager);
    }

    private void initParentStyle() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

        if (SecurityUtils.isUserLoggedIn()) {
            
            final var dialog = new Dialog();
            dialog.add(new Text("You have already logged with an existing account, are you sure you would like to create another?"));
            dialog.setCloseOnEsc(false);
            dialog.setCloseOnOutsideClick(false);

            final var message = new Span();

            final var confirmButton = new Button("Confirm", event -> {
                message.setText("Confirmed!");
                dialog.close();
            });
            final var cancelButton = new Button("Cancel", event -> {
                message.setText("Cancelled...");
                dialog.close();
                UI.getCurrent().navigate(AnonymousNewExamView.class);
            });

            // Cancel action on ESC press
            Shortcuts.addShortcutListener(dialog, () -> {
                message.setText("Cancelled...");
                dialog.close();
                UI.getCurrent().navigate(AnonymousNewExamView.class);
            }, Key.ESCAPE);

            // Confirm action on Enter press
            Shortcuts.addShortcutListener(dialog, () -> {
                message.setText("Confirmed...");
                dialog.close();
            }, Key.ENTER);

            dialog.add(new Div(confirmButton, cancelButton));
        }
    }

    @Override
    public String getViewName() {
        return RegisterView.TITLE;
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        if (Objects.equals(event.getLocale().getLanguage(), "ar")) {
            event.getUI().setDirection(Direction.RIGHT_TO_LEFT);
        } else {
            event.getUI().setDirection(Direction.LEFT_TO_RIGHT);
        }
    }
}
