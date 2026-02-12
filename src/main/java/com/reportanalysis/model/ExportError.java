package com.reportanalysis.model;

public class ExportError {

    private Long userId;
    private Long reportId;
    private String reportDate;
    private String exceptionType;
    private String exceptionMessage;

    public Long getUserId() { 
    	return userId;
    	}
    public void setUserId(Long userId) {
    	this.userId = userId;
    	}

    public Long getReportId() {
    	return reportId;
    	}
    public void setReportId(Long reportId) {
    	this.reportId = reportId;
    	}

    public String getReportDate() {
    	return reportDate;
    	}
    public void setReportDate(String reportDate) {
    	this.reportDate = reportDate;
    	}

    public String getExceptionType() {
    	return exceptionType;
    	}
    public void setExceptionType(String exceptionType) {
    	this.exceptionType = exceptionType;
    	}

    public String getExceptionMessage() {
    	return exceptionMessage; 
    	}
    public void setExceptionMessage(String exceptionMessage) {
    	this.exceptionMessage = exceptionMessage;
    	}
}
