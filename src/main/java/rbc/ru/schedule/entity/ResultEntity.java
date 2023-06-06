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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="todo_id", referencedColumnName = "id")
    private ToDoEntity todo;

    @NotBlank
    String content;

    String url;

    Boolean approved;

    @NotNull
    LocalDateTime dateTime;
    public String toString() {
        return "Todo : " + todo.getContent() + "  Content: " + content  + "  id = " + id;
    }

}
