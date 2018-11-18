package main.bookit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String Title[];
    String Author[];
    String Status[];
    int flags[];
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] Title, String[] Author , String[] Status, int[] flags) {
        this.context = context;
        this.Title = Title;
        this.Author = Author;
        this.Status = Status;
        this.flags = flags;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return Title.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_list_item, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView author = (TextView) view.findViewById(R.id.author);
        TextView status = (TextView) view.findViewById(R.id.status);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        title.setText(Title[i]);
        author.setText(Author[i]);
        status.setText(Status[i]);
        image.setImageResource(flags[i]);
        return view;
    }
}