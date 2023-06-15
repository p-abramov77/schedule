package rbc.ru.schedule.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import rbc.ru.schedule.service.GroupServiceImpl;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name="users", uniqueConstraints={@UniqueConstraint(columnNames={"username"})})
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull()
    @Size(min = 3)
    @Column(unique = true)
    String username;

    @NotNull
    @Size(min = 3)
    String password;

    @NotBlank()
    String email;

    String phone;

    Boolean enabled;
    Boolean maker;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "user_group",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<GroupEntity> groups = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void addGroup(GroupEntity group){
        this.groups.add(group);
        group.getUsers().add(this);
    }
    public void removeGroup(GroupEntity group){
        this.groups.remove(group);
        group.getUsers().remove(this);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", enabled=" + enabled +
                ", maker=" + maker +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserEntity)) return false;
        return id != null && id.equals(((UserEntity) o).getId());
    }
    @Override
    public int hashCode() {
        return 31;
    }

}
