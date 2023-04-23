package ru.javarush.todo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.time.LocalDate;
import java.util.Objects;

import static org.hibernate.type.SqlTypes.LONGVARCHAR;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(schema = "todo", name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", length = 250, nullable = false)
    private String title;

    @Lob
    @JdbcTypeCode(LONGVARCHAR)
    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "deadline", nullable = false)
    private LocalDate deadline;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    public User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        if (!title.equals(task.title) || description.equals(task.description) || deadline.equals(task.deadline)
                || status != task.status) return false;
        return Objects.equals(id, task.id) && Objects.equals(user.getId(), task.user.getId());
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (deadline != null ? deadline.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (user.getId() != null ? user.getId().hashCode() : 0);
        return result;
    }
}
