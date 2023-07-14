package com.shishir.drones.service;

import java.util.List;
import java.util.Optional;

public interface GenericCrudService<T, I> {
    List<T> saveAll(List<T> entities);

    Optional<T> save(T entity);

    Optional<T> update(I primaryKey, T entity);

    Optional<T> getOne(I primaryKey);

    List<T> getAll();
}
