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
    private Set<UserEntity> userEntities = new HashSet<>();

    //TODO add & remove to user_group
}
