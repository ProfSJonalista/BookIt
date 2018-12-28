package main.bookit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import main.bookit.R;
import main.bookit.helpers.Children;
import main.bookit.helpers.CalendarService;
import main.bookit.helpers.FirebaseService;
import main.bookit.model.Book;
import main.bookit.model.BookViewModel;
import main.bookit.model.UserBook;

public class ViewBookFragment extends Fragment {

    private TextView titleTextView;
    private TextView authorTextView;
    private TextView genreTextView;
    private TextView publishDateTextView;
    private TextView descriptionContentTextView;
    private Button bookReservationButton;
    private ImageView coverImageView;

    FirebaseService firebaseService;

    private static final String TAG = "ViewBookFragment";

    public static ViewBookFragment newInstance(){
        return new ViewBookFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_book_fragment, container, false);
        return inflater.inflate(R.layout.view_book_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firebaseService = new FirebaseService(TAG);
        setItems(this.getView());

        BookViewModel bookVM = (BookViewModel) this.getActivity().getIntent().getSerializableExtra("Book");
        final Book book = bookVM.getBook();

        titleTextView.setText(book.getTitle());
        authorTextView.setText(book.getAuthor());
        genreTextView.setText(book.getCategory().toString());
        descriptionContentTextView.setText(book.getDescription());

        UserBook userBook = bookVM.getUserBook();
        if(userBook != null){
            //setButton(userBook);
        }
        else if(book.getAmount() > 0){
            bookReservationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userID = firebaseService.getUserId();

                    CalendarService calendarService = new CalendarService();

                    UserBook userBookToAdd = new UserBook(
                            userID,
                            book.getId(),
                            calendarService.getReturnDate(7),
                            calendarService.getLoanDate(),
                            null,
                            false,
                            false);

                    firebaseService.AddOrUpdateUserBook(userBookToAdd);

                    book.setAmount(book.getAmount() - 1);
                    firebaseService.AddOrUpdateBook(book);
                }
            });
        } else {
            bookReservationButton.setText(R.string.book_unavailable);
        }
    }

    private void setItems(View view) {
        titleTextView = view.findViewById(R.id.bookTitle);
        authorTextView = view.findViewById(R.id.author);
        genreTextView = view.findViewById(R.id.genre);
        publishDateTextView = view.findViewById(R.id.publishDate);
        descriptionContentTextView = view.findViewById(R.id.description_content);
        bookReservationButton = view.findViewById(R.id.bookReservationButton);
        coverImageView = view.findViewById(R.id.imageView);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseService.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseService.onStop();
    }

    //add a toast to show when successfully signed in
    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this.getActivity(),message,Toast.LENGTH_SHORT).show();
    }
}
