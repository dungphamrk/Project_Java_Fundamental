CREATE DATABASE IF NOT EXISTS personnel_recruitment;
USE personnel_recruitment;

-- Tạo bảng user
CREATE TABLE user
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    role     ENUM ('admin', 'candidate') DEFAULT 'candidate',
    status   ENUM ('active', 'inactive') DEFAULT 'active'
);

-- Tạo bảng candidate
CREATE TABLE candidate
(
    id          INT PRIMARY KEY,
    name        VARCHAR(100)        NOT NULL,
    email       VARCHAR(100) UNIQUE NOT NULL,
    phone       VARCHAR(20) UNIQUE,
    experience  INT,
    gender      ENUM ('male', 'female', 'other'),
    description TEXT,
    dob         DATE,
    FOREIGN KEY (id) REFERENCES user (id)
);

-- Tạo bảng technology
CREATE TABLE technology
(
    id     INT AUTO_INCREMENT PRIMARY KEY,
    name   VARCHAR(100) NOT NULL UNIQUE,
    status ENUM('active','inactive')
);

-- Tạo bảng recruitment_position
CREATE TABLE recruitment_position
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    description   TEXT,
    minSalary     DECIMAL(10, 2),
    maxSalary     DECIMAL(10, 2),
    minExperience INT,
    createdDate   DATETIME DEFAULT CURRENT_TIMESTAMP,
    expiredDate   DATETIME
);

-- Tạo bảng application
CREATE TABLE application
(
    id                     INT AUTO_INCREMENT PRIMARY KEY,
    candidateId            INT NOT NULL,
    recruitmentPositionId  INT NOT NULL,
    cvUrl                  VARCHAR(255),
    progress               ENUM ('pending', 'handling', 'interviewing', 'done') DEFAULT 'pending',
    interviewRequestDate   DATETIME,
    interviewRequestResult TEXT,
    interviewLink          VARCHAR(255),
    interviewTime          DATETIME,
    interviewResult        TEXT,
    interviewResultNote    TEXT,
    destroyAt              DATETIME,
    createAt               DATETIME                                             DEFAULT CURRENT_TIMESTAMP,
    updateAt               DATETIME                                             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    destroyReason          TEXT,
    FOREIGN KEY (candidateId) REFERENCES candidate (id),
    FOREIGN KEY (recruitmentPositionId) REFERENCES recruitment_position (id)
);

-- Tạo bảng candidate_technology
CREATE TABLE candidate_technology
(
    candidateId  INT,
    technologyId INT,
    PRIMARY KEY (candidateId, technologyId),
    FOREIGN KEY (candidateId) REFERENCES candidate (id),
    FOREIGN KEY (technologyId) REFERENCES technology (id)
);

-- Tạo bảng recruitment_position_technology
CREATE TABLE recruitment_position_technology
(
    recruitmentPositionId INT,
    technologyId          INT,
    PRIMARY KEY (recruitmentPositionId, technologyId),
    FOREIGN KEY (recruitmentPositionId) REFERENCES recruitment_position (id),
    FOREIGN KEY (technologyId) REFERENCES technology (id)
);

-- Khởi tạo admin
INSERT INTO user (id, username, password, role, status)
VALUES (1, 'admin', SHA2('admin', 256), 'admin', 'active');

-- Dữ liệu mẫu cho user và candidate
INSERT INTO user (username, password, role, status)
VALUES ('nguyenvanan', SHA2('password123', 256), 'candidate', 'active');

INSERT INTO candidate (id, name, email, phone, experience, gender, description, dob)
VALUES (LAST_INSERT_ID(),
        'Nguyễn Văn An',
        'nguyenvanan@example.com',
        '+84912345678',
        3,
        'male',
        'Ứng viên có kinh nghiệm phát triển web với Java và Spring Framework.',
        '1995-05-15');

DELIMITER $$

-- Stored Procedure: Kiểm tra tên công nghệ có tồn tại
CREATE PROCEDURE sp_CheckTechnologyNameExists(
    IN in_name VARCHAR(100),
    OUT is_exists BOOLEAN
)
BEGIN
SELECT EXISTS(
    SELECT 1 FROM technology WHERE name = in_name
) INTO is_exists;
END $$

