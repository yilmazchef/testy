package be.intecbrussel.student.data.entity;


import java.util.Objects;

public abstract class AUserEntity extends AEntity {

	private String username;
	private String password;
	private String activation;
	private Boolean authenticated;
	private String roles;


	public Boolean getAuthenticated() {

		return authenticated;
	}


	public void setAuthenticated( Boolean authenticated ) {

		this.authenticated = authenticated;
	}


	public abstract AUserEntity withAuthenticated( Boolean authenticated );


	public void addRole( String role ) {

        if ( !this.getRoles().contains( role ) ) {
            this.setRoles( this.getRoles().concat( "," ).concat( role ) );
        }
	}


	public void removeRole( String role ) {

        if ( this.getRoles().contains( role ) ) {
            this.setRoles( this.getRoles().replace( "," + role, "" ) );
        }
	}


	public String getRoles() {

		return roles;
	}


	public void setRoles( String roles ) {

		this.roles = roles;
	}


	public abstract AUserEntity withRoles( final String roles );


	public String getUsername() {

		return username;
	}


	public void setUsername( String username ) {

		this.username = username;
	}


	public abstract AUserEntity withUsername( String username );

	public abstract AUserEntity withPassword( String password );

	public abstract AUserEntity withActivation( String activation );


	public String getPassword() {

		return password;
	}


	public void setPassword( String password ) {

		this.password = password;
	}


	public String getActivation() {

		return activation;
	}


	public void setActivation( String activation ) {

		this.activation = activation;
	}


	@Override
	public boolean equals( Object o ) {

        if ( this == o ) {
            return true;
        }
        if ( !( o instanceof AUserEntity ) ) {
            return false;
        }
        if ( !super.equals( o ) ) {
            return false;
        }
		AUserEntity that = ( AUserEntity ) o;
		return getUsername().equals( that.getUsername() );
	}


	@Override
	public int hashCode() {

		return Objects.hash( super.hashCode(), getUsername() );
	}


	@Override
	public String toString() {

		return "username='" + username + '\'' +
				", password='" + password + '\'' +
				", activation='" + activation + '\'' +
				", authenticated=" + authenticated +
				", roles='" + roles + '\'';
	}

}
