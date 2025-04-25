package ra.edu.business.dao.technology;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.technology.Status;
import ra.edu.business.model.technology.Technology;
import ra.edu.business.service.technology.TechnologyService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TechnologyDaoImp implements TechnologyDao {

    @Override
    public List<Technology> findAll(int pageNumber, int pageSize) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        List<Technology> technologies = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{CALL sp_FindAllTechnologiesByAdmin(?,?,?)}");
            callStmt.setInt(1, pageNumber);
            callStmt.setInt(2, pageSize);
            callStmt.registerOutParameter(3, Types.INTEGER);
            rs = callStmt.executeQuery();
            while (rs.next()) {
                Technology technology = new Technology();
                technology.setId(rs.getInt("id"));
                technology.setName(rs.getString("name"));
                technology.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
                technologies.add(technology);
            }
            return technologies;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách công nghệ: " + e.getMessage());
            return technologies;
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng ResultSet: " + e.getMessage());
            }
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int save(Technology technology) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{CALL sp_CreateTechnology(?,?,?)}");
            callStmt.setString(1, technology.getName());
            callStmt.setString(2, technology.getStatus().name().toUpperCase());
            callStmt.registerOutParameter(3, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(3);
            if (returnCode == 0) {
                conn.commit();
            } else {
                conn.rollback();
            }
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm công nghệ: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 1;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi khôi phục auto-commit: " + ex.getMessage());
            }
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int updateField(Integer id, String fieldName, Object newValue) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{CALL sp_UpdateTechnologyField(?,?,?,?)}");
            callStmt.setInt(1, id);
            callStmt.setString(2, fieldName);
            callStmt.setString(3, newValue.toString());
            callStmt.registerOutParameter(4, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(4);
            if (returnCode == 0) {
                conn.commit();
            } else {
                conn.rollback();
            }
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi cập nhật trường công nghệ: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 1;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi khôi phục auto-commit: " + ex.getMessage());
            }
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public List<Technology> findAllTechnologiesByCandidate(int pageNumber, int pageSize) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        List<Technology> technologies = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{CALL sp_FindAllTechnologiesByAdmin(?,?,?)}");
            callStmt.setInt(1, pageNumber);
            callStmt.setInt(2, pageSize);
            callStmt.registerOutParameter(3, Types.INTEGER);
            rs = callStmt.executeQuery();
            while (rs.next()) {
                Technology technology = new Technology();
                technology.setId(rs.getInt("id"));
                technology.setName(rs.getString("name"));
                technology.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
                technologies.add(technology);
            }
            return technologies;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách công nghệ: " + e.getMessage());
            return technologies;
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng ResultSet: " + e.getMessage());
            }
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int delete(Integer id) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{CALL sp_DeleteTechnology(?,?)}");
            callStmt.setInt(1, id);
            callStmt.registerOutParameter(2, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(2);
            if (returnCode == 0) {
                conn.commit();
            } else {
                conn.rollback();
            }
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi xóa công nghệ: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 1;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi khôi phục auto-commit: " + ex.getMessage());
            }
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public Technology findById(int id) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{CALL sp_FindTechnologyById(?)}");
            callStmt.setInt(1, id);
            rs = callStmt.executeQuery();
            if (rs.next()) {
                Technology technology = new Technology();
                technology.setId(rs.getInt("id"));
                technology.setName(rs.getString("name"));
                technology.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
                return technology;
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi tìm kiếm công nghệ theo ID: " + e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("Trạng thái công nghệ không hợp lệ: " + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng ResultSet: " + e.getMessage());
            }
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public List<Technology> searchByName(String keyword, int pageNumber, int pageSize) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        List<Technology> technologies = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{CALL sp_SearchTechnologies(?,?,?)}");
            callStmt.setString(1, "%" + keyword + "%");
            callStmt.setInt(2, pageNumber);
            callStmt.setInt(3, pageSize);
            rs = callStmt.executeQuery();
            while (rs.next()) {
                Technology technology = new Technology();
                technology.setId(rs.getInt("id"));
                technology.setName(rs.getString("name"));
                technology.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
                technologies.add(technology);
            }
            return technologies;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi tìm kiếm công nghệ: " + e.getMessage());
            return technologies;
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng ResultSet: " + e.getMessage());
            }
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public TechnologyService.TechnologyPage getCandidateTechnologiesWithCount(Integer candidateId, int pageNumber, int pageSize) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        List<Technology> technologies = new ArrayList<>();
        int totalRecords = 0;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{CALL sp_GetCandidateTechnologies(?,?,?,?)}");
            callStmt.setInt(1, candidateId);
            callStmt.setInt(2, pageNumber);
            callStmt.setInt(3, pageSize);
            callStmt.registerOutParameter(4, Types.INTEGER);
            rs = callStmt.executeQuery();
            while (rs.next()) {
                Technology technology = new Technology();
                technology.setId(rs.getInt("id"));
                technology.setName(rs.getString("name"));
                technology.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
                technologies.add(technology);
            }
            totalRecords = callStmt.getInt(4);
            return new TechnologyService.TechnologyPage(technologies, totalRecords);
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách công nghệ của ứng viên: " + e.getMessage());
            return new TechnologyService.TechnologyPage(technologies, 0);
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng ResultSet: " + e.getMessage());
            }
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int addCandidateTechnology(Integer candidateId, Integer technologyId) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{CALL sp_AddCandidateTechnology(?,?,?)}");
            callStmt.setInt(1, candidateId);
            callStmt.setInt(2, technologyId);
            callStmt.registerOutParameter(3, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(3);
            if (returnCode == 0) {
                conn.commit();
            } else {
                conn.rollback();
            }
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm công nghệ cho ứng viên: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 1;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi khôi phục auto-commit: " + ex.getMessage());
            }
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int removeCandidateTechnology(Integer candidateId, Integer technologyId) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{CALL sp_RemoveCandidateTechnology(?,?,?)}");
            callStmt.setInt(1, candidateId);
            callStmt.setInt(2, technologyId);
            callStmt.registerOutParameter(3, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(3);
            if (returnCode == 0) {
                conn.commit();
            } else {
                conn.rollback();
            }
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi xóa công nghệ của ứng viên: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 1;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi khôi phục auto-commit: " + ex.getMessage());
            }
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public TechnologyService.TechnologyPage findActiveTechnologiesWithCount(int pageNumber, int pageSize) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        List<Technology> technologies = new ArrayList<>();
        int totalRecords = 0;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{CALL sp_GetActiveTechnologies(?,?,?)}");
            callStmt.setInt(1, pageNumber);
            callStmt.setInt(2, pageSize);
            callStmt.registerOutParameter(3, Types.INTEGER);
            rs = callStmt.executeQuery();
            while (rs.next()) {
                Technology technology = new Technology();
                technology.setId(rs.getInt("id"));
                technology.setName(rs.getString("name"));
                technology.setStatus(Status.valueOf(rs.getString("status").toUpperCase()));
                technologies.add(technology);
            }
            totalRecords = callStmt.getInt(3);
            return new TechnologyService.TechnologyPage(technologies, totalRecords);
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách công nghệ đang hoạt động: " + e.getMessage());
            return new TechnologyService.TechnologyPage(technologies, 0);
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng ResultSet: " + e.getMessage());
            }
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int getTotalTechnologiesCount() {
        Connection conn = null;
        CallableStatement callStmt = null;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{CALL sp_GetTotalTechnologiesCount(?)}");
            callStmt.registerOutParameter(1, Types.INTEGER);
            callStmt.execute();
            return callStmt.getInt(1);
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi đếm tổng số công nghệ: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int getTotalTechnologiesByName(String keyword) {
        Connection conn = null;
        CallableStatement callStmt = null;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{CALL sp_GetTotalTechnologiesByName(?,?)}");
            callStmt.setString(1, "%" + keyword + "%");
            callStmt.registerOutParameter(2, Types.INTEGER);
            callStmt.execute();
            return callStmt.getInt(2);
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi đếm tổng số công nghệ theo tên: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }
}