-- Stored Procedure: Kiểm tra username có tồn tại
CREATE PROCEDURE sp_CheckUsernameExists(
    IN in_username VARCHAR(100),
    OUT is_exists BOOLEAN
)
BEGIN
SELECT EXISTS(
    SELECT 1 FROM user WHERE username = in_username
) INTO is_exists;
END $$

-- Stored Procedure: Kiểm tra email ứng viên có tồn tại
CREATE PROCEDURE sp_CheckCandidateEmailExists(
    IN in_email VARCHAR(100),
    OUT is_exists BOOLEAN
)
BEGIN
SELECT EXISTS(
    SELECT 1 FROM candidate WHERE email = in_email
) INTO is_exists;
END $$

-- Stored Procedure: Kiểm tra số điện thoại ứng viên có tồn tại
CREATE PROCEDURE sp_CheckCandidatePhoneExists(
    IN in_phone VARCHAR(20),
    OUT is_exists BOOLEAN
)
BEGIN
SELECT EXISTS(
    SELECT 1 FROM candidate WHERE phone = in_phone
) INTO is_exists;
END $$

-- Stored Procedure: Đăng ký người dùng
CREATE PROCEDURE sp_RegisterUser(
    IN p_username VARCHAR(100),
    IN p_password VARCHAR(255),
    OUT p_return_code INT,
    OUT p_userId INT
)
BEGIN
    DECLARE is_exists BOOLEAN;

CALL sp_CheckUsernameExists(p_username, is_exists);

IF is_exists THEN
        SET p_return_code = 1; -- Username đã tồn tại
ELSE
        INSERT INTO user (username, password, role, status)
        VALUES (p_username, SHA2(p_password, 256), 'candidate', 'active');

        SET p_return_code = 0; -- Thành công
        SET p_userId = LAST_INSERT_ID();
END IF;
END $$

-- Stored Procedure: Đăng ký ứng viên
CREATE PROCEDURE sp_RegisterCandidate(
    IN p_id INT,
    IN p_name VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_phone VARCHAR(20),
    IN p_experience INT,
    IN p_gender ENUM ('male', 'female', 'other'),
    IN p_description TEXT,
    IN p_dob DATE,
    OUT p_return_code INT
        )
BEGIN
    DECLARE email_exists BOOLEAN;
    DECLARE phone_exists BOOLEAN;

CALL sp_CheckCandidateEmailExists(p_email, email_exists);
CALL sp_CheckCandidatePhoneExists(p_phone, phone_exists);

IF email_exists THEN
        SET p_return_code = 1; -- Email đã tồn tại
    ELSEIF phone_exists THEN
        SET p_return_code = 2; -- Số điện thoại đã tồn tại
ELSE
        INSERT INTO candidate (id, name, email, phone, experience, gender, description, dob)
        VALUES (p_id, p_name, p_email, p_phone, p_experience, p_gender, p_description, p_dob);
        SET p_return_code = 0; -- Thành công
END IF;
END $$

-- Stored Procedure: Đăng nhập
CREATE PROCEDURE sp_Login(
    IN in_username VARCHAR(100),
    IN in_password VARCHAR(255),
    OUT out_user_id INT,
    OUT out_user_role ENUM ('admin', 'candidate'),
    OUT out_return_code INT
        )
BEGIN
    SET out_user_id = NULL;
    SET out_user_role = NULL;
    SET out_return_code = 1;

SELECT id, role
INTO out_user_id, out_user_role
FROM user
WHERE username = in_username
  AND password = SHA2(in_password, 256)
  AND status = 'active'
    LIMIT 1;

IF out_user_id IS NOT NULL THEN
        SET out_return_code = 0; -- Thành công
ELSE
        SET out_return_code = 2; -- Tài khoản không tồn tại hoặc bị khóa
END IF;
END $$


-- Stored Procedure: Cập nhật thông tin ứng viên
CREATE PROCEDURE sp_UpdateCandidate(
    IN in_id INT,
    IN in_name VARCHAR(100),
    IN in_email VARCHAR(100),
    IN in_phone VARCHAR(20),
    IN in_experience INT,
    IN in_gender ENUM ('male', 'female', 'other'),
    IN in_description TEXT,
    IN in_dob DATE,
    OUT out_return_code INT
        )
