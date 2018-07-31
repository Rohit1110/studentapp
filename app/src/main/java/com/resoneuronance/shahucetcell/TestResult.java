package com.resoneuronance.shahucetcell;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import adapter.TestResultAdapter;
import api.ResultApi;
import model.testmodel.EdoServiceRequest;
import model.testmodel.EdoServiceResponse;
import model.testmodel.EdoStudent;
import model.testmodel.EdoTest;
import model.testmodel.TestResults;
import model.testmodel.Test_;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Utility;

/**
 * Created by Rohit on 7/30/2018.
 */



public class TestResult extends Fragment {
    ProgressDialog proDialog;

    TextView name, scontact, category, pcontact, sclass, roll, ppercent;

   /* FirebaseFirestore db = FirebaseFirestore.getInstance();
    Sprofile sprofile;
    String phone;
    private Utility utility;
    ImageView profilep;*/
   RecyclerView recyclerView;
   EdoServiceRequest request = new EdoServiceRequest();
   EdoStudent student = new EdoStudent();
    EdoTest test = new EdoTest();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tes_result_layout, container, false);
        getActivity().setTitle(Html.fromHtml("<font color='#ffffff'>Student Test Result </font>"));
       recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerview);
        request=new EdoServiceRequest();
        student.setId(970);
        test.setId(29);

        request.setStudent(student);
        request.setTest(test);
if(Utility.isInternetOn(getActivity())) {
    getData();
}else{
    readFromFile();
}





        return rootView;
    }

    private void getData() {

    Call<TestResults> loginCall = ResultApi.getService().getAuthenticate(request);
    System.out.println("UserName call"+request.getTest().getId()+" "+request.getStudent().getId());
          loginCall.enqueue(new Callback<TestResults>() {
        @Override
        public void onResponse(Call<TestResults> call, Response<TestResults> response) {
            Log.v("Response ", response.body()+"");
            TestResults testResults = response.body();
            writeToFile(response.body().toString());
            Log.v("UserName" , testResults.getTest().toString());
            Log.v("UserName get" , testResults.getTest().getTest().get(0).getQuestion());
            Log.v("UserName" , response.toString());
            //Toast.makeText(getActivity(),"Login Sccess",Toast.LENGTH_LONG).show();
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new TestResultAdapter(testResults.getTest().getTest(), getActivity()));

        }

        @Override
        public void onFailure(Call<TestResults> call, Throwable t) {
            Toast.makeText(getActivity(),"Error occured",Toast.LENGTH_LONG).show();
        }
    });
}


    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getActivity().openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = getActivity().openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

}
