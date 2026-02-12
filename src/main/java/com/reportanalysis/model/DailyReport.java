package com.reportanalysis.model;

import java.time.LocalDate;

public class DailyReport {

    private Long reportId;
    private Long userId;
    private int chats;
    private int calls;
    private int meetings;
    private int webinars;
    private LocalDate reportDate;
    private Long createdTime;

    public Long getReportId() { 
    	return reportId; 
    	}
    public void setReportId(Long reportId) {
    	this.reportId = reportId;
    	}

    public Long getUserId() { 
    	return userId; 
    	}
    public void setUserId(Long userId) { 
    	this.userId = userId; 
    	}

    public int getChats() { 
    	return chats;
    	}
    public void setChats(int chats) {
    	this.chats = chats;
    	}

    public int getCalls() {
    	return calls;
    	}
    public void setCalls(int calls) {
    	this.calls = calls;
    	}

    public int getMeetings() {
    	return meetings;
    	}
    public void setMeetings(int meetings) {
    	this.meetings = meetings;
    	}

    public int getWebinars() { 
    	return webinars; 
    	}
    public void setWebinars(int webinars) {
    	this.webinars = webinars; 
    	}

    public LocalDate getReportDate() { 
    	return reportDate; 
    	}
    public void setReportDate(LocalDate reportDate) {
    	this.reportDate = reportDate; 
    	}

    public Long getCreatedTime() { 
    	return createdTime; 
    	}
    public void setCreatedTime(Long createdTime) { 
    	this.createdTime = createdTime; 
    	}
}
