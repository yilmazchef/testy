package be.intecbrussel.student.views.anonymous;

import be.intecbrussel.student.service.IExamService;
import be.intecbrussel.student.views.AbstractView;
import be.intecbrussel.student.views.MainAppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle(AnonymousExamAnalyticsView.TITLE)
@Route(value = AnonymousExamAnalyticsView.ROUTE, layout = MainAppLayout.class)
public class AnonymousExamAnalyticsView extends AbstractView {

    public static final String TITLE = "Anonymous Exam Analytics";
    public static final String ROUTE = "anonymous/analytics";

    private final VaadinSession currentSession = VaadinSession.getCurrent();
    private final String session = currentSession.getSession().getId();

    public AnonymousExamAnalyticsView(@Autowired IExamService examService) {

        initParentComponentStyle();

        final var examAnalyzerLayout = new VerticalLayout();
        examAnalyzerLayout.setWidthFull();
        examAnalyzerLayout.setAlignItems(Alignment.CENTER);
        examAnalyzerLayout.setPadding(false);

        final var examCodeLabel = new Label("Exam Code");
        final var examCodeField = new TextField();
        final var startExamButton = new Button("Analyze Exam", onClick -> {
            final var code = examCodeField.getValue();
            final var examsResponse = examService.selectAllByCodeAndSession(code, session);
            if (examsResponse.hasBody() && examsResponse.getBody() != null) {
                final var exams = examsResponse.getBody();
                add(new AnonymousExamResultsGrid(exams));
                add(new AnonymousExamCharts(exams));
                add(new AnonymousExamRecapStepper(exams));
            }
        });
        startExamButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        examAnalyzerLayout.add(examCodeLabel, examCodeField, startExamButton);
        add(examAnalyzerLayout);
    }

    private void initParentComponentStyle() {
        setWidthFull();
        setMargin(false);
        setPadding(false);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);
    }

    @Override
    public String getViewName() {
        return AnonymousExamAnalyticsView.ROUTE;
    }
}
