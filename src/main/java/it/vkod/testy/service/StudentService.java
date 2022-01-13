package it.vkod.testy.service;


import it.vkod.testy.data.dto.UserDto;
import it.vkod.testy.data.entity.UserEntity;
import it.vkod.testy.data.index.UserFilter;
import it.vkod.testy.mapper.IUserObjectMapper;
import it.vkod.testy.repository.IUserRepository;
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
public class StudentService implements IStudentService {

	private static final Logger log = LoggerFactory.getLogger( StudentService.class );

	private final IUserRepository studentRepository;
	private final IUserObjectMapper studentMapper;
	private final BCryptPasswordEncoder passwordEncoder;
	private final IUserRepository userRepository;


	public StudentService( final IUserRepository studentRepository, final IUserObjectMapper studentMapper, final BCryptPasswordEncoder passwordEncoder, final IUserRepository userRepository ) {

		this.studentRepository = studentRepository;
		this.studentMapper = studentMapper;
		this.passwordEncoder = passwordEncoder;

		this.userRepository = userRepository;
	}


	@Override
	public String addNewStudent( final UserDto student ) {

		student.setPassword( passwordEncoder.encode( student.getPassword() ) );
		log.info( "Password for " + student + " is encoded for secure login." );

		final var sequence = new Object() {
			String effectedId = null;
		};

		try {
			final var oUser = userRepository.selectByUserName( student.getUsername() );
			final var studentEntity = studentMapper.toEntity( student );
			if ( oUser.isEmpty() ) {
				final var userEntity = studentMapper.toEntity( student );
				final var savedUserId = userRepository.save( userEntity );
				student.setId( savedUserId );
			} else {
				studentEntity.setId( sequence.effectedId );
			}

			sequence.effectedId = studentRepository.save( studentEntity );
			log.info( "New student is created .." );

		} catch ( SQLException sqlEx ) {
			log.error( Arrays.toString( sqlEx.getStackTrace() ) );
		}
		student.setId( sequence.effectedId );

		return sequence.effectedId;
	}


	@Override
	public String updateStudentById( final String id, final UserDto student ) {

		final var sequence = new Object() {
			String effectedId = null;
		};

		if ( !userRepository.existsByUserName( student.getUsername() ) ) {
			try {
				final var studentEntity = studentMapper.toEntity( student );
				sequence.effectedId = studentRepository.save( studentEntity );
			} catch ( SQLException sqlEx ) {
				log.error( Arrays.toString( sqlEx.getStackTrace() ) );
			}
		}

		return sequence.effectedId;
	}


	@Override
	public String removeStudentById( final String studentId ) {

		final var sequence = new Object() {
			String effectedId = null;
		};

		if ( studentRepository.existsByUserId( studentId ) ) {
			try {
				sequence.effectedId = studentRepository.delete( studentId );
			} catch ( SQLException sqlEx ) {
				log.error( Arrays.toString( sqlEx.getStackTrace() ) );
			}
		}

		return sequence.effectedId;
	}


	@Override
	public Integer getStudentsCount() {

		final var sequence = new Object() {
			int result = 0;
		};

		try {
			sequence.result = studentRepository.count();
		} catch ( SQLException sqlEx ) {
			log.error( Arrays.toString( sqlEx.getStackTrace() ) );
		}

		return sequence.result;
	}


	@Override
	public Integer getStudentsCountByFullName( final String firstName, final String lastName ) {

		final var sequence = new Object() {
			int result = 0;
		};

		try {
			sequence.result = studentRepository.countByFullName( firstName, lastName );
		} catch ( SQLException sqlEx ) {
			log.error( Arrays.toString( sqlEx.getStackTrace() ) );
		}

		return sequence.result;
	}


	@Override
	public Optional< UserDto > fetchStudentById( final String studentId ) {

		final var sequence = new Object() {
			Optional< UserEntity > student = Optional.empty();
		};

		try {
			sequence.student = studentRepository.selectById( studentId );
		} catch ( SQLException sqlEx ) {
			log.error( Arrays.toString( sqlEx.getStackTrace() ) );
		}

		return sequence.student.map( studentMapper::toDTO );
	}


	@Override
	public Optional< UserDto > fetchStudentByUserName( final String username ) {

		final var sequence = new Object() {
			Optional< UserEntity > student = Optional.empty();
		};

		try {
			sequence.student = studentRepository.selectByUserName( username );
		} catch ( SQLException sqlEx ) {
			log.error( Arrays.toString( sqlEx.getStackTrace() ) );
		}

		return sequence.student.map( studentMapper::toDTO );
	}


	@Override
	public Optional< UserDto > fetchStudentByLoginDetails( final String username, final String password ) {

		final var sequence = new Object() {
			Optional< UserEntity > student = Optional.empty();
		};

		try {
			sequence.student = studentRepository.selectForLogin( username, password );
		} catch ( SQLException sqlEx ) {
			log.error( Arrays.toString( sqlEx.getStackTrace() ) );
		}

		return sequence.student.map( studentMapper::toDTO );
	}


	@Override
	public List< UserDto > fetchStudents( final Integer offset, final Integer limit ) {

		final var sequence = new Object() {
			List< UserEntity > students = Collections.emptyList();
		};

		try {
			sequence.students = studentRepository.selectAll();
		} catch ( SQLException sqlEx ) {
			log.error( Arrays.toString( sqlEx.getStackTrace() ) );
		}

		return sequence.students.stream().map( studentMapper::toDTO ).collect( Collectors.toUnmodifiableList() );
	}


	@Override
	public List< UserDto > fetchStudents( final Integer offset, final Integer limit, final String filterText ) {

		final var sequence = new Object() {
			List< UserEntity > students = Collections.emptyList();
		};

		try {
			sequence.students = studentRepository.filter( filterText );
		} catch ( SQLException sqlEx ) {
			log.error( Arrays.toString( sqlEx.getStackTrace() ) );
		}

		return sequence.students.stream().map( studentMapper::toDTO ).collect( Collectors.toUnmodifiableList() );
	}


	@Override
	public List< UserDto > fetchStudents( final Integer offset, final Integer limit, final UserFilter userFilter ) {

		final var sequence = new Object() {
			List< UserEntity > students = Collections.emptyList();
		};

		try {
			sequence.students = studentRepository.filter( userFilter );
		} catch ( SQLException sqlEx ) {
			log.error( Arrays.toString( sqlEx.getStackTrace() ) );
		}

		return sequence.students.stream().map( studentMapper::toDTO ).collect( Collectors.toUnmodifiableList() );
	}

}
