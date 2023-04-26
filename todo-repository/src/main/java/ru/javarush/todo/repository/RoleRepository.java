package ru.javarush.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import ru.javarush.todo.model.Role;
import ru.javarush.todo.model.User;

import java.util.List;
import java.util.Set;

@EnableJpaRepositories
public interface RoleRepository extends JpaRepository<User, Long> {

    @Query("from Role where name in (:names)")
    List<Role> getAllByNames(@Param("names") Set<String> names);
}