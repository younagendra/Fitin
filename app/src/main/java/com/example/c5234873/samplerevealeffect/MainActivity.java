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
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputFilter;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import static android.R.attr.visible;

public class MainActivity extends AppCompatActivity {

    Button mButtonBmi;
    Button mButtonBmr;
    CardView cardView;
    View revealView;
    boolean mFlag;
    ImageView mImage;
    Button mResultButton;
    Spinner mGenderSpinner, mWorkoutSpinner;
    int selectedPosition;

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
        EditText ageEditText = (EditText) findViewById(R.id.ageEditText);

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
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        mWorkoutSpinner = (Spinner) findViewById(R.id.routine);

        EditText weightEditText = (EditText) findViewById(R.id.weight_value);
        EditText heightEditText = (EditText) findViewById(R.id.height_value);

        populateSpinnerValues(mGenderSpinner, R.array.gender_array);
        populateSpinnerValues(mWorkoutSpinner, R.array.workout_plan);

        setEditTextMaxLength(ageEditText, 2);
        setEditTextMaxLength(weightEditText, 3);
        setEditTextMaxLength(heightEditText, 3);



    }

    private void setEditTextMaxLength(EditText ageText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        ageText.setFilters(FilterArray);

    }

    private void populateSpinnerValues(Spinner spinner, int itemsLayoutId) {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter
                .createFromResource(this, itemsLayoutId, R.layout.support_simple_spinner_dropdown_item);

        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                selectedPosition = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    @Override
    public void onBackPressed() {

        startUnRevealEffect();

//        super.onBackPressed();
    }

    private void prepareRevealEffect(int color) {

        mButtonBmi.setVisibility(View.GONE);
        mButtonBmr.setVisibility(View.GONE);
        revealView.setBackgroundColor(color);
        startRevealEffect();
        startRevealEffect();
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
