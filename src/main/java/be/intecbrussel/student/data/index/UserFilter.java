package be.intecbrussel.student.data.index;

import java.util.Objects;

public class StudentFilter {

    private String firstName;
    private String lastName;
    private String className;
    private String username;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getClassName() {
        return className;
    }

    public String getUsername() {
        return username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public StudentFilter withFirstName(final String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public StudentFilter withLastName(final String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public StudentFilter withFullName(final String firstName, final String lastName) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        return this;
    }

    public StudentFilter withClassName(final String className) {
        this.setClassName(className);
        return this;
    }

    public StudentFilter withUserName(final String username) {
        this.setUsername(username);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentFilter)) return false;
        StudentFilter that = (StudentFilter) o;
        return Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getClassName(), that.getClassName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getClassName());
    }

    @Override
    public String toString() {
        return this.getFirstName() + " " + this.getLastName() + " -> " + this.getClassName();
    }
}
