package rbc.ru.schedule.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "todos")
public class ToDoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull
    private LocalDateTime start;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull
    private LocalDateTime stop;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull
    LocalDateTime dateTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producer_id", referencedColumnName="id")
    private UserEntity producer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executor_id", referencedColumnName = "id")
    private UserEntity executor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="project_id", referencedColumnName = "id")
    private ProjectEntity project;

    @OneToMany(mappedBy = "todo")
    private Set<ResultEntity> results = new HashSet<>();

    @OneToMany(mappedBy = "todo")
    private Set<CommentEntity> comments = new HashSet<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "todo_equipment",
            joinColumns = @JoinColumn(name = "todo_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_id")
    )
    private Set<EquipmentEntity> equipments = new HashSet<>();

    public void addEquipment(EquipmentEntity equipment){
        this.equipments.add(equipment);
        equipment.getTodos().add(this);
    }
    public void removeEquipment(EquipmentEntity equipment){
        this.equipments.remove(equipment);
        equipment.getTodos().remove(this);
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
