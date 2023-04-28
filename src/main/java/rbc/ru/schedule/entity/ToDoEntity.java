package rbc.ru.schedule.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "todo_s")
public class ToDoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProjectEntity project;

    private boolean active;
    private String content;
    private LocalDateTime start;
    private LocalDateTime stop;
    private LocalDateTime close;

}
