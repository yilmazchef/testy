package be.intecbrussel.student.service;

import be.intecbrussel.student.data.dto.CategoryDto;
import be.intecbrussel.student.data.dto.QuestionDto;
import be.intecbrussel.student.data.dto.TaskDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IQuestionService {

    String createQuestion(QuestionDto question) throws SQLException;

    Set<String> createQuestions(Set<QuestionDto> questionSet) throws SQLException;

    String updateQuestion(QuestionDto question) throws SQLException;

    String deleteQuestion(String questionId) throws SQLException;

    Optional<QuestionDto> getQuestionById(String questionId) throws SQLException;

    List<QuestionDto> getAllQuestions() throws SQLException;

    String addTask(TaskDto task) throws SQLException;

    /**
     * TASK RELATED METHODS
     */

    Set<String> addTasks(Set<TaskDto> taskSet) throws SQLException;

    String updateTask(TaskDto task) throws SQLException;

    String deleteTask(String taskId) throws SQLException;

    Optional<TaskDto> getTaskById(String taskId) throws SQLException;

    List<TaskDto> getAllTasks(String questionId) throws SQLException;

    List<TaskDto> getAllTasks(String questionId, TaskDto.Type type) throws SQLException;

    Long getTaskCountByQuestion(String questionId) throws SQLException;

    /**
     * CATEGORY RELATED METHODS
     */

    String addCategory(CategoryDto category) throws SQLException;

    Set<String> addCategories(Set<CategoryDto> categorySet) throws SQLException;

    String updateCategory(CategoryDto category) throws SQLException;

    String deleteCategory(String categoryId) throws SQLException;

    Optional<CategoryDto> getCategoryById(String categoryId) throws SQLException;

    List<CategoryDto> getAllCategories() throws SQLException;

    List<CategoryDto> getAllCategories(String questionId) throws SQLException;

    Long getCategoryCountByQuestion(String questionId) throws SQLException;

}
