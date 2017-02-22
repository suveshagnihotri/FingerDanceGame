package com.example.suvesh.fingerdance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.suvesh.fingerdance.R;
import com.example.suvesh.fingerdance.utils.Constants;
import com.example.suvesh.fingerdance.utils.MyBounceInterpolator;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SpalshActivity extends AppCompatActivity {

    @BindView(R.id.llParent)
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce_anim);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.4, 20);
        myAnim.setInterpolator(interpolator);
        linearLayout.startAnimation(myAnim);

        navigateToNextActivity();

    }

    private void navigateToNextActivity(){
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
                        Intent intent=new Intent(SpalshActivity.this, DetailActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}
