package be.intecbrussel.student.repository;


import be.intecbrussel.student.data.entity.UserEntity;
import be.intecbrussel.student.data.index.UserFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.*;

@Repository
public class UserRepository implements IUserRepository {

	private static final Logger log = LoggerFactory.getLogger( UserRepository.class );

	private final JdbcTemplate jdbc;


	@Autowired
	public UserRepository( JdbcTemplate jdbc ) {

		this.jdbc = jdbc;
	}


	@Override
	public Integer count() throws SQLException {

		final var sql = "SELECT COUNT(*) FROM testy_user WHERE active = true";
		log.info( sql );

		Integer count = jdbc.queryForObject( sql, Integer.class );
		return count != null ? count : 0;

	}


	@Override
	public Integer countByFullName( final String firstName, final String lastName ) throws SQLException {

		final var sql = "SELECT COUNT(*) FROM testy_user WHERE active = true AND firstName LIKE ? AND lastName LIKE ?";
		log.info( sql );

		Integer count = jdbc.queryForObject( sql, Integer.class, firstName, lastName );
		return count != null ? count : 0;

	}


	@Override
	public String save( UserEntity user ) throws SQLException {

		if ( user.getId() == null ) {
			user.setId( UUID.randomUUID().toString() );
		}

		final var sql = "INSERT INTO " +
				"testy_user " +
				"(id, firstName, lastName, authenticated, roles, username, email, phone, password, activation)" +
				" values " +
				"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		log.info( sql );

		int insertCount = jdbc.update( sql,
				user.getId(), user.getFirstName(), user.getLastName(),
				user.getAuthenticated() != null && user.getAuthenticated(), user.getRoles(),
				user.getUsername(), user.getEmail(), user.getPhone(),
				user.getPassword(), user.getActivation() );

		if ( insertCount <= 0 ) {
			throw new SQLException( "user could not be saved" );
		}

		return user.getId();
	}


	@Override
	public String update( UserEntity user ) throws SQLException {

		final var sql = "update testy_user set " +
				"firstName = ?, lastName = ?, authenticated = ?, roles = ?, " +
				"username = ?, email = ?, phone = ?, " +
				"password = ?, activation = ?, active = ? " +
				"where id = ? AND active = true";
		log.info( sql );

		int updateCount = jdbc.update( sql,
				user.getFirstName(), user.getLastName(),
				user.getAuthenticated(), user.getRoles(), user.getUsername(), user.getEmail(), user.getPhone(),
				user.getPassword(), user.getActivation(), !user.getActive(), user.getId() );

		if ( updateCount <= 0 ) {
			throw new SQLException( "user could not be updated" );
		}

		return user.getId();
	}


	@Override
	public String patchAddRole( String userId, String newRole ) throws SQLException {

		final var oUser = selectById( userId );

		if ( oUser.isEmpty() ) {
			throw new SQLException( "User NOT found!" );
		}

		final var sql = "update testy_user set roles = ? where id = ? AND active = TRUE";
		log.info( sql );

		final var roleSet = new HashSet<>( Arrays.asList( oUser.get().getRoles().split( "," ) ) );
		roleSet.add( newRole );

		final var updateCount = jdbc.update( sql, String.join( ",", roleSet ), userId );

		if ( updateCount <= 0 ) {
			throw new SQLException( "user could not be updated" );
		}

		return userId;
	}


	@Override
	public String patchRemoveRole( String userId, String newRole ) throws SQLException {

		final var oUser = selectById( userId );

		if ( oUser.isEmpty() ) {
			throw new SQLException( "User NOT found!" );
		}

		final var sql = "update testy_user set roles = ? where id = ? AND active = TRUE";
		log.info( sql );

		final var roleSet = new HashSet<>( Arrays.asList( oUser.get().getRoles().split( "," ) ) );
		roleSet.remove( newRole );

		final var updateCount = jdbc.update( sql, String.join( ",", roleSet ), userId );

		if ( updateCount <= 0 ) {
			throw new SQLException( "user could not be updated" );
		}

		return userId;
	}


	@Override
	public String delete( String id ) throws SQLException {

		final var sql = "delete from testy_user where id = ?";
		log.info( sql );
		final var deleteCount = jdbc.update( sql, id );

		if ( deleteCount <= 0 ) {
			throw new SQLException( "user could not be deleted" );
		}

		return id;
	}


