package be.intecbrussel.student.data.dto;


import java.util.Arrays;
import java.util.HashSet;

public class StudentDto extends APersonDto {

    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public StudentDto withClassName(String className) {
        setClassName(className);
        return this;
    }

    @Override
    public StudentDto withId(String id) {
        super.setId(id);
        return this;
    }

    @Override
    public StudentDto withActive(Boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public StudentDto withFirstName(String firstName) {
        super.setFirstName(firstName);
        return this;
    }

    @Override
    public StudentDto withLastName(String lastName) {
        super.setLastName(lastName);
        return this;
    }

    @Override
    public StudentDto withAnonymous(Boolean anonymous) {
        super.setAnonymous(anonymous);
        return this;
    }

    @Override
    public String toString() {
        return super.toString() + ":" + this.getClassName();
    }

    @Override
    public StudentDto withAuthenticated(Boolean authenticated) {
        super.setAuthenticated(authenticated);
        return this;
    }

    @Override
    public StudentDto withRoles(String... roles) {
        super.setRoles(new HashSet<>(Arrays.asList(roles)));
        return this;
    }

    @Override
    public StudentDto withUsername(String username) {
        super.setUsername(username);
        return this;
    }

    @Override
    public StudentDto withPassword(String password) {
        super.setPassword(password);
        return this;
    }

    @Override
    public StudentDto withActivation(String activation) {
        super.setActivation(activation);
        return this;
    }
}
