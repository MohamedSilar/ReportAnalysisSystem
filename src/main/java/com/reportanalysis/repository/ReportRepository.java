package com.reportanalysis.repository;

import com.reportanalysis.model.DailyReport;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository {

    List<DailyReport> findReportsByUserAndDateRange(Long userId,LocalDate startDate,LocalDate endDate);

    List<DailyReport> findAllReports();  
}
