package main.bookit.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import main.bookit.R;
import main.bookit.ui.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        //starts fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance())
                    .commitNow();
        }
    }
}
