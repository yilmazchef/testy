package be.intecbrussel.student.service;

import be.intecbrussel.student.data.dto.TeacherDto;
import be.intecbrussel.student.data.entity.TeacherEntity;
import be.intecbrussel.student.mapper.ITeacherObjectMapper;
import be.intecbrussel.student.repository.ITeacherRepository;
import be.intecbrussel.student.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeacherService implements ITeacherService {

    private static final Logger log = LoggerFactory.getLogger(TeacherService.class);

    private final ITeacherRepository teacherRepository;
    private final ITeacherObjectMapper teacherMapper;
    private final IUserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public TeacherService(ITeacherRepository teacherRepository, ITeacherObjectMapper teacherMapper, IUserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String addNewTeacher(final TeacherDto teacher) {

        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        log.info("Password for " + teacher + " is encoded for secure login.");

        final var sequence = new Object() {
            String effectedId = null;
        };

        try {
            final var oUser = userRepository.selectByUserName(teacher.getUsername());
            final var teacherEntity = teacherMapper.toEntity(teacher);
            if (oUser.isEmpty()) {
                final var userEntity = teacherMapper.toUserEntity(teacher);
                final var savedUserId = userRepository.save(userEntity);
                teacher.setId(savedUserId);
            } else {
                teacherEntity.setId(sequence.effectedId);
            }

            sequence.effectedId = teacherRepository.save(teacherEntity);
            log.info("New teacher is created ..");

        } catch (SQLException sqlEx) {
            log.error(Arrays.toString(sqlEx.getStackTrace()));
        }
        teacher.setId(sequence.effectedId);

        return sequence.effectedId;
    }

    @Override
    public String updateTeacherById(final String id, final TeacherDto teacher) {

        final var sequence = new Object() {
            String effectedId = null;
        };

        if (!userRepository.existsByUserName(teacher.getUsername())) {
            try {
                final var teacherEntity = teacherMapper.toEntity(teacher);
                sequence.effectedId = teacherRepository.save(teacherEntity);
            } catch (SQLException sqlEx) {
                log.error(Arrays.toString(sqlEx.getStackTrace()));
            }
        }

        return sequence.effectedId;
    }

    @Override
    public String removeTeacherById(final String teacherId) {

        final var sequence = new Object() {
            String effectedId = null;
        };

        if (userRepository.existsByUserId(teacherId)) {
            try {
                sequence.effectedId = teacherRepository.delete(teacherId);
            } catch (SQLException sqlEx) {
                log.error(Arrays.toString(sqlEx.getStackTrace()));
            }
        }

        return sequence.effectedId;
    }

    @Override
    public Integer getTeachersCount() {

        final var sequence = new Object() {
            int result = 0;
        };

        try {
            sequence.result = teacherRepository.count();
        } catch (SQLException sqlEx) {
            log.error(Arrays.toString(sqlEx.getStackTrace()));
        }

        return sequence.result;
    }

    @Override
    public Optional<TeacherDto> fetchTeacherById(final String teacherId) {

        final var sequence = new Object() {
            Optional<TeacherEntity> teacher = Optional.empty();
        };

        try {
            sequence.teacher = teacherRepository.selectById(teacherId);
        } catch (SQLException sqlEx) {
            log.error(Arrays.toString(sqlEx.getStackTrace()));
        }

        return sequence.teacher.map(teacherMapper::toDTO);
    }

    @Override
    public Optional<TeacherDto> fetchTeacherByUserName(final String username) {

        final var sequence = new Object() {
            Optional<TeacherEntity> student = Optional.empty();
        };

        try {
            sequence.student = teacherRepository.selectByUserName(username);
        } catch (SQLException sqlEx) {
            log.error(Arrays.toString(sqlEx.getStackTrace()));
        }

        return sequence.student.map(teacherMapper::toDTO);
    }

    @Override
    public Optional<TeacherDto> fetchTeacherByLoginDetails(final String username, final String password) {

        final var sequence = new Object() {
            Optional<TeacherEntity> teacher = Optional.empty();
        };

        try {
            sequence.teacher = teacherRepository.selectByLoginDetails(username, password);
        } catch (SQLException sqlEx) {
            log.error(Arrays.toString(sqlEx.getStackTrace()));
        }

        return sequence.teacher.map(teacherMapper::toDTO);
    }

    @Override
    public List<TeacherDto> fetchTeachers(Integer offset, Integer limit) {

        final var sequence = new Object() {
            List<TeacherEntity> teachers = Collections.emptyList();
        };

        try {
            sequence.teachers = teacherRepository.selectAll();
        } catch (SQLException sqlEx) {
            log.error(Arrays.toString(sqlEx.getStackTrace()));
        }

        return sequence.teachers.stream().map(teacherMapper::toDTO).collect(Collectors.toUnmodifiableList());
    }
}