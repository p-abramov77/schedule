package rbc.ru.schedule.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="todo_id", referencedColumnName = "id")
    private ToDoEntity todo;

    private String comment;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime dateTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private UserEntity user;

    public String toString() {
        return "Todo : " + todo.getContent() + "  Comment: " + comment + "  user: " + user.getUsername() + "  id = " + id;
    }
}
