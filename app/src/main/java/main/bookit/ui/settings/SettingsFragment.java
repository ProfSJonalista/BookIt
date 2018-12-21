package main.bookit.ui.settings;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import io.paperdb.Paper;
import main.bookit.R;
import main.bookit.helpers.LocaleHelper;

public class SettingsFragment extends Fragment {

    private SettingsViewModel mViewModel;
    TextView text;
    Button saveButton;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        Spinner spinner = (Spinner) view.findViewById(R.id.languageSpinner);
        String[] values = { "English", "Polski"};
        text = (TextView) view.findViewById(R.id.languageChooseId);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        spinner.setAdapter(adapter);

        Paper.init(this.getActivity());

        final String language = Paper.book().read("language");
        if(language == null){
            Paper.book().write("language", "en");
        }


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(position == 0){
                    Paper.book().write("language", "en");
                    updateView((String)Paper.book().read("language"));
                }
                else if(position == 1){
                    Paper.book().write("language", "pl");
                    updateView((String)Paper.book().read("language"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel\
    }

    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(this.getActivity(), language);

        Resources resources = context.getResources();

        //ZMIENIAMY JĘZYK TUTAJ
        //TODO - sprawdzić czy na pozostałych też się zmienia
        text.setText(resources.getString(R.string.language_change));
    }
}