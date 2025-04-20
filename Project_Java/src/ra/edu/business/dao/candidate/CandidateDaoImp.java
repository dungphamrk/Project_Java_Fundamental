package ra.edu.business.dao.candidate;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.candidate.Candidate;
import ra.edu.presentation.ServiceProvider;

import java.sql.*;
import java.util.Scanner;

public class CandidateDaoImp implements CandidateDao {

    @Override
    public int findAll(int pageNumber, int pageSize) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int count = 0;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_FindAllCandidates()}");

            ResultSet rs = callStmt.executeQuery();
            System.out.println("Danh sách ứng viên:");
            System.out.println("--------------------------------------------------");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Tên: " + rs.getString("name") +
                        ", Email: " + rs.getString("email") +
                        ", Vai trò: " + rs.getString("role"));
                count++;
            }
            if (count == 0) {
                System.out.println("Không có ứng viên nào.");
            }
            return count;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách ứng viên: " + e.getMessage());
            return 0;
        } finally {
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
            conn.setAutoCommit(false); // Tắt auto-commit
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
                conn.commit(); // Commit nếu thành công
            } else {
                conn.rollback(); // Rollback nếu thất bại
            }
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi đăng ký: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback nếu có lỗi
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 0;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Khôi phục auto-commit
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi khôi phục auto-commit: " + ex.getMessage());
            }
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int update(Scanner scanner) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false); // Tắt auto-commit
            System.out.println("Nhập thông tin mới cho hồ sơ:");
            callStmt = conn.prepareCall("{call sp_UpdateCandidate(?,?,?,?,?,?,?,?,?)}");
            Candidate candidate = new Candidate();
            candidate.setId(ServiceProvider.userService.getCurrentUserId()); // Lấy ID ứng viên hiện tại
            candidate.inputData(scanner);
            callStmt.setInt(1, candidate.getId());
            callStmt.setString(2, candidate.getName());
            callStmt.setString(3, candidate.getEmail());
            callStmt.setString(4, candidate.getPhone());
            callStmt.setInt(5, candidate.getExperience());
            callStmt.setString(6, candidate.getGender().name().toLowerCase());
            callStmt.setString(7, candidate.getDescription());
            if (candidate.getDob() != null) {
                callStmt.setDate(8, java.sql.Date.valueOf(candidate.getDob()));
            } else {
                callStmt.setNull(8, Types.DATE);
            }
            callStmt.registerOutParameter(9, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(9);
            if (returnCode == 0) {
                conn.commit(); // Commit nếu thành công
            } else {
                conn.rollback(); // Rollback nếu thất bại
            }
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi cập nhật ứng viên: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback nếu có lỗi
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi rollback: " + ex.getMessage());
            }
            return 0;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Khôi phục auto-commit
                }
            } catch (SQLException ex) {
                System.err.println("Lỗi khi khôi phục auto-commit: " + ex.getMessage());
            }
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int delete(Scanner scanner) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false); // Tắt auto-commit
            callStmt = conn.prepareCall("{call sp_DeleteCandidate(?,?)}");
            System.out.println("Nhập ID ứng viên cần xóa:");
            int candidateId = Integer.parseInt(scanner.nextLine());
            callStmt.setInt(1, candidateId);
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
    public int changePassword(int userId, String oldPassword, String newPassword) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false); // Tắt auto-commit
            callStmt = conn.prepareCall("{call sp_UpdatePassword(?,?,?,?)}");
            callStmt.setInt(1, userId);
            callStmt.setString(2, oldPassword);
            callStmt.setString(3, newPassword);
            callStmt.registerOutParameter(4, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(4);
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
}