package be.intecbrussel.student.repository;


import be.intecbrussel.student.data.entity.ExamEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ExamRepository implements IExamRepository {

	private final JdbcTemplate jdbcTemplate;

	private static final Logger EXAM_LOGGER = LoggerFactory.getLogger( ExamRepository.class );


	@Autowired
	public ExamRepository( JdbcTemplate jdbcTemplate ) {

		this.jdbcTemplate = jdbcTemplate;
	}


	@Override
	public Long count() throws SQLException {

		String sql = "SELECT COUNT(*) FROM testy_exam WHERE active = TRUE";
		EXAM_LOGGER.info( sql );
		Long count = jdbcTemplate.queryForObject( sql, Long.class );
		return count != null ? count : 0;
	}


	@Override
	public Boolean exists( ExamEntity exam ) {

		String sql = "SELECT COUNT(*) FROM testy_exam" +
				" WHERE (session = ?) AND (organizerId = ?) AND (studentId = ?)" +
				" AND (taskId = ?) AND (active = TRUE) ";
		EXAM_LOGGER.info( sql );
		Long count = jdbcTemplate.queryForObject( sql, Long.class,
				exam.getSession(), exam.getOrganizerId(), exam.getStudentId(), exam.getTaskId() );
		return count > 0;
	}


	@Override
	public Boolean exists( String examId ) {

		String sql = "SELECT COUNT(*) FROM testy_exam WHERE (id = ?) AND (active = TRUE) ";
		EXAM_LOGGER.info( sql );
		Long count = jdbcTemplate.queryForObject( sql, Long.class, examId );
		return count > 0;
	}


	@Override
	public Boolean existsByTask( String taskId, String session ) {

		String sql = "SELECT COUNT(*) FROM testy_exam WHERE taskId = ? AND session = ? AND active = TRUE";
		EXAM_LOGGER.info( sql );
		Long count = jdbcTemplate.queryForObject( sql, Long.class, taskId, session );
		return count > 0;
	}


	@Override
	public Boolean existsByCode( String code ) {

		String sql = "SELECT COUNT(*) FROM testy_exam WHERE (code = ?) AND (active = TRUE) ";
		EXAM_LOGGER.info( sql );
		Long count = jdbcTemplate.queryForObject( sql, Long.class, code );
		return count > 0;
	}


	@Override
	public Long countByQuestions( final Set< String > questionIdSet, final String session ) throws SQLException {

		String sql = "SELECT COUNT(*) FROM testy_exam" +
				" INNER JOIN testy_task" +
				" ON testy_exam.taskId = testy_task.id" +
				" WHERE testy_task.questionId IN (?) AND testy_exam.session = ?";
		EXAM_LOGGER.info( sql );
		Long count = jdbcTemplate.queryForObject( sql, Long.class, questionIdSet, session );
		return count != null ? count : 0;
	}


	@Override
	public Boolean exists( String session, String studentId, String questionId ) {

		String sql = "SELECT COUNT(*) FROM testy_exam" +
				" WHERE active = TRUE AND (testy_exam.session = ?)" +
				" AND (testy_exam.organizerId = ?) AND (testy_exam.studentId = ?)" +
				" AND (testy_exam.taskId IN (SELECT testy_task.id FROM testy_task WHERE testy_task.questionId = ?) ) ";
		EXAM_LOGGER.info( sql );
		Long count = jdbcTemplate.queryForObject( sql, Long.class, session, studentId, questionId );
		return count > 0;
	}


	@Override
	public String save( ExamEntity exam ) throws SQLException {

		String sql = "INSERT INTO testy_exam " +
				"(id, code, session, organizerId, studentId, taskId, startTime, endTime, score, selected)" +
				" " + "VALUES" + " " +
				"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		EXAM_LOGGER.info( sql );

		if ( exists( exam ).equals( Boolean.TRUE ) ) {
			throw new SQLException( "Exam already exists." );
		}

		if ( exam.getId() == null ) {
			exam.setId( UUID.randomUUID().toString() );
		}
		int insertCount = jdbcTemplate.update( sql,
				exam.getId(), exam.getCode(), exam.getSession(), exam.getOrganizerId(), exam.getStudentId(),
				exam.getTaskId(), exam.getStartTime(), exam.getEndTime(), exam.getScore(), exam.getSelected()
		);

		if ( insertCount <= 0 ) {
			throw new SQLException( "Exam could not be saved." );
		}

		return exam.getId();
	}


	@Override
	public String update( ExamEntity exam ) throws SQLException {

		String sql = "UPDATE testy_exam" +
				" SET " +
				"(session = ?), (code = ?), (organizerId = ?), (studentId = ?), (taskId = ?)," +
				" (startTime = ?), (endTime = ?), (score = ?), (active = ?), (selected = ?)" +
				" WHERE " +
				"(id = ?)";
		EXAM_LOGGER.info( sql );
		int updateCount = jdbcTemplate.update( sql,
				exam.getSession(), exam.getCode(), exam.getOrganizerId(), exam.getStudentId(),
				exam.getTaskId(), exam.getStartTime(), exam.getEndTime(), exam.getScore(),
				exam.getActive(), exam.getSelected(), exam.getId()
		);

		if ( updateCount <= 0 ) {
			throw new SQLException( "exam could not be updated" );
		}

		return exam.getId();
	}


	@Override
	public String patch( String examId, Boolean isSelected ) throws SQLException {

		if ( exists( examId ).equals( Boolean.FALSE ) ) {
			throw new SQLException( "Exam does NOT exists." );
		}

		String sql = "UPDATE testy_exam" +
				" SET " +
				"(selected = ?)" +
				" WHERE " +
				"(id = ?)";
		EXAM_LOGGER.info( sql );
		int updateCount = jdbcTemplate.update( sql,
				isSelected, examId
		);

		if ( updateCount <= 0 ) {
			throw new SQLException( "exam could not be patched" );
		}

		return examId;
	}


	@Override
	public String patchTask( String taskId, String session, Boolean isSelected ) throws SQLException {

		if ( existsByTask( taskId, session ).equals( Boolean.FALSE ) ) {
			throw new SQLException( "Task does NOT exists." );
		}

		String sql = "UPDATE testy_exam" +
				" SET " +
				"selected = ?" +
				" WHERE " +
				"taskId = ? AND session = ?";
		EXAM_LOGGER.info( sql );
		int updateCount = jdbcTemplate.update( sql,
				isSelected, taskId, session
		);

		if ( updateCount <= 0 ) {
			throw new SQLException( "exam could not be patched" );
		}

		return taskId;
	}


	@Override
	public List< String > patchSession( String examCode, String session ) throws SQLException {

		if ( existsByCode( examCode ).equals( Boolean.FALSE ) ) {
			throw new SQLException( "Exams NOT found." );
		}

		String sql = "UPDATE testy_exam" +
				" SET " +
				"session = ?" +
				" WHERE " +
				"code = ? AND active = TRUE";
		EXAM_LOGGER.info( sql );
		int updateCount = jdbcTemplate.update( sql,
				session, examCode
		);

		if ( updateCount <= 0 ) {
			throw new SQLException( "exams could not be patched" );
		}

		return selectAllBySession( session ).stream().map( ExamEntity::getId ).collect( Collectors.toUnmodifiableList() );
	}


	@Override
	public Set< String > patchTasks( Set< String > taskIdSet, String session, Boolean isSelected ) throws SQLException {

		final var nonExistingTasksCount = taskIdSet
				.stream()
				.filter( taskId -> existsByTask( taskId, session ).equals( Boolean.FALSE ) )
				.count();

		if ( nonExistingTasksCount > 0 ) {
			throw new SQLException( "Some of tasks do not exist" );
		}

		Set< String > set = new HashSet<>();
		for ( String taskId : taskIdSet ) {
			String s = patchTask( taskId, session, isSelected );
			if ( s != null ) {
				set.add( s );
			}
		}
		return Collections.unmodifiableSet( set );
	}


	@Override
	public String delete( String id ) throws SQLException {

		String sql = "DELETE testy_exam WHERE (id = ?)";
		EXAM_LOGGER.info( sql );
		int deleteCount = jdbcTemplate.update( sql, id );
		if ( deleteCount <= 0 ) {
			throw new SQLException( "exam could not deleted" );
		}

		return id;
	}


	@Override
	public List< ExamEntity > selectAll() throws SQLException {

		String sql = "SELECT * FROM testy_exam WHERE active = TRUE";
		EXAM_LOGGER.info( sql );
		return jdbcTemplate.query( sql, rowMapper() );
	}


	@Override
	public List< ExamEntity > selectAllByCode( String examCode ) throws SQLException {

		String sql = "SELECT * FROM testy_exam WHERE code = ? AND active = TRUE";
		EXAM_LOGGER.info( sql );
		return jdbcTemplate.query( sql, rowMapper(), examCode );
	}


	@Override
	public List< ExamEntity > selectAllBySession( String session ) throws SQLException {

		String sql = "SELECT * FROM testy_exam WHERE session = ? AND active = TRUE";
		EXAM_LOGGER.info( sql );
		return jdbcTemplate.query( sql, rowMapper(), session );
	}


	@Override
	public List< ExamEntity > selectAllByCodeAndSession( String code, String session ) throws SQLException {

		String sql = "SELECT * FROM testy_exam WHERE code = ? AND session = ? AND active = TRUE";
		EXAM_LOGGER.info( sql );
		return jdbcTemplate.query( sql, rowMapper(), code, session );
	}


	@Override
	public Optional< ExamEntity > select( String id ) throws SQLException {

		String sql = "SELECT * FROM testy_exam WHERE id = ? AND active = TRUE";
		EXAM_LOGGER.info( sql );
		return Optional.ofNullable( jdbcTemplate.queryForObject( sql, rowMapper(), id ) );
	}


	private RowMapper< ExamEntity > rowMapper() {

		return new BeanPropertyRowMapper<>( ExamEntity.class, true );
	}

}
