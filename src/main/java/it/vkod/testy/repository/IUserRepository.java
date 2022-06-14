package it.vkod.testy.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import it.vkod.testy.data.entity.UserEntity;
import it.vkod.testy.data.index.UserFilter;

public interface IUserRepository {

	Integer count() throws SQLException;

	Integer countByFullName(final String firstName, final String lastName) throws SQLException;

	String save(final UserEntity user) throws SQLException;

	String update(final UserEntity user) throws SQLException;

	String patchAddRole(final String userId, String newRole) throws SQLException;

	String patchRemoveRole(final String userId, String newRole) throws SQLException;

	String delete(final String id) throws SQLException;

	List<UserEntity> selectAll() throws SQLException;

	List<UserEntity> filter(final UserFilter user) throws SQLException;

	List<UserEntity> filter(final String keyword) throws SQLException;

	Optional<UserEntity> selectForLogin(final String username, final String password) throws SQLException;

	Optional<UserEntity> selectByUniqueFields(final String key) throws SQLException;

	Optional<UserEntity> selectByUserName(final String username) throws SQLException;

	Optional<UserEntity> selectByEmail(final String username) throws SQLException;

	List<UserEntity> selectByCourse(final String course) throws SQLException;

	Optional<UserEntity> selectByPhone(final String phone) throws SQLException;

	Optional<UserEntity> selectById(final String id) throws SQLException;

	boolean existsByUserName(final String username);

	boolean existsByUserId(final String userId);

}
