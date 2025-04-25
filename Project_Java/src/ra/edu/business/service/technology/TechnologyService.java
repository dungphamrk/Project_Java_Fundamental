package ra.edu.business.service.technology;

import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.AppService;

import java.util.List;

public interface TechnologyService extends AppService<Technology, Integer> {
    Technology findById(int id);
    List<Technology> searchByName(String keyword, int pageNumber, int pageSize);
    TechnologyPage getCandidateTechnologiesWithCount(Integer candidateId, int pageNumber, int pageSize);
    int addCandidateTechnology(Integer candidateId, Integer technologyId);
    int removeCandidateTechnology(Integer candidateId, Integer technologyId);
    List<Technology> findAllTechnologiesByAdmin(int pageNumber, int pageSize);
    int updateField(Integer id, String fieldName, Object newValue);
    List<Technology> findAllTechnologiesByCandidates(int pageNumber, int pageSize);
    TechnologyPage findActiveTechnologiesWithCount(int pageNumber, int pageSize);
    int getTotalTechnologiesCount(); // Thêm để lấy tổng số công nghệ
    int getTotalTechnologiesByName(String keyword); // Thêm để lấy tổng số công nghệ theo tên

    class TechnologyPage {
        private List<Technology> technologies;
        private int totalRecords;

        public TechnologyPage(List<Technology> technologies, int totalRecords) {
            this.technologies = technologies;
            this.totalRecords = totalRecords;
        }

        public List<Technology> getTechnologies() {
            return technologies;
        }

        public int getTotalRecords() {
            return totalRecords;
        }
    }
}