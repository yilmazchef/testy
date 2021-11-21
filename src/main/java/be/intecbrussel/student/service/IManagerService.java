package be.intecbrussel.student.service;


import be.intecbrussel.student.data.dto.UserDto;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IManagerService {

	String addNewManager( final UserDto manager );

	default Set< String > addNewManagers( final Set< UserDto > managerSet ) {

		Set< String > set = new HashSet<>();
		for ( UserDto managerDto : managerSet ) {
			String addedManagerId = addNewManager( managerDto );
			set.add( addedManagerId );
		}
		return set;
	}

	String updateManagerById( final String id, final UserDto manager );

	String removeManagerById( final String managerId );

	Integer getManagersCount();

	Optional< UserDto > fetchManagerById( final String managerId );

	Optional< UserDto > fetchManagerByLoginDetails( final String username, final String password );

	Optional< UserDto > fetchManagerByUserName( final String username );

	List< UserDto > fetchManagers( Integer offset, Integer limit );

}
