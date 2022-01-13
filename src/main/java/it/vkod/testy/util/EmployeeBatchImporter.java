package it.vkod.testy.util;


import it.vkod.testy.data.entity.UserEntity;
import it.vkod.testy.repository.IUserRepository;
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

	private static final Logger log = LoggerFactory.getLogger( EmployeeBatchImporter.class );

	private final IUserRepository userRepository;

	private final BCryptPasswordEncoder passwordEncoder;

	private final List< UserEntity > admins = new ArrayList<>();
	private final List< UserEntity > managers = new ArrayList<>();
	private final List< UserEntity > teachers = new ArrayList<>();
	private final List< UserEntity > students = new ArrayList<>();


	public EmployeeBatchImporter( IUserRepository userRepository, BCryptPasswordEncoder passwordEncoder ) {

		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}


	public EmployeeBatchImporter withAdmin( UserEntity admin ) {

		this.admins.add( admin );
		return this;
	}


	public EmployeeBatchImporter withManager( UserEntity manager ) {

		this.managers.add( manager );
		return this;
	}


	public EmployeeBatchImporter withAdmins( List< UserEntity > admins ) {

		this.admins.addAll( admins );
		return this;
	}


	public EmployeeBatchImporter withManagers( List< UserEntity > managers ) {

		this.managers.addAll( managers );
		return this;
	}


	public EmployeeBatchImporter withTeacher( UserEntity teacher ) {

		this.teachers.add( teacher );
		return this;
	}


	public EmployeeBatchImporter withTeachers( List< UserEntity > teachers ) {

		this.teachers.addAll( teachers );
		return this;
	}


	public EmployeeBatchImporter withStudent( UserEntity student ) {

		this.students.add( student );
		return this;
	}


	public EmployeeBatchImporter withStudents( List< UserEntity > students ) {

		this.students.addAll( students );
		return this;
	}


	public void generate() {

		log.info( "---------------------------------------------------- DUMMY DATA (DELETE THIS IN PRODUCTION) ----------------------------------------------------" );

		final var adminList = this.admins
				.stream()
				.map( admin -> {

					final var effected = new Object() {
						final UserEntity entity = admin;
					};

					final var user = new UserEntity()
							.withUsername( admin.getUsername() )
							.withPassword( admin.getPassword() )
							.withFirstName( admin.getFirstName() )
							.withLastName( admin.getLastName() )
							.withEmail( admin.getEmail() )
							.withPhone( admin.getPhone() )
							.withActivation( UUID.randomUUID().toString() )
							.withAnonymous( Boolean.FALSE )
							.withActive( true )
							.withAuthenticated( true );

					user.setRoles( "ADMIN" );

					log.info( user.toString() );
					user.setPassword( passwordEncoder.encode( user.getPassword() ) );

					try {
						final var savedUserId = userRepository.save( user );
						effected.entity.setId( savedUserId );
					} catch ( SQLException sqlEx ) {
						log.error( Arrays.toString( sqlEx.getStackTrace() ) );
					}

					return effected.entity;
				} ).collect( Collectors.toUnmodifiableList() );

		final var managerList = this.managers
				.stream()
				.map( manager -> {

					final var effected = new Object() {
						final UserEntity entity = manager;
					};

					final var user = new UserEntity()
							.withUsername( manager.getUsername() )
							.withPassword( manager.getPassword() )
							.withFirstName( manager.getFirstName() )
							.withLastName( manager.getLastName() )
							.withEmail( manager.getEmail() )
							.withPhone( manager.getPhone() )
							.withActivation( UUID.randomUUID().toString() )
							.withAnonymous( Boolean.FALSE )
							.withActive( true )
							.withAuthenticated( true );

					user.setRoles( "MANAGER,TEACHER" );

					log.info( user.toString() );
					user.setPassword( passwordEncoder.encode( user.getPassword() ) );

					try {
						final var savedUserId = userRepository.save( user );
						effected.entity.setId( savedUserId );
					} catch ( SQLException sqlEx ) {
						log.error( Arrays.toString( sqlEx.getStackTrace() ) );
					}

					return effected.entity;
				} ).collect( Collectors.toUnmodifiableList() );

		final var teacherList = this.teachers
				.stream()
				.map( teacher -> {

					final var effected = new Object() {
						final UserEntity entity = teacher;
					};

					final var user = new UserEntity()
							.withUsername( teacher.getUsername() )
							.withPassword( teacher.getPassword() )
							.withActivation( UUID.randomUUID().toString() )
							.withAnonymous( Boolean.FALSE )
							.withFirstName( teacher.getFirstName() )
							.withLastName( teacher.getLastName() )
							.withEmail( teacher.getEmail() )
							.withPhone( teacher.getPhone() )
							.withActive( true )
							.withAuthenticated( true );

					user.setRoles( "TEACHER" );

					log.info( user.toString() );
					user.setPassword( passwordEncoder.encode( user.getPassword() ) );

					try {
						final var savedUserId = userRepository.save( user );
						effected.entity.setId( savedUserId );
					} catch ( SQLException sqlEx ) {
						log.error( Arrays.toString( sqlEx.getStackTrace() ) );
					}

					return effected.entity;
				} ).collect( Collectors.toUnmodifiableList() );

		final var studentList = this.students
				.stream()
				.map( student -> {

					final var effected = new Object() {
						final UserEntity entity = student;
					};

					final var user = new UserEntity()
							.withUsername( student.getUsername() )
							.withPassword( student.getPassword() )
							.withActivation( UUID.randomUUID().toString() )
							.withAnonymous( Boolean.FALSE )
							.withFirstName( student.getFirstName() )
							.withLastName( student.getLastName() )
							.withEmail( student.getEmail() )
							.withPhone( student.getPhone() )
							.withActive( true )
							.withAuthenticated( true );

					user.setRoles( "STUDENT,USER" );

					log.info( user.toString() );
					user.setPassword( passwordEncoder.encode( user.getPassword() ) );

					try {
						final var savedUserId = userRepository.save( user );
						effected.entity.setId( savedUserId );
					} catch ( SQLException sqlEx ) {
						log.error( Arrays.toString( sqlEx.getStackTrace() ) );
					}

					return effected.entity;
				} ).collect( Collectors.toUnmodifiableList() );

		log.info( format( "Managers are generated: {0}", Arrays.toString( managerList.toArray() ) ) );
		log.info( format( "Teachers are generated: {0}", Arrays.toString( teacherList.toArray() ) ) );
		log.info( format( "Students are generated: {0}", Arrays.toString( studentList.toArray() ) ) );

		log.info( "----------------------------------------------------------------------------------------------------------------------------------------" );

	}

}
