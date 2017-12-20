package adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.resoneuronance.shahucetcell.Activityfullscreen;
import com.resoneuronance.shahucetcell.AnalysisShow;
import com.resoneuronance.shahucetcell.CorrectkeyShow;
import com.resoneuronance.shahucetcell.PDfViewer;
import com.resoneuronance.shahucetcell.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import model.Exam;

/**
 * Created by Rohit on 11/24/2017.
 */

public class ExamAdapter extends ArrayAdapter<Exam>  {

    private final Activity context;
    private final ArrayList<Exam> items;
    ImageView image;
    String omrimageUrl;
    String parerImageUrl;
    private ImageView img;
    private TextView txtexam;
    private String name;
    String dest_file_path = "test.pdf";
    int downloadedSize = 0, totalsize;
    float per = 0;
    ProgressDialog proDialog;
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
        Button btnanalysis=(Button)rowView.findViewById(R.id.btnanalysis);
        Button btncorrectkey=(Button) rowView.findViewById(R.id.btncorrectkey);
       // image = (ImageView) rowView.findViewById(R.id.imageView);


        Exam exam = getItem(position);
        txtexam.setText(exam.getTestName());
        name=exam.getTestName();
        omrimageUrl=exam.getOMR();
        parerImageUrl=exam.getPSolution();
        proDialog = new ProgressDialog(getContext());
        proDialog.setMessage("please wait....");
        proDialog.dismiss();
        btnomr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Exam exam=getItem(position);
                Intent inf=new Intent(getContext(),Activityfullscreen.class);
                Bundle bundle = new Bundle();

                //Add your data to bundle
                bundle.putString("url",  exam.getOMR());
                bundle.putString("testname",exam.getTestName());

                //Add the bundle to the intent
                inf.putExtras(bundle);

                getContext().startActivity(inf);
            }
        });
        btnpapersol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Exam exam=getItem(position);
                Intent inf=new Intent(getContext(),PDfViewer.class);
                Bundle bundle = new Bundle();

                //Add your data to bundle
                bundle.putString("testname",exam.getTestName());
                bundle.putString("examid",  exam.getExamid());


                //Add the bundle to the intent
                inf.putExtras(bundle);

                getContext().startActivity(inf);
            }
        });
        btnanalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //proDialog.setCancelable(false);
                //proDialog.show();
               Exam exam= getItem(position);
               Intent inf=new Intent(getContext(),AnalysisShow.class);
                Bundle bundle = new Bundle();

//Add your data to bundle
                bundle.putString("testname",exam.getTestName());
                bundle.putString("url",  exam.getAnalysis());
                inf.putExtras(bundle);
                context.startActivity(inf);
            }
        });
        btncorrectkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Exam exam= getItem(position);

                Intent inf=new Intent(getContext(),CorrectkeyShow.class);
                Bundle bundle = new Bundle();

//Add your data to bundle

                bundle.putString("examid",  exam.getExamid());
                bundle.putString("testname",exam.getTestName());
                inf.putExtras(bundle);
                getContext().startActivity(inf);


            }
        });


        return rowView;
    }

    //PDF Downloader


    File downloadFile(String dwnload_file_path) {
        File file = null;
        try {

            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            // connect
            urlConnection.connect();

            // set the path where we want to save the file
            //File SDCardRoot = Environment.getDataDirectory();
            // create a new file, to save the downloaded file
            file = new File(Environment.getDataDirectory()
                    + "/ShahuApp/",dest_file_path);
            //file = new File(SDCardRoot, dest_file_path);

            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            // this is the total size of the file which we are
            // downloading
            totalsize = urlConnection.getContentLength();


            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                per = ((float) downloadedSize / totalsize) * 100;

            }
            // close the output stream when complete //
            fileOutput.close();
            //setText("Download Complete. Open PDF Application installed in the device.");

        } catch (final MalformedURLException e) {

        } catch (final IOException e) {


        } catch (final Exception e) {

        }
        return file;
    }


}
