package be.intecbrussel.student.data.entity;


public class StudentEntity extends APersonEntity {

    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public StudentEntity withClassName(String className) {
        setClassName(className);
        return this;
    }

    @Override
    public StudentEntity withId(String id) {
        super.setId(id);
        return this;
    }

    @Override
    public StudentEntity withActive(Boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public StudentEntity withFirstName(String firstName) {
        super.setFirstName(firstName);
        return this;
    }

    @Override
    public StudentEntity withLastName(String lastName) {
        super.setLastName(lastName);
        return this;
    }

    @Override
    public StudentEntity withAnonymous(Boolean anonymous) {
        super.setAnonymous(anonymous);
        return this;
    }

    @Override
    public StudentEntity withAuthenticated(Boolean authenticated) {
        super.setAuthenticated(authenticated);
        return this;
    }

    @Override
    public StudentEntity withUsername(String username) {
        super.setUsername(username);
        return this;
    }

    @Override
    public StudentEntity withPassword(String password) {
        super.setPassword(password);
        return this;
    }

    @Override
    public StudentEntity withActivation(String activation) {
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

    public StudentEntity withUser(AUserEntity user) {
        super.setId(user.getId());
        super.setActive(user.getActive());
        super.setUsername(user.getUsername());
        super.setPassword(user.getPassword());
        super.setActivation(user.getActivation());
        return this;
    }
}
