package be.intecbrussel.student.security;

import be.intecbrussel.student.views.anonymous.AnonymousExamAnalyticsView;
import be.intecbrussel.student.views.anonymous.AnonymousNewExamView;
import be.intecbrussel.student.views.commons.ContinueOnAnotherDeviceView;
import be.intecbrussel.student.views.commons.PluginsView;
import be.intecbrussel.student.views.user.LoginView;
import be.intecbrussel.student.views.user.RegisterView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

// Spring does not have SPA handlers for URLs .. That's why we need this class ..
@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        });
    }

    private void authenticateNavigation(BeforeEnterEvent event) {
        final var target = event.getNavigationTarget();
        if (!LoginView.class.equals(target) && !RegisterView.class.equals(target)
                && !ContinueOnAnotherDeviceView.class.equals(target) && !PluginsView.class.equals(target)
                && !SecurityUtils.isUserLoggedIn()
                && !AnonymousNewExamView.class.equals(target) && !AnonymousExamAnalyticsView.class.equals(target)) {

            event.rerouteTo(LoginView.class);
        }
    }
}