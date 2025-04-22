package service;

import java.util.List;
// wrapper class wraps prim data type into obj so it can be used where objs are req
public interface CrudService<T, ID> {
    T findById(ID id);
    List<T> findAll();
    T save(T entity);
    boolean update(T entity);
    boolean delete(ID id);
}
