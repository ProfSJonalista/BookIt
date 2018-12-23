package main.bookit;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import main.bookit.helpers.SearchResultService;
import main.bookit.helpers.Status;
import main.bookit.helpers.Children;
import main.bookit.model.Book;
import main.bookit.model.Category;
import main.bookit.model.UserBook;


public class SearchResultActivity extends AppCompatActivity {

    private static final String TAG = "SearchResultActivity";

    private int flags[] = {R.drawable.cover_book_1, R.drawable.cover_book_2, R.drawable.cover_book_3, R.drawable.cover_book_4, R.drawable.cover_book_5, R.drawable.cover_book_6};

    private String userID;
    private FirebaseAuth mAuth;
    private ListView simpleList;
    private List<Book> bookList;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        simpleList = (ListView) findViewById(R.id.searchResultListView);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        Bundle bundle = getIntent().getExtras();
        String searchFor = null;
        String searchBy = null;
        if (bundle != null) {
            searchFor = bundle.getString("searchBoxValue");
            searchBy = bundle.getString("spinnerValue");
        }

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
        List<Status> statuses = new ArrayList<>();
        List<Integer> covers = new ArrayList<>();

        DataSnapshot bookSnapshot = dataSnapshot.child(Children.BOOKS);
        SearchResultService searchResultService = new SearchResultService();

        for (DataSnapshot bookS : bookSnapshot.getChildren()) {
            String bookId = bookS.getKey();

            if(!searchResultService.matchSearch(bookS, searchBy, searchFor))
                continue;

            Book book = new Book(bookId,
                    bookS.getValue(Book.class).getTitle(),
                    bookS.getValue(Book.class).getDescription(),
                    bookS.getValue(Book.class).getAuthor(),
                    bookS.getValue(Book.class).getAmount(),
                    bookS.getValue(Book.class).getCategory());

            bookList.add(book);
            titles.add(book.getTitle());
            authors.add(book.getAuthor());
            statuses.add(searchResultService.getStatus(dataSnapshot, bookId, bookS.getValue(Book.class).getAmount(), userID));
            covers.add(flags[0]);
        }

        String[] Title = new String[titles.size()];
        String[] Author = new String[authors.size()];
        Status[] Status = new Status[statuses.size()];


        titles.toArray(Title);
        authors.toArray(Author);
        statuses.toArray(Status);

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), Title, Author, Status, flags);
        final Context context = this;
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book bookToShow = bookList.get(position);

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
