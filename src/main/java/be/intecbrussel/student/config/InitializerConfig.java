package be.intecbrussel.student.config;


import be.intecbrussel.student.data.entity.UserEntity;
import be.intecbrussel.student.util.EmployeeBatchImporter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class InitializerConfig implements CommandLineRunner {

	private final EmployeeBatchImporter importer;


	public InitializerConfig( final EmployeeBatchImporter importer ) {

		this.importer = importer;
	}


	@Override
	public void run( final String... args ) throws Exception {

		this.importer
				.withAdmin(
						new UserEntity()
								.withId( "BOT" )
								.withActivation( UUID.randomUUID().toString() )
								.withPassword("P@ssw0rd" )
								.withUsername( "testy.bot" )
								.withEmail( "testy.bot@intecbrussel.be" )
								.withFirstName( "Testy" )
								.withLastName( "Bot" )
								.withPhone( "0411111111" )
				)
				.withTeacher(
						new UserEntity()
								.withActivation( UUID.randomUUID().toString() )
								.withPassword("P@ssw0rd" )
								.withUsername( "yilmaz.mustafa" )
								.withEmail( "yilmaz.mustafa@intecbrussel.be" )
								.withFirstName( "Yilmaz" )
								.withLastName( "Mustafa" )
								.withPhone( "0467711709" )
				)
				.withTeacher(
						new UserEntity()
								.withActivation( UUID.randomUUID().toString() )
								.withPassword("P@ssw0rd")
								.withUsername( "pearl.de.smet" )
								.withEmail( "pearl.de.smet@intecbrussel.be" )
								.withFirstName( "Pearl" )
								.withLastName( "De Smet" )
								.withPhone( "0467554499" )
								.withRoles( "ROLE_MANAGER,ROLE_TEACHER" )
				)
				.withTeacher(
						new UserEntity()
								.withActivation( UUID.randomUUID().toString() )
								.withPassword("P@ssw0rd")
								.withUsername( "patrick.geudens" )
								.withEmail( "patrick.geudens@intecbrussel.be" )
								.withFirstName( "Patrick" )
								.withLastName( "Geudens" )
								.withPhone( "046777524754" )
				)
				.withStudent(
						new UserEntity()
								.withActivation( UUID.randomUUID().toString() )
								.withActive( Boolean.TRUE )
								.withPassword("P@ssw0rd")
								.withUsername( "john.doe" )
								.withAuthenticated( Boolean.TRUE )
								.withEmail( "john.doe@intecbrussel.be" )
								.withFirstName( "John" )
								.withLastName( "Doe" )
								.withPhone( "046711223344" )
								.withAnonymous( Boolean.FALSE )
				)
				.generate();

	}

}
