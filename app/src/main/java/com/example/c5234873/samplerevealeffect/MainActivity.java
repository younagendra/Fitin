package com.example.c5234873.samplerevealeffect;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Path;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button mButtonBmi;
    Button mButtonBmr;
    CardView cardView;
    View revealView;
    boolean mFlag;
    ImageView mImage;
    Button mResultButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardView = (CardView) findViewById(R.id.card);
        revealView = findViewById(R.id.reveal_view);

        mButtonBmi = (Button) findViewById(R.id.button_bmi);
        mButtonBmr = (Button) findViewById(R.id.button_bmr);
        mImage = (ImageView) findViewById(R.id.imageView);
        mResultButton = (Button) findViewById(R.id.resultbutton);

        mButtonBmr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFlag = true;
                prepareRevealEffect(getResources().getColor(R.color.color_bmr));
            }
        });
        mButtonBmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFlag = false;
                prepareRevealEffect(getResources().getColor(R.color.color_bmi));
            }
        });

        mResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultsActivity = new Intent(getApplicationContext(), ResultsActivity.class);
                startActivity(resultsActivity);
            }
        });
    }

    @Override
    public void onBackPressed() {

        startUnRevealEffect();
//        super.onBackPressed();
    }

    private void prepareRevealEffect(int color) {

        Animator buttonBmiAnim = AnimatorInflater.loadAnimator(this, R.animator.bmi_animation);
        buttonBmiAnim.setTarget(mButtonBmi);

        Animator buttonBmrAnim = AnimatorInflater.loadAnimator(this, R.animator.bmi_animation);
        buttonBmrAnim.setTarget(mButtonBmr);

        revealView.setBackgroundColor(color);

        if (mButtonBmi.isPressed()) {
            buttonBmrAnim.setStartDelay(300);
        } else {
            buttonBmiAnim.setStartDelay(300);
        }
        buttonBmrAnim.start();
        buttonBmiAnim.start();

        Path path = new Path();
        buttonBmiAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                mButtonBmi.setVisibility(View.GONE);
                startRevealEffect();
            }
        });

        buttonBmrAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
//                mButtonBmr.setVisibility(View.GONE);
                startRevealEffect();
            }
        });
        //
    }

    private void startRevealEffect() {
        //get card height and width
        int cx = cardView.getHeight();
        int cy = cardView.getWidth();

        //get hypotenuse for the diagonal reveal effect
        int hypotenuse = (int) Math.hypot(cardView.getWidth(), cardView.getHeight());

        //get instance of animator
        Animator anim = ViewAnimationUtils.createCircularReveal(revealView, cx, cy, 0, hypotenuse);
        revealView.setVisibility(View.VISIBLE);

        //start the animation
        anim.setDuration(900);
        anim.start();
    }
    private void startUnRevealEffect() {
        //get card height and width
        int cx = cardView.getHeight();
        int cy = cardView.getWidth();

        //get hypotenuse for the diagonal reveal effect
        int hypotenuse = (int) Math.hypot(cardView.getWidth(), cardView.getHeight());

        //get instance of animator
        Animator anim = ViewAnimationUtils.createCircularReveal(revealView, cx, cy, hypotenuse, 0);
        revealView.setVisibility(View.VISIBLE);

        //start the animation
        anim.setDuration(900);
        anim.start();
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                revealView.setVisibility(View.INVISIBLE);

                mButtonBmi.setVisibility(View.VISIBLE);
                mButtonBmr.setVisibility(View.VISIBLE);
            }
        });
    }


}
