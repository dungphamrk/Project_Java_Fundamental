package ra.edu.business.dao;

import java.util.List;


public interface AppDao<T, ID> {
    List<T> findAll(int pageNumber, int pageSize);

    int save(T obj);

    int updateField(ID id, String fieldName, Object newValue);

    int delete(ID id);
}
