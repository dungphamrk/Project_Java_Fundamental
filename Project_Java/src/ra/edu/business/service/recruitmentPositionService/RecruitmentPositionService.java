package ra.edu.business.service.recruitmentPositionService;

import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;
import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.AppService;

import java.util.List;

public interface RecruitmentPositionService extends AppService<RecruitmentPosition, Integer> {
    List<RecruitmentPosition> findAll(int pageNumber, int pageSize);
    List<RecruitmentPosition> getActivePositions(int pageNumber, int pageSize);
    List<RecruitmentPosition> getAllPositions(int pageNumber, int pageSize);
    boolean isPositionActive(int positionId);
    int updateField(Integer id, String fieldName, Object newValue);
    List<Technology> getPositionTechnologies(Integer positionId);
    int addPositionTechnology(Integer positionId, Integer technologyId);
    int removePositionTechnology(Integer positionId, Integer technologyId);
    int getTotalPositionsCount(); // Thêm để lấy tổng số vị trí
    int getActivePositionsCount(); // Thêm để lấy tổng số vị trí active
}