BEGIN
    SET out_return_code = 1; -- Mặc định lỗi
    IF EXISTS (SELECT 1 FROM candidate WHERE id = in_id) THEN
UPDATE candidate
SET name        = in_name,
    email       = in_email,
    phone       = in_phone,
    experience  = in_experience,
    gender      = in_gender,
    description = in_description,
    dob         = in_dob
WHERE id = in_id;
SET out_return_code = 0; -- Thành công
ELSE
        SET out_return_code = 2; -- Không tìm thấy ứng viên
END IF;
END $$

-- Stored Procedure: Xóa ứng viên (xóa mềm)
CREATE PROCEDURE sp_DeleteCandidate(
    IN in_id INT,
    OUT out_return_code INT
)
BEGIN
    SET out_return_code = 1; -- Mặc định lỗi
    IF EXISTS (SELECT 1 FROM user WHERE id = in_id AND role = 'candidate') THEN
UPDATE user
SET status = 'inactive'
WHERE id = in_id;
SET out_return_code = 0; -- Thành công
ELSE
        SET out_return_code = 2; -- Không tìm thấy ứng viên
END IF;
END $$

-- Stored Procedure: Lấy danh sách tất cả công nghệ
CREATE PROCEDURE sp_FindAllTechnologies(
    IN pageNumber INT,
    IN pageSize INT,
    OUT totalPages INT
)
BEGIN
    DECLARE offsetValue INT;
    DECLARE totalRecords INT;

    SET offsetValue = (pageNumber - 1) * pageSize;

SELECT COUNT(*) INTO totalRecords
FROM technology
WHERE status = 'active';

SET totalPages = CEIL(totalRecords / pageSize);

SELECT id, name, status
FROM technology
WHERE status = 'active'
    LIMIT pageSize
OFFSET offsetValue;
END $$

-- Stored Procedure: Tạo công nghệ mới
CREATE PROCEDURE sp_CreateTechnology(
    IN p_name VARCHAR(100),
    IN p_status ENUM('active', 'inactive'),
    OUT p_return_code INT
        )
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_return_code = 0; -- Failure
ROLLBACK;
END;

START TRANSACTION;

IF EXISTS (SELECT 1 FROM technology WHERE name = p_name) THEN
        SET p_return_code = 0; -- Name already exists
ROLLBACK;
ELSE
        INSERT INTO technology (name, status)
        VALUES (p_name, p_status);
        SET p_return_code = 1; -- Success
COMMIT;
END IF;
END $$

-- Stored Procedure: Cập nhật công nghệ
CREATE PROCEDURE sp_UpdateTechnology(
    IN p_id INT,
    IN p_name VARCHAR(100),
    IN p_status ENUM('active', 'inactive'),
    OUT p_return_code INT
        )
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_return_code = 0; -- Failure
ROLLBACK;
END;

START TRANSACTION;

IF NOT EXISTS (SELECT 1 FROM technology WHERE id = p_id) THEN
        SET p_return_code = 0; -- Technology not found
ROLLBACK;
ELSEIF EXISTS (SELECT 1 FROM technology WHERE name = p_name AND id != p_id) THEN
        SET p_return_code = 0; -- Name already exists
ROLLBACK;
ELSE
UPDATE technology
SET name = p_name, status = p_status
WHERE id = p_id;
SET p_return_code = 1; -- Success
COMMIT;
END IF;
END $$

