package main.bookit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import main.bookit.model.Book;

public class BookPageActivity extends AppCompatActivity {

    TextView titleTextView;
    TextView authorTextView;
    TextView genreTextView;
    TextView publishDateTextView;
    TextView descriptionTextView;

    Button bookReservationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Book book = (Book) getIntent().getSerializableExtra("Book");

    }
}
