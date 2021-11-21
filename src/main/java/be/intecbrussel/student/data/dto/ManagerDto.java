package be.intecbrussel.student.data.dto;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

public class ManagerDto extends APersonDto {

    @Override
    public ManagerDto withId(String id) {
        super.setId(id);
        return this;
    }

    @Override
    public ManagerDto withActive(Boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public ManagerDto withFirstName(String firstName) {
        super.setFirstName(firstName);
        return this;
    }

    @Override
    public ManagerDto withLastName(String lastName) {
        super.setLastName(lastName);
        return this;
    }

    @Override
    public ManagerDto withAnonymous(Boolean anonymous) {
        super.setAnonymous(anonymous);
        return this;
    }

    @Override
    public ManagerDto withAuthenticated(Boolean authenticated) {
        super.setAuthenticated(authenticated);
        return this;
    }

    @Override
    public AUserDto withRoles(String... roles) {
        super.setRoles(new HashSet<>(Arrays.asList(roles)));
        return this;
    }

    @Override
    public ManagerDto withUsername(String username) {
        super.setUsername(username);
        return this;
    }

    @Override
    public ManagerDto withPassword(String password) {
        super.setPassword(password);
        return this;
    }

    @Override
    public ManagerDto withActivation(String activation) {
        super.setActivation(activation);
        return this;
    }

    @Override
    public String toString() {
        return "Manager: " + super.toString();
    }
}
