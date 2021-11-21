package be.intecbrussel.student.util;

import be.intecbrussel.student.data.dto.QuestionDto;
import be.intecbrussel.student.data.dto.TaskDto;
import be.intecbrussel.student.mapper.IQuestionObjectMapper;
import be.intecbrussel.student.mapper.ITaskObjectMapper;
import be.intecbrussel.student.repository.ICategoryRepository;
import be.intecbrussel.student.repository.IQuestionRepository;
import be.intecbrussel.student.repository.ITaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class QuestionBatchImporter {

    private final IQuestionRepository questionRepository;
    private final ITaskRepository taskRepository;
    private final ICategoryRepository categoryRepository;

    private final IQuestionObjectMapper questionMapper;
    private final ITaskObjectMapper taskMapper;

    private static final Logger BATCH_IMPORT_LOGGER = LoggerFactory.getLogger(QuestionBatchImporter.class);

    public QuestionBatchImporter(IQuestionRepository questionRepository, ITaskRepository taskRepository, ICategoryRepository categoryRepository, IQuestionObjectMapper questionMapper, ITaskObjectMapper taskMapper) {
        this.questionRepository = questionRepository;
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
        this.questionMapper = questionMapper;
        this.taskMapper = taskMapper;
    }

    public List<TaskDto> batchImportTasks(String questionId, TaskDto... tasks) {

        return Arrays.stream(tasks)
                .map(task -> {
                    var qIdRef = new Object() {
                        String savedTaskId = null;
                    };

                    try {
                        task.setQuestionId(questionId);
                        qIdRef.savedTaskId = taskRepository.save(taskMapper.toEntity(task));
                        BATCH_IMPORT_LOGGER.info("Batch tasks are imported...");
                    } catch (SQLException sqlEx) {
                        sqlEx.printStackTrace();
                    }

                    task.setId(qIdRef.savedTaskId);

                    return task;

                })
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());

    }

    public List<QuestionDto> batchImportQuestions(QuestionDto... questions) {

        return Arrays.stream(questions)
                .map(question -> {

                    var qIdRef = new Object() {
                        String savedQuestionId = null;
                    };

                    try {
                        question.setActive();
                        qIdRef.savedQuestionId = questionRepository.save(questionMapper.toEntity(question));
                        question.setId(qIdRef.savedQuestionId);
                        BATCH_IMPORT_LOGGER.info("Batch questions are imported...");

                        return question;

                    } catch (SQLException sqlEx) {
                        BATCH_IMPORT_LOGGER.error(sqlEx.getMessage(), sqlEx);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());

    }
}