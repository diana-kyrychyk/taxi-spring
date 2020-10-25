package ua.com.taxi.domain.dto.user;

import ua.com.taxi.domain.Role;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

public class UserUpdateDto {

    @Min(1)
    private int id;

    @NotBlank
    @Size(min = 2, max = 20)
    private String name;

    private List<Role> roles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserUpdateDto that = (UserUpdateDto) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, roles);
    }

    @Override
    public String toString() {
        return "UserUpdateDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", roles=" + roles +
                '}';
    }
}
