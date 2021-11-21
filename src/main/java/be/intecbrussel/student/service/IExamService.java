package be.intecbrussel.student.service;

import be.intecbrussel.student.data.dto.ExamDto;
import org.springframework.http.HttpEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IExamService {

    HttpEntity<Long> countByQuestions(final Set<String> questionIdSet, final String sessionId);

    HttpEntity<String> create(final ExamDto exam);

    HttpEntity<String> update(final ExamDto exam);

    HttpEntity<String> patch(final String examId, final Boolean isSelected);

    HttpEntity<String> patchTask(final String taskId, final String session, final Boolean isSelected);

    HttpEntity<List<String>> patchSession(final String examCode, final String session);

    HttpEntity<String> delete(final String examId);

    HttpEntity<ExamDto> select(final String examId);

    HttpEntity<List<ExamDto>> selectAllBySession();

    HttpEntity<List<ExamDto>> selectAllByCode(final String code);

    HttpEntity<List<ExamDto>> selectAllByCodeAndSession(final String code, final String session);

    HttpEntity<List<ExamDto>> selectAllBySession(final String session);

    HttpEntity<Map<String, List<ExamDto>>> selectAllExamsGroupedByCode();
}
