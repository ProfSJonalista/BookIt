package main.bookit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class SearchResultActivity extends AppCompatActivity {



        ListView simpleList;
        String  Title[] = {"Every Breath", "Fire and Blood", "Stan Lee", "The Choice", "Killing Commendatore", "The Stand"};
        String  Author[] = {"Nicholas Sparks", "George R. R. Martin", "Bob Batchelor", "Edith Eger", "Haruki Murakami", "Stephen King"};
        String  Status[] = {"Available", "Not available", "Owned", "Reserved", "Available", "Not available"};
        int flags[] = {R.drawable.cover_book_1, R.drawable.cover_book_2, R.drawable.cover_book_3, R.drawable.cover_book_4, R.drawable.cover_book_5, R.drawable.cover_book_6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        simpleList = (ListView)findViewById(R.id.ListView);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), Title, Author, Status, flags);
        simpleList.setAdapter(customAdapter);
        }
    }




