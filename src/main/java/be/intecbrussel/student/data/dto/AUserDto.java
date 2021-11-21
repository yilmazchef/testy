package be.intecbrussel.student.data.dto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class AUserDto extends ADto {

    private String username;
    private String password;
    private String activation;
    private Boolean authenticated;
    private Set<String> roles = new HashSet<>();

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    public abstract AUserDto withAuthenticated(Boolean authenticated);

    public Set<String> getRoles() {
        return roles;
    }

    public void addRole(String role) {
        this.roles.add(role);
    }

    public void removeRole(String role) {
        this.roles.remove(role);
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public abstract AUserDto withRoles(String... roles);

    public abstract AUserDto withUsername(String username);

    public abstract AUserDto withPassword(String password);

    public abstract AUserDto withActivation(String activation);

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AUserDto)) return false;
        if (!super.equals(o)) return false;
        AUserDto aUserDto = (AUserDto) o;
        return getUsername().equals(aUserDto.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUsername());
    }
}
