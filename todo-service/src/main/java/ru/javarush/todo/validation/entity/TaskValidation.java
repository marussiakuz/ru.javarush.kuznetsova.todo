package ru.javarush.todo.validation.entity;

public interface TaskValidation {

    void verifyWhetherTaskBelongsToUser(long userId, long taskId);

}
