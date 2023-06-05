package rbc.ru.schedule.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="results")
public class ResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="todo_s_id")
    private ToDoEntity todo;

    @NotNull
    @NotBlank
    String content;

    String url;

    Boolean approved;

    @NotNull
    LocalDateTime dateTime;
}
