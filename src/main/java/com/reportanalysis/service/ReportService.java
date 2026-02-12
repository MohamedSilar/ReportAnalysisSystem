package com.reportanalysis.service;

import com.reportanalysis.model.DailyReport;
import com.reportanalysis.model.ExportSummary;
import com.reportanalysis.model.User;

import java.util.List;

public interface ReportService {

    List<DailyReport> getLast30DaysReport(Long userId);

    ExportSummary calculateSummary(List<DailyReport> reports);

    String exportUserReport(Long userId);

    String exportAllUsersFullReportWithErrorSheet();
    List<User> getAllUsers();  
}
