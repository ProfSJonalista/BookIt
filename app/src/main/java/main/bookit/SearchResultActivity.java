package main.bookit;

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
import android.widget.Toast;

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

import main.bookit.helpers.CoverService;
import main.bookit.helpers.SearchResultService;
import main.bookit.helpers.Status;
import main.bookit.helpers.Children;
import main.bookit.helpers.ToolbarService;
import main.bookit.model.Book;
import main.bookit.model.BookViewModel;
import main.bookit.model.UserBook;


public class SearchResultActivity extends AppCompatActivity {

    private static final String TAG = "SearchResultActivity";

    //private int flags[] = {R.drawable.cb_every_breath_nicholas_sparks, R.drawable.cb_fire_and_blood_george_martin, R.drawable.cb_stan_lee_bob_batchelor, R.drawable.cb_the_choice_edith_eger, R.drawable.cb_killing_commendatore_haruki_murakami, R.drawable.cb_the_stand_stephen_king};

    private String userID;
    private ListView simpleList;
    private List<BookViewModel> bookList;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String searchFor;
    private String searchBy;
    private ImageView userBooksImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        setToolbar();

        simpleList = (ListView) findViewById(R.id.searchResultListView);

        setFirebase();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ToolbarService toolbarService = new ToolbarService();
        userBooksImage = toolbarService.getUserBooksImageButton(this);
    }

    private void getBundleItems() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            searchFor = bundle.getString("searchBoxValue");
            searchBy = bundle.getString("spinnerValue");
        }
    }

    private void setFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        getBundleItems();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
                // ...
            }
        };

        final String finalSearchFor = searchFor;
        final String finalSearchBy = searchBy;

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot, finalSearchBy, finalSearchFor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot, String searchBy, String searchFor) {
        bookList = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<String> authors = new ArrayList<>();
        final List<Status> statuses = new ArrayList<>();
        List<Integer> covers = new ArrayList<>();
        CoverService coverService = new CoverService();

        DataSnapshot bookSnapshot = dataSnapshot.child(Children.BOOKS);
        SearchResultService searchResultService = new SearchResultService();

        for (DataSnapshot newBook : bookSnapshot.getChildren()) {
            String bookID = newBook.getKey();

            if (!searchResultService.matchSearch(newBook, searchBy, searchFor))
                continue;

            Book book = new Book(bookID,
                    newBook.getValue(Book.class).getTitle(),
                    newBook.getValue(Book.class).getDescription(),
                    newBook.getValue(Book.class).getAuthor(),
                    newBook.getValue(Book.class).getAmount(),
                    newBook.getValue(Book.class).getCategory());

            UserBook userBook = searchResultService.getUserBook(dataSnapshot, userID, bookID);
            Status statusToAdd = searchResultService.getStatus(userBook, newBook.getValue(Book.class).getAmount());
            Integer coverId = coverService.getCover(bookID);

            BookViewModel bookViewModel = new BookViewModel(book, userBook, coverId, statusToAdd);
            bookList.add(bookViewModel);

            titles.add(book.getTitle());
            authors.add(book.getAuthor());
            statuses.add(statusToAdd);
            covers.add(coverId);
        }

        String[] title = new String[titles.size()];
        String[] author = new String[authors.size()];
        Status[] status = new Status[statuses.size()];
        int[] flags = ArrayUtils.toPrimitiveArray(covers);

        titles.toArray(title);
        authors.toArray(author);
        statuses.toArray(status);

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), title, author, status, flags);
        final Context context = this;
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookViewModel bookToShow = bookList.get(position);
                Status bookStatus = statuses.get(position);

                Intent intent = new Intent();
                intent.setClass(context, BookPageActivity.class);
                intent.putExtra("Book", bookToShow);

                startActivity(intent);
            }
        });

        simpleList.setAdapter(customAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
