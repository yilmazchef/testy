package be.intecbrussel.student.service;

import be.intecbrussel.student.data.dto.ManagerDto;
import be.intecbrussel.student.data.entity.ManagerEntity;
import be.intecbrussel.student.mapper.IManagerObjectMapper;
import be.intecbrussel.student.repository.IManagerRepository;
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
public class ManagerService implements IManagerService {

    private static final Logger log = LoggerFactory.getLogger(ManagerService.class);

    private final IManagerRepository managerRepository;
    private final IManagerObjectMapper managerMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;

    public ManagerService(IManagerRepository managerRepository, IManagerObjectMapper managerMapper, BCryptPasswordEncoder passwordEncoder, IUserRepository userRepository) {
        this.managerRepository = managerRepository;
        this.managerMapper = managerMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public String addNewManager(final ManagerDto manager) {

        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
        log.info("Password for " + manager + " is encoded for secure login.");

        final var sequence = new Object() {
            String effectedId = null;
        };

        try {
            final var oUser = userRepository.selectByUserName(manager.getUsername());
            final var managerEntity = managerMapper.toEntity(manager);
            if (oUser.isEmpty()) {
                final var userEntity = managerMapper.toUserEntity(manager);
                final var savedUserId = userRepository.save(userEntity);
                manager.setId(savedUserId);
            } else {
                managerEntity.setId(sequence.effectedId);
            }

            sequence.effectedId = managerRepository.save(managerEntity);
            log.info("New manager is created ..");

        } catch (SQLException sqlEx) {
            log.error(Arrays.toString(sqlEx.getStackTrace()));
        }

        manager.setId(sequence.effectedId);

        return sequence.effectedId;
    }

    @Override
    public String updateManagerById(final String id, final ManagerDto manager) {

        final var sequence = new Object() {
            String effectedId = null;
        };

        if (!userRepository.existsByUserName(manager.getUsername())) {
            try {
                final var managerEntity = managerMapper.toEntity(manager);
                sequence.effectedId = managerRepository.save(managerEntity);
            } catch (SQLException sqlEx) {
                log.error(Arrays.toString(sqlEx.getStackTrace()));
            }
        }

        return sequence.effectedId;
    }

    @Override
    public String removeManagerById(final String managerId) {

        final var sequence = new Object() {
            String effectedId = null;
        };

        if (managerRepository.existsByUserId(managerId)) {
            try {
                sequence.effectedId = managerRepository.delete(managerId);
            } catch (SQLException sqlEx) {
                log.error(Arrays.toString(sqlEx.getStackTrace()));
            }
        }

        return sequence.effectedId;
    }

    @Override
    public Integer getManagersCount() {

        final var sequence = new Object() {
            int result = 0;
        };

        try {
            sequence.result = managerRepository.count();
        } catch (SQLException sqlEx) {
            log.error(Arrays.toString(sqlEx.getStackTrace()));
        }

        return sequence.result;
    }

    @Override
    public Optional<ManagerDto> fetchManagerById(final String managerId) {

        final var sequence = new Object() {
            Optional<ManagerEntity> manager = Optional.empty();
        };

        try {
            sequence.manager = managerRepository.selectById(managerId);
        } catch (SQLException sqlEx) {
            log.error(Arrays.toString(sqlEx.getStackTrace()));
        }

        return sequence.manager.map(managerMapper::toDTO);
    }

    @Override
    public Optional<ManagerDto> fetchManagerByLoginDetails(final String username, final String password) {

        final var sequence = new Object() {
            Optional<ManagerEntity> manager = Optional.empty();
        };

        try {
            sequence.manager = managerRepository.selectByLoginDetails(username, password);
        } catch (SQLException sqlEx) {
            log.error(Arrays.toString(sqlEx.getStackTrace()));
        }

        return sequence.manager.map(managerMapper::toDTO);
    }

    @Override
    public Optional<ManagerDto> fetchManagerByUserName(final String username) {

        final var sequence = new Object() {
            Optional<ManagerEntity> manager = Optional.empty();
        };

        try {
            sequence.manager = managerRepository.selectByUserName(username);
        } catch (SQLException sqlEx) {
            log.error(Arrays.toString(sqlEx.getStackTrace()));
        }

        return sequence.manager.map(managerMapper::toDTO);
    }

    @Override
    public List<ManagerDto> fetchManagers(Integer offset, Integer limit) {

        final var sequence = new Object() {
            List<ManagerEntity> managers = Collections.emptyList();
        };

        try {
            sequence.managers = managerRepository.select();
        } catch (SQLException sqlEx) {
            log.error(Arrays.toString(sqlEx.getStackTrace()));
        }

        return sequence.managers.stream().map(managerMapper::toDTO).collect(Collectors.toUnmodifiableList());
    }
}
