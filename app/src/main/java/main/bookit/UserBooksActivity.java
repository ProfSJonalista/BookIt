package main.bookit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;


public class UserBooksActivity extends AppCompatActivity {



        ListView simpleList;
        String  Title[] = {"Every Breath", "Fire and Blood", "Stan Lee"};
        String  Author[] = {"Nicholas Sparks", "George R. R. Martin", "Bob Batchelor"};
        String  Status[] = {"Owned", "Owned", "Reserved"};
        int flags[] = {R.drawable.cb_every_breath_nicholas_sparks, R.drawable.cb_fire_and_blood_george_martin, R.drawable.cb_stan_lee_bob_batchelor, R.drawable.cb_the_choice_edith_eger, R.drawable.cb_killing_commendatore_haruki_murakami, R.drawable.cb_the_stand_stephen_king};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_books);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //simpleList = (ListView)findViewById(R.id.ListView);
//        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), Title, Author, Status, flags);
//        simpleList.setAdapter(customAdapter);
        }
    }




