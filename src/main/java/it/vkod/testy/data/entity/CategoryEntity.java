package it.vkod.testy.data.entity;

import java.util.Objects;

public class CategoryEntity extends AEntity {

    private String value;
    private String parentId;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public CategoryEntity withValue(String value) {
        this.setValue(value);
        return this;
    }

    public CategoryEntity withParentId(String parentId) {
        if (parentId == null) {
            this.setParentId(this.getId());
        }
        this.setParentId(parentId);
        return this;
    }

    @Override
    public CategoryEntity withId(String id) {
        super.setId(id);
        return this;
    }

    @Override
    public CategoryEntity withActive(Boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryEntity)) return false;
        if (!super.equals(o)) return false;
        CategoryEntity that = (CategoryEntity) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getValue());
    }
}
