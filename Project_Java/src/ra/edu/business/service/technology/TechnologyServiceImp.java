package ra.edu.business.service.technology;

import ra.edu.business.dao.technology.TechnologyDao;
import ra.edu.business.dao.technology.TechnologyDaoImp;
import ra.edu.business.model.technology.Status;
import ra.edu.business.model.technology.Technology;

import java.util.List;

public class TechnologyServiceImp implements TechnologyService {
    private final TechnologyDao technologyDao;

    public TechnologyServiceImp() {
        this.technologyDao = new TechnologyDaoImp();
    }

    @Override
    public List<Technology> findAll(int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            return List.of();
        }
        return technologyDao.findAll(pageNumber, pageSize);
    }

    @Override
    public int save(Technology technology) {
        if (technology.getName() == null || technology.getName().trim().isEmpty()) {
            return 1; // Mã lỗi: tên không hợp lệ
        }
        if (technology.getStatus() == null) {
            return 2; // Mã lỗi: trạng thái không hợp lệ
        }
        return technologyDao.save(technology);
    }

    @Override
    public int updateField(Integer id, String fieldName, Object newValue) {
        if (id <= 0) {
            return 1; // Mã lỗi: ID không hợp lệ
        }
        if (newValue == null || newValue.toString().trim().isEmpty()) {
            return 2; // Mã lỗi: giá trị rỗng
        }
        if (!fieldName.equals("name") && !fieldName.equals("status")) {
            return 3; // Mã lỗi: trường không hợp lệ
        }
        if (fieldName.equals("status")) {
            try {
                Status.valueOf(newValue.toString().toUpperCase());
            } catch (IllegalArgumentException e) {
                return 4; // Mã lỗi: trạng thái không hợp lệ
            }
        }
        return technologyDao.updateField(id, fieldName, newValue);
    }

    @Override
    public List<Technology> findAllTechnologiesByCandidates(int pageNumber, int pageSize) {
        return technologyDao.findAllTechnologiesByCandidate(pageNumber, pageSize);
    }

    @Override
    public int delete(Integer id) {
        if (id <= 0) {
            return 1; // Mã lỗi: ID không hợp lệ
        }
        Technology technology = technologyDao.findById(id);
        if (technology == null) {
            return 2;
        }
        return technologyDao.delete(id);
    }

    @Override
    public Technology findById(int id) {
        if (id <= 0) {
            return null; // ID không hợp lệ
        }
        return technologyDao.findById(id);
    }

    @Override
    public List<Technology> searchByName(String keyword, int pageNumber, int pageSize) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of(); // Trả về danh sách rỗng nếu từ khóa không hợp lệ
        }
        if (pageNumber < 1 || pageSize < 1) {
            return List.of(); // Trả về danh sách rỗng nếu phân trang không hợp lệ
        }
        return technologyDao.searchByName(keyword, pageNumber, pageSize);
    }

    @Override
    public TechnologyPage getCandidateTechnologiesWithCount(Integer candidateId, int pageNumber, int pageSize) {
        if (candidateId <= 0 || pageNumber < 1 || pageSize < 1) {
            return new TechnologyPage(List.of(), 0);
        }
        return technologyDao.getCandidateTechnologiesWithCount(candidateId, pageNumber, pageSize);
    }

    @Override
    public int addCandidateTechnology(Integer candidateId, Integer technologyId) {
        if (candidateId <= 0 || technologyId <= 0) {
            return 1; // Mã lỗi: ID không hợp lệ
        }
        return technologyDao.addCandidateTechnology(candidateId, technologyId);
    }

    @Override
    public int removeCandidateTechnology(Integer candidateId, Integer technologyId) {
        if (candidateId <= 0 || technologyId <= 0) {
            return 1; // Mã lỗi: ID không hợp lệ
        }
        return technologyDao.removeCandidateTechnology(candidateId, technologyId);
    }

    @Override
    public List<Technology> findAllTechnologiesByAdmin(int pageNumber, int pageSize) {
        return findAll(pageNumber, pageSize);
    }

    @Override
    public TechnologyPage findActiveTechnologiesWithCount(int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            return new TechnologyPage(List.of(), 0);
        }
        return technologyDao.findActiveTechnologiesWithCount(pageNumber, pageSize);
    }

    @Override
    public int getTotalTechnologiesCount() {
        return technologyDao.getTotalTechnologiesCount();
    }

    @Override
    public int getTotalTechnologiesByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return 0;
        }
        return technologyDao.getTotalTechnologiesByName(keyword);
    }
}