package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.resoneuronance.shahucetcell.R;

import java.util.ArrayList;

import model.Exam;
import model.Notice;

/**
 * Created by Rohit on 11/24/2017.
 */

public class ExamAdapter extends ArrayAdapter<Exam> {

    private final Activity context;
    private final ArrayList<Exam> items;

    public ExamAdapter (Activity context, ArrayList items) {


        super(context, R.layout.exam_row_layout, items);
        this.context = context;
        this.items= items;
    }


    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.exam_row_layout, null, true);

        TextView txtexam = (TextView) rowView.findViewById(R.id.examshow);
        Button btnomr=(Button)rowView.findViewById(R.id.omrshow);
        Button btnpapersol=(Button)rowView.findViewById(R.id.psolshow);


        Exam exam = getItem(position);
        txtexam.setText(exam.getTestName());


        return rowView;
    }
}
