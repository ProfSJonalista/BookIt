package main.bookit.model;

import java.io.Serializable;

import main.bookit.helpers.Status;

public class BookViewModel implements Serializable {
    private Book book;
    private UserBook userBook;
    private Integer coverId;
    private Status status;

    public BookViewModel(Book book, UserBook userBook, Integer coverId, Status status) {
        this.book = book;
        this.userBook = userBook;
        this.coverId = coverId;
        this.status = status;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public UserBook getUserBook() {
        return userBook;
    }

    public void setUserBook(UserBook userBook) {
        this.userBook = userBook;
    }

    public Integer getCoverId() {
        return coverId;
    }

    public void setCoverId(Integer coverId) {
        this.coverId = coverId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
