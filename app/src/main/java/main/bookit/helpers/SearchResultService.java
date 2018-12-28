package main.bookit.helpers;

import com.google.firebase.database.DataSnapshot;

import java.util.Date;

import main.bookit.model.Book;
import main.bookit.model.Category;
import main.bookit.model.UserBook;
import main.bookit.ui.fragments.ViewBookFragment;

public class SearchResultService {
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

    public UserBook getUserBook(DataSnapshot dataSnapshot, String userID, String bookID) {
        DataSnapshot user = dataSnapshot.child(Children.USER_BOOKS).child(userID);

        if (user.getValue() == null)
            return null;

        DataSnapshot userBook = user.child(bookID);

        if(userBook.getValue() == null)
            return null;

        return new UserBook(
                userBook.getValue(UserBook.class).getUserId(),
                userBook.getValue(UserBook.class).getBookId(),
                userBook.getValue(UserBook.class).getReturnDate(),
                userBook.getValue(UserBook.class).getLoanDate(),
                userBook.getValue(UserBook.class).getBookDate(),
                userBook.getValue(UserBook.class).getBooked(),
                userBook.getValue(UserBook.class).getReturned()
        );

    }
}
