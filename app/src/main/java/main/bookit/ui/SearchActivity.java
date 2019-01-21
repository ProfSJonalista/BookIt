package main.bookit.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import main.bookit.R;
import main.bookit.helpers.ToolbarService;


public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    Toolbar toolbar;
    EditText searchBox;
    Button searchButton;
    Spinner categorySpinner;
    //toolbar
    ImageView userBooksImage;
    ImageView settingsImage;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setToolbar();
        setFirebase();
        setCategorySpinner();

        searchBox = (EditText) findViewById(R.id.searchBox);
        setButton();
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ToolbarService toolbarService = new ToolbarService();
        userBooksImage = toolbarService.getUserBooksImageButton(this);
        settingsImage = toolbarService.getSettingsImageButton(this);
    }

    private void setFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
        };
    }

    private void setCategorySpinner() {
        categorySpinner = findViewById(R.id.categorySpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void setButton() {
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {

            final String searchBoxValue = searchBox.getText().toString();
            final String spinnerValue = categorySpinner.getSelectedItem().toString();

            //if search box value is not empty, value is put in the bundle to take it to next activity and starts SearchResultActivity
            if (!searchBoxValue.equals("")) {
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("searchBoxValue", searchBoxValue);
                bundle.putString("spinnerValue", spinnerValue);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                toastMessage("Search box cannot be empty!");
            }
        });
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
