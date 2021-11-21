package be.intecbrussel.student.service;

import be.intecbrussel.student.data.dto.CategoryDto;
import be.intecbrussel.student.data.dto.QuestionDto;
import be.intecbrussel.student.data.dto.TaskDto;
import be.intecbrussel.student.data.entity.CategoryEntity;
import be.intecbrussel.student.data.entity.QuestionEntity;
import be.intecbrussel.student.data.entity.TaskEntity;
import be.intecbrussel.student.mapper.ICategoryObjectMapper;
import be.intecbrussel.student.mapper.IQuestionObjectMapper;
import be.intecbrussel.student.mapper.ITaskObjectMapper;
import be.intecbrussel.student.repository.ICategoryRepository;
import be.intecbrussel.student.repository.IQuestionRepository;
import be.intecbrussel.student.repository.ITaskRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionService implements IQuestionService {

    private final IQuestionRepository questionRepository;
    private final IQuestionObjectMapper questionMapper;
    private final ITaskRepository taskRepository;
    private final ITaskObjectMapper taskMapper;
    private final ICategoryRepository categoryRepository;
    private final ICategoryObjectMapper categoryMapper;

    public QuestionService(
            IQuestionRepository questionRepository,
            IQuestionObjectMapper questionMapper,
            ITaskRepository taskRepository,
            ITaskObjectMapper taskMapper,
            ICategoryRepository categoryRepository,
            ICategoryObjectMapper categoryMapper) {

        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;

    }

    @Override
    public String createQuestion(QuestionDto question) throws SQLException {
        return questionRepository.save(questionMapper.toEntity(question));
    }

    @Override
    public Set<String> createQuestions(Set<QuestionDto> questionSet) throws SQLException {
        Set<String> set = new HashSet<>();
        for (QuestionDto question : questionSet) {
            String savedQuestionId = questionRepository.save(questionMapper.toEntity(question));
            set.add(savedQuestionId);
        }
        return Collections.unmodifiableSet(set);
    }

    @Override
    public String updateQuestion(QuestionDto question) throws SQLException {
        return questionRepository.update(questionMapper.toEntity(question));
    }

    @Override
    public String deleteQuestion(String questionId) throws SQLException {
        return questionRepository.delete(questionId);
    }

    @Override
    public Optional<QuestionDto> getQuestionById(String questionId) throws SQLException {
        Optional<QuestionEntity> foundQuestionEntity = questionRepository.select(questionId);
        return foundQuestionEntity.map(questionMapper::toDTO);
    }

    @Override
    public List<QuestionDto> getAllQuestions() throws SQLException {
        return questionRepository
                .select()
                .stream()
                .map(questionEntity -> {
                    try {
                        final var taskEntities = taskRepository.selectByQuestionId(questionEntity.getId());
                        return questionMapper.toDTO(questionEntity, new HashSet<>(taskEntities));
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * TASK RELATED METHODS
     */

    @Override
    public String addTask(TaskDto task) throws SQLException {
        return taskRepository.save(taskMapper.toEntity(task));
    }

    @Override
    public Set<String> addTasks(Set<TaskDto> taskSet) throws SQLException {
        Set<String> set = new HashSet<>();
        for (TaskDto task : taskSet) {
            String savedTaskId = taskRepository.save(taskMapper.toEntity(task));
            set.add(savedTaskId);
        }
        return Collections.unmodifiableSet(set);
    }

    @Override
    public String updateTask(TaskDto task) throws SQLException {
        return taskRepository.update(taskMapper.toEntity(task));
    }

    @Override
    public String deleteTask(String taskId) throws SQLException {
        return taskRepository.delete(taskId);
    }

    @Override
    public Optional<TaskDto> getTaskById(String taskId) throws SQLException {
        Optional<TaskEntity> foundTaskEntity = taskRepository.selectById(taskId);
        return foundTaskEntity
                .map(taskMapper::toDTO)
                .map(this::updateTaskQuestion);
    }

    @Override
    public List<TaskDto> getAllTasks(String questionId) throws SQLException {
        return taskRepository
                .selectByQuestionId(questionId)
                .stream()
                .map(taskMapper::toDTO)
                .map(this::updateTaskQuestion)
                .collect(Collectors.toUnmodifiableList());
    }

    private TaskDto updateTaskQuestion(TaskDto taskDto) {
        try {
            final var oQuestionEntity = questionRepository.select(taskDto.getQuestion().getId());
            oQuestionEntity.ifPresent(questionEntity -> taskDto.setQuestion(questionMapper.toDTO(questionEntity)));
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return taskDto;
    }

    @Override
    public List<TaskDto> getAllTasks(String questionId, TaskDto.Type type) throws SQLException {
        return taskRepository
                .selectByQuestionId(questionId, type.name())
                .stream()
                .map(taskMapper::toDTO)
                .map(this::updateTaskQuestion)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Long getTaskCountByQuestion(String questionId) throws SQLException {
        return taskRepository.count(questionId);
    }

    /**
     * CATEGORY RELATED METHODS
     */

    @Override
    public String addCategory(CategoryDto category) throws SQLException {
        return categoryRepository.save(categoryMapper.toEntity(category));
    }

    public Set<String> addCategories(Set<CategoryDto> categorySet) throws SQLException {
        Set<String> set = new HashSet<>();
        for (CategoryDto category : categorySet) {
            String savedCategoryId = categoryRepository.save(categoryMapper.toEntity(category));
            set.add(savedCategoryId);
        }
        return Collections.unmodifiableSet(set);
    }

    @Override
    public String updateCategory(CategoryDto category) throws SQLException {
        return categoryRepository.update(categoryMapper.toEntity(category));
    }

    @Override
    public String deleteCategory(String categoryId) throws SQLException {
        return categoryRepository.delete(categoryId);
    }

    @Override
    public Optional<CategoryDto> getCategoryById(String categoryId) throws SQLException {
        Optional<CategoryEntity> foundCategoryEntity = categoryRepository.selectById(categoryId);
        return foundCategoryEntity.map(categoryMapper::toDTO);
    }

    @Override
    public List<CategoryDto> getAllCategories() throws SQLException {
        return categoryRepository.selectAll().stream().map(categoryMapper::toDTO).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<CategoryDto> getAllCategories(String questionId) throws SQLException {
        return categoryRepository.selectByQuestionId(questionId).stream().map(categoryMapper::toDTO).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Long getCategoryCountByQuestion(String questionId) throws SQLException {
        return categoryRepository.count(questionId);
    }
}
