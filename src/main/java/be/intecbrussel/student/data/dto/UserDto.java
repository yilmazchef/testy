package be.intecbrussel.student.data.dto;

import java.util.Arrays;
import java.util.HashSet;

public class UserDto extends AUserDto {

    @Override
    public UserDto withId(String id) {
        super.setId(id);
        return this;
    }

    @Override
    public UserDto withActive(Boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public UserDto withAuthenticated(Boolean authenticated) {
        super.setAuthenticated(authenticated);
        return this;
    }

    @Override
    public AUserDto withRoles(String... roles) {
        super.setRoles(new HashSet<>(Arrays.asList(roles)));
        return this;
    }

    @Override
    public UserDto withUsername(String username) {
        super.setUsername(username);
        return this;
    }

    @Override
    public UserDto withPassword(String password) {
        super.setPassword(password);
        return this;
    }

    @Override
    public UserDto withActivation(String activation) {
        super.setActivation(activation);
        return this;
    }
}
