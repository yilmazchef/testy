package it.vkod.testy.repository;


import it.vkod.testy.data.entity.UserEntity;
import it.vkod.testy.data.index.UserFilter;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IUserRepository {

	Integer count() throws SQLException;

	Integer countByFullName( final String firstName, final String lastName ) throws SQLException;

	String save( UserEntity user ) throws SQLException;

	String update( UserEntity user ) throws SQLException;

	String patchAddRole( String userId, String newRole ) throws SQLException;

	String patchRemoveRole( String userId, String newRole ) throws SQLException;

	String delete( String id ) throws SQLException;

	List< UserEntity > selectAll() throws SQLException;

	List< UserEntity > filter( final UserFilter user ) throws SQLException;

	List< UserEntity > filter( final String keyword ) throws SQLException;

	Optional< UserEntity > selectForLogin( String username, String password ) throws SQLException;

	Optional< UserEntity > selectByUniqueFields( String key ) throws SQLException;

	Optional< UserEntity > selectByUserName( String username ) throws SQLException;

	Optional< UserEntity > selectByEmail( String username ) throws SQLException;

	Optional< UserEntity > selectByPhone( String phone ) throws SQLException;

	Optional< UserEntity > selectById( String id ) throws SQLException;

	boolean existsByUserName( String username );

	boolean existsByUserId( String userId );

}
