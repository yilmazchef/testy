package be.intecbrussel.student.data.entity;

public class UserEntity extends AUserEntity {

    @Override
    public UserEntity withId(String id) {
        super.setId(id);
        return this;
    }

    @Override
    public UserEntity withActive(Boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public UserEntity withAuthenticated(Boolean authenticated) {
        super.setAuthenticated(authenticated);
        return this;
    }

    @Override
    public UserEntity withUsername(String username) {
        super.setUsername(username);
        return this;
    }

    @Override
    public UserEntity withPassword(String password) {
        super.setPassword(password);
        return this;
    }

    @Override
    public UserEntity withActivation(String activation) {
        super.setActivation(activation);
        return this;
    }

    @Override
    public String toString() {
        return "User: " + super.toString();
    }
}
