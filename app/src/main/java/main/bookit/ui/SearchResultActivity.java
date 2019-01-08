package main.bookit.ui;

import android.arch.lifecycle.ViewModelProviders;
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
import main.bookit.helpers.CoverService;
import main.bookit.helpers.BookService;
import main.bookit.helpers.Status;
import main.bookit.helpers.Children;
import main.bookit.helpers.ToolbarService;
import main.bookit.model.Book;
import main.bookit.model.BookViewModel;
import main.bookit.model.UserBook;
import main.bookit.model.viewModels.SearchResultViewModel;
import main.bookit.ui.customs.CustomAdapter;


public class SearchResultActivity extends AppCompatActivity {

    private static final String TAG = "SearchResultActivity";

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

        SearchResultViewModel searchResultViewModel = ViewModelProviders.of(this).get(SearchResultViewModel.class);
        searchResultViewModel.getCustomAdapter().observe(this, customAdapter ->{
            simpleList.setAdapter(customAdapter);
        });

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
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
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

        BookService bookService = new BookService();
        CoverService coverService = new CoverService();

        DataSnapshot bookSnapshot = dataSnapshot.child(Children.BOOKS);
        DataSnapshot userBookSnapshot = dataSnapshot.child(Children.USER_BOOKS).child(userID);

        for (DataSnapshot newBook : bookSnapshot.getChildren()) {
            String bookID = newBook.getKey();

            if (!bookService.matchSearch(newBook, searchBy, searchFor))
                continue;

            Book book = bookService.getBook(newBook);

            UserBook userBook;

            if (userBookSnapshot.getValue() != null) {
                userBook = bookService.getUserBook(userBookSnapshot, bookID);
            } else {
                userBook = null;
            }

            Status statusToAdd = bookService.getStatus(userBook, newBook.getValue(Book.class).getAmount());
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
}
