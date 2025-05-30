package ra.edu.business.dao.candidate;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.candidate.Candidate;
import ra.edu.business.model.candidate.Gender;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CandidateDaoImp implements CandidateDao {

    @Override
    public List<Candidate> findAll(int pageNumber, int pageSize) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        List<Candidate> candidates = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_FindAllCandidates(?,?)}");
            callStmt.setInt(1, pageNumber);
            callStmt.setInt(2, pageSize);
            rs = callStmt.executeQuery();
            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
                candidate.setExperience(rs.getInt("experience"));
                candidate.setGender(Gender.valueOf(rs.getString("gender").toUpperCase()));
                candidates.add(candidate);
            }
            return candidates;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách ứng viên: " + e.getMessage());
            return candidates;
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
    public int save(Candidate newCandidate) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_RegisterCandidate(?,?,?,?,?,?,?,?,?)}");
            callStmt.setInt(1, newCandidate.getId());
            callStmt.setString(2, newCandidate.getName());
            callStmt.setString(3, newCandidate.getEmail());
            callStmt.setString(4, newCandidate.getPhone());
            callStmt.setInt(5, newCandidate.getExperience());
            callStmt.setString(6, newCandidate.getGender().name().toLowerCase());
            callStmt.setString(7, newCandidate.getDescription());
            if (newCandidate.getDob() != null) {
                callStmt.setDate(8, java.sql.Date.valueOf(newCandidate.getDob()));
            } else {
                callStmt.setNull(8, Types.DATE);
            }
            callStmt.registerOutParameter(9, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(9);
            if (returnCode == 0) {
                conn.commit();
            } else {
                conn.rollback();
            }
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi đăng ký: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 0;
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
            callStmt = conn.prepareCall("{call sp_UpdateCandidateField(?,?,?,?)}");
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
            System.err.println("Lỗi SQL khi cập nhật trường ứng viên: " + e.getMessage());
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
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_DeleteCandidate(?,?)}");
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
            System.err.println("Lỗi SQL khi xóa ứng viên: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 0;
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
    public int changePassword(Integer userId, String oldPassword, String newPassword, String phone) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_UpdatePassword(?,?,?,?,?)}");
            callStmt.setInt(1, userId);
            callStmt.setString(2, oldPassword);
            callStmt.setString(3, newPassword);
            callStmt.setString(4, phone);
            callStmt.registerOutParameter(5, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(5);
            if (returnCode == 1) {
                conn.commit();
            } else {
                conn.rollback();
            }
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi đổi mật khẩu: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 0;
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
    public int resetPassword(int userId, String newPassword) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_ResetPassword(?,?,?)}");
            callStmt.setInt(1, userId);
            callStmt.setString(2, newPassword);
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
            System.err.println("Lỗi SQL khi reset mật khẩu: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 0;
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
    public boolean lockUnlockAccount(int candidateId) {
        Connection conn = null;
        CallableStatement callStmt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_LockUnlockCandidate(?,?)}");
            callStmt.setInt(1, candidateId);
            callStmt.registerOutParameter(2, Types.INTEGER);
            callStmt.execute();
            int returnCode = callStmt.getInt(2);
            if (returnCode == 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi khóa/mở khóa tài khoản: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return false;
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
    public List<Candidate> searchByName(String name, int pageNumber, int pageSize) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        List<Candidate> candidates = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_SearchCandidateByName(?,?,?)}");
            callStmt.setString(1, "%" + name + "%");
            callStmt.setInt(2, pageNumber);
            callStmt.setInt(3, pageSize);
            rs = callStmt.executeQuery();
            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
                candidate.setExperience(rs.getInt("experience"));
                candidate.setGender(Gender.valueOf(rs.getString("gender").toUpperCase()));
                candidates.add(candidate);
            }
            return candidates;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi tìm kiếm ứng viên: " + e.getMessage());
            return candidates;
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
    public List<Candidate> filterByExperience(int experience, int pageNumber, int pageSize) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        List<Candidate> candidates = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_FilterCandidateByExperience(?,?,?)}");
            callStmt.setInt(1, experience);
            callStmt.setInt(2, pageNumber);
            callStmt.setInt(3, pageSize);
            rs = callStmt.executeQuery();
            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
                candidate.setExperience(rs.getInt("experience"));
                candidate.setGender(Gender.valueOf(rs.getString("gender").toUpperCase()));
                candidates.add(candidate);
            }
            return candidates;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lọc ứng viên theo kinh nghiệm: " + e.getMessage());
            return candidates;
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
    public List<Candidate> filterByAge(int age, int pageNumber, int pageSize) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        List<Candidate> candidates = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_FilterCandidateByAge(?,?,?)}");
            callStmt.setInt(1, age);
            callStmt.setInt(2, pageNumber);
            callStmt.setInt(3, pageSize);
            rs = callStmt.executeQuery();
            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
                candidate.setDob(rs.getDate("dob") != null ? rs.getDate("dob").toLocalDate() : null);
                candidate.setGender(Gender.valueOf(rs.getString("gender").toUpperCase()));
                candidates.add(candidate);
            }
            return candidates;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lọc ứng viên theo tuổi: " + e.getMessage());
            return candidates;
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
    public List<Candidate> filterByGender(String gender, int pageNumber, int pageSize) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        List<Candidate> candidates = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_FilterCandidateByGender(?,?,?)}");
            callStmt.setString(1, gender.toLowerCase());
            callStmt.setInt(2, pageNumber);
            callStmt.setInt(3, pageSize);
            rs = callStmt.executeQuery();
            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
                candidate.setExperience(rs.getInt("experience"));
                candidate.setGender(Gender.valueOf(rs.getString("gender").toUpperCase()));
                candidates.add(candidate);
            }
            return candidates;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lọc ứng viên theo giới tính: " + e.getMessage());
            return candidates;
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi giá trị enum gender: " + e.getMessage());
            return candidates;
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
    public List<Candidate> filterByTechnology(String technology, int pageNumber, int pageSize) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        List<Candidate> candidates = new ArrayList<>();
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_FilterCandidateByTechnology(?,?,?)}");
            callStmt.setString(1, "%" + technology + "%");
            callStmt.setInt(2, pageNumber);
            callStmt.setInt(3, pageSize);
            rs = callStmt.executeQuery();
            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
                candidate.setExperience(rs.getInt("experience"));
                candidate.setGender(Gender.valueOf(rs.getString("gender").toUpperCase()));
                candidates.add(candidate);
            }
            return candidates;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lọc ứng viên theo công nghệ: " + e.getMessage());
            return candidates;
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
    public Candidate getCandidateById(int userId) {
        Connection conn = null;
        CallableStatement callStmt = null;
        ResultSet rs = null;
        Candidate candidate = null;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_GetCandidateById(?)}");
            callStmt.setInt(1, userId);
            rs = callStmt.executeQuery();
            if (rs.next()) {
                candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setName(rs.getString("name"));
                candidate.setEmail(rs.getString("email"));
                candidate.setPhone(rs.getString("phone"));
                candidate.setExperience(rs.getInt("experience"));
                candidate.setGender(Gender.valueOf(rs.getString("gender").toUpperCase()));
                candidate.setDescription(rs.getString("description"));
                Date dob = rs.getDate("dob");
                if (dob != null) {
                    candidate.setDob(dob.toLocalDate());
                }
            }
            return candidate;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy thông tin ứng viên: " + e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi giá trị enum gender: " + e.getMessage());
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
    public int getTotalCandidatesCount() {
        Connection conn = null;
        CallableStatement callStmt = null;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_GetTotalCandidatesCount(?)}");
            callStmt.registerOutParameter(1, Types.INTEGER);
            callStmt.execute();
            return callStmt.getInt(1);
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi đếm tổng số ứng viên: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int getTotalCandidatesByName(String name) {
        Connection conn = null;
        CallableStatement callStmt = null;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_GetTotalCandidatesByName(?,?)}");
            callStmt.setString(1, "%" + name + "%");
            callStmt.registerOutParameter(2, Types.INTEGER);
            callStmt.execute();
            return callStmt.getInt(2);
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi đếm tổng số ứng viên theo tên: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int getTotalCandidatesByExperience(int experience) {
        Connection conn = null;
        CallableStatement callStmt = null;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_GetTotalCandidatesByExperience(?,?)}");
            callStmt.setInt(1, experience);
            callStmt.registerOutParameter(2, Types.INTEGER);
            callStmt.execute();
            return callStmt.getInt(2);
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi đếm tổng số ứng viên theo kinh nghiệm: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int getTotalCandidatesByAge(int age) {
        Connection conn = null;
        CallableStatement callStmt = null;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_GetTotalCandidatesByAge(?,?)}");
            callStmt.setInt(1, age);
            callStmt.registerOutParameter(2, Types.INTEGER);
            callStmt.execute();
            return callStmt.getInt(2);
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi đếm tổng số ứng viên theo tuổi: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int getTotalCandidatesByGender(String gender) {
        Connection conn = null;
        CallableStatement callStmt = null;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_GetTotalCandidatesByGender(?,?)}");
            callStmt.setString(1, gender.toLowerCase());
            callStmt.registerOutParameter(2, Types.INTEGER);
            callStmt.execute();
            return callStmt.getInt(2);
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi đếm tổng số ứng viên theo giới tính: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int getTotalCandidatesByTechnology(String technology) {
        Connection conn = null;
        CallableStatement callStmt = null;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_GetTotalCandidatesByTechnology(?,?)}");
            callStmt.setString(1, "%" + technology + "%");
            callStmt.registerOutParameter(2, Types.INTEGER);
            callStmt.execute();
            return callStmt.getInt(2);
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi đếm tổng số ứng viên theo công nghệ: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }
}