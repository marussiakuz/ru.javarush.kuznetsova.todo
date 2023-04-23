package ru.javarush.todo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import ru.javarush.todo.model.Task;

@EnableJpaRepositories
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findAllByUserId(@Param("userId") long userId, Pageable pageable);

    boolean existsByIdAndUserId(long taskId, long userId);

}
