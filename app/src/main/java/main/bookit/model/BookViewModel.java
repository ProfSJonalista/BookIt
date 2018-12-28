package main.bookit.model;

import java.io.Serializable;

public class BookViewModel implements Serializable {
    private Book book;
    private UserBook userBook;

    public BookViewModel(Book book, UserBook userBook) {
        this.book = book;
        this.userBook = userBook;
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
}
