package com.example.suvesh.fingerdance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.suvesh.fingerdance.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Suvesh on 21/02/2017 AD.
 */

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.llRule)
      void navigateToRuleActivity(){
        Intent intent=new Intent(DetailActivity.this,GameRuleActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.llStart)
    void navigateToStartActivity(){
        Intent intent=new Intent(DetailActivity.this,StartActivity.class);
        startActivity(intent);
    }
}
