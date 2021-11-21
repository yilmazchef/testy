package be.intecbrussel.student.service;

import be.intecbrussel.student.data.dto.StudentDto;
import be.intecbrussel.student.data.index.StudentFilter;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IStudentService {

    String addNewStudent(final StudentDto student);

    default Set<String> addNewStudents(final Set<StudentDto> studentSet) {
        Set<String> set = new HashSet<>();
        for (StudentDto studentDto : studentSet) {
            String addedStudentId = addNewStudent(studentDto);
            set.add(addedStudentId);
        }
        return set;
    }

    String updateStudentById(final String id, final StudentDto student);

    String removeStudentById(final String studentId);

    Integer getStudentsCount();

    Integer getStudentsCountByFullName(final String firstName, final String lastName);

    Integer getStudentsCountByClassName(final String className);

    Optional<StudentDto> fetchStudentById(final String studentId);

    Optional<StudentDto> fetchStudentByUserName(final String username);

    Optional<StudentDto> fetchStudentByLoginDetails(final String username, final String password);

    List<StudentDto> fetchStudents(final Integer offset, final Integer limit);

    List<StudentDto> fetchStudents(final Integer offset, final Integer limit, final String filterText);

    List<StudentDto> fetchStudents(final Integer offset, final Integer limit, final StudentFilter filter);

}
