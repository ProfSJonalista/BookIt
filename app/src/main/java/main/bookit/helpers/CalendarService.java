package main.bookit.helpers;

import java.util.Calendar;
import java.util.Date;

public class CalendarService {
    private Date currentDate = new Date();
    private Calendar calendar = Calendar.getInstance();

    public Date getReturnDate(int daysToAdd) {
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, daysToAdd);
        return calendar.getTime();
    }

    public Date getLoanDate() {
        return currentDate;
    }

    public boolean checkIfReservationDateHasPassed(Date bookDate) {
        calendar.setTime(currentDate);
        return calendar.after(bookDate);
    }
}
