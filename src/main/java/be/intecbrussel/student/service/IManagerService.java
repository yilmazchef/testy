package be.intecbrussel.student.service;

import be.intecbrussel.student.data.dto.ManagerDto;
import be.intecbrussel.student.data.dto.TeacherDto;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IManagerService {

    String addNewManager(final ManagerDto manager);

    default Set<String> addNewManagers(final Set<ManagerDto> managerSet) {
        Set<String> set = new HashSet<>();
        for (ManagerDto managerDto : managerSet) {
            String addedManagerId = addNewManager(managerDto);
            set.add(addedManagerId);
        }
        return set;
    }

    String updateManagerById(final String id, final ManagerDto manager);

    String removeManagerById(final String managerId);

    Integer getManagersCount();

    Optional<ManagerDto> fetchManagerById(final String managerId);

    Optional<ManagerDto> fetchManagerByLoginDetails(final String username, final String password);

    Optional<ManagerDto> fetchManagerByUserName(final String username);

    List<ManagerDto> fetchManagers(Integer offset, Integer limit);
}
