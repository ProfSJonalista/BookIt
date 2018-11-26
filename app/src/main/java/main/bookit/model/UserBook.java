package main.bookit.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserBook {
    private String userId;
    private String bookId;
    private String returnDate;
    private String loanDate;
    private String bookDate;
    private Boolean isBooked;
    private Boolean isReturned;

    public UserBook (){}

    public UserBook(String userId, String bookId, String returnDate, String loanDate, String bookDate, Boolean isBooked, Boolean isReturned) {
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

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
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
