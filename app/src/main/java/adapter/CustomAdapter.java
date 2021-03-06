package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.resoneuronance.shahucetcell.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Notice;

/**
 * Created by Rohit on 11/24/2017.
 */

public class CustomAdapter extends ArrayAdapter<Notice> {

    private  Activity context;
    private  ArrayList<Notice> items;

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
        System.out.println("Length "+ notice.getMessage().length());

            txtnotice.setText(notice.getMessage());
            SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
            Date mydate = notice.getDate();
            // noticedate.setText(notice.getDate().toString());
            String formatdate = timeStampFormat.format(mydate);
            ndate.setText(formatdate);
            // ndate.setText(notice.getDate().toString());


        return rowView;
    }
}
