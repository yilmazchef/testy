package it.vkod.testy.service;


import it.vkod.testy.data.dto.CourseDto;
import it.vkod.testy.data.dto.UserDto;
import it.vkod.testy.data.index.UserFilter;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IStudentService {

	String addNewStudent( final UserDto student );

	default Set< String > addNewStudents( final Set< UserDto > studentSet ) {

		Set< String > set = new HashSet<>();
		for ( UserDto studentDto : studentSet ) {
			String addedStudentId = addNewStudent( studentDto );
			set.add( addedStudentId );
		}
		return set;
	}

	String updateStudentById( final String id, final UserDto student );

	String removeStudentById( final String studentId );

	Integer getStudentsCount();

	Integer getStudentsCountByFullName( final String firstName, final String lastName );

	Optional< UserDto > fetchStudentById( final String studentId );

	Optional< UserDto > fetchStudentByUserName( final String username );

	List< UserDto > fetchStudentByCourse( final CourseDto course );

	Optional< UserDto > fetchStudentByLoginDetails( final String username, final String password );

	List< UserDto > fetchStudents( final Integer offset, final Integer limit );

	List< UserDto > fetchStudents( final Integer offset, final Integer limit, final String filterText );

	List< UserDto > fetchStudents( final Integer offset, final Integer limit, final UserFilter filter );

}
