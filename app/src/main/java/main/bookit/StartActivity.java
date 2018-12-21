package main.bookit;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.SecureRandom;
import java.util.List;

import io.paperdb.Paper;
import main.bookit.helpers.Children;
import main.bookit.helpers.LocaleHelper;
import main.bookit.model.Book;
import main.bookit.model.Category;
import main.bookit.helpers.RandomString;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";

    private FirebaseAuth mAuth;                             //Firebase authentication manager
    private EditText mPasswordView;                         //password text box
    private AutoCompleteTextView mEmailView;                //email text box
    private FirebaseAuth.AuthStateListener mAuthListener;   //Firebase authentication listener

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Paper.init(this);

        String language = Paper.book().read("language");
        if(language == null){
            Paper.book().write("language", "en");
        }

        //Firebase Authentication initializer
        mAuth = FirebaseAuth.getInstance();

        //listener to check if current user is logged in
        //and to move to next Activity when login state changes
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //downloads user
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //if user isn't null, his ID is written in logs
                    Log.d(TAG, "onAuthStateChanged:signed_in" + user.getUid());
                    //a toast message to view email user used to access
                    toastMessage("Succesfully signed in with: " + user.getEmail());
                    //starts new activity
                    //startActivity(new Intent(StartActivity.this, SearchActivity.class));
                    startActivity(new Intent(StartActivity.this, SettingsActivity.class));
                    //finishes current activity, when new one is opened
                    finish();
                } else {
                    //when user signs out, it is written in logs
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    //a toast message to inform user he signed out
                    toastMessage("Successfully signed out");
                }
            }
        };

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        //listens for the button to be clicked
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //gets user email
                final String email = mEmailView.getText().toString();
                //gets user password
                final String pass = mPasswordView.getText().toString();

                //fetches sign in methods for email user has given
                mAuth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                //if task is successful AND user has sign in methods, logs in user to the application
                                if (task.isSuccessful()) {
                                    SignInMethodQueryResult result = task.getResult();
                                    List<String> signInMethods = result.getSignInMethods();
                                    if (signInMethods != null && signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                                        mAuth.signInWithEmailAndPassword(email, pass);
                                    } else {
                                        mAuth.createUserWithEmailAndPassword(email, pass);
                                    }
                                    //if not, registers user
                                } else {
                                    mAuth.createUserWithEmailAndPassword(email, pass);
                                    Log.e(TAG, "Error getting sign in methods for user", task.getException());
                                }
                            }
                        });
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
        if (mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}