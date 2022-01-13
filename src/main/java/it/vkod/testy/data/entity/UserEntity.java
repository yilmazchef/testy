package it.vkod.testy.data.entity;


public class UserEntity extends AUserEntity {

	private String firstName;
	private String lastName;
	private Boolean anonymous;
	private String email;
	private String phone;


	public String getEmail() {

		return email;
	}


	public void setEmail( final String email ) {

		this.email = email;
	}


	public UserEntity withEmail( final String email ) {

		this.setEmail( email );
		return this;
	}


	public String getPhone() {

		return phone;
	}


	public void setPhone( final String phone ) {

		this.phone = phone;
	}


	public UserEntity withPhone( final String phone ) {

		this.setPhone( phone );
		return this;
	}


	public String getFirstName() {

		return firstName;
	}


	public void setFirstName( final String firstName ) {

		this.firstName = firstName;
	}


	public UserEntity withFirstName( final String firstName ) {

		this.setFirstName( firstName );
		return this;
	}


	public String getLastName() {

		return lastName;
	}


	public void setLastName( final String lastName ) {

		this.lastName = lastName;
	}


	public UserEntity withLastName( final String lastName ) {

		this.lastName = lastName;
		return this;
	}


	public Boolean getAnonymous() {

		return anonymous;
	}


	public void setAnonymous( final Boolean anonymous ) {

		this.anonymous = anonymous;
	}


	public UserEntity withAnonymous( final Boolean anonymous ) {

		this.setAnonymous( anonymous );
		return this;
	}


	@Override
	public UserEntity withId( String id ) {

		super.setId( id );
		return this;
	}


	@Override
	public UserEntity withActive( Boolean active ) {

		super.setActive( active );
		return this;
	}


	@Override
	public UserEntity withAuthenticated( Boolean authenticated ) {

		super.setAuthenticated( authenticated );
		return this;
	}


	@Override
	public UserEntity withRoles( final String roles ) {

		this.setRoles( roles );
		return this;
	}


	@Override
	public UserEntity withUsername( String username ) {

		super.setUsername( username );
		return this;
	}


	@Override
	public UserEntity withPassword( String password ) {

		super.setPassword( password );
		return this;
	}


	@Override
	public UserEntity withActivation( String activation ) {

		super.setActivation( activation );
		return this;
	}


	@Override
	public String toString() {

		return super.getUsername();
	}

}
