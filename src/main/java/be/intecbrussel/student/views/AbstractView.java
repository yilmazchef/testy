package be.intecbrussel.student.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.VaadinSessionState;

import java.util.List;

public abstract class AbstractView extends VerticalLayout {

    private final VaadinSession currentSession = VaadinSession.getCurrent();

    protected AbstractView() {

        final var cookies = List.of(VaadinService.getCurrentRequest().getCookies());
        if (cookies != null && currentSession.getSession().isNew() && !cookies.isEmpty()) {
            add(new CookieConsentDialog());
        }
    }

    public VaadinSession getCurrentSession() {
        return currentSession;
    }

    public abstract String getViewName();
}