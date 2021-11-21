package be.intecbrussel.student.views;


import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@JsModule("prefers-color-scheme.js")
public class MainAppLayout extends AppLayout {

	private final List< Span > notifications = new ArrayList<>();


	public MainAppLayout() {

		Image img = new Image( "images/logo.png", "Testy" );
		img.setHeight( "44px" );
		final boolean touchOptimized = true;
		addToNavbar( touchOptimized, new DrawerToggle(), img );
		Tabs tabs = new Tabs( new Tab( "Home" ), new Tab( "About" ) );
		tabs.setOrientation( Tabs.Orientation.VERTICAL );
		addToDrawer( tabs );
	}


	public List< Span > getNotifications() {

		return notifications;
	}

}
