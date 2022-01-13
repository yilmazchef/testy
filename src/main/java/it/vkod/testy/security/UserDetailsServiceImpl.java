package it.vkod.testy.security;


import it.vkod.testy.data.entity.UserEntity;
import it.vkod.testy.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;


	public UserDetailsServiceImpl( final UserRepository userRepository ) {

		this.userRepository = userRepository;
	}


	@Override
	public UserDetails loadUserByUsername( String key ) throws UsernameNotFoundException {

		final Optional< UserEntity > oUser;
		try {
			oUser = userRepository.selectByUniqueFields( key );

			if ( oUser.isEmpty() ) {
				throw new UsernameNotFoundException( "No user present with key: " + key );
			} else {
				return new org.springframework.security.core.userdetails.User( oUser.get().getUsername(),
						oUser.get().getPassword(), getAuthorities( oUser.get() ) );
			}

		} catch ( SQLException sqlException ) {
			return null;
		}

	}


	private static List< GrantedAuthority > getAuthorities( UserEntity user ) {

		return Arrays
				.stream( user.getRoles().split( "," ) )
				.map( role -> new SimpleGrantedAuthority( "ROLE_" + role ) )
				.collect( Collectors.toUnmodifiableList() );

	}

}
