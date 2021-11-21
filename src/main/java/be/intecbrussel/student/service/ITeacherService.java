package be.intecbrussel.student.service;


import be.intecbrussel.student.data.dto.UserDto;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ITeacherService {

    String addNewTeacher(final UserDto teacher);

    default Set<String> addNewTeachers(final Set<UserDto> teacherSet) {
        Set<String> set = new HashSet<>();
        for (UserDto teacherDto : teacherSet) {
            String addedTeacherId = addNewTeacher(teacherDto);
            set.add(addedTeacherId);
        }
        return set;
    }

    String updateTeacherById(final String id, final UserDto teacher);

    String removeTeacherById(final String teacherId);

    Integer getTeachersCount();

    Optional<UserDto> fetchTeacherById(final String teacherId);

    Optional<UserDto> fetchTeacherByUserName(final String username);

    Optional<UserDto> fetchTeacherByLoginDetails(final String username, final String password);

    List<UserDto> fetchTeachers(Integer offset, Integer limit);
}
