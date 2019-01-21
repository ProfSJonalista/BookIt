package main.bookit.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;
import main.bookit.R;
import main.bookit.helpers.LocaleHelper;
import main.bookit.helpers.ToolbarService;
import main.bookit.ui.StartActivity;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";

    private TextView languageChooseText;
    private EditText currentPasswordText;
    private EditText newPasswordText;
    private EditText confirmPasswordText;
    private Button saveButton;
    private Spinner languageSpinner;
    private Button logoutButton;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    //toolbar
    private ImageView userBooksImage;
    private ImageView searchImage;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        setItems(view);

        Paper.init(this.getActivity());

        final String language = Paper.book().read("language");
        if (language == null) {
            Paper.book().write("language", "en");
        }

        return view;
    }

    private void setItems(View view) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        languageChooseText = (TextView) view.findViewById(R.id.languageChooseId);
        languageSpinner = (Spinner) view.findViewById(R.id.languageSpinner);
        String[] values = {"English", "Polski"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, values);

        languageSpinner.setAdapter(adapter);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (position == 0) {
                    Paper.book().write("language", "en");
                    updateView((String) Paper.book().read("language"));
                } else if (position == 1) {
                    Paper.book().write("language", "pl");
                    updateView((String) Paper.book().read("language"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        currentPasswordText = view.findViewById(R.id.currentPassword);
        newPasswordText = view.findViewById(R.id.newPassword);
        confirmPasswordText = view.findViewById(R.id.confirmPassword);

        saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> {
            String newPassword = newPasswordText.getText().toString();
            String confirmPassword = confirmPasswordText.getText().toString();
            String currentPass = currentPasswordText.getText().toString();

            //if password does not equals to empty string, the password is changed
            if (!newPassword.equals("") && !confirmPassword.equals("") && !currentPass.equals("")) {
                if (newPassword.equals(confirmPassword)) {

                    mAuth.signInWithEmailAndPassword(user.getEmail(), currentPass);

                    user.updatePassword(newPassword)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    newPasswordText.setText("");
                                    confirmPasswordText.setText("");
                                    currentPasswordText.setText("");

                                    Log.d(TAG, "User password updated.");
                                    toastMessage(getString(R.string.password_updated));
                                } else {
                                    String e = task.getException().getLocalizedMessage();
                                    toastMessage("Failed");
                                }
                            });
                } else {
                    toastMessage(getString(R.string.passwords_do_not_match));
                }
            }
        });

        //logs out the user and starts StartActivity
        logoutButton = view.findViewById(R.id.logout);
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            this.getActivity().startActivity(new Intent(this.getActivity(), StartActivity.class));
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setToolbarActions();
    }

    //updates view
    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(this.getActivity(), language);

        Resources resources = context.getResources();

        languageChooseText.setText(resources.getString(R.string.language_change));
    }

    //sets toolbar actions
    private void setToolbarActions() {
        ToolbarService toolbarService = new ToolbarService();
        userBooksImage = toolbarService.getUserBooksImageButton(this.getActivity());
        searchImage = toolbarService.getSearchImageButton(this.getActivity());
    }

    private void toastMessage(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}