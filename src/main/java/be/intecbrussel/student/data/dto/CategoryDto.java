package be.intecbrussel.student.data.dto;

import java.util.Objects;

public class CategoryDto extends ADto {

    private String value;
    private CategoryDto parent;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CategoryDto getParent() {
        return parent;
    }

    public void setParent(CategoryDto parent) {
        this.parent = parent;
    }

    public void setParentId(String parentId) {
        this.setParent(new CategoryDto().withId(parentId));
    }

    public CategoryDto withValue(String value) {
        this.setValue(value);
        return this;
    }

    public CategoryDto withParentId(String parentId) {
        this.setParentId(parentId);
        return this;
    }

    public CategoryDto withParent(CategoryDto parent) {
        this.setParent(parent);
        return this;
    }

    @Override
    public CategoryDto withId(String id) {
        super.setId(id);
        return this;
    }

    @Override
    public CategoryDto withActive(Boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryDto)) return false;
        if (!super.equals(o)) return false;
        CategoryDto that = (CategoryDto) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getValue());
    }
}
