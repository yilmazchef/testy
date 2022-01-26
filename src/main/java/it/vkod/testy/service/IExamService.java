package it.vkod.testy.service;


import it.vkod.testy.data.dto.ExamDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IExamService {

    Long countByQuestions(final Set<String> questionIdSet, final String sessionId);

    String create(final ExamDto exam);

    String update(final ExamDto exam);

    String patch(final String examId, final Boolean isSelected);

    String patchTask(final String examCode, final String taskId, final String session, final Boolean isSelected);

    List<String> patchSession(final String examCode, final String session);

    String delete(final String examId);

    ExamDto select(final String examId);

    List<ExamDto> selectAllBySession();

    List<ExamDto> selectAllByCode(final String code);

    List<ExamDto> selectAllByCodeAndSession(final String code, final String session);

    List<ExamDto> selectAllBySession(final String session);

    Map<String, List<ExamDto>> selectAllExamsGroupedByCode();
}
