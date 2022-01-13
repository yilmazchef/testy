package it.vkod.testy.data.mock;


import it.vkod.testy.data.entity.AUserEntity;
import com.github.javafaker.Faker;

import java.util.Locale;

public class UserMock extends AUserEntity {

	private static final Faker FAKE = new Faker( Locale.getDefault() );


	public UserMock( Integer userIndex ) {

		setUsername( FAKE.name().username() );
		setPassword( FAKE.internet().password() );
		setActivation( FAKE.space().galaxy() );
		setAuthenticated( true );
		setRoles( "ANON_ROLE" );
		setActive();
	}


	public UserMock( Integer userIndex, String username, String password ) {

		setUsername( username );
		setPassword( password );
		setActivation( FAKE.space().galaxy() );
		setRoles( "ANON_ROLE" );
		setActive();
	}


	@Override
	public UserMock withId( String id ) {

		super.setId( id );
		return this;
	}


	@Override
	public UserMock withActive( Boolean active ) {

		super.setActive( active );
		return this;
	}


	@Override
	public UserMock withAuthenticated( Boolean authenticated ) {

		super.setAuthenticated( authenticated );
		return this;
	}


	@Override
	public UserMock withRoles( final String roles ) {

		this.setRoles( roles );
		return this;
	}


	@Override
	public UserMock withUsername( String username ) {

		super.setUsername( username );
		return this;
	}


	@Override
	public UserMock withPassword( String password ) {

		super.setPassword( password );
		return this;
	}


	@Override
	public UserMock withActivation( String activation ) {

		super.setActivation( activation );
		return this;
	}

}
