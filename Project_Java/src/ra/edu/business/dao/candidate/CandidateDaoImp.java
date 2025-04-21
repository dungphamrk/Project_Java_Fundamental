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
            callStmt = conn.prepareCall("{call sp_FindAllCandidates(?,?)}");
            callStmt.setInt(1, pageNumber);
            callStmt.setInt(2, pageSize);
            ResultSet rs = callStmt.executeQuery();
            System.out.println("Danh sách ứng viên:");
            System.out.println("--------------------------------------------------");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Tên: " + rs.getString("name") +
                        ", Email: " + rs.getString("email") +
                        ", Vai trò: " + rs.getString("role") +
                        ", Trạng thái: " + rs.getString("user_status"));
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
    public int update(Scanner scanner) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            System.out.println("Nhập thông tin mới cho hồ sơ:");
            callStmt = conn.prepareCall("{call sp_UpdateCandidate(?,?,?,?,?,?,?,?,?)}");
            Candidate candidate = new Candidate();
            candidate.setId(ServiceProvider.userService.getCurrentUserId());
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
                conn.commit();
            } else {
                conn.rollback();
            }
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi cập nhật ứng viên: " + e.getMessage());
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
    public int delete(Scanner scanner) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
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
            conn.setAutoCommit(false);
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

    @Override
    public boolean lockUnlockAccount(int candidateId, boolean lockStatus) {
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
                System.out.println("Cập nhật trạng thái tài khoản thành công.");
                return true;
            } else {
                conn.rollback();
                System.out.println("Cập nhật trạng thái tài khoản thất bại.");
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
    public boolean searchByName(String name) {
        Connection conn = null;
        CallableStatement callStmt = null;
        boolean found = false;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_SearchCandidateByName(?)}");
            callStmt.setString(1, "%" + name + "%");
            ResultSet rs = callStmt.executeQuery();
            System.out.println("Kết quả tìm kiếm ứng viên theo tên:");
            System.out.println("--------------------------------------------------");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Tên: " + rs.getString("name") +
                        ", Email: " + rs.getString("email") +
                        ", Vai trò: " + rs.getString("role"));
                found = true;
            }
            return found;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi tìm kiếm ứng viên: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public boolean filterByExperience(int experience) {
        Connection conn = null;
        CallableStatement callStmt = null;
        boolean found = false;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_FilterCandidateByExperience(?)}");
            callStmt.setInt(1, experience);
            ResultSet rs = callStmt.executeQuery();
            System.out.println("Danh sách ứng viên có kinh nghiệm từ " + experience + " năm trở lên:");
            System.out.println("--------------------------------------------------");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Tên: " + rs.getString("name") +
                        ", Email: " + rs.getString("email") +
                        ", Kinh nghiệm: " + rs.getInt("experience") + " năm");
                found = true;
            }
            return found;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lọc ứng viên theo kinh nghiệm: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public boolean filterByAge(int age) {
        Connection conn = null;
        CallableStatement callStmt = null;
        boolean found = false;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_FilterCandidateByAge(?)}");
            callStmt.setInt(1, age);
            ResultSet rs = callStmt.executeQuery();
            System.out.println("Danh sách ứng viên từ " + age + " tuổi trở lên:");
            System.out.println("--------------------------------------------------");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Tên: " + rs.getString("name") +
                        ", Email: " + rs.getString("email") +
                        ", Ngày sinh: " + rs.getDate("dob"));
                found = true;
            }
            return found;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lọc ứng viên theo tuổi: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public boolean filterByGender(String gender) {
        Connection conn = null;
        CallableStatement callStmt = null;
        boolean found = false;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_FilterCandidateByGender(?)}");
            callStmt.setString(1, gender.toLowerCase());
            ResultSet rs = callStmt.executeQuery();
            System.out.println("Danh sách ứng viên có giới tính " + gender + ":");
            System.out.println("--------------------------------------------------");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Tên: " + rs.getString("name") +
                        ", Email: " + rs.getString("email") +
                        ", Giới tính: " + rs.getString("gender"));
                found = true;
            }
            return found;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lọc ứng viên theo giới tính: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public boolean filterByTechnology(String technology) {
        Connection conn = null;
        CallableStatement callStmt = null;
        boolean found = false;
        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_FilterCandidateByTechnology(?)}");
            callStmt.setString(1, "%" + technology + "%");
            ResultSet rs = callStmt.executeQuery();
            System.out.println("Danh sách ứng viên sử dụng công nghệ " + technology + ":");
            System.out.println("--------------------------------------------------");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Tên: " + rs.getString("name") +
                        ", Email: " + rs.getString("email") +
                        ", Công nghệ: " + rs.getString("technology_name"));
                found = true;
            }
            return found;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lọc ứng viên theo công nghệ: " + e.getMessage());
            return false;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }
}