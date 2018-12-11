package main.bookit.helpers;

import com.google.firebase.database.DataSnapshot;

import main.bookit.model.Book;
import main.bookit.model.Category;
import main.bookit.model.UserBook;

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

    public Status getStatus(DataSnapshot dataSnapshot, String bookId, Integer amount, String userID) {
        DataSnapshot userBooks = dataSnapshot.child(Children.USER_BOOKS).child(userID);

        if (userBooks.getValue() == null) {
            if (amount == 0) {
                return Status.NOT_AVAILABLE;
            } else {
                return Status.AVAILABLE;
            }
        } else {
            DataSnapshot book = userBooks.child(bookId);

            if (book.getValue() == null && amount > 0) {
                return Status.AVAILABLE;
            } else if (book.getValue() == null && amount == 0) {
                return Status.NOT_AVAILABLE;
            } else {
                String reserved = book.getValue(UserBook.class).getReturnDate();

                if (reserved != null || !reserved.equals("")) {
                    //jeśli jest po aktualnej dacie - odrezerwować i zwrócić available
                    return Status.RESERVED;
                }

                String deadLine = book.getValue(UserBook.class).getReturnDate();

                if (deadLine != null || deadLine.equals("")) {
                    return Status.OWNED;
                }
            }
        }

        return Status.AVAILABLE;
    }
}
