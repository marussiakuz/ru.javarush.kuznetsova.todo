package ru.javarush.todo.converter;

import org.mapstruct.MappingTarget;

public interface EntityMapper<D, E> {

    E toEntity(D d);

    void updateEntityFromDto(D d, @MappingTarget E e);
}
