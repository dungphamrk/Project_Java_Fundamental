package ra.edu.business.service.technology;

import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.AppService;

import java.util.List;

public interface TechnologyService extends AppService<Technology> {
    Technology findById(int id);
    int searchByName(String keyword, int pageNumber, int pageSize);
    List<Technology> getCandidateTechnologies(int candidateId);
    int addCandidateTechnology(int candidateId, int technologyId);
    int removeCandidateTechnology(int candidateId, int technologyId);
}