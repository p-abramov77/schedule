package rbc.ru.schedule.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="tag", uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String comment;
    @ManyToMany(mappedBy = "tags")
    private Set<ProjectEntity> projectEntities = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TagEntity)) return false;
        return id != null && id.equals(((TagEntity) o).getId());
    }
    @Override
    public int hashCode() {
        return 31;
    }

}
