package com.automotive.hhi.mileagetracker.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.automotive.hhi.mileagetracker.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AbouteMTeeActivity extends AppCompatActivity {


    @Bind(R.id.about_emtee_text)
    public TextView mAboutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboute_mtee);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        mAboutText.setMovementMethod(LinkMovementMethod.getInstance());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
