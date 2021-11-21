package be.intecbrussel.student.mapper;


import be.intecbrussel.student.data.dto.UserDto;
import be.intecbrussel.student.data.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class UserObjectMapper implements IUserObjectMapper {

	@Override
	public UserEntity toEntity( UserDto dto ) {

		final var entity = new UserEntity();

		if ( dto.getId() != null ) {
			entity.setId( dto.getId() );
		}

		if ( dto.getFirstName() != null ) {
			entity.setFirstName( dto.getFirstName() );
		}

		if ( dto.getLastName() != null ) {
			entity.setLastName( dto.getLastName() );
		}

		if ( dto.getActive() != null ) {
			entity.setActive( dto.getActive() );
		}
		if ( dto.getAuthenticated() != null ) {
			entity.setAuthenticated( dto.getAuthenticated() );
		}
		if ( dto.getRoles() != null ) {
			entity.setRoles( String.join( "", dto.getRoles() ) );
		}
		if ( dto.getUsername() != null ) {
			entity.setUsername( dto.getUsername() );
		}

		if ( dto.getEmail() != null ) {
			entity.setEmail( dto.getEmail() );
		}

		if ( dto.getPhone() != null ) {
			entity.setPhone( dto.getPhone() );
		}

		if ( dto.getPassword() != null ) {
			entity.setPassword( dto.getPassword() );
		}
		if ( dto.getActivation() != null ) {
			entity.setActivation( dto.getActivation() );
		}

		return entity;
	}


	@Override
	public UserDto toDTO( UserEntity entity ) {

		final var dto = new UserDto();

		if ( entity.getId() != null ) {
			dto.setId( entity.getId() );
		}

		if ( entity.getActive() != null ) {
			dto.setActive( entity.getActive() );
		}

		if ( entity.getAuthenticated() != null ) {
			dto.setAuthenticated( entity.getAuthenticated() );
		}

		if ( entity.getRoles() != null ) {
			dto.setRoles( new HashSet<>( Arrays.asList( entity.getRoles().split( "," ) ) ) );
		}

		if ( entity.getUsername() != null ) {
			dto.setUsername( entity.getUsername() );
		}

		if ( entity.getPassword() != null ) {
			dto.setPassword( entity.getPassword() );
		}

		if ( entity.getActivation() != null ) {
			dto.setActivation( entity.getActivation() );
		}

		if ( entity.getFirstName() != null ) {
			dto.setFirstName( entity.getFirstName() );
		}

		if ( entity.getLastName() != null ) {
			dto.setLastName( entity.getLastName() );
		}

		if ( entity.getEmail() != null ) {
			dto.setEmail( entity.getEmail() );
		}

		if ( entity.getPhone() != null ) {
			dto.setPhone( entity.getPhone() );
		}

		return dto;
	}

}
