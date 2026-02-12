package com.reportanalysis.repository.impl;

import com.reportanalysis.config.DatabaseConfig;
import com.reportanalysis.model.DailyReport;
import com.reportanalysis.repository.ReportRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportRepositoryImpl implements ReportRepository {

	@Override
	public List<DailyReport> findAllReports() {

	    List<DailyReport> reports = new ArrayList<>();
	    String sql = "SELECT * FROM daily_reports";

	    try (Connection conn = DatabaseConfig.getConnection()) {

	    	System.out.println("Connected DB URL: " + conn.getMetaData().getURL());
	    	System.out.println("Connected DB User: " + conn.getMetaData().getUserName());

	    	ResultSet Checking = conn.createStatement().executeQuery("SELECT COUNT(*) FROM daily_reports");

	    	if (Checking.next()) {
	    	    System.out.println("COUNT from SAME connection = " + Checking.getInt(1));
	    	}

	        try (PreparedStatement ps = conn.prepareStatement(sql);
	             ResultSet rs = ps.executeQuery()) {

	            int rowCount = 0;

	            while (rs.next()) {

	                DailyReport report = new DailyReport();

	                report.setReportId(rs.getLong("report_id"));
	                report.setUserId(rs.getLong("user_id"));
	                report.setChats(rs.getInt("chats"));
	                report.setCalls(rs.getInt("calls"));
	                report.setMeetings(rs.getInt("meetings"));
	                report.setWebinars(rs.getInt("webinars"));
	            
	                if (rs.getDate("report_date") != null) {
	                    report.setReportDate(rs.getDate("report_date").toLocalDate());
	                }

	                report.setCreatedTime(rs.getLong("created_time"));

	                reports.add(report);
	                rowCount++;
	            }
	            System.out.println("Total reports fetched from DB: " + rowCount);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Failed to fetch reports", e);
	    }

	    return reports;
	}

    @Override
    public List<DailyReport> findReportsByUserAndDateRange(Long userId,LocalDate startDate,LocalDate endDate) {

        List<DailyReport> reports = new ArrayList<>();

        String sql = "SELECT * FROM daily_reports " + "WHERE user_id = ? AND report_date BETWEEN ? AND ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setDate(2, Date.valueOf(startDate));
            ps.setDate(3, Date.valueOf(endDate));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                DailyReport report = new DailyReport();
                report.setReportId(rs.getLong("report_id"));
                report.setUserId(rs.getLong("user_id"));
                report.setChats(rs.getInt("chats"));
                report.setCalls(rs.getInt("calls"));
                report.setMeetings(rs.getInt("meetings"));
                report.setWebinars(rs.getInt("webinars"));
                report.setReportDate(
                        rs.getDate("report_date") != null ? rs.getDate("report_date").toLocalDate(): null);
                report.setCreatedTime(rs.getLong("created_time"));

                reports.add(report);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch reports by date range", e);
        }

        return reports;
    }
}
