package com.edu.med;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TipsActivity extends AppCompatActivity {

    @BindView(R.id.thumblin)
    ImageView thumblin;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.deatils)
    TextView deatils;
    @BindView(R.id.linearLayout3)
    ConstraintLayout linearLayout3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_category);
        ButterKnife.bind(this);

        Picasso.get().load(getIntent().getStringExtra("image")).into(thumblin);
        Log.i("123321", "34:"+getIntent().getStringExtra("image"));
        title.setText(getIntent().getStringExtra("title"));
        deatils.setText(Html.fromHtml(getIntent().getStringExtra("details")));


    }

}
