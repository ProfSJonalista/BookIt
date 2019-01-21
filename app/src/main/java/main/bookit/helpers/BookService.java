package main.bookit.helpers;

import com.google.firebase.database.DataSnapshot;

import java.util.Date;

import main.bookit.model.Book;
import main.bookit.model.Category;
import main.bookit.model.UserBook;

public class BookService {

    //checks if current book matches the search requirements
    public boolean matchSearch(DataSnapshot bookToCheck, String searchBy, String searchFor) {
        switch (searchBy) {
            case "Author name":
                String author = bookToCheck.getValue(Book.class).getAuthor();
                return author.toLowerCase().contains(searchFor.toLowerCase());

            case "Book name":
                String title = bookToCheck.getValue(Book.class).getTitle();
                return title.toLowerCase().contains(searchFor.toLowerCase());

            case "Category":
                Category category = bookToCheck.getValue(Book.class).getCategory();
                return category.toString().toLowerCase().contains(searchFor.toLowerCase());

            default:
                return true;
        }
    }

    //gets status of the book
    public Status getStatus(UserBook userBook, Integer amount) {

        if (userBook == null) {
            if (amount == 0) {
                return Status.NOT_AVAILABLE;
            } else {
                return Status.AVAILABLE;
            }
        } else {
            boolean isBooked = userBook.getBooked();

            if (isBooked) {
                Date bookDate = userBook.getBookDate();
                CalendarService calendarService = new CalendarService();
                if (bookDate != null && calendarService.checkIfReservationDateHasPassed(bookDate)) {
                    return Status.AVAILABLE;
                }

                return Status.RESERVED;
            }

            Date returnDate = userBook.getReturnDate();

            if (returnDate != null || !returnDate.equals("")) {
                return Status.OWNED;
            }
        }

        return Status.AVAILABLE;
    }

    //returns
    public UserBook getUserBook(DataSnapshot userBookSnapshot, String bookID) {
        DataSnapshot userBook = userBookSnapshot.child(bookID);

        if (userBook.getValue() == null)
            return null;

        return getUserBook(userBook);
    }

    //maps DataSnapshot to UserBook
    public UserBook getUserBook(DataSnapshot newUserBook) {
        return new UserBook(
                newUserBook.getValue(UserBook.class).getUserId(),
                newUserBook.getValue(UserBook.class).getBookId(),
                newUserBook.getValue(UserBook.class).getReturnDate(),
                newUserBook.getValue(UserBook.class).getLoanDate(),
                newUserBook.getValue(UserBook.class).getBookDate(),
                newUserBook.getValue(UserBook.class).getBookExpirationDate(),
                newUserBook.getValue(UserBook.class).getBooked(),
                newUserBook.getValue(UserBook.class).getReturned()
        );
    }

    //maps DataSnapshot to Book
    public Book getBook(DataSnapshot newBook) {
        return new Book(
                newBook.getValue(Book.class).getId(),
                newBook.getValue(Book.class).getTitle(),
                newBook.getValue(Book.class).getDescription(),
                newBook.getValue(Book.class).getAuthor(),
                newBook.getValue(Book.class).getAmount(),
                newBook.getValue(Book.class).getCategory());
    }
}
