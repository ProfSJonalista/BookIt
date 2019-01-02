package main.bookit.helpers;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import main.bookit.R;
import main.bookit.ui.UserBooksActivity;

public class ToolbarService {

    public ImageView getUserBooksImageButton(final Activity activity) {
        ImageView userBooksImage = activity.findViewById(R.id.userBooks);
        userBooksImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, UserBooksActivity.class));
            }
        });

        return userBooksImage;
    }
}
