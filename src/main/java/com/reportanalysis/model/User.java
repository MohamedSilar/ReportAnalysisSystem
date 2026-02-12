package com.reportanalysis.model;

public class User {

    private Long id;
    private String username;
    private String email;
    private String countryCode;
    private String mobileNumber;
    private Long createdAt;

    public Long getId() { 
    	return id;
    	}
    public void setId(Long id) {
    	this.id = id;
    	}

    public String getUsername() { 
    	return username;
    	}
    public void setUsername(String username) {
    	this.username = username;
    	}

    public String getEmail() { 
    	return email;
    	}
    public void setEmail(String email) {
    	this.email = email;
    	}

    public String getCountryCode() {
    	return countryCode;
    	}
    public void setCountryCode(String countryCode) {
    	this.countryCode = countryCode;
    	}

    public String getMobileNumber() {
    	return mobileNumber;
    	}
    public void setMobileNumber(String mobileNumber) {
    	this.mobileNumber = mobileNumber;
    	}

    public Long getCreatedAt() { 
    	return createdAt;
    	}
    public void setCreatedAt(Long createdAt) { 
    	this.createdAt = createdAt;
    	}
}
