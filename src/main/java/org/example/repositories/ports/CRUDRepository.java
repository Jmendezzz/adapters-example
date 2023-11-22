package org.example.repositories.ports;

import java.util.List;

public interface CRUDRepository<T> {
    T create(T t);
    T getById(Long id);
    List<T> getALl();
    T update(T t);
    T deleteById(Long id);

}
