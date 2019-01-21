package main.bookit.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import main.bookit.R;
import main.bookit.helpers.BookService;
import main.bookit.helpers.Children;
import main.bookit.helpers.CoverService;
import main.bookit.helpers.Status;
import main.bookit.helpers.ToolbarService;
import main.bookit.model.Book;
import main.bookit.model.BookViewModel;
import main.bookit.model.User;
import main.bookit.model.UserBook;
import main.bookit.ui.customs.CustomAdapter;


public class UserBooksActivity extends AppCompatActivity {

    private static final String TAG = "UserBooksActivity";

    private String userID;
    private ListView simpleList;
    private List<BookViewModel> bookList;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;

    private ImageView settingsImage;
    private ImageView searchImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_books);
        setToolbar();

        simpleList = (ListView) findViewById(R.id.ListView);

        setFirebase();
    }

    //sets toolbar
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ToolbarService toolbarService = new ToolbarService();
        settingsImage = toolbarService.getSettingsImageButton(this);
        searchImage = toolbarService.getSearchImageButton(this);
    }

    //sets Firebase
    private void setFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        //shows user books
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        bookList = new ArrayList<>();

        //creates temporary lists to adapt in CustomAdapter for list view
        List<String> titles = new ArrayList<>();
        List<String> authors = new ArrayList<>();
        List<Status> statuses = new ArrayList<>();
        List<Integer> covers = new ArrayList<>();

        BookService bookService = new BookService();
        CoverService coverService = new CoverService();

        //gets DataSnapshot of books
        DataSnapshot bookSnapshot = dataSnapshot.child(Children.BOOKS);

        //gets DataSnapshot of user books
        DataSnapshot userBooksSnapshot = dataSnapshot.child(Children.USER_BOOKS).child(userID);

        //if user books is not null, it shows books
        if (userBooksSnapshot.getValue() != null) {
            for (DataSnapshot newUserBook : userBooksSnapshot.getChildren()) {
                //gets mapped user book
                UserBook userBook = bookService.getUserBook(newUserBook);
                //gets mapped book
                Book book = bookService.getBook(bookSnapshot.child(userBook.getBookId()));

                //gets status
                Status statusToAdd = bookService.getStatus(userBook, book.getAmount());
                Integer coverId = coverService.getCover(book.getId());

                BookViewModel bookViewModel = new BookViewModel(book, userBook, coverId, statusToAdd);
                bookList.add(bookViewModel);

                titles.add(book.getTitle());
                authors.add(book.getAuthor());
                statuses.add(statusToAdd);
                covers.add(coverId);
            }

            //lists are converted to primitive arrays to pass it to CustomAdapter constructor
            String[] title = new String[titles.size()];
            String[] author = new String[authors.size()];
            Status[] status = new Status[statuses.size()];
            int[] flags = ArrayUtils.toPrimitiveArray(covers);

            titles.toArray(title);
            authors.toArray(author);
            statuses.toArray(status);

            CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), title, author, status, flags);
            final Context context = this;

            //sets onClickListener to view specific book
            simpleList.setOnItemClickListener(
                    (parent, view, position, id) -> {
                        BookViewModel bookToShow = bookList.get(position);

                        //book is added to intent extras
                        Intent intent = new Intent();
                        intent.setClass(context, UserBookPageActivity.class);
                        intent.putExtra("Book", bookToShow);

                        startActivity(intent);
                    });

            //sets adapter to the list
            simpleList.setAdapter(customAdapter);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}




































































