package com.reportanalysis.model;

public class ExportSummary {

    private int totalChats;
    private int totalCalls;
    private int totalMeetings;
    private int totalWebinars;

    public int getTotalChats() { 
    	return totalChats; 
    	}
    public void setTotalChats(int totalChats) {
    	this.totalChats = totalChats; 
    	}

    public int getTotalCalls() { 
    	return totalCalls; 
    	}
    public void setTotalCalls(int totalCalls) {
    	this.totalCalls = totalCalls; 
    	}

    public int getTotalMeetings() {
    	return totalMeetings; 
    	}
    public void setTotalMeetings(int totalMeetings) {
    	this.totalMeetings = totalMeetings; 
    	}

    public int getTotalWebinars() { 
    	return totalWebinars; 
    	}
    public void setTotalWebinars(int totalWebinars) {
    	this.totalWebinars = totalWebinars; 
    	}
}
