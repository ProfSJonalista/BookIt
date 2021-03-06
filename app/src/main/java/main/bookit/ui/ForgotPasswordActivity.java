package main.bookit.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import main.bookit.R;
import main.bookit.helpers.ToolbarService;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";

    private EditText editText;
    private Button confirmButton;
    private FirebaseAuth mAuth;    //Firebase authentication manager

    //toolbar
    private Toolbar toolbar;
    private ImageView homeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setToolbar();
        mAuth = FirebaseAuth.getInstance();
        setItems();
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ToolbarService toolbarService = new ToolbarService();
        homeIcon = toolbarService.getHomeButton(this);
    }

    private void setItems() {
        editText = (EditText) findViewById(R.id.emailBox);
        confirmButton = (Button) findViewById(R.id.button_confirm);

        //sets on click listener to send email
        confirmButton.setOnClickListener(
                v -> {
                    final String email = editText.getText().toString();

                    //if email contains @, email is sent to the given address
                    if (email.contains("@")) {
                        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(
                                task -> {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, getString(R.string.email_sent) + email);
                                        toastMessage(getString(R.string.email_sent) + email);
                                    }
                                });
                        //else toast is shown about wrong email
                    } else {
                        toastMessage(getString(R.string.wrong_email));
                    }
                });
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
