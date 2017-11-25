package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.resoneuronance.shahucetcell.R;

import java.util.ArrayList;

import model.Exam;

/**
 * Created by Rohit on 11/25/2017.
 */

public class ProgressAdapter extends ArrayAdapter<Exam> {
    private final Activity context;
    private final ArrayList<Exam> items;
    String progresurl;

    public ProgressAdapter(Activity context, ArrayList items) {


        super(context, R.layout.progress_row, items);
        this.context = context;
        this.items = items;

    }

    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.progress_row, null, true);


        ImageView image = (ImageView) rowView.findViewById(R.id.progressreport);
        Exam exam = getItem(position);
        progresurl=exam.getProgressReport();

        Glide.with(context)
                .load(progresurl)
                .override(300, 200)
                .into(image);
        return rowView;


    }
}


