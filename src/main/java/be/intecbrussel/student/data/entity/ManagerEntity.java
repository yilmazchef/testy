package be.intecbrussel.student.data.entity;


public class ManagerEntity extends APersonEntity {

    @Override
    public ManagerEntity withId(String id) {
        super.setId(id);
        return this;
    }

    @Override
    public ManagerEntity withActive(Boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public ManagerEntity withFirstName(String firstName) {
        super.setFirstName(firstName);
        return this;
    }

    @Override
    public ManagerEntity withLastName(String lastName) {
        super.setLastName(lastName);
        return this;
    }

    @Override
    public ManagerEntity withAnonymous(Boolean anonymous) {
        super.setAnonymous(anonymous);
        return this;
    }

    @Override
    public ManagerEntity withAuthenticated(Boolean authenticated) {
        super.setAuthenticated(authenticated);
        return this;
    }

    @Override
    public ManagerEntity withUsername(String username) {
        super.setUsername(username);
        return this;
    }

    @Override
    public ManagerEntity withPassword(String password) {
        super.setPassword(password);
        return this;
    }

    @Override
    public ManagerEntity withActivation(String activation) {
        super.setActivation(activation);
        return this;
    }

    public void setUser(AUserEntity user) {
        super.setId(user.getId());
        super.setActive(user.getActive());
        super.setUsername(user.getUsername());
        super.setPassword(user.getPassword());
        super.setActivation(user.getActivation());
    }

    public ManagerEntity withUser(AUserEntity user) {
        super.setId(user.getId());
        super.setActive(user.getActive());
        super.setUsername(user.getUsername());
        super.setPassword(user.getPassword());
        super.setActivation(user.getActivation());
        return this;
    }
}
