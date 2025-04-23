package ra.edu.business.service;

import java.util.List;

public interface AppService<T, ID> {
    List<T> findAll(int pageNumber, int pageSize);

    int save(T obj);

    int updateField(ID id, String fieldName, Object newValue);

    int delete(ID id);
}