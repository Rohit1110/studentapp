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

import model.GeneralNotice;
import model.Notice;

/**
 * Created by Rohit on 11/30/2017.
 */

public class GeneralNoticeAdapter extends ArrayAdapter<GeneralNotice> {

    private final Activity context;
    private final ArrayList<Notice> items;

    public GeneralNoticeAdapter (Activity context, ArrayList items) {


        super(context, R.layout.genaral_notification_row, items);
        this.context = context;
        this.items= items;
    }


    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.genaral_notification_row, null, true);

        TextView txtnotice = (TextView) rowView.findViewById(R.id.gnotice);
        TextView noticedate= (TextView) rowView.findViewById(R.id.gnoticedate);

        GeneralNotice notice = getItem(position);
        txtnotice.setText(notice.getMessage());
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
        Date mydate =notice.getDate();
       // noticedate.setText(notice.getDate().toString());
        String formatdate=timeStampFormat.format(mydate);
        noticedate.setText(formatdate);

        return rowView;
    }
}