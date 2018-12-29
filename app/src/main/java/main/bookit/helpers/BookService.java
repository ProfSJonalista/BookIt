package main.bookit.helpers;

import com.google.firebase.database.DataSnapshot;

import java.util.Date;

import main.bookit.model.Book;
import main.bookit.model.Category;
import main.bookit.model.UserBook;

public class BookService {
    public boolean matchSearch(DataSnapshot bookS, String searchBy, String searchFor) {
        switch (searchBy) {
            case "Author name":
                String author = bookS.getValue(Book.class).getAuthor();
                return author.toLowerCase().contains(searchFor.toLowerCase());

            case "Book name":
                String title = bookS.getValue(Book.class).getTitle();
                return title.toLowerCase().contains(searchFor.toLowerCase());

            case "Category":
                Category category = bookS.getValue(Book.class).getCategory();
                return category.toString().toLowerCase().contains(searchFor.toLowerCase());

            default:
                return true;
        }
    }

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
                //TODO jeśli jest po aktualnej dacie - odrezerwować
                Date bookDate = userBook.getBookDate();
                CalendarService calendarService = new CalendarService();
                if(bookDate != null && calendarService.checkIfReservationDateHasPassed(bookDate)){
                    return Status.AVAILABLE;
                }

                return Status.RESERVED;
            }

            Date returnDate = userBook.getReturnDate();

            if (returnDate != null || !returnDate.equals("")) {
                //TODO jeśli jest po dacie zwrotu, zwiększyść ilość książki o 1 i usunąć z książek użytkownika
                return Status.OWNED;
            }
        }

        return Status.AVAILABLE;
    }

    public UserBook getUserBook(DataSnapshot userBookSnapshot, String bookID) {
        DataSnapshot userBook = userBookSnapshot.child(bookID);

        if(userBook.getValue() == null)
            return null;

        return getUserBook(userBook);
    }

    public UserBook getUserBook(DataSnapshot newUserBook){
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
