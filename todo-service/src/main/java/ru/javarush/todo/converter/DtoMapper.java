package ru.javarush.todo.converter;

import org.springframework.data.domain.Page;

import java.util.List;

public interface DtoMapper<D, E> {

    D toDto(E e);

    List<D> toDto(List<? extends E> eList);

    default Page<D> toDto(Page<? extends E> ePage) {
        return ePage.map(this::toDto);
    }
}
