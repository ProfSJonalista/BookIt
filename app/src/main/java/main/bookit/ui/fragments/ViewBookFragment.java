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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import main.bookit.R;
import main.bookit.helpers.Children;
import main.bookit.helpers.CalendarService;
import main.bookit.helpers.Status;
import main.bookit.helpers.ToolbarService;
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

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    private CalendarService calendarService;

    private ImageView userBooksImage;

    private static final String TAG = "ViewBookFragment";

    public static ViewBookFragment newInstance() {
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
        setItems(this.getView());
        setFirebase();
        setToolbarActions();
        calendarService = new CalendarService();

        BookViewModel bookVM = (BookViewModel) this.getActivity().getIntent().getSerializableExtra("Book");
        Status bookStatus = bookVM.getStatus();
        final Book book = bookVM.getBook();

        titleTextView.setText(book.getTitle());
        authorTextView.setText(book.getAuthor());
        genreTextView.setText(book.getCategory().toString());
        descriptionContentTextView.setText(book.getDescription());
        coverImageView.setImageResource(bookVM.getCoverId());

        final UserBook userBook = bookVM.getUserBook();
        if (userBook != null) {
            if(bookStatus.equals(Status.RESERVED)){
                setButton(getString(R.string.book_reservation_expires), userBook.getBookExpirationDate());
            } else {
                setButton(getString(R.string.book_return), userBook.getReturnDate());
            }
        } else if (book.getAmount() > 0 && !(bookStatus.equals(Status.RESERVED) || bookStatus.equals(Status.OWNED))) {
            bookReservationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userID = mAuth.getCurrentUser().getUid();

                    UserBook userBookToAdd = new UserBook(
                            userID,
                            book.getId(),
                            null,
                            null,
                            calendarService.getCurrentDate(),
                            calendarService.getExpirationDate(3),
                            true,
                            false);

                    myRef.child(Children.USER_BOOKS).child(userBookToAdd.getUserId()).child(userBookToAdd.getBookId()).setValue(userBookToAdd);

                    book.setAmount(book.getAmount() - 1);
                    myRef.child(Children.BOOKS).child(book.getId()).setValue(book);

                    setButton(getString(R.string.book_reservation_expires), userBookToAdd.getBookExpirationDate());
                }
            });
        } else {
            bookReservationButton.setText(getString(R.string.book_unavailable));
        }
    }

    private void setToolbarActions() {
        ToolbarService toolbarService = new ToolbarService();
        userBooksImage = toolbarService.getUserBooksImageButton(this.getActivity());
    }

    private void setButton(String message, Date bookExpirationDate) {
        String text = message + " " + calendarService.getDateAsString(bookExpirationDate);
        bookReservationButton.setText(text);
    }

    private void setFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
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
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }
}
