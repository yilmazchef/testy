package it.vkod.testy;


import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * The entry point of the Spring Boot application.
 * <p>
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@SpringBootApplication
@PWA( name = "testy", shortName = "testy", offlineResources = { "images/logo.png" } )
@NpmPackage( value = "line-awesome", version = "1.3.0" )
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

	public static void main( String[] args ) {

		SpringApplication.run( Application.class, args );
	}

}