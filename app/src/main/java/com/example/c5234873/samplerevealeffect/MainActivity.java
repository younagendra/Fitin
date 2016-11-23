package com.example.c5234873.samplerevealeffect;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton mFloatingButton;
    CardView cardView;
    TextView mTextView2;
    View revealView;
    boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardView = (CardView) findViewById(R.id.card);
        revealView = findViewById(R.id.reveal_view);
        mTextView2 = (TextView) findViewById(R.id.textView2);

        mFloatingButton = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performRevealEffect();
            }
        });
    }

    private void performRevealEffect() {
        //get card height and width
        int cx = cardView.getHeight();
        int cy = cardView.getWidth();

        //get hypotenuse for the diagonal reveal effect
        int hypotenuse = (int) Math.hypot(cardView.getWidth(), cardView.getHeight());

        //get instance of animator
        Animator anim = ViewAnimationUtils.createCircularReveal(revealView, cx, cy, 0, hypotenuse);
        //visible the required view
        revealView.setVisibility(View.VISIBLE);
        //start the animation
        anim.setDuration(500);
        anim.start();
        //
        mTextView2.setVisibility(View.GONE);
    }


}
