package main.bookit.helpers;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import main.bookit.R;
import main.bookit.ui.ForgotPasswordActivity;
import main.bookit.ui.SearchActivity;
import main.bookit.ui.SettingsActivity;
import main.bookit.ui.StartActivity;
import main.bookit.ui.UserBooksActivity;

public class ToolbarService {

    public ImageView getUserBooksImageButton(final Activity activity) {
        ImageView image = activity.findViewById(R.id.userBooks);
        image.setOnClickListener(v ->
                activity.startActivity(new Intent(activity, UserBooksActivity.class)));

        return image;
    }

    public ImageView getSettingsImageButton(final Activity activity) {
        ImageView image = activity.findViewById(R.id.settingsIcon);
        image.setOnClickListener(v ->
                activity.startActivity(new Intent(activity, SettingsActivity.class)));

        return image;
    }

    public ImageView getSearchImageButton(final Activity activity) {
        ImageView image = activity.findViewById(R.id.searchIcon);
        image.setOnClickListener(v ->
                activity.startActivity(new Intent(activity, SearchActivity.class)));

        return image;
    }

    public ImageView getHomeButton(final Activity activity) {
        ImageView image = activity.findViewById(R.id.homeIcon);
        image.setOnClickListener(v ->
                activity.startActivity(new Intent(activity, StartActivity.class)));

        return image;
    }
}
