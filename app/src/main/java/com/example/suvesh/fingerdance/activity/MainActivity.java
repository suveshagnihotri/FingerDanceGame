package com.example.suvesh.fingerdance.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.suvesh.fingerdance.R;
import com.example.suvesh.fingerdance.utils.Constants;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Suvesh on 22/02/2017 AD.
 */

public class MainActivity extends AppCompatActivity implements View.OnTouchListener  {

    @BindView(R.id.gridLayout)
    GridLayout gridLayout;
    @BindView(R.id.tvTurns)
    TextView tvTurns;
    private int size;
    private View myViews[];
    private ArrayList available;
    private String player1, temp;
    private String player2;
    private String[] turn;
    private int gameover, reqindex, touch1, touch2;
    private BottomSheetDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        ButterKnife.bind(this);
        size = getIntent().getIntExtra("size", 0);
        if (getIntent().getStringExtra("player1") != null) {
            player1 = getIntent().getStringExtra("player1");
        }
        if (getIntent().getStringExtra("player2") != null) {
            player2 = getIntent().getStringExtra("player2");
        }
        setGridLayout();
        assignTurns();
    }

    private void setGridLayout() {
        gridLayout.setColumnCount(size);
        gridLayout.setRowCount(size);

        int numOfCol = gridLayout.getColumnCount();
        final int numOfRow = gridLayout.getRowCount();
        myViews = new View[numOfCol * numOfRow];
        available = new ArrayList(numOfCol * numOfRow);
        turn = new String[size * size];

        for (int i = 0; i < (size * size); i++) {
            available.add(i);
        }

        int xPos, yPos;
        for (yPos = 0; yPos < numOfRow; yPos++) {
            for (xPos = 0; xPos < numOfCol; xPos++) {
                View tView = new View(this);
                tView.setBackgroundColor(getResources().getColor(R.color.primary_200));
                myViews[(yPos * numOfRow) + xPos] = tView;
                gridLayout.addView(tView);
            }
        }

        gridLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        final int MARGIN = 5;
                        int pWidth = gridLayout.getWidth();
                        int pHeight = gridLayout.getHeight();
                        int numOfCol = gridLayout.getColumnCount();
                        int numOfRow = gridLayout.getRowCount();
                        int w = pWidth / numOfCol;
                        int h = pHeight / numOfRow;

                        for (int yPos = 0; yPos < numOfRow; yPos++) {
                            for (int xPos = 0; xPos < numOfCol; xPos++) {
                                GridLayout.LayoutParams params =
                                        (GridLayout.LayoutParams) myViews[yPos * numOfRow + xPos].getLayoutParams();
                                params.width = w - 2 * MARGIN;
                                params.height = h - 2 * MARGIN;
                                params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
                                myViews[yPos * numOfRow + xPos].setLayoutParams(params);
                            }
                        }
                    }
                }
        );
    }

    private void assignTurns() {
        temp = player1;
        tvTurns.setText(temp + "'s turn");
        int x = nextTile();
        int tile = (int) available.get(x);
        available.remove(x);
        turn[tile] = temp;
        myViews[tile].setBackgroundColor(getResources().getColor(R.color.accent));
        myViews[tile].setOnTouchListener(this);
    }


    private int nextTile() {
        int rnd = new Random().nextInt(available.size());
        return rnd;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (gameover == 0) {
            //If a view is clicked, then generating next random tile along with changing the current player
            if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                if (!available.isEmpty()) {
                    if (temp.equals(player1)) {
                        temp = player2;
                    } else if (temp.equals(player2)) {
                        temp = player1;
                    }

                    view.setBackgroundColor(getResources().getColor(R.color.primary));
                    tvTurns.setText(temp + "'s turn");
                    int x = nextTile();
                    int tile = (int) available.get(x);
                    available.remove(x);
                    turn[tile] = temp;
                    myViews[tile].setBackgroundColor(getResources().getColor(R.color.red_yellow));
                    myViews[tile].setOnTouchListener(this);
                }
            }

            //if finger is lifted from any tile, that player is assigned as the loser
            //and gameover flag is set. A new thread is started which tells the scores
            //of both the players
            else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
                reqindex = 0;
                gameover = 1;
                touch1 = 0;
                touch2 = 0;
                for (int i = 0; i < (size * size); i++) {
                    if (view == myViews[i]) {
                        reqindex = i;
                        break;
                    }
                }
                tvTurns.setText(turn[reqindex] + " loses");
                view.setBackgroundColor(Color.RED);

                Observable.range(1, 1)
                        .delay(Constants.SPLASH_ACTIVITY_TIME_OUT, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Integer>() {
                            @Override
                            public void onCompleted() {

                            }
                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Integer integer) {
                               createDialog();
                            }
                        });

                //counting the number of successful touches by both the players
                touch1 = 0;
                touch2 = 0;
                for (int i = 0; i < (size * size); i++) {
                    if (player1.equals(turn[i])) touch1++;
                    else if (player2.equals(turn[i])) touch2++;
                }

                if (turn[reqindex].equals(player1)) touch1--;
                else if (turn[reqindex].equals(player2)) touch2--;
            }
        }
        return true;
    }






    private void createDialog() {
        if (dismissDialog()) {
            return;
        }
        View view = getLayoutInflater().inflate(R.layout.layout_bottomheet, null);
        TextView tvResult1=(TextView)view.findViewById(R.id.result1);
        TextView tvResult2=(TextView)view.findViewById(R.id.result2);
        Button btnAgain=(Button)view.findViewById(R.id.brnAgain);
        tvResult1.setText(turn[reqindex].toString() + " loses the game.");
        tvResult2.setText("Finger  touches counts  " + player1 + " : " + touch1
                + "," + player2 + " : " + touch2);

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,StartActivity.class);
                startActivity(intent);
                finish();
                dismissDialog();
            }
        });

        dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();

    }

    private boolean dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            return true;
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }
}
