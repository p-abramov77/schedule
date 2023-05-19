package rbc.ru.schedule.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "todo_s")
public class ToDoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="project_id")
    private ProjectEntity project;

    @NotBlank
    private String content;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime stop;
    @NotNull
    private Long changedByUser;
    @NotNull
    LocalDateTime dateTime;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "todo_equipment",
            joinColumns = @JoinColumn(name = "todo_s_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_id")
    )
    private Set<EquipmentEntity> equipmentEntities = new HashSet<>();

    @OneToMany(mappedBy = "toDoEntity", cascade = CascadeType.ALL)
    private Set<ResultEntity> resultEntities = new HashSet<>();

    public void addEquipment(EquipmentEntity equipment){
        this.equipmentEntities.add(equipment);
        equipment.getToDoEntities().add(this);
    }
    public void removeEquipment(EquipmentEntity equipment){
        this.equipmentEntities.remove(equipment);
        equipment.getToDoEntities().remove(this);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ToDoEntity)) return false;
        return id != null && id.equals(((ToDoEntity) o).getId());
    }
    @Override
    public int hashCode() {
        return 31;
    }
}
