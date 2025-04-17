package ra.edu.business.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Application implements Inputable {
    private int id;
    private int candidateId;
    private int recruitmentPositionId;
    private String cvUrl;
    private Progress progress;
    private Date interviewRequestDate;
    private String interviewRequestResult;
    private String interviewLink;
    private Date interviewTime;
    private String interviewResult;
    private String interviewResultNote;
    private Date destroyAt;
    private Date createAt;
    private Date updateAt;
    private String destroyReason;

    public enum Progress {
        PENDING, HANDLING, INTERVIEWING, DONE
    }

    // Constructors
    public Application() {
    }

    public Application(int id, int candidateId, int recruitmentPositionId, String cvUrl, Progress progress,
                       Date interviewRequestDate, String interviewRequestResult, String interviewLink,
                       Date interviewTime, String interviewResult, String interviewResultNote, Date destroyAt,
                       Date createAt, Date updateAt, String destroyReason) {
        this.id = id;
        this.candidateId = candidateId;
        this.recruitmentPositionId = recruitmentPositionId;
        this.cvUrl = cvUrl;
        this.progress = progress;
        this.interviewRequestDate = interviewRequestDate;
        this.interviewRequestResult = interviewRequestResult;
        this.interviewLink = interviewLink;
        this.interviewTime = interviewTime;
        this.interviewResult = interviewResult;
        this.interviewResultNote = interviewResultNote;
        this.destroyAt = destroyAt;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.destroyReason = destroyReason;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public int getRecruitmentPositionId() {
        return recruitmentPositionId;
    }

    public void setRecruitmentPositionId(int recruitmentPositionId) {
        this.recruitmentPositionId = recruitmentPositionId;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public Date getInterviewRequestDate() {
        return interviewRequestDate;
    }

    public void setInterviewRequestDate(Date interviewRequestDate) {
        this.interviewRequestDate = interviewRequestDate;
    }

    public String getInterviewRequestResult() {
        return interviewRequestResult;
    }

    public void setInterviewRequestResult(String interviewRequestResult) {
        this.interviewRequestResult = interviewRequestResult;
    }

    public String getInterviewLink() {
        return interviewLink;
    }

    public void setInterviewLink(String interviewLink) {
        this.interviewLink = interviewLink;
    }

    public Date getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(Date interviewTime) {
        this.interviewTime = interviewTime;
    }

    public String getInterviewResult() {
        return interviewResult;
    }

    public void setInterviewResult(String interviewResult) {
        this.interviewResult = interviewResult;
    }

    public String getInterviewResultNote() {
        return interviewResultNote;
    }

    public void setInterviewResultNote(String interviewResultNote) {
        this.interviewResultNote = interviewResultNote;
    }

    public Date getDestroyAt() {
        return destroyAt;
    }

    public void setDestroyAt(Date destroyAt) {
        this.destroyAt = destroyAt;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getDestroyReason() {
        return destroyReason;
    }

    public void setDestroyReason(String destroyReason) {
        this.destroyReason = destroyReason;
    }

    @Override
    public void inputData(Scanner scanner) {
        System.out.println("Nhập ID ứng viên:");
        this.candidateId = Integer.parseInt(scanner.nextLine());
        System.out.println("Nhập ID vị trí tuyển dụng:");
        this.recruitmentPositionId = Integer.parseInt(scanner.nextLine());
        System.out.println("Nhập URL CV:");
        this.cvUrl = scanner.nextLine();
        System.out.println("Nhập trạng thái tiến trình (pending/handling/interviewing/done):");
        this.progress = Progress.valueOf(scanner.nextLine().toUpperCase());
        System.out.println("Nhập ngày yêu cầu phỏng vấn (dd/MM/yyyy HH:mm:ss, để trống nếu không có):");
        String interviewRequestDateStr = scanner.nextLine();
        if (!interviewRequestDateStr.isEmpty()) {
            try {
                this.interviewRequestDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(interviewRequestDateStr);
            } catch (ParseException e) {
                System.err.println("Định dạng ngày không hợp lệ, đặt ngày yêu cầu phỏng vấn là null.");
                this.interviewRequestDate = null;
            }
        }
        System.out.println("Nhập kết quả yêu cầu phỏng vấn:");
        this.interviewRequestResult = scanner.nextLine();
        System.out.println("Nhập link phỏng vấn:");
        this.interviewLink = scanner.nextLine();
        System.out.println("Nhập thời gian phỏng vấn (dd/MM/yyyy HH:mm:ss, để trống nếu không có):");
        String interviewTimeStr = scanner.nextLine();
        if (!interviewTimeStr.isEmpty()) {
            try {
                this.interviewTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(interviewTimeStr);
            } catch (ParseException e) {
                System.err.println("Định dạng ngày không hợp lệ, đặt thời gian phỏng vấn là null.");
                this.interviewTime = null;
            }
        }
        System.out.println("Nhập kết quả phỏng vấn:");
        this.interviewResult = scanner.nextLine();
        System.out.println("Nhập ghi chú kết quả phỏng vấn:");
        this.interviewResultNote = scanner.nextLine();
        System.out.println("Nhập ngày hủy (dd/MM/yyyy HH:mm:ss, để trống nếu không có):");
        String destroyAtStr = scanner.nextLine();
        if (!destroyAtStr.isEmpty()) {
            try {
                this.destroyAt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(destroyAtStr);
            } catch (ParseException e) {
                System.err.println("Định dạng ngày không hợp lệ, đặt ngày hủy là null.");
                this.destroyAt = null;
            }
        }
        System.out.println("Nhập lý do hủy:");
        this.destroyReason = scanner.nextLine();
    }
}