-- Stored Procedure: Xóa công nghệ (xóa mềm)
CREATE PROCEDURE sp_DeleteTechnology(
    IN p_id INT,
    OUT p_return_code INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_return_code = 0; -- Failure
ROLLBACK;
END;

START TRANSACTION;

IF NOT EXISTS (SELECT 1 FROM technology WHERE id = p_id) THEN
        SET p_return_code = 0; -- Technology not found
ROLLBACK;
ELSE
UPDATE technology
SET status = 'inactive'
WHERE id = p_id;
SET p_return_code = 1; -- Success
COMMIT;
END IF;
END $$

-- Stored Procedure: Tìm kiếm công nghệ theo tên
CREATE PROCEDURE sp_SearchTechnologies(
    IN p_keyword VARCHAR(255),
    IN p_pageNumber INT,
    IN p_pageSize INT
)
BEGIN
    DECLARE offsetValue INT;
    DECLARE totalRecords INT;
    DECLARE totalPages INT;

    SET offsetValue = (p_pageNumber - 1) * p_pageSize;

    -- Tính tổng số bản ghi
SELECT COUNT(*) INTO totalRecords
FROM technology
WHERE status = 'active' AND name LIKE p_keyword;

-- Tính tổng số trang
SET totalPages = CEIL(totalRecords / p_pageSize);

    -- Trả về totalPages dưới dạng ResultSet đầu tiên
SELECT totalPages AS totalPages;

-- Trả về danh sách công nghệ dưới dạng ResultSet thứ hai
SELECT id, name, status
FROM technology
WHERE status = 'active' AND name LIKE p_keyword
    LIMIT p_pageSize
OFFSET offsetValue;
END $$

-- Stored Procedure: Tìm kiếm công nghệ theo ID
CREATE PROCEDURE sp_FindTechnologyById(
    IN p_id INT
)
BEGIN
    DECLARE found INT;
SELECT COUNT(*) INTO found
FROM technology
WHERE id = p_id AND status = 'active';

SELECT found AS found;

IF found > 0 THEN
SELECT id, name, status
FROM technology
WHERE id = p_id AND status = 'active';
END IF;
END $$

CREATE PROCEDURE sp_UpdatePassword(
    IN p_user_id INT,
    IN p_old_password VARCHAR(255),
    IN p_new_password VARCHAR(255),
    OUT p_return_code INT
)
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM user
        WHERE id = p_user_id
          AND password = SHA2(p_old_password, 256)
          AND status = 'active'
    ) THEN
        SET p_return_code = 2; -- Mật khẩu cũ không đúng hoặc tài khoản không tồn tại
ELSE
UPDATE user
SET password = SHA2(p_new_password, 256)
WHERE id = p_user_id;
SET p_return_code = 1; -- Success
END IF;
END $$

-- Stored Procedure: Lấy danh sách tất cả ứng viên (hỗ trợ phân trang)
CREATE PROCEDURE sp_FindAllCandidates(
    IN p_pageNumber INT,
    IN p_pageSize INT
)
BEGIN
    DECLARE offsetValue INT;
    SET offsetValue = (p_pageNumber - 1) * p_pageSize;

SELECT c.id, c.name, c.email, u.role, u.status AS user_status
FROM candidate c
         JOIN user u ON c.id = u.id
WHERE u.status = 'active'
ORDER BY c.id
    LIMIT p_pageSize OFFSET offsetValue;
END $$

-- Stored Procedure: Khóa/mở khóa tài khoản ứng viên
CREATE PROCEDURE sp_LockUnlockCandidate(
    IN p_candidateId INT,
    IN p_lockStatus ENUM('active', 'inactive'),
    OUT p_return_code INT
        )
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_return_code = 1; -- Lỗi
ROLLBACK;
END;

START TRANSACTION;

IF EXISTS (SELECT 1 FROM user WHERE id = p_candidateId AND role = 'candidate') THEN
UPDATE user
SET status = p_lockStatus
WHERE id = p_candidateId;
SET p_return_code = 0; -- Thành công
COMMIT;
ELSE
        SET p_return_code = 2; -- Không tìm thấy ứng viên
ROLLBACK;
END IF;
END $$

-- Stored Procedure: Tìm kiếm ứng viên theo tên
CREATE PROCEDURE sp_SearchCandidateByName(
    IN p_name VARCHAR(100)
)
BEGIN
SELECT c.id, c.name, c.email, u.role
FROM candidate c
         JOIN user u ON c.id = u.id
WHERE c.name LIKE p_name AND u.status = 'active';
END $$

-- Stored Procedure: Lọc ứng viên theo kinh nghiệm
CREATE PROCEDURE sp_FilterCandidateByExperience(
    IN p_experience INT
)
BEGIN
SELECT c.id, c.name, c.email, c.experience
FROM candidate c
         JOIN user u ON c.id = u.id
WHERE c.experience >= p_experience AND u.status = 'active';
END $$

-- Stored Procedure: Lọc ứng viên theo tuổi
CREATE PROCEDURE sp_FilterCandidateByAge(
    IN p_age INT
)
BEGIN
SELECT c.id, c.name, c.email, c.dob
FROM candidate c
         JOIN user u ON c.id = u.id
