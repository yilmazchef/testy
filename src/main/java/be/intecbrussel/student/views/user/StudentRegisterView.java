package be.intecbrussel.student.views.user;

import be.intecbrussel.student.data.dto.StudentDto;
import be.intecbrussel.student.security.SecurityUtils;
import be.intecbrussel.student.service.EmailService;
import be.intecbrussel.student.service.IStudentService;
import be.intecbrussel.student.views.MainAppLayout;
import com.github.appreciated.app.layout.addons.notification.entity.DefaultNotification;
import com.github.appreciated.app.layout.addons.notification.entity.Priority;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.router.RouteParameters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class StudentRegisterView extends VerticalLayout {

    private final FormLayout layoutWithBinder;
    private final Binder<StudentDto> binder;
    private final StudentDto studentBeingEdited;
    private final MainAppLayout appLayout;

    public StudentRegisterView(IStudentService studentService, MainAppLayout appLayout, EmailService emailService) {
        this.layoutWithBinder = new FormLayout();
        this.binder = new Binder<>();
        this.appLayout = appLayout;

        initParentStyle();

        // The object that will be edited
        if (!SecurityUtils.isUserLoggedIn()) {
            this.studentBeingEdited = new StudentDto();
        } else {
            final var authenticatedUser = SecurityUtils.getAuthenticatedUser();
            final var fetchedStudent = studentService.fetchStudentByUserName(authenticatedUser.getName());
            this.studentBeingEdited = fetchedStudent.orElseGet(() -> new StudentDto().withAnonymous(true));
        }

        // Create the fields
        final var firstName = new TextField();
        firstName.setValueChangeMode(ValueChangeMode.EAGER);
        final var lastName = new TextField();
        lastName.setValueChangeMode(ValueChangeMode.EAGER);
        final var className = new TextField();
        className.setValueChangeMode(ValueChangeMode.EAGER);
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
        layoutWithBinder.addFormItem(className, "Class Code");
        layoutWithBinder.addFormItem(username, "Email (username)");
        layoutWithBinder.addFormItem(password, "Password");

        // Button bar
        final var actionsLayout = new HorizontalLayout();
        actionsLayout.add(saveButton, resetButton);
        saveButton.getStyle().set("marginRight", "10px");

        // E-mail has specific validators
        binder.forField(username).withNullRepresentation("")
                .withValidator(
                        value -> !username.getValue().trim().isEmpty() && value.endsWith("@student.intecbrussel.be"),
                        "Email is not valid to register")
                .withValidator(new EmailValidator("Incorrect email address"))
                .bind(StudentDto::getUsername, StudentDto::setUsername);

        // FirstName is a required field
        firstName.setRequiredIndicatorVisible(true);
        binder.forField(firstName).withValidator(new StringLengthValidator("Please set your first name", 1, null))
                .bind(StudentDto::getFirstName, StudentDto::setFirstName);

        // LastName is a required field
        lastName.setRequiredIndicatorVisible(true);
        binder.forField(lastName).withValidator(new StringLengthValidator("Please set your last name", 1, null))
                .bind(StudentDto::getLastName, StudentDto::setLastName);

        // Password is a required field
        className.setRequiredIndicatorVisible(true);
        binder.forField(className).withValidator(new StringLengthValidator("Please set your class code", 1, null))
                .bind(StudentDto::getClassName, StudentDto::setClassName);

        // Password is a required field
        password.setRequiredIndicatorVisible(true);
        binder.forField(password).withValidator(new StringLengthValidator("Please set your password", 1, null))
                .bind(StudentDto::getPassword, StudentDto::setPassword);

        // Click listeners for the buttons
        saveButton.addClickListener(onClick -> {
            if (binder.writeBeanIfValid(studentBeingEdited)) {
                studentBeingEdited.setActive();
                final var activation = UUID.randomUUID().toString();
                studentBeingEdited.setActivation(activation);
                studentBeingEdited.setRoles(new HashSet<>(Arrays.asList("ANON_ROLE", "STUDENT_ROLE")));
                final var savedStudent = studentService.addNewStudent(studentBeingEdited);
                final var message = "You are now registered as student: " + savedStudent
                        + ". Please activate your account by confirming from the link that we emailed you..";

                // send an email to notify user ..
                emailService.sendTemplatedMessage(studentBeingEdited.getUsername(), activation);
                // show it also in notifications ..
                appLayout.getNotifications().add(new DefaultNotification("New Student!", message));

            } else {
                BinderValidationStatus<StudentDto> validate = binder.validate();
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

    public Binder<StudentDto> getBinder() {
        return binder;
    }

    public StudentDto getStudentBeingEdited() {
        return studentBeingEdited;
    }
}
