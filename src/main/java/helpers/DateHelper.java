package helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    public String getCurrentDate(String dateFormat) {
        SimpleDateFormat simpleDate = new SimpleDateFormat(dateFormat);
        Date currentDate = new Date();
        String date = simpleDate.format(currentDate);
        if (date.contains("31")) {
            date = getFutureDate(0, 0, 1, dateFormat);
        }
        return date;
    }

    public String getFutureDate(int addedYears, int addedMonths, int addedDays, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);


        Date currentDate = new Date();
        //System.out.println(dateFormat.format(currentDate));

        // convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);

        // manipulate date
        c.add(Calendar.YEAR, addedYears);
        c.add(Calendar.MONTH, addedMonths);
        c.add(Calendar.DATE, addedDays); //same with c.add(Calendar.DAY_OF_MONTH, 1);

        // convert calendar to date
        Date currentDatePlus = c.getTime();

        //System.out.println(dateFormat.format(currentDatePlus));

        return dateFormat.format(currentDatePlus);
    }

    public String getRequiredDayDate(int days, String dateFormat) {
        SimpleDateFormat simpleDate = new SimpleDateFormat(dateFormat);
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, days);
        currentDate = c.getTime();
        return simpleDate.format(currentDate);
    }

    public int getCurrentDay() {
        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();
        return currentDay;
    }

    public Month getCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        return currentMonth;
    }

    public int getCurrentYear() {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        return currentYear;
    }
}

