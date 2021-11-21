package be.intecbrussel.student.data.entity;

import java.util.Objects;

public abstract class APersonEntity extends AUserEntity {

    private String firstName;
    private String lastName;
    private Boolean anonymous;

    public APersonEntity() {
    }

    public APersonEntity(String firstName, String lastName, Boolean anonymous) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.anonymous = anonymous;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public abstract APersonEntity withFirstName(String firstName);

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public abstract APersonEntity withLastName(String lastName);

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public abstract APersonEntity withAnonymous(Boolean anonymous);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof APersonEntity)) return false;
        if (!super.equals(o)) return false;
        APersonEntity that = (APersonEntity) o;
        return getFirstName().equals(that.getFirstName()) && getLastName().equals(that.getLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getFirstName(), getLastName());
    }
}
