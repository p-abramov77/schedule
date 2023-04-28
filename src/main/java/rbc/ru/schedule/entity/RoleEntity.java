package rbc.ru.schedule.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name="roles", uniqueConstraints={@UniqueConstraint(columnNames={"project_id", "user_id"})})
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProjectEntity project;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private UserEntity user;

    @NotNull
    private boolean producer;

}
