package be.intecbrussel.student.repository;


import be.intecbrussel.student.data.entity.QuestionEntity;
import be.intecbrussel.student.data.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class QuestionRepository implements IQuestionRepository {

	private final JdbcTemplate jdbcTemplate;
	private static final Logger QUESTION_LOGGER = LoggerFactory.getLogger( QuestionRepository.class );


	@Autowired
	public QuestionRepository( JdbcTemplate jdbcTemplate ) {

		this.jdbcTemplate = jdbcTemplate;
	}


	@Override
	public Long count() {

		String sql = "SELECT COUNT(*) FROM testy_question WHERE active = TRUE";
		QUESTION_LOGGER.info( sql );
		Long count = jdbcTemplate.queryForObject( sql, Long.class );
		return count != null ? count : 0;
	}


	@Override
	public String save( QuestionEntity question ) throws SQLException {

		String sql = "INSERT INTO testy_question (id, orderNo, header, content, weight, teacherId) values (?, ?, ?, ?, ?, ?)";
		QUESTION_LOGGER.info( sql );

        if ( question.getId() == null ) {
            question.setId( UUID.randomUUID().toString() );
        }

		if ( question.getTeacherId() == null ) {

			question.setTeacherId( "BOT" );
		}

		int questionInsertCount = jdbcTemplate.update( sql,
				question.getId(), question.getOrderNo(), question.getHeader(), question.getContent(), question.getWeight(), question.getTeacherId() );

        if ( questionInsertCount <= 0 ) {
            throw new SQLException( "question could not be saved." );
        }

		return question.getId();
	}


	@Override
	public String save( QuestionEntity question, List< TaskEntity > tasks ) throws SQLException {

		String sql = "INSERT INTO testy_question (id, orderNo, header, content, weight, teacherId) values (?, ?, ?, ?, ?, ?)";
		QUESTION_LOGGER.info( sql );

        if ( question.getId() == null ) {
            question.setId( UUID.randomUUID().toString() );
        }

        if ( question.getTeacherId() == null ) {

            question.setTeacherId( "BOT" );
        }

		int questionInsertCount = jdbcTemplate.update( sql,
				question.getId(), question.getOrderNo(), question.getHeader(), question.getContent(), question.getWeight(), question.getTeacherId() );


        if ( questionInsertCount <= 0 ) {
            throw new SQLException( "question could not be added" );
        }

		for ( TaskEntity task : tasks ) {
			String sqlChoice = "INSERT INTO task (id, value, orderNo, weight, type, questionId) VALUES (?, ?, ?, ?, ?, ?)";
			QUESTION_LOGGER.info( sqlChoice );

			int insertCount = jdbcTemplate.update( sqlChoice,
					task.getId(), task.getValue(), task.getOrderNo(), task.getWeight(), task.getType(), task.getQuestionId() );
            if ( insertCount <= 0 ) {
                throw new SQLException( "choice could not be added to question with ID: " + question.getId() );
            }
		}

		return question.getId();
	}


	@Override
	public String update( QuestionEntity question ) throws SQLException {

		String sql = "UPDATE QUESTION SET (orderNo = ?), (header = ?), (content = ?), (weight = ?), (teacherId = ?), (active = ?)  WHERE (id = ?)";
		QUESTION_LOGGER.info( sql );

        if ( question.getTeacherId() == null ) {

            question.setTeacherId( "BOT" );
        }

		int updateCount = jdbcTemplate.update( sql,
				question.getOrderNo(), question.getHeader(), question.getContent(), question.getWeight(), question.getTeacherId(), question.getActive(), question.getId() );

		if ( updateCount <= 0 ) {
			throw new SQLException( "question could not be updated." );
		}

		return question.getId();
	}


	@Override
	public String delete( String id ) throws SQLException {

		String sql = "DELETE testy_question WHERE id = ?";
		QUESTION_LOGGER.info( sql );

		int deleteCount = jdbcTemplate.update( sql, id );
        if ( deleteCount <= 0 ) {
            throw new SQLException( "Question could not be deleted" );
        }

		return id;
	}


	@Override
	public List< QuestionEntity > select() throws SQLException {

		String sql = "SELECT * FROM testy_question WHERE active = TRUE";
		QUESTION_LOGGER.info( sql );
		return jdbcTemplate.query( sql, rowMapper() );
	}


	@Override
	public Optional< QuestionEntity > select( String id ) throws SQLException {

		String sql = "SELECT * FROM testy_question WHERE id = ? AND active = TRUE";
		QUESTION_LOGGER.info( sql );
		return Optional.ofNullable( jdbcTemplate.queryForObject( sql, rowMapper(), id ) );
	}


	private RowMapper< QuestionEntity > rowMapper() {

		return ( rs, rowNum ) -> new QuestionEntity()
				.withId( rs.getString( "id" ) )
				.withActive( rs.getBoolean( "active" ) )
				.withHeader( rs.getString( "header" ) )
				.withContent( rs.getString( "content" ) )
				.withOrderNo( rs.getInt( "orderNo" ) )
				.withWeight( rs.getDouble( "weight" ) )
				.withTeacherId( rs.getString( "teacherId" ) );
	}

}