WHERE TIMESTAMPDIFF(YEAR, c.dob, CURDATE()) >= p_age AND u.status = 'active';
END $$

-- Stored Procedure: Lọc ứng viên theo giới tính
CREATE PROCEDURE sp_FilterCandidateByGender(
    IN p_gender ENUM('male', 'female', 'other')
)
BEGIN
SELECT c.id, c.name, c.email, c.gender
FROM candidate c
         JOIN user u ON c.id = u.id
WHERE c.gender = p_gender AND u.status = 'active';
END $$

-- Stored Procedure: Lọc ứng viên theo công nghệ
CREATE PROCEDURE sp_FilterCandidateByTechnology(
    IN p_technology VARCHAR(100)
)
BEGIN
SELECT c.id, c.name, c.email, t.name AS technology_name
FROM candidate c
         JOIN user u ON c.id = u.id
         JOIN candidate_technology ct ON c.id = ct.candidateId
         JOIN technology t ON ct.technologyId = t.id
WHERE t.name LIKE p_technology AND u.status = 'active' AND t.status = 'active';
END $$

DELIMITER $$

-- Stored Procedure: Lấy danh sách vị trí tuyển dụng
CREATE PROCEDURE sp_FindAllRecruitmentPositions(
    IN p_pageNumber INT,
    IN p_pageSize INT
)
BEGIN
    DECLARE offsetValue INT;
    SET offsetValue = (p_pageNumber - 1) * p_pageSize;

SELECT id, name, description, minSalary, maxSalary, minExperience, createdDate, expiredDate
FROM recruitment_position
ORDER BY id
    LIMIT p_pageSize OFFSET offsetValue;
END $$

-- Stored Procedure: Tạo vị trí tuyển dụng mới
CREATE PROCEDURE sp_CreateRecruitmentPosition(
    IN p_name VARCHAR(100),
    IN p_description TEXT,
    IN p_minSalary DECIMAL(10,2),
    IN p_maxSalary DECIMAL(10,2),
    IN p_minExperience INT,
    IN p_expiredDate DATETIME,
    OUT p_return_code INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_return_code = 1; -- Lỗi
ROLLBACK;
END;

START TRANSACTION;

INSERT INTO recruitment_position (name, description, minSalary, maxSalary, minExperience, expiredDate)
VALUES (p_name, p_description, p_minSalary, p_maxSalary, p_minExperience, p_expiredDate);

SET p_return_code = 0; -- Thành công
COMMIT;
END $$

-- Stored Procedure: Cập nhật vị trí tuyển dụng
CREATE PROCEDURE sp_UpdateRecruitmentPosition(
    IN p_id INT,
    IN p_name VARCHAR(100),
    IN p_description TEXT,
    IN p_minSalary DECIMAL(10,2),
    IN p_maxSalary DECIMAL(10,2),
    IN p_minExperience INT,
    IN p_expiredDate DATETIME,
    OUT p_return_code INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_return_code = 1; -- Lỗi
ROLLBACK;
END;

START TRANSACTION;

IF EXISTS (SELECT 1 FROM recruitment_position WHERE id = p_id) THEN
UPDATE recruitment_position
SET name = p_name,
    description = p_description,
    minSalary = p_minSalary,
    maxSalary = p_maxSalary,
    minExperience = p_minExperience,
    expiredDate = p_expiredDate
WHERE id = p_id;
SET p_return_code = 0; -- Thành công
COMMIT;
ELSE
        SET p_return_code = 2; -- Không tìm thấy vị trí
ROLLBACK;
END IF;
END $$

-- Stored Procedure: Xóa vị trí tuyển dụng
CREATE PROCEDURE sp_DeleteRecruitmentPosition(
    IN p_id INT,
    OUT p_return_code INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_return_code = 1; -- Lỗi
ROLLBACK;
END;

START TRANSACTION;

IF EXISTS (SELECT 1 FROM recruitment_position WHERE id = p_id) THEN
DELETE FROM recruitment_position WHERE id = p_id;
SET p_return_code = 0; -- Thành công
COMMIT;
ELSE
        SET p_return_code = 2; -- Không tìm thấy vị trí
ROLLBACK;
END IF;
END $$


DELIMITER ;