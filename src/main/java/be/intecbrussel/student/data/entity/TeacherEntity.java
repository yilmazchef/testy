package be.intecbrussel.student.data.entity;


public class TeacherEntity extends APersonEntity {

    private String department;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public TeacherEntity withDepartment(String department) {
        this.setDepartment(department);
        return this;
    }

    @Override
    public TeacherEntity withId(String id) {
        super.setId(id);
        return this;
    }

    @Override
    public TeacherEntity withActive(Boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public TeacherEntity withFirstName(String firstName) {
        super.setFirstName(firstName);
        return this;
    }

    @Override
    public TeacherEntity withLastName(String lastName) {
        super.setLastName(lastName);
        return this;
    }

    @Override
    public TeacherEntity withAnonymous(Boolean anonymous) {
        super.setAnonymous(anonymous);
        return this;
    }

    @Override
    public TeacherEntity withAuthenticated(Boolean authenticated) {
        super.setAuthenticated(authenticated);
        return this;
    }

    @Override
    public TeacherEntity withUsername(String username) {
        super.setUsername(username);
        return this;
    }

    @Override
    public TeacherEntity withPassword(String password) {
        super.setPassword(password);
        return this;
    }

    @Override
    public TeacherEntity withActivation(String activation) {
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

    public TeacherEntity withUser(AUserEntity user) {
        super.setId(user.getId());
        super.setActive(user.getActive());
        super.setUsername(user.getUsername());
        super.setPassword(user.getPassword());
        super.setActivation(user.getActivation());
        return this;
    }
}
