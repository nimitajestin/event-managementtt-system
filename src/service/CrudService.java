package service;

import java.util.List;

public interface CrudService<T, ID> {
    T findById(ID id);
    List<T> findAll();
    T save(T entity);
    boolean update(T entity);
    boolean delete(ID id);
}
