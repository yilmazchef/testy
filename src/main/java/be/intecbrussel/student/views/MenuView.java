package be.intecbrussel.student.views;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import static be.intecbrussel.student.views.MenuView.ROUTE;
import static be.intecbrussel.student.views.MenuView.TITLE;

@PageTitle(TITLE)
@Route(value = ROUTE, layout = MainAppLayout.class)
public class MenuView extends AbstractView {

    public static final String TITLE = "Menu View";
    public static final String ROUTE = "menu/all";

    public MenuView() {

    }

    @Override
    public String getViewName() {
        return getClass().getName();
    }
}