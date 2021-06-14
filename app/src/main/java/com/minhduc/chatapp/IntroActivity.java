package com.minhduc.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class IntroActivity extends AppCompatActivity {
    ImageView imgLoginIntro;
    Animation fromTop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        imgLoginIntro = (ImageView)findViewById(R.id.login_image_intro);
        fromTop = AnimationUtils.loadAnimation(this,R.anim.fromtop);
        imgLoginIntro.setAnimation(fromTop);
        CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(IntroActivity.this,StartActivity.class));
                finish();
            }
        }.start();
    }
}