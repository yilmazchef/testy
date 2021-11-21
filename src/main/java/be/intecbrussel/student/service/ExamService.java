package be.intecbrussel.student.service;

import be.intecbrussel.student.data.dto.ExamDto;
import be.intecbrussel.student.data.entity.ExamEntity;
import be.intecbrussel.student.data.http.HttpExceptionMessage;
import be.intecbrussel.student.data.http.HttpSuccessMessage;
import be.intecbrussel.student.mapper.IExamObjectMapper;
import be.intecbrussel.student.mapper.IQuestionObjectMapper;
import be.intecbrussel.student.mapper.ITaskObjectMapper;
import be.intecbrussel.student.repository.IExamRepository;
import be.intecbrussel.student.repository.IQuestionRepository;
import be.intecbrussel.student.repository.ITaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExamService implements IExamService {

    private static final Logger EXAM_SERVICE_LOGGER = LoggerFactory.getLogger(ExamService.class);

    private static final String SUCCESS = "SUCCESS";
    private static final String EXCEPTION = "ERROR";

    private final IExamRepository examRepository;
    private final IExamObjectMapper examMapper;

    private final ITaskRepository taskRepository;
    private final ITaskObjectMapper taskMapper;

    private final IQuestionRepository questionRepository;
    private final IQuestionObjectMapper questionMapper;

    public ExamService(IExamRepository examRepository, IExamObjectMapper examMapper,
                       ITaskRepository taskRepository, ITaskObjectMapper taskMapper,
                       IQuestionRepository questionRepository, IQuestionObjectMapper questionMapper) {

        this.examRepository = examRepository;
        this.examMapper = examMapper;
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    @Override
    public HttpEntity<Long> countByQuestions(Set<String> questionIdSet, String sessionId) {
        try {

            final var count = examRepository.countByQuestions(questionIdSet, sessionId);
            MultiValueMap<String, String> successHeaders = new LinkedMultiValueMap<>();
            successHeaders.addIfAbsent(SUCCESS, HttpSuccessMessage.EXAM_IS_FOUND.name());

            if (count <= 0) {
                MultiValueMap<String, String> exceptionHeaders = new LinkedMultiValueMap<>();
                exceptionHeaders.addIfAbsent(EXCEPTION, HttpExceptionMessage.QUESTIONS_IN_THE_EXAM_DO_NOT_EXIST.name());
                return new HttpEntity<>(exceptionHeaders);
            }

            return new HttpEntity<>(count, successHeaders);

        } catch (SQLException sqlException) {

            EXAM_SERVICE_LOGGER.error(sqlException.getLocalizedMessage());
            MultiValueMap<String, String> exceptionHeaders = new LinkedMultiValueMap<>();
            exceptionHeaders.addIfAbsent(EXCEPTION, HttpExceptionMessage.EXAM_IS_NOT_FOUND.name());
            return new HttpEntity<>(exceptionHeaders);
        }
    }

    @Override
    public HttpEntity<String> create(ExamDto exam) {

        final var examEntity = examMapper.toEntity(exam);

        final var eId = new Object() {
            String savedExamId = null;
        };

        try {

            eId.savedExamId = examRepository.save(examEntity);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.addIfAbsent(SUCCESS, HttpSuccessMessage.EXAM_IS_CREATED.name());
            return new HttpEntity<>(eId.savedExamId, headers);

        } catch (SQLException sqlException) {

            EXAM_SERVICE_LOGGER.error(sqlException.getLocalizedMessage());
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.addIfAbsent(EXCEPTION, HttpExceptionMessage.EXAM_COULD_NOT_BE_CREATED.name());
            return new HttpEntity<>(headers);
        }
    }

    @Override
    public HttpEntity<String> update(ExamDto exam) {
        ExamEntity examEntity = examMapper.toEntity(exam);

        final var eId = new Object() {
            String updatedExamId = null;
        };

        try {
            eId.updatedExamId = examRepository.update(examEntity);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.addIfAbsent(SUCCESS, HttpSuccessMessage.EXAM_IS_UPDATED.name());
            return new HttpEntity<>(eId.updatedExamId, headers);
        } catch (SQLException sqlException) {
            EXAM_SERVICE_LOGGER.error(sqlException.getLocalizedMessage());
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.addIfAbsent(EXCEPTION, HttpExceptionMessage.EXAM_COULD_NOT_BE_UPDATED.name());
            return new HttpEntity<>(headers);
        }
    }

    @Override
    public HttpEntity<String> patch(String examId, Boolean isSelected) {
        final var eId = new Object() {
            String patchedExamId = null;
        };

        try {
            eId.patchedExamId = examRepository.patch(examId, isSelected);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.addIfAbsent(SUCCESS, HttpSuccessMessage.EXAM_IS_UPDATED.name());
            return new HttpEntity<>(eId.patchedExamId, headers);

        } catch (SQLException sqlException) {
            EXAM_SERVICE_LOGGER.error(sqlException.getLocalizedMessage());
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.addIfAbsent(EXCEPTION, HttpExceptionMessage.EXAM_COULD_NOT_BE_PATCHED.name());
            return new HttpEntity<>(headers);
        }
    }

    @Override
    public HttpEntity<String> patchTask(String taskId, String session, Boolean isSelected) {
        final var eId = new Object() {
            String patchedTaskIdByExam = null;
        };

        try {
            eId.patchedTaskIdByExam = examRepository.patchTask(taskId, session, isSelected);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.addIfAbsent(SUCCESS, HttpSuccessMessage.EXAM_IS_UPDATED.name());
            return new HttpEntity<>(eId.patchedTaskIdByExam, headers);

        } catch (SQLException sqlException) {
            EXAM_SERVICE_LOGGER.error(sqlException.getLocalizedMessage());
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.addIfAbsent(EXCEPTION, HttpExceptionMessage.EXAM_COULD_NOT_BE_PATCHED.name());
            return new HttpEntity<>(headers);
        }
    }

    @Override
    public HttpEntity<List<String>> patchSession(String examCode, String session) {
        final var eIds = new Object() {
            final List<String> patchedExamsWithSession = new ArrayList<>();
        };

        try {
            eIds.patchedExamsWithSession.addAll(examRepository.patchSession(examCode, session));
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.addIfAbsent(SUCCESS, HttpSuccessMessage.EXAM_IS_UPDATED.name());
            return new HttpEntity<>(eIds.patchedExamsWithSession, headers);

        } catch (SQLException sqlException) {
            EXAM_SERVICE_LOGGER.error(sqlException.getLocalizedMessage());
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.addIfAbsent(EXCEPTION, HttpExceptionMessage.EXAM_COULD_NOT_BE_PATCHED.name());
            return new HttpEntity<>(headers);
        }
    }

    @Override
    public HttpEntity<String> delete(String examId) {

        final var eId = new Object() {
            String deletedExamId = null;
        };

        try {

            eId.deletedExamId = examRepository.delete(examId);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.addIfAbsent(SUCCESS, HttpSuccessMessage.EXAM_IS_DELETED.name());
            return new HttpEntity<>(eId.deletedExamId, headers);
        } catch (SQLException sqlException) {

            EXAM_SERVICE_LOGGER.error(sqlException.getLocalizedMessage());
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.addIfAbsent(EXCEPTION, HttpExceptionMessage.EXAM_COULD_NOT_BE_DELETED.name());
            return new HttpEntity<>(sqlException.getLocalizedMessage(), headers);
        }
    }

    @Override
    public HttpEntity<ExamDto> select(String examId) {

        try {

            final var foundExamEntity = examRepository.select(examId);
            MultiValueMap<String, String> successHeaders = new LinkedMultiValueMap<>();
            successHeaders.addIfAbsent(SUCCESS, HttpSuccessMessage.EXAM_IS_FOUND.name());

            if (foundExamEntity.isEmpty()) {
                MultiValueMap<String, String> exceptionHeaders = new LinkedMultiValueMap<>();
                exceptionHeaders.addIfAbsent(EXCEPTION, HttpExceptionMessage.EXAM_IS_NOT_FOUND.name());
                return new HttpEntity<>(exceptionHeaders);
            }

            return new HttpEntity<>(examMapper.toDTO(foundExamEntity.get()), successHeaders);

        } catch (SQLException sqlException) {

            EXAM_SERVICE_LOGGER.error(sqlException.getLocalizedMessage());
            MultiValueMap<String, String> exceptionHeaders = new LinkedMultiValueMap<>();
            exceptionHeaders.addIfAbsent(EXCEPTION, HttpExceptionMessage.EXAM_IS_NOT_FOUND.name());
            return new HttpEntity<>(exceptionHeaders);
        }
    }

    @Override
    public HttpEntity<List<ExamDto>> selectAllBySession() {

        try {

            List<ExamDto> exams = examRepository
                    .selectAll()
                    .stream()
                    .map(examMapper::toDTO)
                    .map(examDto -> {
                        try {
                            final var oTask = taskRepository.selectById(examDto.getTask().getId());
                            oTask.ifPresent(taskEntity -> {
                                final var task = taskMapper.toDTO(taskEntity);
                                try {
                                    final var oQuestion = questionRepository.select(task.getQuestion().getId());
                                    oQuestion.ifPresent(question -> {
                                        final var questionDTO = questionMapper.toDTO(question);
                                        task.setQuestion(questionDTO);
                                    });
                                } catch (SQLException questionSqlEx) {
                                    EXAM_SERVICE_LOGGER.error(questionSqlEx.getLocalizedMessage());
                                }
                                examDto.setTask(task);
                            });
                        } catch (SQLException examSqlEx) {
                            EXAM_SERVICE_LOGGER.error(examSqlEx.getLocalizedMessage());
                        }

                        return examDto;
                    })
                    .collect(Collectors.toUnmodifiableList());
            return new HttpEntity<>(exams);

        } catch (SQLException sqlException) {

            EXAM_SERVICE_LOGGER.error(sqlException.getLocalizedMessage());
            MultiValueMap<String, String> exceptionHeaders = new LinkedMultiValueMap<>();
            exceptionHeaders.addIfAbsent(EXCEPTION, HttpExceptionMessage.NO_EXAM_FOUND.name());
            return new HttpEntity<>(exceptionHeaders);
        }
    }

    @Override
    public HttpEntity<List<ExamDto>> selectAllByCode(String code) {
        try {

            List<ExamDto> exams = examRepository
                    .selectAllByCode(code)
                    .stream()
                    .map(examMapper::toDTO)
                    .map(examDto -> {
                        try {
                            final var oTask = taskRepository.selectById(examDto.getTask().getId());
                            oTask.ifPresent(taskEntity -> {
                                final var task = taskMapper.toDTO(taskEntity);
                                try {
                                    final var oQuestion = questionRepository.select(task.getQuestion().getId());
                                    oQuestion.ifPresent(question -> {
                                        final var questionDTO = questionMapper.toDTO(question);
                                        task.setQuestion(questionDTO);
                                    });
                                } catch (SQLException questionSqlEx) {
                                    EXAM_SERVICE_LOGGER.error(questionSqlEx.getLocalizedMessage());
                                }
                                examDto.setTask(task);
                            });
                        } catch (SQLException examSqlEx) {
                            EXAM_SERVICE_LOGGER.error(examSqlEx.getLocalizedMessage());
                        }

                        return examDto;
                    })
                    .collect(Collectors.toUnmodifiableList());
            return new HttpEntity<>(exams);
        } catch (SQLException sqlException) {

            EXAM_SERVICE_LOGGER.error(sqlException.getLocalizedMessage());
            MultiValueMap<String, String> exceptionHeaders = new LinkedMultiValueMap<>();
            exceptionHeaders.addIfAbsent(EXCEPTION, HttpExceptionMessage.NO_EXAM_FOUND.name());
            return new HttpEntity<>(exceptionHeaders);
        }
    }

    @Override
    public HttpEntity<List<ExamDto>> selectAllByCodeAndSession(String code, String session) {
        try {

            List<ExamDto> exams = examRepository
                    .selectAllByCodeAndSession(code, session)
                    .stream()
                    .map(examMapper::toDTO)
                    .map(examDto -> {
                        try {
                            final var oTask = taskRepository.selectById(examDto.getTask().getId());
                            oTask.ifPresent(taskEntity -> {
                                final var task = taskMapper.toDTO(taskEntity);
                                try {
                                    final var oQuestion = questionRepository.select(task.getQuestion().getId());
                                    oQuestion.ifPresent(question -> {
                                        final var questionDTO = questionMapper.toDTO(question);
                                        task.setQuestion(questionDTO);
                                    });
                                } catch (SQLException questionSqlEx) {
                                    EXAM_SERVICE_LOGGER.error(questionSqlEx.getLocalizedMessage());
                                }
                                examDto.setTask(task);
                            });
                        } catch (SQLException examSqlEx) {
                            EXAM_SERVICE_LOGGER.error(examSqlEx.getLocalizedMessage());
                        }

                        return examDto;
                    })
                    .collect(Collectors.toUnmodifiableList());
            return new HttpEntity<>(exams);
        } catch (SQLException sqlException) {

            EXAM_SERVICE_LOGGER.error(sqlException.getLocalizedMessage());
            MultiValueMap<String, String> exceptionHeaders = new LinkedMultiValueMap<>();
            exceptionHeaders.addIfAbsent(EXCEPTION, HttpExceptionMessage.NO_EXAM_FOUND.name());
            return new HttpEntity<>(exceptionHeaders);
        }
    }

    @Override
    public HttpEntity<List<ExamDto>> selectAllBySession(String session) {

        try {

            List<ExamDto> exams = examRepository
                    .selectAllBySession(session)
                    .stream()
                    .map(examMapper::toDTO)
                    .map(examDto -> {
                        try {
                            final var oTask = taskRepository.selectById(examDto.getTask().getId());
                            oTask.ifPresent(taskEntity -> {
                                final var task = taskMapper.toDTO(taskEntity);
                                try {
                                    final var oQuestion = questionRepository.select(task.getQuestion().getId());
                                    oQuestion.ifPresent(question -> {
                                        final var questionDTO = questionMapper.toDTO(question);
                                        task.setQuestion(questionDTO);
                                    });
                                } catch (SQLException questionSqlEx) {
                                    EXAM_SERVICE_LOGGER.error(questionSqlEx.getLocalizedMessage());
                                }
                                examDto.setTask(task);
                            });
                        } catch (SQLException examSqlEx) {
                            EXAM_SERVICE_LOGGER.error(examSqlEx.getLocalizedMessage());
                        }

                        return examDto;
                    })
                    .collect(Collectors.toUnmodifiableList());
            return new HttpEntity<>(exams);
        } catch (SQLException sqlException) {

            EXAM_SERVICE_LOGGER.error(sqlException.getLocalizedMessage());
            MultiValueMap<String, String> exceptionHeaders = new LinkedMultiValueMap<>();
            exceptionHeaders.addIfAbsent(EXCEPTION, HttpExceptionMessage.NO_EXAM_FOUND.name());
            return new HttpEntity<>(exceptionHeaders);
        }
    }

    @Override
    public HttpEntity<Map<String, List<ExamDto>>> selectAllExamsGroupedByCode() {
        try {

            final var groupedExams = examRepository
                    .selectAll()
                    .stream()
                    .map(examMapper::toDTO)
                    .map(examDto -> {
                        try {
                            final var oTask = taskRepository.selectById(examDto.getTask().getId());
                            oTask.ifPresent(taskEntity -> {
                                final var task = taskMapper.toDTO(taskEntity);
                                try {
                                    final var oQuestion = questionRepository.select(task.getQuestion().getId());
                                    oQuestion.ifPresent(question -> {
                                        final var questionDTO = questionMapper.toDTO(question);
                                        task.setQuestion(questionDTO);
                                    });
                                } catch (SQLException questionSqlEx) {
                                    EXAM_SERVICE_LOGGER.error(questionSqlEx.getLocalizedMessage());
                                }
                                examDto.setTask(task);
                            });
                        } catch (SQLException examSqlEx) {
                            EXAM_SERVICE_LOGGER.error(examSqlEx.getLocalizedMessage());
                        }

                        return examDto;
                    })
                    .collect(Collectors.groupingBy(ExamDto::getCode));
            return new HttpEntity<>(groupedExams);

        } catch (SQLException sqlException) {

            EXAM_SERVICE_LOGGER.error(sqlException.getLocalizedMessage());
            MultiValueMap<String, String> exceptionHeaders = new LinkedMultiValueMap<>();
            exceptionHeaders.addIfAbsent(EXCEPTION, HttpExceptionMessage.NO_EXAM_FOUND.name());
            return new HttpEntity<>(exceptionHeaders);
        }
    }


}
