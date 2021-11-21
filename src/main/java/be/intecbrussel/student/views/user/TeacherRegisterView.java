package be.intecbrussel.student.views.user;

import be.intecbrussel.student.data.dto.ManagerDto;
import be.intecbrussel.student.data.dto.StudentDto;
import be.intecbrussel.student.data.dto.TeacherDto;
import be.intecbrussel.student.security.SecurityUtils;
import be.intecbrussel.student.service.EmailService;
import be.intecbrussel.student.service.ITeacherService;
import be.intecbrussel.student.views.MainAppLayout;
import com.github.appreciated.app.layout.addons.notification.entity.DefaultNotification;
import com.github.appreciated.app.layout.addons.notification.entity.Priority;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializablePredicate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class TeacherRegisterView extends VerticalLayout {

    private final FormLayout layoutWithBinder;
    private final Binder<TeacherDto> binder;
    private final TeacherDto teacherBeingEdited;
    private final MainAppLayout appLayout;

    public TeacherRegisterView(ITeacherService teacherService, MainAppLayout appLayout, EmailService emailService) {
        this.layoutWithBinder = new FormLayout();
        this.binder = new Binder<>();
        this.appLayout = appLayout;

        initParentStyle();

        if (!SecurityUtils.isUserLoggedIn()) {
            this.teacherBeingEdited = new TeacherDto();
        } else {
            final var authenticatedUser = SecurityUtils.getAuthenticatedUser();
            final var fetchedStudent = teacherService.fetchTeacherByUserName(authenticatedUser.getName());
            this.teacherBeingEdited = fetchedStudent.orElseGet(() -> new TeacherDto().withAnonymous(true));
        }

        // Create the fields
        final var firstName = new TextField();
        firstName.setValueChangeMode(ValueChangeMode.EAGER);
        final var lastName = new TextField();
        lastName.setValueChangeMode(ValueChangeMode.EAGER);
        final var department = new TextField();
        department.setValueChangeMode(ValueChangeMode.EAGER);
        final var username = new EmailField();
        username.setValueChangeMode(ValueChangeMode.EAGER);
        final var password = new PasswordField();
        password.setValueChangeMode(ValueChangeMode.EAGER);

        final var infoLabel = new Label();
        final var saveButton = new Button("Submit");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        final var resetButton = new Button("Clear All");
        resetButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        layoutWithBinder.addFormItem(firstName, "First Name");
        layoutWithBinder.addFormItem(lastName, "Last Name");
        layoutWithBinder.addFormItem(department, "Department");
        layoutWithBinder.addFormItem(username, "Email (username)");
        layoutWithBinder.addFormItem(password, "Password");

        // Button bar
        final var actionsLayout = new HorizontalLayout();
        actionsLayout.add(saveButton, resetButton);
        saveButton.getStyle().set("marginRight", "10px");

        // E-mail has specific validators
        binder.forField(username).withNullRepresentation("")
                .withValidator(value -> !username.getValue().trim().isEmpty() && value.endsWith("@intecbrussel.be"),
                        "Email is not valid to register")
                .withValidator(new EmailValidator("Incorrect email address"))
                .bind(TeacherDto::getUsername, TeacherDto::setUsername);

        // FirstName is a required field
        firstName.setRequiredIndicatorVisible(true);
        binder.forField(firstName).withValidator(new StringLengthValidator("Please set your first name", 1, null))
                .bind(TeacherDto::getFirstName, TeacherDto::setFirstName);

        // LastName is a required field
        lastName.setRequiredIndicatorVisible(true);
        binder.forField(lastName).withValidator(new StringLengthValidator("Please set your last name", 1, null))
                .bind(TeacherDto::getLastName, TeacherDto::setLastName);

        // Password is a required field
        password.setRequiredIndicatorVisible(true);
        binder.forField(password).withValidator(new StringLengthValidator("Please set your password", 1, null))
                .bind(TeacherDto::getPassword, TeacherDto::setPassword);

        // Click listeners for the buttons
        saveButton.addClickListener(onClick -> {
            if (binder.writeBeanIfValid(teacherBeingEdited)) {
                teacherBeingEdited.setActive();

                final var activation = UUID.randomUUID().toString();
                teacherBeingEdited.setActivation(activation);
                teacherBeingEdited.setRoles(new HashSet<>(Collections.singletonList("TEACHER_ROLE")));
                
                final var savedTeacher = teacherService.addNewTeacher(teacherBeingEdited);
                final var message = "You are now registered as teacher: " + savedTeacher;
                // send an email to notify user ..
                emailService.sendTemplatedMessage(teacherBeingEdited.getUsername(), activation);
                // show in notifications.. 
                appLayout.getNotifications().add(new DefaultNotification("Teacher", message));

            } else {
                BinderValidationStatus<TeacherDto> validate = binder.validate();
                String errorText = validate.getFieldValidationStatuses().stream()
                        .filter(BindingValidationStatus::isError).map(BindingValidationStatus::getMessage)
                        .map(Optional::get).distinct().collect(Collectors.joining(", "));
                appLayout.getNotifications().add(new DefaultNotification("Error", errorText, Priority.ERROR));
            }
        });
        resetButton.addClickListener(onClick -> {
            // clear fields by setting null
            binder.readBean(null);
            infoLabel.setText("");
        });

        add(layoutWithBinder, actionsLayout, infoLabel);

    }

    private void initParentStyle() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setPadding(false);
    }

    public FormLayout getLayoutWithBinder() {
        return layoutWithBinder;
    }

    public Binder<TeacherDto> getBinder() {
        return binder;
    }

    public TeacherDto getTeacherBeingEdited() {
        return teacherBeingEdited;
    }
}
