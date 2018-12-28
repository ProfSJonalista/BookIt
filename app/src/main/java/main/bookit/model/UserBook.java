package main.bookit.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class UserBook {
    private String userId;
    private String bookId;
    private Date returnDate;
    private Date loanDate;
    private Date bookDate;
    private Boolean isBooked;
    private Boolean isReturned;

    public UserBook (){}

    public UserBook(String userId, String bookId, Date returnDate, Date loanDate, Date bookDate, Boolean isBooked, Boolean isReturned) {
        this.userId = userId;
        this.bookId = bookId;
        this.returnDate = returnDate;
        this.loanDate = loanDate;
        this.bookDate = bookDate;
        this.isBooked = isBooked;
        this.isReturned = isReturned;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Date getBookDate() {
        return bookDate;
    }

    public void setBookDate(Date bookDate) {
        this.bookDate = bookDate;
    }

    public Boolean getBooked() {
        return isBooked;
    }

    public void setBooked(Boolean booked) {
        isBooked = booked;
    }

    public Boolean getReturned() {
        return isReturned;
    }

    public void setReturned(Boolean returned) {
        isReturned = returned;
    }
}
