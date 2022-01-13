package it.vkod.testy.views;


import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.List;

@JsModule("prefers-color-scheme.js")
public abstract class AbstractView extends VerticalLayout {

	private final VaadinSession currentSession = VaadinSession.getCurrent();
	private final List< DefaultNotification > notifications = new ArrayList<>();


	protected AbstractView() {

		final var cookies = List.of( VaadinService.getCurrentRequest().getCookies() );
		if ( cookies != null && currentSession.getSession().isNew() && !cookies.isEmpty() ) {
			add( new CookieConsentDialog() );
		}

		for ( final DefaultNotification notification : this.notifications ) {
			add( notification );
		}
	}


	public VaadinSession getCurrentSession() {

		return currentSession;
	}


	public List< DefaultNotification > getNotifications() {

		return notifications;
	}


	public abstract String getViewName();

}