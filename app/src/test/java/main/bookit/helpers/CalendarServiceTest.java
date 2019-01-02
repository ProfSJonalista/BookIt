package main.bookit.helpers;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class CalendarServiceTest {
    private CalendarService calendarService = new CalendarService();
    private Date date = new Date(117, 5, 5);

    @Test
    public void getExpirationDate() {

        Date dateToCheck = new Date();
        dateToCheck.setDate(dateToCheck.getDate() + 3);

        assertEquals(calendarService.getDateAsString(dateToCheck), calendarService.getDateAsString(calendarService.getExpirationDate(3)));
    }

    @Test
    public void getCurrentDate() {
        Date currentDate = calendarService.getCurrentDate();
        assertEquals(calendarService.getDateAsString(new Date()), calendarService.getDateAsString(currentDate));
    }

    @Test
    public void checkIfReservationDateHasPassed() {
        Boolean hasPassed = calendarService.checkIfReservationDateHasPassed(date);
        assertFalse(hasPassed);
    }

    @Test
    public void getDateAsString() {
        String dateAsString = calendarService.getDateAsString(date);
        assertEquals("05/06/2017", dateAsString);
    }
}