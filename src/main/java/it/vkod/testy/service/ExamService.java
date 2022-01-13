package it.vkod.testy.service;


import it.vkod.testy.data.dto.ExamDto;
import it.vkod.testy.data.entity.ExamEntity;
import it.vkod.testy.mapper.IExamObjectMapper;
import it.vkod.testy.mapper.IQuestionObjectMapper;
import it.vkod.testy.mapper.ITaskObjectMapper;
import it.vkod.testy.repository.IExamRepository;
import it.vkod.testy.repository.IQuestionRepository;
import it.vkod.testy.repository.ITaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamService implements IExamService {

	private static final Logger EXAM_SERVICE_LOGGER = LoggerFactory.getLogger( ExamService.class );

	private static final String SUCCESS = "SUCCESS";
	private static final String EXCEPTION = "ERROR";

	private final IExamRepository examRepository;
	private final IExamObjectMapper examMapper;

	private final ITaskRepository taskRepository;
	private final ITaskObjectMapper taskMapper;

	private final IQuestionRepository questionRepository;
	private final IQuestionObjectMapper questionMapper;


	public ExamService( IExamRepository examRepository, IExamObjectMapper examMapper,
	                    ITaskRepository taskRepository, ITaskObjectMapper taskMapper,
	                    IQuestionRepository questionRepository, IQuestionObjectMapper questionMapper ) {

		this.examRepository = examRepository;
		this.examMapper = examMapper;
		this.taskRepository = taskRepository;
		this.taskMapper = taskMapper;
		this.questionRepository = questionRepository;
		this.questionMapper = questionMapper;
	}


	@Override
	public Long countByQuestions( final Set< String > questionIdSet, final String sessionId ) {

		try {

			return examRepository.countByQuestions( questionIdSet, sessionId );

		} catch ( SQLException sqlException ) {

			EXAM_SERVICE_LOGGER.error( sqlException.getLocalizedMessage() );
			return -1L;
		}
	}


	@Override
	public String create( final ExamDto exam ) {

		final var examEntity = examMapper.toEntity( exam );

		final var eId = new Object() {
			String savedExamId = null;
		};

		try {

			EXAM_SERVICE_LOGGER.info("Exam build process is started with: " + examEntity);

			eId.savedExamId = examRepository.save( examEntity );

			EXAM_SERVICE_LOGGER.info("Exam build process is ended with new ID: " + eId.savedExamId);

			return eId.savedExamId;

		} catch ( SQLException sqlException ) {

			EXAM_SERVICE_LOGGER.error( sqlException.getLocalizedMessage() );
			return "-1";
		}
	}


	@Override
	public String update( final ExamDto exam ) {

		ExamEntity examEntity = examMapper.toEntity( exam );

		final var eId = new Object() {
			String updatedExamId = null;
		};

		try {
			eId.updatedExamId = examRepository.update( examEntity );
			return eId.updatedExamId;

		} catch ( SQLException sqlException ) {
			EXAM_SERVICE_LOGGER.error( sqlException.getLocalizedMessage() );
			return "-1";
		}
	}


	@Override
	public String patch( final String examId, final Boolean isSelected ) {

		final var eId = new Object() {
			String patchedExamId = null;
		};

		try {
			eId.patchedExamId = examRepository.patch( examId, isSelected );
			return eId.patchedExamId;

		} catch ( SQLException sqlException ) {
			EXAM_SERVICE_LOGGER.error( sqlException.getLocalizedMessage() );
			return "-1";
		}
	}


	@Override
	public String patchTask( final String taskId, final String session, final Boolean isSelected ) {

		final var eId = new Object() {
			String patchedTaskIdByExam = null;
		};

		try {
			eId.patchedTaskIdByExam = examRepository.patchTask( taskId, session, isSelected );
			return eId.patchedTaskIdByExam;

		} catch ( SQLException sqlException ) {
			EXAM_SERVICE_LOGGER.error( sqlException.getLocalizedMessage() );
			return "-1";
		}
	}


	@Override
	public List< String > patchSession( final String examCode, final String session ) {

		final var eIds = new Object() {
			final List< String > patchedExamsWithSession = new ArrayList<>();
		};

		try {
			eIds.patchedExamsWithSession.addAll( examRepository.patchSession( examCode, session ) );
			return eIds.patchedExamsWithSession;

		} catch ( SQLException sqlException ) {
			EXAM_SERVICE_LOGGER.error( sqlException.getLocalizedMessage() );
			return Collections.singletonList( sqlException.getMessage() );
		}
	}


	@Override
	public String delete( final String examId ) {

		final var eId = new Object() {
			String deletedExamId = null;
		};

		try {

			eId.deletedExamId = examRepository.delete( examId );
			return eId.deletedExamId;

		} catch ( SQLException sqlException ) {

			EXAM_SERVICE_LOGGER.error( sqlException.getLocalizedMessage() );
			return "-1";
		}
	}


	@Override
	public ExamDto select( final String examId ) {

		try {

			final var foundExamEntity = examRepository.select( examId );

			if ( foundExamEntity.isPresent() ) {
				return examMapper.toDTO( foundExamEntity.get() );
			}

		} catch ( SQLException sqlException ) {

			EXAM_SERVICE_LOGGER.error( sqlException.getLocalizedMessage() );
		}

		return null;

	}


	@Override
	public List< ExamDto > selectAllBySession() {

		try {

			return examRepository
					.selectAll()
					.stream()
					.map( examMapper::toDTO )
					.map( examDto -> {
						try {
							final var oTask = taskRepository.selectById( examDto.getTask().getId() );
							oTask.ifPresent( taskEntity -> {
								final var task = taskMapper.toDTO( taskEntity );
								try {
									final var oQuestion = questionRepository.select( task.getQuestion().getId() );
									oQuestion.ifPresent( question -> {
										final var questionDTO = questionMapper.toDTO( question );
										task.setQuestion( questionDTO );
									} );
								} catch ( SQLException questionSqlEx ) {
									EXAM_SERVICE_LOGGER.error( questionSqlEx.getLocalizedMessage() );
								}
								examDto.setTask( task );
							} );
						} catch ( SQLException examSqlEx ) {
							EXAM_SERVICE_LOGGER.error( examSqlEx.getLocalizedMessage() );
						}

						return examDto;
					} )
					.collect( Collectors.toUnmodifiableList() );

		} catch ( SQLException sqlException ) {

			EXAM_SERVICE_LOGGER.error( sqlException.getLocalizedMessage() );
			return Collections.EMPTY_LIST;
		}
	}


	@Override
	public List< ExamDto > selectAllByCode( final String code ) {

		try {

			return examRepository
					.selectAllByCode( code )
					.stream()
					.map( examMapper::toDTO )
					.map( examDto -> {
						try {
							final var oTask = taskRepository.selectById( examDto.getTask().getId() );
							oTask.ifPresent( taskEntity -> {
								final var task = taskMapper.toDTO( taskEntity );
								try {
									final var oQuestion = questionRepository.select( task.getQuestion().getId() );
									oQuestion.ifPresent( question -> {
										final var questionDTO = questionMapper.toDTO( question );
										task.setQuestion( questionDTO );
									} );
								} catch ( SQLException questionSqlEx ) {
									EXAM_SERVICE_LOGGER.error( questionSqlEx.getLocalizedMessage() );
								}
								examDto.setTask( task );
							} );
						} catch ( SQLException examSqlEx ) {
							EXAM_SERVICE_LOGGER.error( examSqlEx.getLocalizedMessage() );
						}

						return examDto;
					} )
					.collect( Collectors.toUnmodifiableList() );

		} catch ( SQLException sqlException ) {

			EXAM_SERVICE_LOGGER.error( sqlException.getLocalizedMessage() );
			return Collections.EMPTY_LIST;
		}
	}


	@Override
	public List< ExamDto > selectAllByCodeAndSession( final String code, final String session ) {

		try {

			return examRepository
					.selectAllByCodeAndSession( code, session )
					.stream()
					.map( examMapper::toDTO )
					.map( examDto -> {
						try {
							final var oTask = taskRepository.selectById( examDto.getTask().getId() );
							oTask.ifPresent( taskEntity -> {
								final var task = taskMapper.toDTO( taskEntity );
								try {
									final var oQuestion = questionRepository.select( task.getQuestion().getId() );
									oQuestion.ifPresent( question -> {
										final var questionDTO = questionMapper.toDTO( question );
										task.setQuestion( questionDTO );
									} );
								} catch ( SQLException questionSqlEx ) {
									EXAM_SERVICE_LOGGER.error( questionSqlEx.getLocalizedMessage() );
								}
								examDto.setTask( task );
							} );
						} catch ( SQLException examSqlEx ) {
							EXAM_SERVICE_LOGGER.error( examSqlEx.getLocalizedMessage() );
						}

						return examDto;
					} )
					.collect( Collectors.toUnmodifiableList() );

		} catch ( SQLException sqlException ) {

			EXAM_SERVICE_LOGGER.error( sqlException.getLocalizedMessage() );
			return Collections.EMPTY_LIST;
		}
	}


	@Override
	public List< ExamDto > selectAllBySession( final String session ) {

		try {

			return examRepository
					.selectAllBySession( session )
					.stream()
					.map( examMapper::toDTO )
					.map( examDto -> {
						try {
							final var oTask = taskRepository.selectById( examDto.getTask().getId() );
							oTask.ifPresent( taskEntity -> {
								final var task = taskMapper.toDTO( taskEntity );
								try {
									final var oQuestion = questionRepository.select( task.getQuestion().getId() );
									oQuestion.ifPresent( question -> {
										final var questionDTO = questionMapper.toDTO( question );
										task.setQuestion( questionDTO );
									} );
								} catch ( SQLException questionSqlEx ) {
									EXAM_SERVICE_LOGGER.error( questionSqlEx.getLocalizedMessage() );
								}
								examDto.setTask( task );
							} );
						} catch ( SQLException examSqlEx ) {
							EXAM_SERVICE_LOGGER.error( examSqlEx.getLocalizedMessage() );
						}

						return examDto;
					} )
					.collect( Collectors.toUnmodifiableList() );

		} catch ( SQLException sqlException ) {

			EXAM_SERVICE_LOGGER.error( sqlException.getLocalizedMessage() );
			return Collections.EMPTY_LIST;
		}
	}


	@Override
	public Map< String, List< ExamDto > > selectAllExamsGroupedByCode() {

		try {

			return examRepository
					.selectAll()
					.stream()
					.map( examMapper::toDTO )
					.map( examDto -> {
						try {
							final var oTask = taskRepository.selectById( examDto.getTask().getId() );
							oTask.ifPresent( taskEntity -> {
								final var task = taskMapper.toDTO( taskEntity );
								try {
									final var oQuestion = questionRepository.select( task.getQuestion().getId() );
									oQuestion.ifPresent( question -> {
										final var questionDTO = questionMapper.toDTO( question );
										task.setQuestion( questionDTO );
									} );
								} catch ( SQLException questionSqlEx ) {
									EXAM_SERVICE_LOGGER.error( questionSqlEx.getLocalizedMessage() );
								}
								examDto.setTask( task );
							} );
						} catch ( SQLException examSqlEx ) {
							EXAM_SERVICE_LOGGER.error( examSqlEx.getLocalizedMessage() );
						}

						return examDto;
					} )
					.collect( Collectors.groupingBy( ExamDto::getCode ) );

		} catch ( SQLException sqlException ) {

			EXAM_SERVICE_LOGGER.error( sqlException.getLocalizedMessage() );
			return Collections.EMPTY_MAP;
		}
	}


}
