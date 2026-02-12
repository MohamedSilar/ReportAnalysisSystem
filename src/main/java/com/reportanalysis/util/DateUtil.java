package com.reportanalysis.util;

import java.time.LocalDate;

public class DateUtil {

    public static LocalDate getToday() {
        return LocalDate.now();
    }

    public static LocalDate getLast30DaysStartDate() {
        return LocalDate.now().minusDays(29);
    }
}
