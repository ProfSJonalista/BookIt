package main.bookit.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarService {
    private Date currentDate = new Date();
    private Calendar calendar = Calendar.getInstance();

    public Date getExpirationDate(int daysToAdd) {
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, daysToAdd);
        return calendar.getTime();
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    boolean checkIfReservationDateHasPassed(Date bookDate) {
        calendar.setTime(currentDate);
        return calendar.after(bookDate);
    }

    public String getDateAsString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }
}
