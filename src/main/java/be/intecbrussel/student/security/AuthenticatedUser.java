package be.intecbrussel.student.security;


import be.intecbrussel.student.data.entity.UserEntity;
import be.intecbrussel.student.repository.UserRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Optional;

@Component
public class AuthenticatedUser {

	private final UserRepository userRepository;


	public AuthenticatedUser( final UserRepository userRepository ) {

		this.userRepository = userRepository;
	}


	private Optional< Authentication > getAuthentication() {

		SecurityContext context = SecurityContextHolder.getContext();
		return Optional.ofNullable( context.getAuthentication() )
				.filter( authentication -> !( authentication instanceof AnonymousAuthenticationToken ) );
	}


	public Optional< UserEntity > get() throws SQLException {

		final var oAuthentication = getAuthentication();
		if( oAuthentication.isPresent() ){
			return userRepository.selectByUniqueFields( oAuthentication.get().getName() );
		} else {
			return Optional.empty();
		}
	}


	public void logout() {

		UI.getCurrent().getPage().setLocation( SecurityConfiguration.LOGOUT_URL );
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout( VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null );
	}

}
