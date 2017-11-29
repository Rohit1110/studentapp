package adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;
import com.resoneuronance.shahucetcell.Activityfullscreen;
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
    String omrimageUrl;
    String parerImageUrl;
    private ImageView img;
    private TextView txtexam;
    private String name;

    public ExamAdapter(Activity context, ArrayList items) {


        super(context, R.layout.exam_row_layout, items);
        this.context = context;
        this.items = items;

    }


    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.exam_row_layout, null, true);

         txtexam = (TextView) rowView.findViewById(R.id.examshow);
        Button btnomr = (Button) rowView.findViewById(R.id.omrshow);
        Button btnpapersol = (Button) rowView.findViewById(R.id.psolshow);
        image = (ImageView) rowView.findViewById(R.id.imageView);


        Exam exam = getItem(position);
        txtexam.setText(exam.getTestName());
        name=exam.getTestName();
        omrimageUrl=exam.getOMR();
        parerImageUrl=exam.getPSolution();
        btnomr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inf=new Intent(getContext(),Activityfullscreen.class);
                Bundle bundle = new Bundle();

                //Add your data to bundle
                bundle.putString("url", omrimageUrl);
                bundle.putString("exam",  name);

                //Add the bundle to the intent
                inf.putExtras(bundle);

                getContext().startActivity(inf);
            }
        });
        btnpapersol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inf=new Intent(getContext(),Activityfullscreen.class);
                Bundle bundle = new Bundle();

//Add your data to bundle
                bundle.putString("url", parerImageUrl);
                bundle.putString("exam",  name);
//Add the bundle to the intent
                inf.putExtras(bundle);
                getContext().startActivity(inf);
            }
        });


        return rowView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.omrshow) {
          /*  Dialog viewimage = new Dialog(getContext());
            viewimage.requestWindowFeature(Window.FEATURE_NO_TITLE);
            viewimage.setContentView(R.layout.image_view);
            ImageView wv = (ImageView) viewimage.findViewById(R.id.omrimg);
           *//* wv.getSettings().setBuiltInZoomControls(true);
            String im = "<html><body><div style=height:30%; width:30%;> <center><img src=\"" + omrimageUrl + "\"alt=\"Smiley face\" height=600 width=400 /></center></div></body></html>";
            wv.loadDataWithBaseURL("fake", im, "text/html", "UTF-8", "");*//*
            Glide.with(getContext())
                    .load(omrimageUrl)

                    .into(wv);
            viewimage.show();*/

            // custom dialog




         Intent inf=new Intent(getContext(),Activityfullscreen.class);
            Bundle bundle = new Bundle();

            //Add your data to bundle
            bundle.putString("url", omrimageUrl);
            bundle.putString("exam",  name);

            //Add the bundle to the intent
            inf.putExtras(bundle);

            getContext().startActivity(inf);

           /* alertadd.setNeutralButton("Here!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dlg, int sumthin) {

                }
            });*/




        }
        if (v.getId() == R.id.psolshow) {
            Intent inf=new Intent(getContext(),Activityfullscreen.class);
            Bundle bundle = new Bundle();

//Add your data to bundle
            bundle.putString("url", parerImageUrl);
            bundle.putString("exam",  name);
//Add the bundle to the intent
            inf.putExtras(bundle);
            getContext().startActivity(inf);

        }
    }


}
