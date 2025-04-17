package ra.edu.business.dao.candidateDao;

import ra.edu.business.config.ConnectionDB;
import ra.edu.business.model.Candidate;

import java.io.*;
import java.sql.*;

public class CandidateDaoImp implements CandidateDao {

    @Override
    public int findAll() {
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
    public int save() {
        System.err.println("Vui lòng sử dụng phương thức register để lưu ứng viên mới.");
        return 0;
    }

    @Override
    public int update() {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_UpdateCandidate(?,?,?,?,?,?,?,?,?,?,?,?)}");
            // Giả định nhập dữ liệu từ Scanner hoặc Candidate object
            Candidate candidate = new Candidate();
            candidate.inputData(new java.util.Scanner(System.in)); // Nhập từ console
            callStmt.setInt(1, candidate.getId());
            callStmt.setString(2, candidate.getName());
            callStmt.setString(3, candidate.getUsername());
            callStmt.setString(4, candidate.getEmail());
            callStmt.setString(5, candidate.getPassword());
            callStmt.setString(6, candidate.getPhone());
            callStmt.setInt(7, candidate.getExperience());
            callStmt.setString(8, candidate.getGender().name().toLowerCase());
            callStmt.setString(9, candidate.getStatus().name().toLowerCase());
            callStmt.setString(10, candidate.getDescription());
            if (candidate.getDob() != null) {
                callStmt.setDate(11, new java.sql.Date(candidate.getDob().getTime()));
            } else {
                callStmt.setNull(11, Types.DATE);
            }
            callStmt.setString(12, candidate.getRole().name().toLowerCase());
            callStmt.registerOutParameter(13, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(13);
            conn.commit();
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi cập nhật ứng viên: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int delete() {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_DeleteCandidate(?,?)}");
            System.out.println("Nhập ID ứng viên cần xóa:");
            int candidateId = Integer.parseInt(new java.util.Scanner(System.in).nextLine());
            callStmt.setInt(1, candidateId);
            callStmt.registerOutParameter(2, Types.INTEGER);
            callStmt.execute();
            returnCode = callStmt.getInt(2);
            conn.commit();
            return returnCode;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi xóa ứng viên: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
    }

    @Override
    public int register(Candidate candidate) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;

        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callStmt = conn.prepareCall("{call sp_Register(?,?,?,?,?,?,?,?,?,?,?,?)}");
            callStmt.setString(1, candidate.getName());
            callStmt.setString(2, candidate.getUsername());
            callStmt.setString(3, candidate.getEmail());
            callStmt.setString(4, candidate.getPassword());
            callStmt.setString(5, candidate.getPhone());
            callStmt.setInt(6, candidate.getExperience());
            callStmt.setString(7, candidate.getGender().name().toLowerCase());
            callStmt.setString(8, candidate.getStatus().name().toLowerCase());
            callStmt.setString(9, candidate.getDescription());
            if (candidate.getDob() != null) {
                callStmt.setDate(10, new java.sql.Date(candidate.getDob().getTime()));
            } else {
                callStmt.setNull(10, Types.DATE);
            }

            callStmt.setString(11, candidate.getRole().name().toLowerCase());

            callStmt.registerOutParameter(12, Types.INTEGER);

            callStmt.execute();
            returnCode = callStmt.getInt(12);
            conn.commit();
        } catch (SQLException e) {
            System.err.println("Có lỗi SQL khi đăng ký: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi khác khi đăng ký: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
        return returnCode;
    }

    @Override
    public int login(String username, String password) {
        Connection conn = null;
        CallableStatement callStmt = null;
        int returnCode = -1;

        try {
            conn = ConnectionDB.openConnection();
            callStmt = conn.prepareCall("{call sp_Login(?,?,?,?,?)}");
            callStmt.setString(1, username);
            callStmt.setString(2, password);
            callStmt.registerOutParameter(3, Types.INTEGER); // userId
            callStmt.registerOutParameter(4, Types.VARCHAR); // role
            callStmt.registerOutParameter(5, Types.INTEGER); // returnCode

            callStmt.execute();
            returnCode = callStmt.getInt(5);

            if (returnCode == 0) {
                int userId = callStmt.getInt(3);
                String role = callStmt.getString(4);
                writeLoginInfoToFile(userId, role); // Ghi ra file
            }

        } catch (SQLException e) {
            System.err.println("Có lỗi SQL khi đăng nhập: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi khác khi đăng nhập: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callStmt);
        }
        return returnCode;
    }

    @Override
    public void logout() {
        File file = new File("login_info.txt");
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Đăng xuất thành công. File thông tin đăng nhập đã được xóa.");
            } else {
                System.err.println("Không thể xóa file đăng nhập.");
            }
        } else {
            System.out.println("Không có file đăng nhập để xóa.");
        }
    }

    @Override
    public String isLoggedIn() {
        File file = new File("login_info.txt");
        if (file.exists() && file.length() > 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("role=")) {
                        return line.substring("role=".length());
                    }
                }
            } catch (IOException e) {
                System.err.println("Lỗi khi đọc file đăng nhập: " + e.getMessage());
            }
        }
        return null;
    }
    private void writeLoginInfoToFile(int userId, String role) {
        String filePath = "login_info.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("user_id=" + userId);
            writer.newLine();
            writer.write("role=" + role);
            writer.newLine();
            System.out.println("Đăng nhập thành công " + filePath);
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi file đăng nhập: " + e.getMessage());
        }
    }


}
