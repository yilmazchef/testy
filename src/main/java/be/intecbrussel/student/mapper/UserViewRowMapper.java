package be.intecbrussel.student.mapper;


import be.intecbrussel.student.data.entity.UserEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserViewRowMapper implements RowMapper< UserEntity > {

	@Override
	public UserEntity mapRow( ResultSet rs, int i ) throws SQLException {

		final var singleResult = new UserEntity();
		singleResult.setId( rs.getString( "id" ) );
		singleResult.setFirstName( rs.getString( "firstname" ) );
		singleResult.setLastName( rs.getString( "lastname" ) );
		singleResult.setAuthenticated( rs.getBoolean( "authenticated" ) );
		singleResult.setUsername( rs.getString( "username" ) );
		singleResult.setRoles( rs.getString( "roles" ) );
		singleResult.setEmail( rs.getString( "email" ) );
		singleResult.setPhone( rs.getString( "phone" ) );


		return singleResult;
	}

}
