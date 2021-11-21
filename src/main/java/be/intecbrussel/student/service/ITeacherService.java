package be.intecbrussel.student.service;

import be.intecbrussel.student.data.dto.StudentDto;
import be.intecbrussel.student.data.dto.TeacherDto;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ITeacherService {

    String addNewTeacher(final TeacherDto teacher);

    default Set<String> addNewTeachers(final Set<TeacherDto> teacherSet) {
        Set<String> set = new HashSet<>();
        for (TeacherDto teacherDto : teacherSet) {
            String addedTeacherId = addNewTeacher(teacherDto);
            set.add(addedTeacherId);
        }
        return set;
    }

    String updateTeacherById(final String id, final TeacherDto teacher);

    String removeTeacherById(final String teacherId);

    Integer getTeachersCount();

    Optional<TeacherDto> fetchTeacherById(final String teacherId);

    Optional<TeacherDto> fetchTeacherByUserName(final String username);

    Optional<TeacherDto> fetchTeacherByLoginDetails(final String username, final String password);

    List<TeacherDto> fetchTeachers(Integer offset, Integer limit);
}
