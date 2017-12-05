package com.resoneuronance.shahucetcell;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class CorrectkeyShow extends AppCompatActivity {

    private String imgUrl;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correctkey_show);
        Bundle bundle = getIntent().getExtras();//Extract the data…
        if (bundle != null) {

            imgUrl = bundle.getString("examid");
            img=(ImageView)findViewById(R.id.correctkey);
            Glide.with(CorrectkeyShow.this)
                    .load(imgUrl)

                    .into(img);
        }
    }
}
