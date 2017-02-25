package com.example.suvesh.fingerdance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.suvesh.fingerdance.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Suvesh on 21/02/2017 AD.
 */

public class StartActivity extends AppCompatActivity {

    @BindView(R.id.player1)
    EditText player1;

    @BindView(R.id.player2)
    EditText player2;

    @BindView(R.id.size)
    EditText etSize;

    @BindView(R.id.ll_submit)
    LinearLayout llSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.ll_submit)
    void navigateToStartGame() {
        if (etSize.getText().toString().trim() != null && !etSize.getText().toString().toString().equalsIgnoreCase("") && Integer.parseInt(etSize.getText().toString()) < 10) {
            if (player1.getText().toString().trim() != null && !player1.getText().toString().equalsIgnoreCase("")
                    && player2.getText().toString().trim() != null && !player2.getText().toString().equalsIgnoreCase("") ) {
                Intent in = new Intent(StartActivity.this, MainActivity.class);
                in.putExtra("size", Integer.parseInt(etSize.getText().toString()));
                in.putExtra("player1", player1.getText().toString());
                in.putExtra("player2", player2.getText().toString());
                startActivity(in);
                finish();
            } else {
                Toast.makeText(this, "Enter Player name", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Please Enter Size of matrix less then 10", Toast.LENGTH_SHORT).show();
        }

    }
}
