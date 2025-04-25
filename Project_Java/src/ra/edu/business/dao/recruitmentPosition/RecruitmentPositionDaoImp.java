package ra.edu.business.dao.recruitmentPosition;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.recruitmentPosition.RecruitmentPosition;
import ra.edu.business.model.recruitmentPosition.Status;
import ra.edu.business.model.technology.Technology;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecruitmentPositionDaoImp implements RecruitmentPositionDao {

    @Override
    public List<RecruitmentPosition> findAll(int pageNumber, int pageSize) {
        return getAllPositions(pageNumber, pageSize);
    }

    @Override
    public List<RecruitmentPosition> getAllPositions(int pageNumber, int pageSize) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        List<RecruitmentPosition> positions = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_FindAllRecruitmentPositions(?,?)}");
            callStmt.setInt(1, pageNumber);
            callStmt.setInt(2, pageSize);
            rs = callStmt.executeQuery();
            while (rs.next()) {
                RecruitmentPosition position = new RecruitmentPosition();
                position.setId(rs.getInt("id"));
                position.setName(rs.getString("name"));
                position.setDescription(rs.getString("description"));
                position.setMinSalary(rs.getDouble("minSalary"));
                position.setMaxSalary(rs.getDouble("maxSalary"));
                position.setMinExperience(rs.getInt("minExperience"));
                LocalDate createdDate = rs.getTimestamp("createdDate") != null ?
                        rs.getTimestamp("createdDate").toLocalDateTime().toLocalDate() : null;
                LocalDate expiredDate = rs.getTimestamp("expiredDate") != null ?
                        rs.getTimestamp("expiredDate").toLocalDateTime().toLocalDate() : null;
                position.setCreatedDate(createdDate);
                position.setExpiredDate(expiredDate);
                position.setStatus(Status.valueOf(rs.getString("status").toLowerCase()));
                // Lấy danh sách công nghệ cho vị trí
                position.setTechnologies(getPositionTechnologies(position.getId()));
                positions.add(position);
            }
            return positions;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách vị trí tuyển dụng: " + e.getMessage());
            return positions;
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
    public List<RecruitmentPosition> getActivePositions(int pageNumber, int pageSize) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        List<RecruitmentPosition> positions = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_GetActiveRecruitmentPositions(?,?)}");
            callStmt.setInt(1, pageNumber);
            callStmt.setInt(2, pageSize);
            rs = callStmt.executeQuery();
            while (rs.next()) {
                RecruitmentPosition position = new RecruitmentPosition();
                position.setId(rs.getInt("id"));
                position.setName(rs.getString("name"));
                position.setDescription(rs.getString("description"));
                position.setMinSalary(rs.getDouble("minSalary"));
                position.setMaxSalary(rs.getDouble("maxSalary"));
                position.setMinExperience(rs.getInt("minExperience"));
                LocalDate createdDate = rs.getTimestamp("createdDate") != null ?
                        rs.getTimestamp("createdDate").toLocalDateTime().toLocalDate() : null;
                LocalDate expiredDate = rs.getTimestamp("expiredDate") != null ?
                        rs.getTimestamp("expiredDate").toLocalDateTime().toLocalDate() : null;
                position.setCreatedDate(createdDate);
                position.setExpiredDate(expiredDate);
                position.setStatus(Status.valueOf(rs.getString("status")));
                // Lấy danh sách công nghệ cho vị trí
                position.setTechnologies(getPositionTechnologies(position.getId()));
                positions.add(position);
            }
            return positions;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách vị trí công việc: " + e.getMessage());
            return positions;
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
    public int save(RecruitmentPosition position) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        int newId = 0;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_CreateRecruitmentPosition(?,?,?,?,?,?,?,?)}");
            callStmt.setString(1, position.getName());
            callStmt.setString(2, position.getDescription());
            callStmt.setDouble(3, position.getMinSalary());
            callStmt.setDouble(4, position.getMaxSalary());
            callStmt.setInt(5, position.getMinExperience());
            if (position.getExpiredDate() != null) {
                callStmt.setTimestamp(6, Timestamp.valueOf(position.getExpiredDate().atStartOfDay()));
            } else {
                callStmt.setNull(6, Types.TIMESTAMP);
            }
            callStmt.registerOutParameter(7, Types.INTEGER); // p_returnCode
            callStmt.registerOutParameter(8, Types.INTEGER); // p_newId
            callStmt.execute();
            returnCode = callStmt.getInt(7);
            newId = callStmt.getInt(8);
            if (returnCode != 0) {
                conn.rollback();
                return returnCode;
            }

            if (newId == 0) {
                conn.rollback();
                return 1; // Lỗi không lấy được ID
            }

            // Lưu danh sách công nghệ vào bảng recruitment_position_technology
            for (Technology tech : position.getTechnologies()) {
                CallableStatement techStmt = conn.prepareCall("{call sp_AddPositionTechnology(?,?,?)}");
                techStmt.setInt(1, newId);
                techStmt.setInt(2, tech.getId());
                techStmt.registerOutParameter(3, Types.INTEGER);
                techStmt.execute();
                int techResult = techStmt.getInt(3);
                techStmt.close();
                if (techResult != 0) {
                    conn.rollback();
                    return 9;
                }
            }

            conn.commit();
            return 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi tạo vị trí tuyển dụng: " + e.getMessage());
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
            callStmt = conn.prepareCall("{call sp_UpdateRecruitmentPositionField(?,?,?,?)}");
            callStmt.setInt(1, id);
            callStmt.setString(2, fieldName);
            if (newValue instanceof LocalDate) {
                callStmt.setTimestamp(3, Timestamp.valueOf(((LocalDate) newValue).atStartOfDay()));
            } else {
                callStmt.setString(3, newValue.toString());
            }
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
            System.err.println("Lỗi SQL khi cập nhật trường vị trí tuyển dụng: " + e.getMessage());
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
    public int delete(Integer id) {
        return updateField(id, "status", Status.inactive);
    }

    @Override
    public boolean isPositionActive(int positionId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionDB.openConnection();
            stmt = conn.prepareStatement("SELECT status FROM recruitment_position WHERE id = ?");
            stmt.setInt(1, positionId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return "ACTIVE".equalsIgnoreCase(rs.getString("status"));
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi kiểm tra vị trí công việc: " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng ResultSet: " + e.getMessage());
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Lỗi khi đóng PreparedStatement: " + e.getMessage());
                }
            }
            ConnectionDB.closeConnection(conn, null);
        }
    }

    @Override
    public List<Technology> getPositionTechnologies(Integer positionId) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        List<Technology> technologies = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_GetPositionTechnologies(?)}");
            callStmt.setInt(1, positionId);
            rs = callStmt.executeQuery();
            while (rs.next()) {
                Technology technology = new Technology();
                technology.setId(rs.getInt("id"));
                technology.setName(rs.getString("name"));
                technology.setStatus(ra.edu.business.model.technology.Status.valueOf(rs.getString("status").toUpperCase()));
                technologies.add(technology);
            }
            return technologies;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách công nghệ của vị trí tuyển dụng: " + e.getMessage());
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
    public int addPositionTechnology(Integer positionId, Integer technologyId) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_AddPositionTechnology(?,?,?)}");
            callStmt.setInt(1, positionId);
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
            System.err.println("Lỗi SQL khi thêm công nghệ cho vị trí tuyển dụng: " + e.getMessage());
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
    public int removePositionTechnology(Integer positionId, Integer technologyId) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_RemovePositionTechnology(?,?,?)}");
            callStmt.setInt(1, positionId);
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
            System.err.println("Lỗi SQL khi xóa công nghệ khỏi vị trí tuyển dụng: " + e.getMessage());
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
    public int getTotalPositionsCount() {
        Connection conn = null;
        CallableStatement callStmt = null;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_GetTotalPositionsCount(?)}");
            callStmt.registerOutParameter(1, Types.INTEGER);
            callStmt.execute();
            return callStmt.getInt(1);
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi đếm tổng số vị trí tuyển dụng: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int getActivePositionsCount() {
        Connection conn = null;
        CallableStatement callStmt = null;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_GetActivePositionsCount(?)}");
            callStmt.registerOutParameter(1, Types.INTEGER);
            callStmt.execute();
            return callStmt.getInt(1);
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi đếm tổng số vị trí tuyển dụng active: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public List<RecruitmentPosition> getFilteredPositionsByTechnologies(List<Integer> technologyIds, int pageNumber, int pageSize) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        List<RecruitmentPosition> positions = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            // Tạo danh sách ID công nghệ dưới dạng chuỗi
            String techIds = String.join(",", technologyIds.stream().map(String::valueOf).toList());
            callStmt = conn.prepareCall("{call sp_FilterRecruitmentPositionsByTechnologies(?,?,?)}");
            callStmt.setString(1, techIds);
            callStmt.setInt(2, pageNumber);
            callStmt.setInt(3, pageSize);
            rs = callStmt.executeQuery();
            while (rs.next()) {
                RecruitmentPosition position = new RecruitmentPosition();
                position.setId(rs.getInt("id"));
                position.setName(rs.getString("name"));
                position.setDescription(rs.getString("description"));
                position.setMinSalary(rs.getDouble("minSalary"));
                position.setMaxSalary(rs.getDouble("maxSalary"));
                position.setMinExperience(rs.getInt("minExperience"));
                LocalDate createdDate = rs.getTimestamp("createdDate") != null ?
                        rs.getTimestamp("createdDate").toLocalDateTime().toLocalDate() : null;
                LocalDate expiredDate = rs.getTimestamp("expiredDate") != null ?
                        rs.getTimestamp("expiredDate").toLocalDateTime().toLocalDate() : null;
                position.setCreatedDate(createdDate);
                position.setExpiredDate(expiredDate);
                position.setStatus(Status.valueOf(rs.getString("status").toLowerCase()));
                position.setTechnologies(getPositionTechnologies(position.getId()));
                positions.add(position);
            }
            return positions;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lọc vị trí tuyển dụng theo công nghệ: " + e.getMessage());
            return positions;
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
    public int getFilteredPositionsCountByTechnologies(List<Integer> technologyIds) {
        Connection conn = null;
        CallableStatement callStmt = null;
        try {
            conn = ConnectionDB.openConnection();
            String techIds = String.join(",", technologyIds.stream().map(String::valueOf).toList());
            callStmt = conn.prepareCall("{call sp_GetFilteredPositionsCountByTechnologies(?,?)}");
            callStmt.setString(1, techIds);
            callStmt.registerOutParameter(2, Types.INTEGER);
            callStmt.execute();
            return callStmt.getInt(2);
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi đếm số vị trí tuyển dụng theo công nghệ: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }
}