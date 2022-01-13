package it.vkod.testy.data.dto;

import java.io.Serializable;
import java.util.Objects;

public abstract class ADto implements Serializable, Cloneable {

    private String id;
    private Boolean isActive;

    public ADto() {
    }

    public ADto(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public abstract ADto withId(String id);

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public abstract ADto withActive(Boolean active);

    public void setActive() {
        setActive(Boolean.TRUE);
    }

    public void setPassive() {
        setActive(Boolean.FALSE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ADto)) return false;
        ADto aEntity = (ADto) o;
        return getId().equals(aEntity.getId());
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
