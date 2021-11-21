package be.intecbrussel.student.data.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public abstract class AEntity implements Serializable, Cloneable {

    private String id = UUID.randomUUID().toString();
    private Boolean isActive = false;

    public AEntity() {
    }

    public AEntity(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public abstract AEntity withId(String id);

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public abstract AEntity withActive(Boolean active);

    public void setActive() {
        setActive(Boolean.TRUE);
    }

    public void setPassive() {
        setActive(Boolean.FALSE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AEntity)) return false;
        AEntity that = (AEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return this.getId();
    }
}
