package it.vkod.testy.views.user;


import it.vkod.testy.data.entity.UserEntity;
import it.vkod.testy.repository.UserRepository;
import it.vkod.testy.views.AbstractView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;
import java.util.Locale;
import java.util.UUID;

import static it.vkod.testy.views.user.RegisterView.ROUTE;

@PageTitle( "Register" )
@Route( ROUTE )
@AnonymousAllowed
public class RegisterView extends AbstractView {

	public static final String TITLE = "Register";
	public static final String ROUTE = "register";


	public RegisterView( @Autowired UserRepository userRepository, @Autowired BCryptPasswordEncoder passwordEncoder ) {

		final var formLayout = new FormLayout();

		final var phoneField = new TextField();
		final var emailField = new EmailField();
		emailField.setReadOnly( true );

		final var usernameField = new TextField();
		usernameField.addValueChangeListener( onChange -> {
			emailField.setValue( onChange.getValue().toLowerCase( Locale.ROOT ).concat( "@intecbrussel.be" ) );
			if ( emailField.isInvalid() ) {
				usernameField.clear();
			}
		} );

		final var firstNameField = new TextField();

		final var lastNameField = new TextField();
		lastNameField.addValueChangeListener( onChange -> {
			usernameField.setValue( firstNameField.getValue().concat( "." ).concat( lastNameField.getValue() ) );
		} );

		final var passwordField = new TextField();
		passwordField.setClearButtonVisible( true );

		final var repeatField = new TextField();
		repeatField.setClearButtonVisible( true );
		lastNameField.addValueChangeListener( onChange -> {
			final var passwordsMatch = passwordField.getValue().contentEquals( onChange.getValue() );
			if ( !passwordsMatch || ( passwordField.getValue().isEmpty() && repeatField.getValue().isEmpty() )
					|| ( passwordField.getValue().length() < 8 ) ) {
				passwordField.getStyle().set( "color", "red" );
				passwordField.setInvalid( true );
				repeatField.getStyle().set( "color", "red" );
				repeatField.setInvalid( true );
			} else {
				passwordField.getStyle().set( "color", "green" );
				repeatField.getStyle().set( "color", "green" );
			}
		} );

		final var acceptCheck = new Checkbox( "I accept terms and conditions." );

		formLayout.addFormItem( firstNameField, "First name" );
		formLayout.addFormItem( lastNameField, "Last name" );
		formLayout.addFormItem( usernameField, "Username" );
		formLayout.addFormItem( emailField, "E-mail" );
		formLayout.addFormItem( phoneField, "Phone" );
		formLayout.addFormItem( passwordField, "Password" );
		formLayout.addFormItem( repeatField, "Repeat Password" );
		formLayout.add( acceptCheck );

		final var submitButton = new Button( "Submit Form", onClick -> {
			final var exists = userRepository.existsByUserName( usernameField.getValue().toLowerCase() );
			if ( Boolean.FALSE.equals( exists ) && !passwordField.isEmpty() && acceptCheck.getValue().equals( Boolean.TRUE ) ) {
				final var user = new UserEntity()
						.withFirstName( firstNameField.getValue().toLowerCase( Locale.ROOT ) )
						.withLastName( lastNameField.getValue().toLowerCase( Locale.ROOT ) )
						.withUsername( usernameField.getValue().toLowerCase( Locale.ROOT ) )
						.withEmail( emailField.getValue().toLowerCase( Locale.ROOT ) )
						.withPassword( passwordEncoder.encode( passwordField.getValue() ) )
						.withPhone( phoneField.getValue() )
						.withRoles( "USER" )
						.withActivation( UUID.randomUUID().toString() )
						.withActive( Boolean.TRUE );

				// TODO:  will be changed to false later when email-sending-feature is added.

				final var savedUser = new Object() {
					String newId = null;


					boolean isNew() {

						return newId == null;
					}
				};

				try {
					savedUser.newId = userRepository.save( user );
				} catch ( SQLException sqlException ) {
					Notification.show( "Failed! User CANNOT BE registered.", 3000,
							Notification.Position.BOTTOM_CENTER ).open();
				}

				if ( !savedUser.isNew() ) {
					Notification.show( "Success! User is registered.", 3000, Notification.Position.BOTTOM_CENTER ).open();
				}
			}
		} );

		formLayout.add( submitButton );

		add( formLayout );

	}


	@Override
	public String getViewName() {

		return ROUTE;
	}

}