package rbc.ru.schedule.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "equipment")
public class EquipmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 4)
    private String name;

    @ManyToMany(mappedBy = "equipments")
    private Set<ToDoEntity> todos = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof EquipmentEntity)) return false;
        return id != null && id.equals(((EquipmentEntity) o).getId());
    }
    @Override
    public int hashCode() {
        return 31;
    }
}
