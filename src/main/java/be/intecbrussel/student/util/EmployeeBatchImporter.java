package be.intecbrussel.student.util;

import be.intecbrussel.student.data.entity.ManagerEntity;
import be.intecbrussel.student.data.entity.StudentEntity;
import be.intecbrussel.student.data.entity.TeacherEntity;
import be.intecbrussel.student.data.entity.UserEntity;
import be.intecbrussel.student.repository.IManagerRepository;
import be.intecbrussel.student.repository.IStudentRepository;
import be.intecbrussel.student.repository.ITeacherRepository;
import be.intecbrussel.student.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.text.MessageFormat.format;

@Component
public class EmployeeBatchImporter {

    private static final Logger log = LoggerFactory.getLogger(EmployeeBatchImporter.class);

    private final IManagerRepository managerRepository;
    private final ITeacherRepository teacherRepository;
    private final IStudentRepository studentRepository;

    private final IUserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final List<ManagerEntity> managers = new ArrayList<>();
    private final List<TeacherEntity> teachers = new ArrayList<>();
    private final List<StudentEntity> students = new ArrayList<>();

    public EmployeeBatchImporter(IManagerRepository managerRepository, ITeacherRepository teacherRepository, IStudentRepository studentRepository, IUserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.managerRepository = managerRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public EmployeeBatchImporter withManager(ManagerEntity manager) {
        this.managers.add(manager);
        return this;
    }

    public EmployeeBatchImporter withManagers(List<ManagerEntity> managers) {
        this.managers.addAll(managers);
        return this;
    }

    public EmployeeBatchImporter withTeacher(TeacherEntity teacher) {
        this.teachers.add(teacher);
        return this;
    }

    public EmployeeBatchImporter withTeachers(List<TeacherEntity> teachers) {
        this.teachers.addAll(teachers);
        return this;
    }

    public EmployeeBatchImporter withStudent(StudentEntity student) {
        this.students.add(student);
        return this;
    }

    public EmployeeBatchImporter withStudents(List<StudentEntity> students) {
        this.students.addAll(students);
        return this;
    }

    public void generate() {

        log.info("---------------------------------------------------- DUMMY DATA (DELETE THIS IN PRODUCTION) ----------------------------------------------------");

        try {

            final var managerList = this.managers
                    .stream()
                    .map(manager -> {

                        final var effected = new Object() {
                            final ManagerEntity entity = manager;
                        };

                        final var user = new UserEntity()
                                .withUsername(manager.getUsername())
                                .withPassword(manager.getPassword())
                                .withActivation(UUID.randomUUID().toString())
                                .withActive(true)
                                .withAuthenticated(true);

                        user.setRoles("MANAGER_ROLE, TEACHER_ROLE");

                        log.info(user.toString());
                        user.setPassword(passwordEncoder.encode(user.getPassword()));

                        try {
                            final var savedUserId = userRepository.save(user);
                            effected.entity.setId(savedUserId);
                        } catch (SQLException sqlEx) {
                            log.error(Arrays.toString(sqlEx.getStackTrace()));
                        }

                        return effected.entity;
                    }).collect(Collectors.toUnmodifiableList());

            final var teacherList = this.teachers
                    .stream()
                    .map(teacher -> {

                        final var effected = new Object() {
                            final TeacherEntity entity = teacher;
                        };

                        final var user = new UserEntity()
                                .withUsername(teacher.getUsername())
                                .withPassword(teacher.getPassword())
                                .withActivation(UUID.randomUUID().toString())
                                .withActive(true)
                                .withAuthenticated(true);

                        user.setRoles("TEACHER_ROLE");

                        log.info(user.toString());
                        user.setPassword(passwordEncoder.encode(user.getPassword()));

                        try {
                            final var savedUserId = userRepository.save(user);
                            effected.entity.setId(savedUserId);
                        } catch (SQLException sqlEx) {
                            log.error(Arrays.toString(sqlEx.getStackTrace()));
                        }

                        return effected.entity;
                    }).collect(Collectors.toUnmodifiableList());

            final var studentList = this.students
                    .stream()
                    .map(student -> {

                        final var effected = new Object() {
                            final StudentEntity entity = student;
                        };

                        final var user = new UserEntity()
                                .withUsername(student.getUsername())
                                .withPassword(student.getPassword())
                                .withActivation(UUID.randomUUID().toString())
                                .withActive(true)
                                .withAuthenticated(true);

                        user.setRoles("ANON_ROLE,STUDENT_ROLE");

                        log.info(user.toString());
                        user.setPassword(passwordEncoder.encode(user.getPassword()));

                        try {
                            final var savedUserId = userRepository.save(user);
                            effected.entity.setId(savedUserId);
                        } catch (SQLException sqlEx) {
                            log.error(Arrays.toString(sqlEx.getStackTrace()));
                        }

                        return effected.entity;
                    }).collect(Collectors.toUnmodifiableList());

            final var managerIdSet = managerRepository.save(managerList);
            final var teacherIdSet = teacherRepository.saveAll(teacherList);
            final var studentIdSet = studentRepository.save(studentList);

            log.info(format("Managers are generated: {0}", Arrays.toString(managerIdSet)));
            log.info(format("Teachers are generated: {0}", Arrays.toString(teacherIdSet)));
            log.info(format("Students are generated: {0}", Arrays.toString(studentIdSet)));

            log.info("----------------------------------------------------------------------------------------------------------------------------------------");

        } catch (final SQLException sqlEx) {
            log.error(Arrays.toString(sqlEx.getStackTrace()));
        }
    }

}
