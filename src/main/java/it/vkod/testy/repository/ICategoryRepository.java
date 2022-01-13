package it.vkod.testy.repository;

import it.vkod.testy.data.entity.CategoryEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ICategoryRepository {

    Long count() throws SQLException;

    Long count(String categoryId) throws SQLException;

    String save(CategoryEntity task) throws SQLException;

    String[] save(List<CategoryEntity> tasks) throws SQLException;

    String update(CategoryEntity task) throws SQLException;

    String delete(String id) throws SQLException;

    List<CategoryEntity> selectAll() throws SQLException;

    List<CategoryEntity> selectByQuestionId(String categoryId) throws SQLException;

    Optional<CategoryEntity> selectById(String id) throws SQLException;
}
