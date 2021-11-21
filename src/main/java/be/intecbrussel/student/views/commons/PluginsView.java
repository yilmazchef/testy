package be.intecbrussel.student.views.commons;

import be.intecbrussel.student.views.AbstractView;
import be.intecbrussel.student.views.MainAppLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@PageTitle(PluginsView.TITLE)
@Route(value = PluginsView.ROUTE, layout = MainAppLayout.class)
public class PluginsView extends AbstractView {

    public static final String TITLE = "Plugins for applications";
    public static final String ROUTE = "plugins";
    private final VaadinSession currentSession = UI.getCurrent().getSession();

    private final MainAppLayout appLayout;

    public PluginsView(MainAppLayout appLayout) {
        this.appLayout = appLayout;

        final var viewId = ROUTE + "_" + currentSession.getSession().getId();
        this.setId(viewId);

        initStyle();

        add(new Text("Plugins ... "));

    }

    private void initStyle() {
        setAlignItems(Alignment.CENTER);
        setHeightFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    @Override
    public String getViewName() {
        return getClass().getName();
    }
}