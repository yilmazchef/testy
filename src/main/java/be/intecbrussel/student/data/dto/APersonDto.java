package be.intecbrussel.student.data.dto;

import java.util.Objects;

public abstract class APersonDto extends AUserDto {

    private String firstName;
    private String lastName;
    private Boolean anonymous;

    public APersonDto() {
    }

    public APersonDto(String firstName, String lastName, Boolean anonymous) {
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

    public abstract APersonDto withFirstName(String firstName);

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public abstract APersonDto withLastName(String lastName);

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public abstract APersonDto withAnonymous(Boolean anonymous);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof APersonDto)) return false;
        if (!super.equals(o)) return false;
        APersonDto that = (APersonDto) o;
        return getFirstName().equals(that.getFirstName()) && getLastName().equals(that.getLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getFirstName(), getLastName());
    }

    @Override
    public String toString() {
        return this.getFirstName() + " " + this.getLastName();
    }
}
