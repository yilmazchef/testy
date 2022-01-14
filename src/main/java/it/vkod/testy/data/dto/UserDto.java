package it.vkod.testy.data.dto;


import it.vkod.testy.data.entity.CourseEntity;

public class UserDto extends AUserDto {

	private String firstName;
	private String lastName;
	private Boolean anonymous;
	private String email;
	private String phone;
	private CourseDto course;


	public CourseDto getCourse() {

		return course;
	}


	public void setCourse( final String course ) {

		this.course = CourseDto.valueOf( course );
	}


	public UserDto withCourse( final String course ) {

		this.setCourse( CourseDto.valueOf( course ) );
		return this;
	}


	public void setCourse( final CourseDto course ) {

		this.course = course;
	}


	public UserDto withCourse( final CourseDto course ) {

		this.setCourse( course );
		return this;
	}


	public String getEmail() {

		return email;
	}


	public void setEmail( final String email ) {

		this.email = email;
	}


	public void setPhone( final String phone ) {

		this.phone = phone;
	}


	public UserDto withEmail( final String email ) {

		this.email = email;
		return this;
	}


	public String getPhone() {

		return phone;
	}


	public UserDto withPhone( final String phone ) {

		this.phone = phone;
		return this;
	}


	public String getFirstName() {

		return firstName;
	}


	public void setFirstName( final String firstName ) {

		this.firstName = firstName;
	}


	public UserDto withFirstName( final String firstName ) {

		this.setFirstName( firstName );
		return this;
	}


	public String getLastName() {

		return lastName;
	}


	public void setLastName( final String lastName ) {

		this.lastName = lastName;
	}


	public UserDto withLastName( final String lastName ) {

		this.setLastName( lastName );
		return this;
	}


	public Boolean getAnonymous() {

		return anonymous;
	}


	public void setAnonymous( Boolean anonymous ) {

		this.anonymous = anonymous;
	}


	public UserDto withAnonymous( final Boolean anonymous ) {

		this.setAnonymous( anonymous );
		return this;
	}


	@Override
	public UserDto withId( String id ) {

		super.setId( id );
		return this;
	}


	@Override
	public UserDto withActive( Boolean active ) {

		super.setActive( active );
		return this;
	}


	@Override
	public UserDto withAuthenticated( Boolean authenticated ) {

		super.setAuthenticated( authenticated );
		return this;
	}


	@Override
	public AUserDto withRoles( String... roles ) {

		super.setRoles( String.join( ",", roles ) );
		return this;
	}


	@Override
	public UserDto withUsername( String username ) {

		super.setUsername( username );
		return this;
	}


	@Override
	public UserDto withPassword( String password ) {

		super.setPassword( password );
		return this;
	}


	@Override
	public UserDto withActivation( String activation ) {

		super.setActivation( activation );
		return this;
	}

}
