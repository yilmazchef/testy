package it.vkod.testy.repository;

import it.vkod.testy.data.entity.SolutionEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ISolutionRepository {

    Long count() throws SQLException;

    Boolean exists(SolutionEntity exam);

    Boolean exists(String session, String studentId, String questionId);

    String save(SolutionEntity exam) throws SQLException;

    String update(SolutionEntity exam) throws SQLException;

    String delete(String id) throws SQLException;

    List<SolutionEntity> selectAll() throws SQLException;

    List<SolutionEntity> selectAll(String examId) throws SQLException;

    Optional<SolutionEntity> select(String id) throws SQLException;
}
