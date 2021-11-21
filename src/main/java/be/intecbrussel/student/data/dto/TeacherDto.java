package be.intecbrussel.student.data.dto;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

public class TeacherDto extends APersonDto {

    private String department;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public TeacherDto withDepartment(String department) {
        this.setDepartment(department);
        return this;
    }

    @Override
    public TeacherDto withId(String id) {
        super.setId(id);
        return this;
    }

    @Override
    public TeacherDto withActive(Boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public TeacherDto withFirstName(String firstName) {
        super.setFirstName(firstName);
        return this;
    }

    @Override
    public TeacherDto withLastName(String lastName) {
        super.setLastName(lastName);
        return this;
    }

    @Override
    public TeacherDto withAnonymous(Boolean anonymous) {
        super.setAnonymous(anonymous);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeacherDto)) return false;
        if (!super.equals(o)) return false;
        TeacherDto that = (TeacherDto) o;
        return getDepartment().equals(that.getDepartment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDepartment());
    }

    @Override
    public String toString() {
        return super.toString() + ":" + this.getDepartment();
    }

    @Override
    public TeacherDto withAuthenticated(Boolean authenticated) {
        super.setAuthenticated(authenticated);
        return this;
    }

    @Override
    public AUserDto withRoles(String... roles) {
        super.setRoles(new HashSet<>(Arrays.asList(roles)));
        return this;
    }

    @Override
    public TeacherDto withUsername(String username) {
        super.setUsername(username);
        return this;
    }

    @Override
    public TeacherDto withPassword(String password) {
        super.setPassword(password);
        return this;
    }

    @Override
    public TeacherDto withActivation(String activation) {
        super.setActivation(activation);
        return this;
    }
}
