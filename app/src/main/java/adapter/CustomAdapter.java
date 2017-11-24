package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.resoneuronance.shahucetcell.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Notice;

/**
 * Created by Rohit on 11/24/2017.
 */

public class CustomAdapter extends ArrayAdapter<Notice> {

    private final Activity context;
    private final ArrayList<Notice> items;

    public CustomAdapter (Activity context, ArrayList items) {


        super(context, R.layout.noticerow, items);
        this.context = context;
        this.items= items;
    }


    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.noticerow, null, true);

        TextView txtnotice = (TextView) rowView.findViewById(R.id.notice);
        TextView ndate= (TextView) rowView.findViewById(R.id.noticedate);

        Notice notice = getItem(position);
        txtnotice.setText(notice.getMessage());
        ndate.setText(notice.getDate().toString());

        return rowView;
    }
}
