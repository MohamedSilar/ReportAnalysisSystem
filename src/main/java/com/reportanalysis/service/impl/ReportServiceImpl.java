package com.reportanalysis.service.impl;

import com.reportanalysis.model.DailyReport;
import com.reportanalysis.model.ExportError;
import com.reportanalysis.model.ExportSummary;
import com.reportanalysis.model.User;
import com.reportanalysis.repository.ReportRepository;
import com.reportanalysis.repository.UserRepository;
import com.reportanalysis.repository.impl.ReportRepositoryImpl;
import com.reportanalysis.repository.impl.UserRepositoryImpl;
import com.reportanalysis.service.ReportService;
import com.reportanalysis.util.DateUtil;
import com.reportanalysis.util.ExcelUtil;
import com.reportanalysis.util.ThreadPoolManager;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class ReportServiceImpl implements ReportService {

    private final UserRepository userRepository = new UserRepositoryImpl();
    private final ReportRepository reportRepository = new ReportRepositoryImpl();
    @Override
    public List<DailyReport> getLast30DaysReport(Long userId) {

        User user = userRepository.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        LocalDate endDate = DateUtil.getToday();
        LocalDate startDate = DateUtil.getLast30DaysStartDate();

        return reportRepository.findReportsByUserAndDateRange(userId, startDate, endDate);
    }
    @Override
    public ExportSummary calculateSummary(List<DailyReport> reports) {

        int totalChats = 0;
        int totalCalls = 0;
        int totalMeetings = 0;
        int totalWebinars = 0;

        for (DailyReport report : reports) {
            totalChats += report.getChats();
            totalCalls += report.getCalls();
            totalMeetings += report.getMeetings();
            totalWebinars += report.getWebinars();
        }

        ExportSummary summary = new ExportSummary();
        summary.setTotalChats(totalChats);
        summary.setTotalCalls(totalCalls);
        summary.setTotalMeetings(totalMeetings);
        summary.setTotalWebinars(totalWebinars);

        return summary;
    }

    @Override
    public String exportUserReport(Long userId) {

        User user = userRepository.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        List<DailyReport> reports = getLast30DaysReport(userId);
        ExportSummary summary = calculateSummary(reports);

        return ExcelUtil.generateUserReportExcel(user, reports, summary);
    }

    public String exportAllUsersFullReportWithErrorSheet() {

        List<User> users = userRepository.findAllUsers();
        List<DailyReport> reports = reportRepository.findAllReports();

        Map<Long, List<DailyReport>> reportMap =
                reports.stream()
                        .collect(Collectors.groupingBy(DailyReport::getUserId));

        List<Object[]> successData = Collections.synchronizedList(new ArrayList<>());
        List<ExportError> errorData = Collections.synchronizedList(new ArrayList<>());

        ExecutorService executor = ThreadPoolManager.getExecutor();
        List<Future<?>> futures = new ArrayList<>();

        for (User user : users) {

            Future<?> future = executor.submit(() -> {

                List<DailyReport> userReports =
                        reportMap.getOrDefault(user.getId(), Collections.emptyList());

                for (DailyReport report : userReports) {
                    try {
                    	if (user.getId() == 55L) {
                            throw new NullPointerException("Manual test NPE");
                        }

                        successData.add(new Object[]{report.getReportId(),user.getUsername(),user.getEmail(),user.getCountryCode(),user.getMobileNumber(),report.getChats(),report.getCalls(),report.getMeetings(),report.getWebinars(),report.getCreatedTime()});

                    } catch (Exception e) {

                        ExportError error = new ExportError();
                        error.setUserId(user.getId());
                        error.setReportId(report.getReportId());
                        error.setReportDate(report.getReportDate() != null ?report.getReportDate().toString() : "NULL");
                        error.setExceptionType(e.getClass().getSimpleName());
                        error.setExceptionMessage(e.getMessage());

                        errorData.add(error);
                    }
                }
            });

            futures.add(future);
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                throw new RuntimeException("Thread execution failed", e);
            }
        }

        return ExcelUtil.generateExcelWithErrorSheet(successData, errorData);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllUsers();
    }
}
