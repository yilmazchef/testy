package be.intecbrussel.student.views.teacher;

import be.intecbrussel.student.data.dto.TeacherDto;
import be.intecbrussel.student.security.SecurityUtils;
import be.intecbrussel.student.service.IQuestionService;
import be.intecbrussel.student.service.ITeacherService;
import be.intecbrussel.student.util.QuestionBatchImporter;
import be.intecbrussel.student.views.AbstractView;
import be.intecbrussel.student.views.MainAppLayout;
import be.intecbrussel.student.views.user.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@PageTitle(TeacherQuestionTemplateGeneratorView.TITLE)
@Route(value = TeacherQuestionTemplateGeneratorView.ROUTE, layout = MainAppLayout.class)
public class TeacherQuestionTemplateGeneratorView extends AbstractView {

    public static final String TITLE = "Question File Generator";
    public static final String ROUTE = "teacher/generator";

    private final VaadinSession currentSession = VaadinSession.getCurrent();

    private final IQuestionService questionService;
    private final ITeacherService teacherService;
    private final QuestionBatchImporter batchImporter;
    private final MainAppLayout appLayout;

    public TeacherQuestionTemplateGeneratorView(
            IQuestionService questionService, ITeacherService teacherService,
            QuestionBatchImporter batchImporter,
            MainAppLayout appLayout) {

        this.questionService = questionService;
        this.teacherService = teacherService;
        this.batchImporter = batchImporter;
        this.appLayout = appLayout;

        initParentComponentStyle();

        if (!SecurityUtils.isUserLoggedIn()) {
            UI.getCurrent().navigate(LoginView.class);
        } else {
            final var authenticatedUser = SecurityUtils.getAuthenticatedUser();
            final var teacher = teacherService.fetchTeacherByUserName(authenticatedUser.getName());
            initExam(teacher.orElse(new TeacherDto().withUsername(authenticatedUser.getName())));
        }
    }

    private void initExam(TeacherDto teacher) {

        final var generatorLayout = new VerticalLayout();
        generatorLayout.setWidthFull();
        generatorLayout.setAlignItems(Alignment.CENTER);

        final var filenameTextField = new TextField("File Name");
        final var defaultFileName = teacher.getUsername().replace(".", "_").replace("@", "_") + "_questions" + ".yaml";

        final var noOfQuestionsField = new NumberField("Number of questions");
        noOfQuestionsField.setHasControls(true);
        noOfQuestionsField.setMin(1.00);
        noOfQuestionsField.setMax(100.00);
        noOfQuestionsField.setStep(1.00);

        filenameTextField.setValue(defaultFileName);

        final var generateAnchor = new Anchor(new StreamResource(filenameTextField.getValue(), () -> getExamFileStream(noOfQuestionsField.getValue().intValue())), "Generate & Download");
        generateAnchor.getElement().setAttribute("download", true);
        filenameTextField.addValueChangeListener(onValueChange ->
                generateAnchor.setHref(new StreamResource(filenameTextField.getValue(), () -> getExamFileStream(noOfQuestionsField.getValue().intValue()))));

        generatorLayout.add(filenameTextField, noOfQuestionsField, generateAnchor);
        add(generatorLayout);
    }

    private InputStream getExamFileStream(Integer noOfQuestions) {

        final var yaml = "---\n" +
                IntStream.rangeClosed(1, noOfQuestions)
                        .mapToObj(qIndex -> "\n" +
                                "- header: QUESTION_HEADER_" + (qIndex < 10 ? "0" + qIndex : qIndex) + "\n" +
                                "  content:\n" +
                                "    - |\n" +
                                "      You can create question content:\n" +
                                "      with more than one line.\n" +
                                "      by setting '|' as the first line of the content\n" +
                                "  weight: 1.0\n" +
                                "  orderNo: 1.1\n" +
                                "  tasks:\n" +
                                "    - value: value 1 is true when weight is set to 1.00\n" +
                                "      weight: 1.00\n" +
                                "      orderNo: 1.1\n" +
                                "      type: CHOICE\n" +
                                "    - value: value 2 is false when weight is set to 0.00\n" +
                                "      weight: 0.00\n" +
                                "      orderNo: 1.2\n" +
                                "      type: CHOICE\n" +
                                "    - value: value 3 is also true when weight is set to 1.00, you can have multiple correct choices\n" +
                                "      weight: 1.00\n" +
                                "      orderNo: 1.3\n" +
                                "      type: CHOICE\n")
                        .collect(Collectors.joining("\n"));


        return new ByteArrayInputStream(yaml.getBytes(StandardCharsets.UTF_8));
    }

    private void initParentComponentStyle() {
        setWidthFull();
        setPadding(false);
        setAlignItems(Alignment.CENTER);
    }

    @Override
    public String getViewName() {
        return TeacherQuestionTemplateGeneratorView.ROUTE;
    }
}