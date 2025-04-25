package ra.edu.business.dao.recruitmentPosition;

import ra.edu.business.dao.AppDao;
import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;
import ra.edu.business.model.technology.Technology;

import java.util.List;
public interface RecruitmentPositionDao extends AppDao<RecruitmentPosition, Integer> {
    List<RecruitmentPosition> findAll(int pageNumber, int pageSize);
    List<RecruitmentPosition> getActivePositions(int pageNumber, int pageSize);
    List<RecruitmentPosition> getAllPositions(int pageNumber, int pageSize);
    boolean isPositionActive(int positionId);
    int updateField(Integer id, String fieldName, Object newValue);
    List<Technology> getPositionTechnologies(Integer positionId);
    int addPositionTechnology(Integer positionId, Integer technologyId);
    int removePositionTechnology(Integer positionId, Integer technologyId);
    int getTotalPositionsCount();
    int getActivePositionsCount();
    // Thêm phương thức mới
    List<RecruitmentPosition> getFilteredPositionsByTechnologies(List<Integer> technologyIds, int pageNumber, int pageSize);
    int getFilteredPositionsCountByTechnologies(List<Integer> technologyIds);
}