package be.intecbrussel.student.views.user;

import static be.intecbrussel.student.views.user.LoginView.ROUTE;
import static be.intecbrussel.student.views.user.LoginView.TITLE;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import be.intecbrussel.student.views.AbstractView;
import be.intecbrussel.student.views.MainAppLayout;

@PageTitle(TITLE)
@Route(value = ROUTE, layout = MainAppLayout.class)
public class LoginView extends AbstractView implements BeforeEnterObserver {

    public static final String ROUTE = "login";
    public static final String TITLE = "Login | Testy";

    private final MainAppLayout appLayout;

    private final LoginForm login = new LoginForm();

    public LoginView(MainAppLayout appLayout) {
        this.appLayout = appLayout;

        initParentStyle();

        login.setAction("login");

        add(
                new H1("Testy Login"),
                login
        );
    }

    private void initParentStyle() {
        addClassName("login-view");
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
    }

    @Override
    public String getViewName() {
        return TITLE;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if (beforeEnterEvent.getLocation().getQueryParameters() != null && beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            login.setError(true);
        }
    }

}

