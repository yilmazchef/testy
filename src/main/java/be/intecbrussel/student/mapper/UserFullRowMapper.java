package be.intecbrussel.student.mapper;


import be.intecbrussel.student.data.entity.UserEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserFullRowMapper implements RowMapper< UserEntity > {

	@Override
	public UserEntity mapRow( ResultSet rs, int i ) throws SQLException {

		final var singleResult = new UserEntity();
		singleResult.setId( rs.getString( "id" ) );
		singleResult.setFirstName( rs.getString( "firstName" ) );
		singleResult.setLastName( rs.getString( "lastName" ) );
		singleResult.setActivation( rs.getString( "activation" ) );
		singleResult.setUsername( rs.getString( "username" ) );
		singleResult.setActive( rs.getBoolean( "active" ) );
		singleResult.setAuthenticated( rs.getBoolean( "authenticated" ) );
		singleResult.setEmail( rs.getString( "email" ) );
		singleResult.setPhone( rs.getString( "phone" ) );
		singleResult.setPassword( rs.getString( "password" ) );
		singleResult.setRoles( rs.getString( "roles" ) );

		return singleResult;
	}

}
