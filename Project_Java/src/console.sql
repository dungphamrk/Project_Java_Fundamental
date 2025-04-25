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
    status ENUM ('active','inactive')
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
    expiredDate   DATETIME,
    status        ENUM ('active','inactive')
);

-- Tạo bảng application
CREATE TABLE application
(
    id                     INT AUTO_INCREMENT PRIMARY KEY,
    candidateId            INT NOT NULL,
    recruitmentPositionId  INT NOT NULL,
    cvUrl                  VARCHAR(255),
    progress               ENUM ('pending', 'handling', 'interviewing', 'done','reject','cancel') DEFAULT 'pending',
    interviewRequestDate   DATETIME,
    interviewRequestResult TEXT,
    interviewLink          VARCHAR(255),
    interviewTime          DATETIME,
    interviewResult        TEXT,
    interviewResultNote    TEXT,
    destroyAt              DATETIME,
    createAt               DATETIME                                                               DEFAULT CURRENT_TIMESTAMP,
    updateAt               DATETIME                                                               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
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

-- Candidate 2
INSERT INTO user (username, password, role, status)
VALUES ('lethithao', SHA2('password456', 256), 'candidate', 'active');
INSERT INTO candidate (id, name, email, phone, experience, gender, description, dob)
VALUES (LAST_INSERT_ID(),
        'Lê Thị Thảo',
        'lethithao@example.com',
        '+84923456789',
        2,
        'female',
        'Front-end developer với kỹ năng Vue.js và TailwindCSS.',
        '1998-03-20');

-- Candidate 3
INSERT INTO user (username, password, role, status)
VALUES ('tranminhduc', SHA2('password789', 256), 'candidate', 'inactive');
INSERT INTO candidate (id, name, email, phone, experience, gender, description, dob)
VALUES (LAST_INSERT_ID(),
        'Trần Minh Đức',
        'tranminhduc@example.com',
        '+84987654321',
        5,
        'male',
        'Back-end developer chuyên về Node.js và MongoDB.',
        '1990-09-01');

-- Candidate 4
INSERT INTO user (username, password, role, status)
VALUES ('phamthithu', SHA2('pass123', 256), 'candidate', 'active');
INSERT INTO candidate (id, name, email, phone, experience, gender, description, dob)
VALUES (LAST_INSERT_ID(),
        'Phạm Thị Thu',
        'phamthithu@example.com',
        '+84911223344',
        4,
        'female',
        'Full-stack developer với kinh nghiệm React và Java.',
        '1993-12-12');

INSERT INTO technology (name, status)
VALUES ('Java', 'active'),
       ('Spring Boot', 'active'),
       ('React', 'active'),
       ('Vue.js', 'active'),
       ('Node.js', 'active'),
       ('TailwindCSS', 'active'),
       ('MongoDB', 'inactive');

INSERT INTO recruitment_position (name, description, minSalary, maxSalary, minExperience, expiredDate, status)
VALUES ('Java Developer', 'Phát triển ứng dụng doanh nghiệp sử dụng Java và Spring Boot.', 1000.00, 2000.00, 2,
        '2025-06-01', 'active'),
       ('Front-end Developer', 'Phát triển giao diện người dùng với React hoặc Vue.js.', 800.00, 1500.00, 1,
        '2025-05-15', 'active'),
       ('Full-stack Developer', 'Làm việc trên cả front-end và back-end.', 1200.00, 2500.00, 3, '2025-06-30', 'active');


-- Nguyễn Văn An
INSERT INTO candidate_technology (candidateId, technologyId)
VALUES (5, 1),
       (5, 2);
-- Java, Spring Boot

-- Lê Thị Thảo
INSERT INTO candidate_technology (candidateId, technologyId)
VALUES (2, 4),
       (2, 6);
-- Vue.js, TailwindCSS

-- Trần Minh Đức
INSERT INTO candidate_technology (candidateId, technologyId)
VALUES (3, 5),
       (3, 7);
-- Node.js, MongoDB

-- Phạm Thị Thu
INSERT INTO candidate_technology (candidateId, technologyId)
VALUES (4, 3),
       (4, 1);
-- React, Java


-- An apply Java Developer
INSERT INTO application (candidateId, recruitmentPositionId, cvUrl)
VALUES (5, 1, 'https://example.com/cv-an.pdf');

-- Thảo apply Front-end
INSERT INTO application (candidateId, recruitmentPositionId, cvUrl, progress)
VALUES (2, 2, 'https://example.com/cv-thao.pdf', 'interviewing');

-- Đức apply Full-stack
INSERT INTO application (candidateId, recruitmentPositionId, cvUrl, progress)
VALUES (3, 3, 'https://example.com/cv-duc.pdf', 'pending');


DELIMITER $$
-- Stored Procedure: Kiểm tra tên công nghệ có tồn tại
CREATE PROCEDURE sp_CheckTechnologyNameExists(
    IN in_name VARCHAR(100),
    OUT is_exists BOOLEAN
)
BEGIN
SELECT EXISTS(SELECT 1
              FROM technology
              WHERE name = in_name)
INTO is_exists;
END $$

-- Stored Procedure: Kiểm tra username có tồn tại
CREATE PROCEDURE sp_CheckUsernameExists(
    IN in_username VARCHAR(100),
    OUT is_exists BOOLEAN
)
BEGIN
SELECT EXISTS(SELECT 1
              FROM user
              WHERE username = in_username)
INTO is_exists;
END $$

-- Stored Procedure: Kiểm tra email ứng viên có tồn tại
CREATE PROCEDURE sp_CheckCandidateEmailExists(
    IN in_email VARCHAR(100),
    OUT is_exists BOOLEAN
)
BEGIN
SELECT EXISTS(SELECT 1
              FROM candidate
              WHERE email = in_email)
INTO is_exists;
END $$

-- Stored Procedure: Kiểm tra số điện thoại ứng viên có tồn tại
CREATE PROCEDURE sp_CheckCandidatePhoneExists(
    IN in_phone VARCHAR(20),
    OUT is_exists BOOLEAN
)
BEGIN
SELECT EXISTS(SELECT 1
              FROM candidate
              WHERE phone = in_phone)
INTO is_exists;
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
CREATE PROCEDURE sp_FindAllTechnologiesByAdmin(
    IN pageNumber INT,
    IN pageSize INT,
    OUT totalPages INT
)
BEGIN
    DECLARE offsetValue INT;
    DECLARE totalRecords INT;

    SET offsetValue = (pageNumber - 1) * pageSize;

SELECT COUNT(*)
INTO totalRecords
FROM technology;

SET totalPages = CEIL(totalRecords / pageSize);

SELECT id, name, status
FROM technology
         LIMIT pageSize OFFSET offsetValue;
END $$

CREATE PROCEDURE sp_FindAllTechnologiesByCandidates(
    IN pageNumber INT,
    IN pageSize INT,
    OUT totalPages INT
)
BEGIN
    DECLARE offsetValue INT;
    DECLARE totalRecords INT;

    SET offsetValue = (pageNumber - 1) * pageSize;

SELECT COUNT(*)
INTO totalRecords
FROM technology
WHERE status = 'active';

SET totalPages = CEIL(totalRecords / pageSize);

SELECT id, name, status
FROM technology
WHERE status = 'active'
    LIMIT pageSize OFFSET offsetValue;
END $$

-- Stored Procedure: Tạo công nghệ mới
CREATE PROCEDURE sp_CreateTechnology(
    IN p_name VARCHAR(100),
    IN p_status ENUM ('active', 'inactive'),
    OUT p_return_code INT
        )
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_return_code = 1;
ROLLBACK;
END;

START TRANSACTION;

IF EXISTS (SELECT 1 FROM technology WHERE name = p_name) THEN
        SET p_return_code = 1;
ROLLBACK;
ELSE
        INSERT INTO technology (name, status)
        VALUES (p_name, p_status);
        SET p_return_code = 0;
COMMIT;
END IF;
END $$

-- Stored Procedure: Cập nhật công nghệ
CREATE PROCEDURE sp_UpdateTechnologyField(
    IN p_id INT,
    IN p_field_name VARCHAR(50),
    IN p_new_value VARCHAR(255),
    OUT p_result INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_result = 1;
ROLLBACK;
END;

START TRANSACTION;

SET p_result = 0;
    -- Khởi tạo mặc định là thành công

    -- Kiểm tra trường hợp field_name không hợp lệ
    IF p_field_name NOT IN ('name', 'status') THEN
        SET p_result = 3; -- Trường không hợp lệ
ROLLBACK;
ELSE
        -- Thực hiện cập nhật dựa trên field_name
        IF p_field_name = 'name' THEN
UPDATE technology SET name = p_new_value WHERE id = p_id;
ELSEIF p_field_name = 'status' THEN
UPDATE technology SET status = p_new_value WHERE id = p_id;
END IF;

        -- Kiểm tra xem có bản ghi nào được cập nhật không
        IF ROW_COUNT() = 0 THEN
            SET p_result = 2; -- Không tìm thấy công nghệ
ROLLBACK;
ELSE
            SET p_result = 0; -- Thành công
COMMIT;
END IF;
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
            SET p_return_code = 0;
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



-- Stored Procedure: Tìm kiếm công nghệ theo ID
CREATE PROCEDURE sp_FindTechnologyById(
    IN p_id INT
)
BEGIN
SELECT id, name, status
FROM technology
WHERE id = p_id
  AND status = 'active';
END $$
CREATE PROCEDURE sp_UpdatePassword(
    IN p_user_id INT,
    IN p_old_password VARCHAR(255),
    IN p_new_password VARCHAR(255),
    IN p_phone VARCHAR(255),
    OUT p_return_code INT
)
BEGIN
    IF NOT EXISTS (SELECT 1
                   FROM user
                   JOIN candidate c ON c.id = user.id
                   WHERE user.id = p_user_id AND c.phone = p_phone
                     AND password = SHA2(p_old_password, 256)
                     AND status = 'active')  THEN
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
    #     WHERE u.status = 'active'
ORDER BY c.id
    LIMIT p_pageSize OFFSET offsetValue;
END $$

-- Stored Procedure: Khóa/mở khóa tài khoản ứng viên
CREATE PROCEDURE sp_LockUnlockCandidate(
    IN p_candidateId INT,
    OUT p_return_code INT
)
BEGIN
    DECLARE current_status ENUM ('active', 'inactive');
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_return_code = 1; -- Lỗi
ROLLBACK;
END;

START TRANSACTION;

SELECT status
INTO current_status
FROM user
WHERE id = p_candidateId
  AND role = 'candidate'
    LIMIT 1;

IF current_status IS NULL THEN
        SET p_return_code = 2; -- Không tìm thấy ứng viên
ROLLBACK;
ELSE
        -- Đảo trạng thái: active -> inactive hoặc ngược lại
UPDATE user
SET status = CASE current_status
                 WHEN 'active' THEN 'inactive'
                 WHEN 'inactive' THEN 'active'
    END
WHERE id = p_candidateId;
SET p_return_code = 0; -- Thành công
COMMIT;
END IF;
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

SELECT id,
       name,
       description,
       minSalary,
       maxSalary,
       minExperience,
       createdDate,
       expiredDate,
       status
FROM recruitment_position
ORDER BY id
    LIMIT p_pageSize OFFSET offsetValue;
END $$

-- Stored Procedure: Tạo vị trí tuyển dụng mới
CREATE PROCEDURE sp_CreateRecruitmentPosition(
    IN p_name VARCHAR(100),
    IN p_description TEXT,
    IN p_minSalary DECIMAL(10, 2),
    IN p_maxSalary DECIMAL(10, 2),
    IN p_minExperience INT,
    IN p_expiredDate DATETIME,
    OUT p_returnCode INT,
    OUT p_newId INT -- Thêm tham số để trả về ID
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_returnCode = 1;
            SET p_newId = 0;
ROLLBACK;
END;

START TRANSACTION;

-- Kiểm tra dữ liệu đầu vào
IF p_name IS NULL OR p_name = '' THEN
        SET p_returnCode = 1; -- Tên không hợp lệ
        SET p_newId = 0;
ROLLBACK;
ELSEIF p_minSalary < 0 OR p_maxSalary < p_minSalary THEN
        SET p_returnCode = 2; -- Lương không hợp lệ
        SET p_newId = 0;
ROLLBACK;
ELSEIF p_minExperience < 0 THEN
        SET p_returnCode = 3; -- Kinh nghiệm không hợp lệ
        SET p_newId = 0;
ROLLBACK;
ELSE
        -- Thêm vị trí tuyển dụng mới
        INSERT INTO recruitment_position (name, description, minSalary, maxSalary, minExperience, createdDate,
                                          expiredDate, status)
        VALUES (p_name, p_description, p_minSalary, p_maxSalary, p_minExperience, NOW(), p_expiredDate, 'active');

        -- Lấy ID của bản ghi vừa thêm
        SET p_newId = LAST_INSERT_ID();
        SET p_returnCode = 0; -- Thành công
COMMIT;
END IF;
END $$


CREATE PROCEDURE sp_ResetPassword(
    IN p_user_id INT,
    IN p_new_password VARCHAR(255),
    OUT p_return_code INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_return_code = 1; -- Lỗi
ROLLBACK;
END;

START TRANSACTION;

IF NOT EXISTS (SELECT 1
                   FROM user
                   WHERE id = p_user_id
                     AND role = 'candidate') THEN
        SET p_return_code = 2; -- Không tìm thấy ứng viên
ROLLBACK;
ELSE
UPDATE user
SET password = SHA2(p_new_password, 256)
WHERE id = p_user_id;
SET p_return_code = 0; -- Thành công
COMMIT;
END IF;
END $$

CREATE PROCEDURE sp_RemoveCandidateTechnology(
    IN p_candidateId INT,
    IN p_technologyId INT,
    OUT p_return_code INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_return_code = 0; -- Lỗi
ROLLBACK;
END;

START TRANSACTION;

-- Kiểm tra xem bản ghi liên kết có tồn tại không
IF EXISTS (SELECT 1
               FROM candidate_technology
               WHERE candidateId = p_candidateId
                 AND technologyId = p_technologyId) THEN
        -- Xóa bản ghi từ bảng candidate_technology
DELETE
FROM candidate_technology
WHERE candidateId = p_candidateId
  AND technologyId = p_technologyId;

SET p_return_code = 1; -- Thành công
COMMIT;
ELSE
        SET p_return_code = 0; -- Không tìm thấy bản ghi để xóa
ROLLBACK;
END IF;
END $$

CREATE PROCEDURE sp_AddCandidateTechnology(
    IN p_candidateId INT,
    IN p_technologyId INT,
    OUT p_return_code INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_return_code = 0; -- Lỗi
ROLLBACK;
END;

START TRANSACTION;

-- Kiểm tra ứng viên tồn tại
IF NOT EXISTS (SELECT 1 FROM candidate WHERE id = p_candidateId) THEN
        SET p_return_code = 2; -- Không tìm thấy ứng viên
ROLLBACK;
-- Kiểm tra công nghệ tồn tại và active
ELSEIF NOT EXISTS (SELECT 1 FROM technology WHERE id = p_technologyId AND status = 'active') THEN
        SET p_return_code = 3; -- Không tìm thấy công nghệ hoặc công nghệ không hoạt động
ROLLBACK;
-- Kiểm tra xem ứng viên đã đăng ký công nghệ này chưa
ELSEIF EXISTS (SELECT 1
                   FROM candidate_technology
                   WHERE candidateId = p_candidateId
                     AND technologyId = p_technologyId) THEN
        SET p_return_code = 4; -- Công nghệ đã được đăng ký
ROLLBACK;
ELSE
        INSERT INTO candidate_technology (candidateId, technologyId)
        VALUES (p_candidateId, p_technologyId);
        SET p_return_code = 1; -- Thành công
COMMIT;
END IF;
END $$


CREATE PROCEDURE sp_CreateApplication(
    IN p_candidateId INT,
    IN p_recruitmentPositionId INT,
    IN p_cvUrl VARCHAR(255)
)
BEGIN
INSERT INTO application (candidateId,
                         recruitmentPositionId,
                         cvUrl,
                         progress,
                         createAt,
                         updateAt)
VALUES (p_candidateId,
        p_recruitmentPositionId,
        p_cvUrl,
        'pending',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);
END $$


CREATE PROCEDURE sp_GetActiveRecruitmentPositions(
    IN p_page_number INT,
    IN p_page_size INT
)
BEGIN
    DECLARE v_offset INT;
    SET v_offset = (p_page_number - 1) * p_page_size;

SELECT id,
       name,
       description,
       minSalary,
       maxSalary,
       minExperience,
       createdDate,
       expiredDate,
       status
FROM recruitment_position
WHERE status = 'ACTIVE'
ORDER BY id
    LIMIT p_page_size OFFSET v_offset;
END $$



-- Lấy danh sách công nghệ của một vị trí tuyển dụng
CREATE PROCEDURE sp_GetPositionTechnologies(
    IN p_position_id INT
)
BEGIN
SELECT t.id, t.name, t.status
FROM technology t
         JOIN recruitment_position_technology rpt ON t.id = rpt.technologyId
WHERE rpt.recruitmentPositionId = p_position_id;
END $$

-- Thêm một công nghệ vào vị trí tuyển dụng
CREATE PROCEDURE sp_AddPositionTechnology(
    IN p_position_id INT,
    IN p_technology_id INT,
    OUT p_result INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_result = 1;
ROLLBACK;
END;

START TRANSACTION;

-- Kiểm tra xem vị trí tuyển dụng và công nghệ có tồn tại không
IF NOT EXISTS (SELECT 1 FROM recruitment_position WHERE id = p_position_id) THEN
        SET p_result = 2; -- Vị trí không tồn tại
ROLLBACK;
ELSEIF NOT EXISTS (SELECT 1 FROM technology WHERE id = p_technology_id) THEN
        SET p_result = 3; -- Công nghệ không tồn tại
ROLLBACK;
ELSEIF EXISTS (SELECT 1
                   FROM recruitment_position_technology
                   WHERE recruitmentPositionId = p_position_id
                     AND technologyId = p_technology_id) THEN
        SET p_result = 4; -- Đã tồn tại mối quan hệ
ROLLBACK;
ELSE
        INSERT INTO recruitment_position_technology (recruitmentPositionId, technologyId)
        VALUES (p_position_id, p_technology_id);
        SET p_result = 0; -- Thành công
COMMIT;
END IF;
END $$

-- Xóa một công nghệ khỏi vị trí tuyển dụng
CREATE PROCEDURE sp_RemovePositionTechnology(
    IN p_position_id INT,
    IN p_technology_id INT,
    OUT p_result INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_result = 1;
ROLLBACK;
END;

START TRANSACTION;

-- Kiểm tra xem mối quan hệ có tồn tại không
IF NOT EXISTS (SELECT 1
                   FROM recruitment_position_technology
                   WHERE recruitmentPositionId = p_position_id
                     AND technologyId = p_technology_id) THEN
        SET p_result = 2; -- Mối quan hệ không tồn tại
ROLLBACK;
ELSE
DELETE
FROM recruitment_position_technology
WHERE recruitmentPositionId = p_position_id
  AND technologyId = p_technology_id;
SET p_result = 0; -- Thành công
COMMIT;
END IF;
END $$


DELIMITER ;

DELIMITER $$

CREATE PROCEDURE sp_UpdateCandidateField(
    IN p_id INT,
    IN p_field_name VARCHAR(50),
    IN p_new_value VARCHAR(255),
    OUT p_result INT
)
BEGIN
    DECLARE email_exists BOOLEAN DEFAULT FALSE;
    DECLARE phone_exists BOOLEAN DEFAULT FALSE;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_result = 1; -- Lỗi SQL
ROLLBACK;
END;

START TRANSACTION;
SET p_result = 1;

    -- Kiểm tra field hợp lệ
    IF p_field_name NOT IN (
                            'email', 'phone', 'experience', 'gender', 'dob', 'description'
        ) THEN
        SET p_result = 3; -- Trường không hợp lệ
ROLLBACK;
ELSE
        -- Kiểm tra tồn tại candidate
        IF NOT EXISTS (SELECT 1 FROM candidate WHERE id = p_id) THEN
            SET p_result = 2; -- Không tìm thấy ứng viên
ROLLBACK;
ELSE
            -- Trường hợp email
            IF p_field_name = 'email' THEN
                CALL sp_CheckCandidateEmailExists(p_new_value, email_exists);
                IF email_exists THEN
                    SET p_result = 4; -- Email đã tồn tại
ROLLBACK;
ELSE
UPDATE candidate SET email = p_new_value WHERE id = p_id;
SET p_result = 0;
END IF;

                -- Trường hợp phone
            ELSEIF p_field_name = 'phone' THEN
                CALL sp_CheckCandidatePhoneExists(p_new_value, phone_exists);
                IF phone_exists THEN
                    SET p_result = 5; -- Số điện thoại đã tồn tại
ROLLBACK;
ELSE
UPDATE candidate SET phone = p_new_value WHERE id = p_id;
SET p_result = 0;
END IF;

                -- Trường hợp experience
            ELSEIF p_field_name = 'experience' THEN
UPDATE candidate SET experience = CAST(p_new_value AS SIGNED) WHERE id = p_id;
SET p_result = 0;

                -- Trường hợp gender
            ELSEIF p_field_name = 'gender' THEN
UPDATE candidate SET gender = p_new_value WHERE id = p_id;
SET p_result = 0;

                -- Trường hợp dob
            ELSEIF p_field_name = 'dob' THEN
UPDATE candidate SET dob = CAST(p_new_value AS DATE) WHERE id = p_id;
SET p_result = 0;

                -- Trường hợp description
            ELSEIF p_field_name = 'description' THEN
UPDATE candidate SET description = p_new_value WHERE id = p_id;
SET p_result = 0;
END IF;
END IF;
END IF;

    IF p_result = 0 THEN
        COMMIT;
END IF;
END$$

DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_GetCandidateById(
    IN p_id INT
)
BEGIN
SELECT id,
       name,
       email,
       phone,
       experience,
       gender,
       description,
       dob
FROM candidate
WHERE id = p_id;
END $$
DELIMITER ;

DELIMITER $$

CREATE PROCEDURE sp_UpdateRecruitmentPositionField(
    IN p_id INT,
    IN p_field_name VARCHAR(50),
    IN p_new_value VARCHAR(255),
    OUT p_result INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_result = 1;
ROLLBACK;
END;

START TRANSACTION;

SET p_result = 0;
    -- Khởi tạo mặc định là thành công

    -- Kiểm tra trường hợp field_name không hợp lệ
    IF p_field_name NOT IN
       ('name', 'description', 'minSalary', 'maxSalary', 'minExperience', 'expiredDate', 'status') THEN
        SET p_result = 3; -- Trường không hợp lệ
ROLLBACK;
ELSE
        -- Thực hiện cập nhật dựa trên field_name
        IF p_field_name = 'name' THEN
UPDATE recruitment_position SET name = p_new_value WHERE id = p_id;
ELSEIF p_field_name = 'description' THEN
UPDATE recruitment_position SET description = p_new_value WHERE id = p_id;
ELSEIF p_field_name = 'minSalary' THEN
UPDATE recruitment_position SET minSalary = CAST(p_new_value AS DECIMAL(10, 2)) WHERE id = p_id;
ELSEIF p_field_name = 'maxSalary' THEN
UPDATE recruitment_position SET maxSalary = CAST(p_new_value AS DECIMAL(10, 2)) WHERE id = p_id;
ELSEIF p_field_name = 'minExperience' THEN
UPDATE recruitment_position SET minExperience = CAST(p_new_value AS SIGNED) WHERE id = p_id;
ELSEIF p_field_name = 'expiredDate' THEN
UPDATE recruitment_position SET expiredDate = CAST(p_new_value AS DATETIME) WHERE id = p_id;
ELSEIF p_field_name = 'status' THEN
UPDATE recruitment_position SET status = p_new_value WHERE id = p_id;
END IF;

        -- Kiểm tra xem có bản ghi nào được cập nhật không
        IF ROW_COUNT() = 0 THEN
            SET p_result = 2; -- Không tìm thấy vị trí
ROLLBACK;
ELSE
            SET p_result = 0; -- Thành công
COMMIT;
END IF;
END IF;
END $$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE sp_UpdateApplicationField(
    IN p_id INT,
    IN p_field_name VARCHAR(50),
    IN p_new_value VARCHAR(255),
    OUT p_result INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_result = 1; -- lỗi SQL
ROLLBACK;
END;

START TRANSACTION;

SET p_result = 0;

    IF p_field_name NOT IN (
                            'cvUrl', 'progress', 'interviewRequestDate', 'interviewRequestResult',
                            'interviewLink', 'interviewTime', 'interviewResult',
                            'interviewResultNote', 'destroyAt', 'destroyReason'
        ) THEN
        SET p_result = 3; -- Trường không hợp lệ
ROLLBACK;
ELSE
        IF p_field_name = 'cvUrl' THEN
UPDATE application SET cvUrl = p_new_value WHERE id = p_id;
ELSEIF p_field_name = 'progress' THEN
UPDATE application
SET progress = p_new_value
WHERE id = p_id AND progress != 'CANCEL' AND progress != 'REJECT';
ELSEIF p_field_name = 'interviewRequestDate' THEN
UPDATE application SET interviewRequestDate = CAST(p_new_value AS DATETIME) WHERE id = p_id;
ELSEIF p_field_name = 'interviewRequestResult' THEN
UPDATE application SET interviewRequestResult = p_new_value WHERE id = p_id;
ELSEIF p_field_name = 'interviewLink' THEN
UPDATE application SET interviewLink = p_new_value WHERE id = p_id;
ELSEIF p_field_name = 'interviewTime' THEN
UPDATE application SET interviewTime = CAST(p_new_value AS DATETIME) WHERE id = p_id;
ELSEIF p_field_name = 'interviewResult' THEN
UPDATE application SET interviewResult = p_new_value WHERE id = p_id;
ELSEIF p_field_name = 'interviewResultNote' THEN
UPDATE application SET interviewResultNote = p_new_value WHERE id = p_id;
ELSEIF p_field_name = 'destroyAt' THEN
UPDATE application SET destroyAt = CAST(p_new_value AS DATETIME) WHERE id = p_id;
ELSEIF p_field_name = 'destroyReason' THEN
UPDATE application SET destroyReason = p_new_value WHERE id = p_id;
END IF;

        IF ROW_COUNT() = 0 THEN
            SET p_result = 2; -- Không tìm thấy application
ROLLBACK;
ELSE
            COMMIT;
            SET p_result = 0; -- Thành công
END IF;
END IF;
END$$

DELIMITER ;


DELIMITER //

CREATE PROCEDURE sp_CancelApplication(
    IN p_applicationId INT,
    IN p_candidateId INT,
    IN p_destroyReason VARCHAR(255),
    IN p_progress ENUM ('PENDING', 'HANDLING', 'INTERVIEWING', 'DONE','REJECT','CANCEL'),
    OUT p_returnCode INT
        )
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_returnCode = 1;
ROLLBACK;
END;

START TRANSACTION;

IF p_destroyReason IS NULL OR TRIM(p_destroyReason) = '' THEN
        SET p_returnCode = 1; -- Lý do hủy không hợp lệ
ROLLBACK;
ELSEIF EXISTS (SELECT 1
                   FROM application
                   WHERE id = p_applicationId
                     AND candidateId = p_candidateId
                     AND destroyAt IS NULL) THEN
UPDATE application
SET destroyAt     = CURRENT_TIMESTAMP,
    destroyReason = p_destroyReason,
    progress      = p_progress
WHERE id = p_applicationId
  AND candidateId = p_candidateId;
SET p_returnCode = 0;
COMMIT;
ELSE
        SET p_returnCode = 1; -- Đơn không tồn tại, không thuộc ứng viên, hoặc đã bị hủy
ROLLBACK;
END IF;
END //

DELIMITER ;
DELIMITER //

CREATE PROCEDURE sp_CancelApplicationByAdmin(
    IN p_applicationId INT,
    IN p_destroyReason VARCHAR(255),
    IN p_progress ENUM ('PENDING', 'HANDLING', 'INTERVIEWING', 'DONE','REJECT','CANCEL'),
    OUT p_returnCode INT
        )
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_returnCode = 1;
ROLLBACK;
END;

START TRANSACTION;

IF p_destroyReason IS NULL OR TRIM(p_destroyReason) = '' THEN
        SET p_returnCode = 1; -- Lý do hủy không hợp lệ
ROLLBACK;
ELSEIF EXISTS (SELECT 1
                   FROM application
                   WHERE id = p_applicationId
                     AND destroyAt IS NULL) THEN
UPDATE application
SET destroyAt     = CURRENT_TIMESTAMP,
    destroyReason = p_destroyReason,
    progress=p_progress
WHERE id = p_applicationId;
SET p_returnCode = 0;
COMMIT;
ELSE
        SET p_returnCode = 1; -- Đơn không tồn tại hoặc đã bị hủy
ROLLBACK;
END IF;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE sp_GetApplicationsByCandidateId(
    IN p_candidateId INT,
    IN p_pageNumber INT,
    IN p_pageSize INT
)
BEGIN
    DECLARE v_offset INT;
    SET v_offset = (p_pageNumber - 1) * p_pageSize;

    IF p_pageNumber < 1 OR p_pageSize < 1 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'pageNumber and pageSize must be positive';
END IF;

SELECT *
FROM application
WHERE candidateId = p_candidateId
    LIMIT p_pageSize OFFSET v_offset;
END //

DELIMITER ;
DELIMITER //

CREATE PROCEDURE sp_FindAllApplications(
    IN p_pageNumber INT,
    IN p_pageSize INT
)
BEGIN
    DECLARE v_offset INT;
    SET v_offset = (p_pageNumber - 1) * p_pageSize;

    IF p_pageNumber < 1 OR p_pageSize < 1 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'pageNumber and pageSize must be positive';
END IF;

SELECT *
FROM application
         LIMIT p_pageSize OFFSET v_offset;
END //

DELIMITER ;
DELIMITER //

CREATE PROCEDURE sp_FindApplicationsByProgress(
    IN p_progress VARCHAR(50),
    IN p_pageNumber INT,
    IN p_pageSize INT
)
BEGIN
    DECLARE v_offset INT;
    SET v_offset = (p_pageNumber - 1) * p_pageSize;

    IF p_pageNumber < 1 OR p_pageSize < 1 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'pageNumber and pageSize must be positive';
END IF;

SELECT *
FROM application
WHERE progress = p_progress
    LIMIT p_pageSize OFFSET v_offset;
END //

DELIMITER ;
DELIMITER //

CREATE PROCEDURE sp_FindApplicationById(
    IN p_id int
)
BEGIN
SELECT *
FROM application
WHERE id = p_id;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE sp_GetUserById(
    IN p_id int
)
BEGIN
SELECT *
FROM user
WHERE id = p_id;
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE sp_GetCandidateTechnologies(
    IN p_candidateId INT,
    IN p_pageNumber INT,
    IN p_pageSize INT,
    OUT p_totalRecords INT
)
BEGIN
    -- Get total records
    DECLARE v_offset INT;
    SET v_offset = (p_pageNumber - 1) * p_pageSize;
SELECT COUNT(*)
INTO p_totalRecords
FROM candidate_technology ct
         JOIN technology t ON ct.technologyId = t.id
WHERE ct.candidateId = p_candidateId
  AND t.status = 'ACTIVE';

-- Get paginated technologies
SELECT t.id, t.name, t.status
FROM candidate_technology ct
         JOIN technology t ON ct.technologyId = t.id
WHERE ct.candidateId = p_candidateId
  AND t.status = 'ACTIVE'
ORDER BY t.name
    LIMIT p_pageSize OFFSET v_offset;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE sp_GetRecruitmentTechnologies(
    IN p_pageNumber INT,
    IN p_pageSize INT
)
BEGIN
    DECLARE v_offset INT;
    SET v_offset = (p_pageNumber - 1) * p_pageSize;
SELECT DISTINCT t.id, t.name, t.status
FROM technology t
         JOIN recruitment_position_technology rpt ON t.id = rpt.recruitmentPositionId
         JOIN recruitment_position rp ON rpt.recruitmentPositionId = rp.id
    AND rp.status = 'OPEN'
WHERE t.status = 'ACTIVE'
ORDER BY t.name
    LIMIT p_pageSize OFFSET v_offset;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE sp_GetRecruitmentTechnologiesCount(
    OUT p_count INT
)
BEGIN
SELECT COUNT(DISTINCT t.id)
INTO p_count
FROM technology t
         JOIN recruitment_position_technology rpt ON t.id = rpt.technologyId
         JOIN recruitment_position rp ON rpt.recruitmentPositionId = rp.id
WHERE t.status = 'ACTIVE'
  AND rp.status = 'OPEN';
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE sp_GetActiveTechnologies(
    IN p_pageNumber INT,
    IN p_pageSize INT,
    OUT p_totalRecords INT
)
BEGIN
    DECLARE v_offset INT;
    SET v_offset = (p_pageNumber - 1) * p_pageSize;
    -- Get total records
SELECT COUNT(*)
INTO p_totalRecords
FROM technology t
WHERE t.status = 'ACTIVE';

-- Get paginated technologies
SELECT t.id, t.name, t.status
FROM technology t
WHERE t.status = 'ACTIVE'
ORDER BY t.name
    LIMIT p_pageSize OFFSET v_offset;
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE sp_GetTotalPositionsCount(
    OUT p_count INT
)
BEGIN
SELECT COUNT(*) INTO p_count
FROM recruitment_position;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE sp_GetActivePositionsCount(
    OUT p_count INT
)
BEGIN
SELECT COUNT(*) INTO p_count
FROM recruitment_position
WHERE status = 'ACTIVE';
END //

DELIMITER ;

-- Stored Procedure: Đếm tổng số ứng viên
DELIMITER //
CREATE PROCEDURE sp_GetTotalCandidatesCount(OUT total INT)
BEGIN
SELECT COUNT(*) INTO total FROM candidate;
END //
DELIMITER ;

-- Stored Procedure: Đếm tổng số ứng viên theo tên
DELIMITER //
CREATE PROCEDURE sp_GetTotalCandidatesByName(IN p_name VARCHAR(255), OUT total INT)
BEGIN
SELECT COUNT(*) INTO total
FROM candidate
WHERE name LIKE p_name;
END //
DELIMITER ;

-- Stored Procedure: Đếm tổng số ứng viên theo kinh nghiệm
DELIMITER //
CREATE PROCEDURE sp_GetTotalCandidatesByExperience(IN p_experience INT, OUT total INT)
BEGIN
SELECT COUNT(*) INTO total
FROM candidate
WHERE experience >= p_experience;
END //
DELIMITER ;

-- Stored Procedure: Đếm tổng số ứng viên theo tuổi
DELIMITER //
CREATE PROCEDURE sp_GetTotalCandidatesByAge(IN age INT, OUT total INT)
BEGIN
SELECT COUNT(*) INTO total
FROM candidate
WHERE YEAR(CURDATE()) - YEAR(dob) >= age;
END //
DELIMITER ;

-- Stored Procedure: Đếm tổng số ứng viên theo giới tính
DELIMITER //
CREATE PROCEDURE sp_GetTotalCandidatesByGender(IN p_gender VARCHAR(10), OUT total INT)
BEGIN
SELECT COUNT(*) INTO total
FROM candidate
WHERE gender = p_gender;
END //
DELIMITER ;

-- Stored Procedure: Đếm tổng số ứng viên theo công nghệ
DELIMITER //
CREATE PROCEDURE sp_GetTotalCandidatesByTechnology(IN p_technology VARCHAR(255), OUT total INT)
BEGIN
SELECT COUNT(*) INTO total
FROM candidate c
         JOIN candidate_technology ct ON c.id = ct.candidateId
         JOIN technology t ON ct.technologyId = t.id
WHERE t.name LIKE p_technology;
END //
DELIMITER ;

-- Stored Procedure: Cập nhật - Tìm kiếm ứng viên theo tên với phân trang
DELIMITER //
CREATE PROCEDURE sp_SearchCandidateByName(IN p_name VARCHAR(255), IN pageNumber INT, IN pageSize INT)
BEGIN
    DECLARE offset INT;
    SET offset = (pageNumber - 1) * pageSize;

SELECT id, name, email, experience, gender
FROM candidate
WHERE name LIKE p_name
ORDER BY name
    LIMIT pageSize OFFSET offset;
END //
DELIMITER ;

-- Stored Procedure: Cập nhật - Lọc ứng viên theo kinh nghiệm với phân trang
DELIMITER //
CREATE PROCEDURE sp_FilterCandidateByExperience(IN p_experience INT, IN pageNumber INT, IN pageSize INT)
BEGIN
    DECLARE offset INT;
    SET offset = (pageNumber - 1) * pageSize;

SELECT id, name, email, experience, gender
FROM candidate
WHERE experience >= p_experience
ORDER BY experience
    LIMIT pageSize OFFSET offset;
END //
DELIMITER ;

-- Stored Procedure: Cập nhật - Lọc ứng viên theo tuổi với phân trang
DELIMITER //
CREATE PROCEDURE sp_FilterCandidateByAge(IN p_age INT, IN pageNumber INT, IN pageSize INT)
BEGIN
    DECLARE offset INT;
    SET offset = (pageNumber - 1) * pageSize;

SELECT id, name, email, dob, gender
FROM candidate
WHERE YEAR(CURDATE()) - YEAR(dob) >= p_age
ORDER BY dob
    LIMIT pageSize OFFSET offset;
END //
DELIMITER ;

-- Stored Procedure: Cập nhật - Lọc ứng viên theo giới tính với phân trang
DELIMITER //
CREATE PROCEDURE sp_FilterCandidateByGender(IN p_gender VARCHAR(10), IN pageNumber INT, IN pageSize INT)
BEGIN
    DECLARE offset INT;
    SET offset = (pageNumber - 1) * pageSize;

SELECT id, name, email, experience, gender
FROM candidate
WHERE p_gender = gender
ORDER BY id
    LIMIT pageSize OFFSET offset;
END //
DELIMITER ;

-- Stored Procedure: Cập nhật - Lọc ứng viên theo công nghệ với phân trang
DELIMITER //
CREATE PROCEDURE sp_FilterCandidateByTechnology(IN p_technology VARCHAR(255), IN pageNumber INT, IN pageSize INT)
BEGIN
    DECLARE offset INT;
    SET offset = (pageNumber - 1) * pageSize;

SELECT c.id, c.name, c.email, c.experience, c.gender
FROM candidate c
         JOIN candidate_technology ct ON c.id = ct.candidateId
         JOIN technology t ON ct.technologyId = t.id
WHERE t.name LIKE p_technology
ORDER BY c.id
    LIMIT pageSize OFFSET offset;
END //
DELIMITER ;


-- Stored Procedure: Đếm tổng số công nghệ
DELIMITER //
CREATE PROCEDURE sp_GetTotalTechnologiesCount(OUT total INT)
BEGIN
SELECT COUNT(*) INTO total FROM technology;
END //
DELIMITER ;

-- Stored Procedure: Đếm tổng số công nghệ theo tên
DELIMITER //
CREATE PROCEDURE sp_GetTotalTechnologiesByName(IN keyword VARCHAR(100), OUT total INT)
BEGIN
SELECT COUNT(*) INTO total
FROM technology
WHERE name LIKE keyword;
END //
DELIMITER ;

-- Stored Procedure: Cập nhật - Tìm kiếm công nghệ theo tên với phân trang và trả về tổng số bản ghi
DELIMITER //
CREATE PROCEDURE sp_SearchTechnologies(IN keyword VARCHAR(100), IN pageNumber INT, IN pageSize INT, OUT totalRecords INT)
BEGIN
    DECLARE offset INT;
    SET offset = (pageNumber - 1) * pageSize;

    -- Đếm tổng số bản ghi
SELECT COUNT(*) INTO totalRecords
FROM technology
WHERE name LIKE keyword;

-- Lấy danh sách công nghệ với phân trang
SELECT id, name, status
FROM technology
WHERE name LIKE keyword
ORDER BY name
    LIMIT pageSize OFFSET offset;
END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE sp_FilterRecruitmentPositionsByTechnologies(
    IN p_techIds VARCHAR(255),
    IN p_pageNumber INT,
    IN p_pageSize INT
)
BEGIN
    DECLARE offset INT;
    SET offset = (p_pageNumber - 1) * p_pageSize;

    -- Tạo bảng tạm để lưu danh sách ID công nghệ
    DROP TEMPORARY TABLE IF EXISTS temp_tech_ids;
    CREATE TEMPORARY TABLE temp_tech_ids (tech_id INT);

    -- Chuyển chuỗi p_techIds thành các bản ghi trong bảng tạm
    WHILE p_techIds != '' DO
            SET @comma_pos = LOCATE(',', p_techIds);
            IF @comma_pos > 0 THEN
                INSERT INTO temp_tech_ids (tech_id) VALUES (SUBSTRING(p_techIds, 1, @comma_pos - 1));
                SET p_techIds = SUBSTRING(p_techIds, @comma_pos + 1);
ELSE
                INSERT INTO temp_tech_ids (tech_id) VALUES (p_techIds);
                SET p_techIds = '';
END IF;
END WHILE;

    -- Lọc các vị trí tuyển dụng có tất cả công nghệ trong danh sách
SELECT DISTINCT rp.*
FROM recruitment_position rp
         INNER JOIN recruitment_position_technology rpt ON rp.id = rpt.recruitmentPositionId
WHERE rp.status = 'ACTIVE'
  AND rpt.technologyId IN (SELECT tech_id FROM temp_tech_ids)
GROUP BY rp.id
HAVING COUNT(DISTINCT rpt.technologyId) = (SELECT COUNT(*) FROM temp_tech_ids)
ORDER BY rp.id
    LIMIT p_pageSize OFFSET offset;

-- Xóa bảng tạm
DROP TEMPORARY TABLE IF EXISTS temp_tech_ids;
END //

CREATE PROCEDURE sp_GetFilteredPositionsCountByTechnologies(
    IN p_techIds VARCHAR(255),
    OUT p_count INT
)
BEGIN
    -- Tạo bảng tạm để lưu danh sách ID công nghệ
    DROP TEMPORARY TABLE IF EXISTS temp_tech_ids;
    CREATE TEMPORARY TABLE temp_tech_ids (tech_id INT);

    -- Chuyển chuỗi p_techIds thành các bản ghi trong bảng tạm
    WHILE p_techIds != '' DO
            SET @comma_pos = LOCATE(',', p_techIds);
            IF @comma_pos > 0 THEN
                INSERT INTO temp_tech_ids (tech_id) VALUES (SUBSTRING(p_techIds, 1, @comma_pos - 1));
                SET p_techIds = SUBSTRING(p_techIds, @comma_pos + 1);
ELSE
                INSERT INTO temp_tech_ids (tech_id) VALUES (p_techIds);
                SET p_techIds = '';
END IF;
END WHILE;

    -- Đếm số vị trí tuyển dụng có tất cả công nghệ trong danh sách
SELECT COUNT(DISTINCT rp.id) INTO p_count
FROM recruitment_position rp
         INNER JOIN recruitment_position_technology rpt ON rp.id = rpt.recruitmentPositionId
WHERE rp.status = 'ACTIVE'
  AND rpt.technologyId IN (SELECT tech_id FROM temp_tech_ids)
GROUP BY rp.id
HAVING COUNT(DISTINCT rpt.technologyId) = (SELECT COUNT(*) FROM temp_tech_ids);

-- Xóa bảng tạm
DROP TEMPORARY TABLE IF EXISTS temp_tech_ids;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE sp_CancelAllApplicationsByUserId(
    IN p_userId INT,
    IN p_destroyReason VARCHAR(255),
    OUT p_returnCode INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
            SET p_returnCode = 1; -- Lỗi SQL
ROLLBACK;
END;

    -- Kiểm tra p_userId hợp lệ
    IF p_userId <= 0 THEN
        SET p_returnCode = 2; -- userId không hợp lệ
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid userId';
END IF;

    -- Kiểm tra p_destroyReason hợp lệ
    IF p_destroyReason IS NULL OR TRIM(p_destroyReason) = '' THEN
        SET p_returnCode = 3; -- destroyReason không hợp lệ
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'destroyReason cannot be empty';
END IF;

START TRANSACTION;

-- Cập nhật tất cả đơn ứng tuyển của userId
UPDATE application
SET
    progress = 'CANCELED',
    destroyReason = p_destroyReason,
    destroyAt = NOW(),
    updateAt = NOW()
WHERE candidateId = p_userId
  AND progress != 'CANCELED'; -- Chỉ cập nhật các đơn chưa bị hủy

SET p_returnCode = 0; -- Thành công
COMMIT;
END //

DELIMITER ;