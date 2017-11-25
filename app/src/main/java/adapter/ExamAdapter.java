package adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;
import com.resoneuronance.shahucetcell.R;

import java.util.ArrayList;

import loder.LoadImageTask;
import model.Exam;
import model.Notice;

/**
 * Created by Rohit on 11/24/2017.
 */

public class ExamAdapter extends ArrayAdapter<Exam> implements View.OnClickListener {

    private final Activity context;
    private final ArrayList<Exam> items;
    ImageView image;
    String omrimageUrl="https://firebasestorage.googleapis.com/v0/b/shahu-cet-cell.appspot.com/o/1.png?alt=media&token=5a850879-1673-4931-8ffe-57f46bd12e9b";
    String parerImageUrl="https://firebasestorage.googleapis.com/v0/b/shahu-cet-cell.appspot.com/o/3.png?alt=media&token=5620ba07-b467-47c9-aaf4-0cf7f9020ce4";

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
        image=(ImageView)rowView.findViewById(R.id.imageView);


        Exam exam = getItem(position);
        txtexam.setText(exam.getTestName());
       // omrimageUrl=exam.getOMR();
        //parerImageUrl=exam.getPSolution();
        btnomr.setOnClickListener(this);
        btnpapersol.setOnClickListener(this);


        return rowView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.omrshow) {
            Dialog viewimage = new Dialog(getContext());
            viewimage.requestWindowFeature(Window.FEATURE_NO_TITLE);
            viewimage.setContentView(R.layout.image_view);
            WebView wv = (WebView) viewimage.findViewById(R.id.omrimg);
            wv.getSettings().setBuiltInZoomControls(true);
            String im = "<html><body><div style=height:30%; width:30%;> <center><img src=\"" + omrimageUrl + "\"alt=\"Smiley face\" height=600 width=400 /></center></div></body></html>";
            wv.loadDataWithBaseURL("fake", im, "text/html", "UTF-8", "");
            viewimage.show();


        }
        if (v.getId() == R.id.psolshow) {
            Dialog viewimage = new Dialog(getContext());
            viewimage.requestWindowFeature(Window.FEATURE_NO_TITLE);
            viewimage.setContentView(R.layout.image_view);
            WebView wv = (WebView) viewimage.findViewById(R.id.omrimg);
            wv.getSettings().setBuiltInZoomControls(true);
            String im = "<html><body><div style=height:30%; width:30%;> <center><img src=\"" + parerImageUrl + "\"alt=\"Smiley face\" height=600 width=400 /></center></div></body></html>";
            wv.loadDataWithBaseURL("fake", im, "text/html", "UTF-8", "");
            viewimage.show();

        }
    }






}
