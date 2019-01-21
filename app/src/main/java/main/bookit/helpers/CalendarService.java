package main.bookit.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarService {
    private Date currentDate = new Date();
    private Calendar calendar = Calendar.getInstance();

    //checks expiration date
    public Date getExpirationDate(int daysToAdd) {
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, daysToAdd);
        return calendar.getTime();
    }

    //returns current date
    public Date getCurrentDate() {
        return currentDate;
    }

    //checks if reservation date has passed
    boolean checkIfReservationDateHasPassed(Date bookDate) {
        calendar.setTime(currentDate);
        return calendar.after(bookDate);
    }

    //maps date to string
    public String getDateAsString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }
}
