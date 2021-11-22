package be.intecbrussel.student.views;


import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import static com.vaadin.flow.component.notification.Notification.Position.BOTTOM_CENTER;

@AnonymousAllowed
public class DefaultNotification extends Span {

	public DefaultNotification( final String text ) {

		this( text, Priority.MEDIUM );

	}


	public DefaultNotification( final String text, Priority priority ) {

		super( text );

		final var notification = new Notification( text, 3000, BOTTOM_CENTER );

		var color = "#161616";

		switch ( priority ) {
			case LOW:
				color = "orange";
				break;
			case HIGH:
				color = "blue";
				break;
			case ERROR:
				color = "red";
				break;
			default:
				color = "black";
		}

		getStyle().set( "color", color );
		notification.getElement().getStyle().set( "color", color );

		notification.open();
	}


	public DefaultNotification( final String header, final String body ) {

		this( header + "\n" + body );
	}


	public DefaultNotification( final String header, final String body, final Priority priority ) {

		this( header + "\n" + body, priority );
	}

}
