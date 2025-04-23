package ra.edu.business.model.application;

import ra.edu.business.model.Inputable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Scanner;

public class Application implements Inputable {
    private int id;
    private int candidateId;
    private int recruitmentPositionId;
    private String cvUrl;
    private Progress progress;
    private LocalDateTime interviewRequestDate;
    private String interviewRequestResult;
    private String interviewLink;
    private LocalDateTime interviewTime;
    private String interviewResult;
    private String interviewResultNote;
    private LocalDateTime destroyAt;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String destroyReason;

    public Application() {
    }

    public Application(int id, int candidateId, int recruitmentPositionId, String cvUrl, Progress progress,
                       LocalDateTime interviewRequestDate, String interviewRequestResult, String interviewLink,
                       LocalDateTime interviewTime, String interviewResult, String interviewResultNote, LocalDateTime destroyAt,
                       LocalDateTime createAt, LocalDateTime updateAt, String destroyReason) {
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

    public void setInterviewRequestDate(LocalDateTime interviewRequestDate) {
        this.interviewRequestDate = interviewRequestDate;
    }

    public void setInterviewTime(LocalDateTime interviewTime) {
        this.interviewTime = interviewTime;
    }

    public LocalDateTime getDestroyAt() {
        return destroyAt;
    }

    public void setDestroyAt(LocalDateTime destroyAt) {
        this.destroyAt = destroyAt;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

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

    public LocalDateTime getInterviewRequestDate() {
        return interviewRequestDate;
    }

    public LocalDateTime getInterviewTime() {
        return interviewTime;
    }

    public String getDestroyReason() {
        return destroyReason;
    }

    public void setDestroyReason(String destroyReason) {
        this.destroyReason = destroyReason;
    }

    @Override
    public void inputData(Scanner scanner) {
        System.out.println("Nhập URL CV:");
        this.cvUrl = scanner.nextLine();
        this.progress = Progress.PENDING;
        this.interviewRequestDate = null;
        this.interviewRequestResult = null;
        this.interviewLink = null;
        this.interviewTime = null;
        this.interviewResult = null;
        this.interviewResultNote = null;
        this.destroyAt = null;
        this.destroyReason = null;
    }
}