package ra.edu.business.dao.technology;

import ra.edu.business.dao.AppDao;
import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.technology.TechnologyService;

import java.util.List;

public interface TechnologyDao extends AppDao<Technology, Integer> {
    Technology findById(int id);
    List<Technology> searchByName(String keyword, int pageNumber, int pageSize);
    TechnologyService.TechnologyPage getCandidateTechnologiesWithCount(Integer candidateId, int pageNumber, int pageSize);
    int addCandidateTechnology(Integer candidateId, Integer technologyId);
    int removeCandidateTechnology(Integer candidateId, Integer technologyId);
    int updateField(Integer id, String fieldName, Object newValue);
    List<Technology> findAllTechnologiesByCandidate(int pageNumber, int pageSize);
    TechnologyService.TechnologyPage findActiveTechnologiesWithCount(int pageNumber, int pageSize);
    int getTotalTechnologiesCount(); // Thêm để lấy tổng số công nghệ
    int getTotalTechnologiesByName(String keyword); // Thêm để lấy tổng số công nghệ theo tên
}