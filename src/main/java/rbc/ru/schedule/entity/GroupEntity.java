package rbc.ru.schedule.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="groups", uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Size(min = 6)
    private String name;

    @ManyToMany(mappedBy = "groups")
    private Set<UserEntity> users = new HashSet<>();

    @Override
    public String toString() {
        return "GroupEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof GroupEntity)) return false;
        return id != null && id.equals(((GroupEntity) o).getId());
    }
    @Override
    public int hashCode() {
        return 31;
    }

}
