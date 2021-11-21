package be.intecbrussel.student.views.student;

import be.intecbrussel.student.data.dto.*;
import be.intecbrussel.student.security.SecurityUtils;
import be.intecbrussel.student.service.IExamService;
import be.intecbrussel.student.service.IQuestionService;
import be.intecbrussel.student.service.IStudentService;
import be.intecbrussel.student.util.QuestionBatchImporter;
import be.intecbrussel.student.views.AbstractView;
import be.intecbrussel.student.views.MainAppLayout;
import be.intecbrussel.student.views.user.LoginView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.appreciated.app.layout.addons.notification.entity.DefaultNotification;
import com.github.appreciated.app.layout.addons.notification.entity.Priority;
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

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@PageTitle(StudentQuestionImporterView.TITLE)
@Route(value = StudentQuestionImporterView.ROUTE, layout = MainAppLayout.class)
public class StudentQuestionImporterView extends AbstractView {

    public static final String TITLE = "Student Exam Loader from External Resource(s)";
    public static final String ROUTE = "student/load";

    private final VaadinSession currentSession = VaadinSession.getCurrent();

    private final IExamService examService;
    private final IQuestionService questionService;
    private final IStudentService studentService;
    private final QuestionBatchImporter batchImporter;
    private final MainAppLayout appLayout;

    public StudentQuestionImporterView(IExamService examService, IQuestionService questionService, IStudentService studentService,
                                       QuestionBatchImporter batchImporter, MainAppLayout appLayout) {

        this.examService = examService;
        this.questionService = questionService;
        this.studentService = studentService;
        this.batchImporter = batchImporter;
        this.appLayout = appLayout;

        initParentComponentStyle();

        if (!SecurityUtils.isUserLoggedIn()) {
            UI.getCurrent().navigate(LoginView.class, new RouteParameters("redirected", "student"));
        } else {
            final var authenticatedUser = SecurityUtils.getAuthenticatedUser();
            final var fetchedStudent = studentService.fetchStudentByUserName(authenticatedUser.getName());
            add(initBatchImporterLayout(fetchedStudent.orElseGet(() -> new StudentDto().withAnonymous(true))));
        }
    }

    private VerticalLayout initBatchImporterLayout(StudentDto student) {

        final var importLayout = new VerticalLayout();
        final var buffer = new MemoryBuffer();
        final var upload = new Upload(buffer);
        final var uploadStatusDiv = new Div();

        upload.addSucceededListener(onSucceeded -> {

            final var inputStream = buffer.getInputStream();
            final var fileName = onSucceeded.getFileName();

            final var yamlFactory = new YAMLFactory();
            final var yamlMapper = new ObjectMapper(yamlFactory);
            try {

                // TODO: student can only import existing questions ..
                // a new batchImporter must be created to make sure all questions exist in the DB
                // all tasks will have no weight but when import is done, questions will retrieve
                // other required data from DB to make sure tasks have weights..

                final var beans = Arrays.asList(yamlMapper.readValue(inputStream, QuestionDto[].class));
                final var countResponse = examService.countByQuestions(
                        beans.stream().map(QuestionDto::getId).collect(Collectors.toUnmodifiableSet()),
                        currentSession.getSession().getId());

                if (countResponse.hasBody()) {
                    for (QuestionDto bean : beans) {
                        bean.setTeacher(new TeacherDto().withAnonymous(true).withId(UUID.randomUUID().toString()));
                    }

                    final var importedQuestions = batchImporter.batchImportQuestions(beans.toArray(QuestionDto[]::new));
                    final var importedTasks = importedQuestions
                            .stream()
                            .map(task -> batchImporter.batchImportTasks(task.getId(), task.getTasks().toArray(TaskDto[]::new)))
                            .collect(Collectors.toUnmodifiableList());

                    uploadStatusDiv.setText(importedQuestions.size() + " new questions with " + importedTasks.size() + " new tasks in total.");

                    final var code = fileName.replace(".yaml", "");
                    final var examsInitialized = initExams(student, code, importedQuestions);

                    final var questionImportMessage = "Question(s) from " + fileName + " is/are imported: ";
                    appLayout.getNotifications().add(new DefaultNotification("Question Batch Import", questionImportMessage));

                    uploadStatusDiv.removeAll();

                    if (examsInitialized) {
                        final var examsGeneratedMessage = "New exams based on the questions imported are generated..";
                        appLayout.getNotifications().add(new DefaultNotification("New Exam", examsGeneratedMessage));
                        UI.getCurrent().navigate(StudentNewExamView.class, new RouteParameters("code", code));
                    }
                }

            } catch (IOException ioException) {
                appLayout.getNotifications().add(new DefaultNotification("ERROR", ioException.getMessage(), Priority.ERROR));
            }

        });

        upload.addFileRejectedListener(onRejected -> {
            uploadStatusDiv.removeAll();
            new Notification(onRejected.getErrorMessage(), 2000, Notification.Position.BOTTOM_CENTER).open();
        });

        upload.getElement().addEventListener("file-remove", onRemoved -> uploadStatusDiv.removeAll());

        importLayout.add(upload, uploadStatusDiv);

        add(importLayout);

        return importLayout;
    }

    private boolean initExams(final StudentDto student, final String examCode, final List<QuestionDto> questions) {
        final var examsCount = questions
                .stream()
                .flatMap(questionDto -> questionDto.getTasks().stream())
                .map(taskDto -> newExam(examCode, taskDto, student))
                .map(examService::create)
                .filter(HttpEntity::hasBody)
                .count();

        final var message = "An exam with " + examCode + " and with " + examsCount + (examsCount == 1 ? "question" : "questions") + " is generated.";
        appLayout.getNotifications().add(new DefaultNotification("New Exam is Ready", message));

        return examsCount > 0;
    }

    private ExamDto newExam(final String examCode, final TaskDto task, StudentDto student) {
        return new ExamDto()
                .withCode(examCode)
                .withSession(currentSession.getSession().getId())
                .withStartTime(Timestamp.valueOf(LocalDateTime.now()))
                .withEndTime(Timestamp.valueOf(LocalDateTime.now()))
                .withStudent(student)
                .withTask(task)
                .withScore(task.getWeight());
    }

    private void initParentComponentStyle() {
        setWidthFull();
        setMargin(false);
        setPadding(false);
        setHorizontalComponentAlignment(Alignment.CENTER);
    }

    @Override
    public String getViewName() {
        return StudentQuestionImporterView.ROUTE;
    }
}
