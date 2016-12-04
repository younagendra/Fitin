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
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
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
import android.widget.Toast;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.numberPickerStyle;
import static android.R.attr.visible;

public class MainActivity extends AppCompatActivity {

    Button mButtonBmi;
    Button mButtonBmr;
    CardView cardView;
    View revealView, mAgeView, mGenderView, mWorkoutView;
    boolean mFlag;
    ImageView mImage;
    Button mResultButton;
    Spinner mGenderSpinner, mWorkoutSpinner;
    ArrayAdapter<CharSequence> mSpinnerAdapter;
    int mGenderValue;
    int mWorkoutValue;
    EditText mAgeEditText;
    EditText mHeightEditText;
    EditText mWeightEditText;

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
        mAgeEditText = (EditText) findViewById(R.id.ageEditText);
        mWeightEditText = (EditText) findViewById(R.id.weight_value);
        mHeightEditText = (EditText) findViewById(R.id.height_value);

        mAgeView = findViewById(R.id.age_layout);
        mWorkoutView = findViewById(R.id.workout_view);
        mGenderView = findViewById(R.id.gender_view);

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

        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        mWorkoutSpinner = (Spinner) findViewById(R.id.routine);


        populateSpinnerValues(mGenderSpinner, R.array.gender_array);
        populateSpinnerValues(mWorkoutSpinner, R.array.workout_plan);

        setEditTextMaxLength(mAgeEditText, 2);
        setEditTextMaxLength(mWeightEditText, 3);
        setEditTextMaxLength(mHeightEditText, 3);

        mResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double bmrValue;
                int bmiValue;
                int caloriesRequired = 0;
                Intent resultsActivity = new Intent(getApplicationContext(), ResultsActivity.class);
                if (mFlag) {
                    bmrValue = calculateBMR();
                    if (bmrValue != 0) {
                        caloriesRequired = (int) calculateCaloriesNeeded(bmrValue);
                        resultsActivity.putExtra("calories", caloriesRequired + "");
                        startActivity(resultsActivity);
                    }
                } else {
                    bmiValue = (int) calculateBMI();
                    if (bmiValue != 0) {
                        resultsActivity.putExtra("bmi_value", bmiValue + "");
                        startActivity(resultsActivity);
                    }
                }
            }
        });

    }

    private double calculateBMI() {

        String height;
        String weight;

        weight = mWeightEditText.getText().toString();
        height = mHeightEditText.getText().toString();

        if (weight.equals("") || height.equals("")) {
            Toast.makeText(this, "Weight and Height Cannot be Blank", Toast.LENGTH_SHORT).show();
            return 0;
        }

        double heightInM = Integer.parseInt(height);
        heightInM = heightInM/100;
        int weightInKg = Integer.parseInt(weight);
        double bmiScore = weightInKg / (heightInM * heightInM);
        return bmiScore;

    }

    private double calculateCaloriesNeeded(double bmrValue) {
        double calories = 0;
        switch (mWorkoutValue) {
            case 0:
                calories = bmrValue * 1.2;
                break;
            case 1:
                calories = bmrValue * 1.375;
                break;
            case 2:
                calories = bmrValue * 1.55;
                break;
            case 3:
                calories = bmrValue * 1.725;
                break;
            case 4:
                calories = bmrValue * 1.9;
                break;
        }
        return calories;

    }

    private double calculateBMR() {
        double bmrValue;
        String weight, height, age;
        weight = mWeightEditText.getText().toString();
        height = mHeightEditText.getText().toString();
        age = mAgeEditText.getText().toString();
        if (weight.equals("") || height.equals("") || age.equals("")) {
            Toast.makeText(this, "All Fields Required", Toast.LENGTH_SHORT).show();
            return 0;
        }

        if (mGenderValue == 0) {
            bmrValue =
                    66.4730 + (13.7516 * Integer.parseInt(weight))
                            + (5.003 * Integer.parseInt(height)
                            - (6.7550 * Integer.parseInt(age)));
        } else {
            bmrValue =
                    655.0955 + (9.5634 * Integer.parseInt(weight))
                            + (1.8496 * Integer.parseInt(height))
                            - (4.6756 * Integer.parseInt(age));
        }
        return bmrValue;
    }

    private void setEditTextMaxLength(EditText ageText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        ageText.setFilters(FilterArray);

    }

    private void populateSpinnerValues(final Spinner spinner, int itemsLayoutId) {
        mSpinnerAdapter = ArrayAdapter
                .createFromResource(this, itemsLayoutId, R.layout.support_simple_spinner_dropdown_item);

        mSpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(mSpinnerAdapter);

        //item selected listener for the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                String selection = (String) parent.getItemAtPosition(pos);
                if (!TextUtils.isEmpty(selection)) {
                    if (spinner == mGenderSpinner) {
                        switch (pos) {
                            case 0://Male selected
                                mGenderValue = Constants.MALE;
                                break;
                            case 1:
                                mGenderValue = Constants.FEMALE;
                        }
                    } else {
                        switch (pos) {
                            case 0:
                                mWorkoutValue = Constants.NO_EXERCISE;
                                break;
                            case 1:
                                mWorkoutValue = Constants.LITTLE;
                                break;
                            case 2:
                                mWorkoutValue = Constants.MODERATE;
                                break;
                            case 3:
                                mWorkoutValue = Constants.HEAVY;
                                break;
                            case 4:
                                mWorkoutValue = Constants.VERY_HEAVY;
                                break;
                        }
                    }
                }

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
        if (!mFlag) {
            setBmiVisibility(View.GONE);
        } else {
            setBmiVisibility(View.VISIBLE);
        }
        //start the animation
        anim.setDuration(900);
        anim.start();
    }

    private void setBmiVisibility(int visibility) {
        mGenderView.setVisibility(visibility);
        mWorkoutView.setVisibility(visibility);
        mAgeView.setVisibility(visibility);
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