	@Override
	public List< UserEntity > selectAll() throws SQLException {

		final var sql = "SELECT * FROM testy_user WHERE active = TRUE";
		log.info( sql );
		return jdbc.queryForList( sql, UserEntity.class );
	}


	@Override
	public List< UserEntity > filter( final UserFilter user ) throws SQLException {

		final var sql = "SELECT * FROM testy_user WHERE active = TRUE AND (firstName LIKE ? OR lastName LIKE ?)";
		log.info( sql );
		return jdbc.queryForList( sql, UserEntity.class, user.getFirstName(), user.getLastName() );
	}


	@Override
	public List< UserEntity > filter( final String keyword ) throws SQLException {

		final var sql = "SELECT * FROM testy_user WHERE active = TRUE" +
				" AND (firstName LIKE ? OR lastName LIKE ?" +
				" OR email LIKE ? OR phone LIKE ? )";
		log.info( sql );
		return jdbc.queryForList( sql, UserEntity.class, keyword, keyword, keyword, keyword );
	}


	@Override
	public Optional< UserEntity > selectForLogin( String username, String password ) throws SQLException {

		String sql = "SELECT * FROM testy_user" +
				" WHERE" +
				" ( (username = ? AND password = ?) OR (email = ? AND password = ?) OR (phone = ? AND password = ?) )" +
				" AND active = TRUE AND authenticated = TRUE";
		log.info( sql );
		return Optional.ofNullable( jdbc.queryForObject( sql, rowMapper(), username, password ) );
	}


	@Override
	public Optional< UserEntity > selectByUniqueFields( String key ) throws SQLException {

		if(key.contains( "@" )){
			return selectByEmail( key );
		} else if (key.matches( "(0/91)?[7-9][0-9]{9}" )){
			return selectByPhone( key );
		} else {
			return selectByUserName( key );
		}
	}


	@Override
	public Optional< UserEntity > selectByUserName( String username ) throws SQLException {

		if ( !existsByUserName( username ) ) {
			return Optional.empty();
		}

		String sql = "SELECT * FROM testy_user WHERE username = ? AND active = TRUE AND authenticated = TRUE";
		log.info( sql );
		return Optional.ofNullable( jdbc.queryForObject( sql, rowMapper(), username.toLowerCase() ) );
	}


	@Override
	public Optional< UserEntity > selectByEmail( String email ) throws SQLException {

		if ( !existsByUserName( email ) ) {
			return Optional.empty();
		}

		String sql = "SELECT * FROM testy_user WHERE email = ? AND active = TRUE AND authenticated = TRUE";
		log.info( sql );
		return Optional.ofNullable( jdbc.queryForObject( sql, rowMapper(), email ) );
	}


	@Override
	public Optional< UserEntity > selectByPhone( String phone ) throws SQLException {

		if ( !existsByUserName( phone ) ) {
			return Optional.empty();
		}

		String sql = "SELECT * FROM testy_user WHERE phone = ? AND active = TRUE AND authenticated = TRUE";
		log.info( sql );
		return Optional.ofNullable( jdbc.queryForObject( sql, rowMapper(), phone ) );
	}


	@Override
	public Optional< UserEntity > selectById( String id ) throws SQLException {

		if ( !existsByUserId( id ) ) {
			return Optional.empty();
		}

		final var sql = "SELECT * FROM testy_user WHERE id = ? AND active = TRUE";
		log.info( sql );
		return Optional.ofNullable( jdbc.queryForObject( sql, rowMapper(), id ) );
	}


	@Override
	public boolean existsByUserId( final String userId ) {

		final var sql = "SELECT COUNT(*) FROM testy_user WHERE id = ? AND active = TRUE";
		log.info( sql );
		Integer count = jdbc.queryForObject( sql, Integer.class, userId );
		return count != null && count > 0;
	}


	@Override
	public boolean existsByUserName( String username ) {

		final var sql = "SELECT COUNT(*) FROM testy_user WHERE username = ? AND active = TRUE";
		log.info( sql );
		Integer count = jdbc.queryForObject( sql, Integer.class, username );
		return count != null && count > 0;
	}


	private RowMapper< UserEntity > rowMapper() {

		return new BeanPropertyRowMapper<>( UserEntity.class );
	}

}
