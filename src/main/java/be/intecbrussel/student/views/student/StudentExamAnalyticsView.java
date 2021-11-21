package be.intecbrussel.student.views.student;


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

import javax.annotation.security.PermitAll;

@PageTitle( StudentExamAnalyticsView.TITLE )
@Route( value = StudentExamAnalyticsView.ROUTE, layout = MainAppLayout.class )
@PermitAll
public class StudentExamAnalyticsView extends AbstractView {

	public static final String TITLE = "Exam Analytics";
	public static final String ROUTE = "student/analytics";

	private final VaadinSession currentSession = VaadinSession.getCurrent();
	private final String session = currentSession.getSession().getId();

	private final IExamService examService;


	public StudentExamAnalyticsView( final IExamService examService ) {

		this.examService = examService;

		initParentStyle();


		final var examAnalyzerLayout = new VerticalLayout();
		examAnalyzerLayout.setWidthFull();
		examAnalyzerLayout.setAlignItems( Alignment.CENTER );
		examAnalyzerLayout.setPadding( false );

		final var examCodeLabel = new Label( "Exam Code" );
		final var examCodeField = new TextField();
		final var startExamButton = new Button( "Analyze Exam", onClick -> {
			final var code = examCodeField.getValue();
			final var examsResponse = examService.selectAllByCodeAndSession( code, session );
			if ( examsResponse.hasBody() && examsResponse.getBody() != null ) {
				final var exams = examsResponse.getBody();
				add( new StudentExamResultsGrid( exams ) );
				add( new StudentExamCharts( exams ) );
				add( new StudentExamRecapStepper( exams ) );
			}
		} );
		startExamButton.addThemeVariants( ButtonVariant.LUMO_PRIMARY );

		examAnalyzerLayout.add( examCodeLabel, examCodeField, startExamButton );
		add( examAnalyzerLayout );
	}


	private void initParentStyle() {

		setWidthFull();
		setMargin( false );
		setPadding( false );
		setSpacing( false );
		setAlignItems( Alignment.CENTER );
	}


	@Override
	public String getViewName() {

		return StudentExamAnalyticsView.ROUTE;
	}

}
