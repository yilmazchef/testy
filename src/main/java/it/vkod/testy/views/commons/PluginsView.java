package it.vkod.testy.views.commons;


import it.vkod.testy.views.AbstractView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle( PluginsView.TITLE )
@Route( value = PluginsView.ROUTE )
@AnonymousAllowed
public class PluginsView extends AbstractView {

	public static final String TITLE = "Plugins for applications";
	public static final String ROUTE = "plugins";


	public PluginsView() {


		final var viewId = ROUTE + "_" + getCurrentSession().getSession().getId();
		this.setId( viewId );

		initStyle();

		add( new Text( "Plugins ... " ) );

	}


	private void initStyle() {

		setAlignItems( Alignment.CENTER );
		setHeightFull();
		setJustifyContentMode( JustifyContentMode.CENTER );
	}


	@Override
	public String getViewName() {

		return getClass().getName();
	}

}