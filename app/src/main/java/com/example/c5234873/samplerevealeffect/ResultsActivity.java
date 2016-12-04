package com.example.c5234873.samplerevealeffect;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;

public class ResultsActivity extends AppCompatActivity {
    TickerView mTickerView, mBmiTickerView;
    String mCloriesRequired;
    String mBmiScore;
    TextView mBmiText;
    View mBmiView, mBmrView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        mBmiTickerView = (TickerView) findViewById(R.id.bmi_score);

        mBmiView = findViewById(R.id.bmi_view);
        mBmrView = findViewById(R.id.bmr_view);


        mBmiText = (TextView) findViewById(R.id.bmi_text);

        DecimalFormat decimalFormat = new DecimalFormat("#,##0");

        Intent i = getIntent();
        try {
            mCloriesRequired = decimalFormat.format(Integer.parseInt(i.getStringExtra("calories")));
        } catch (Exception e) {
            mCloriesRequired = null;
        }
        try {
            mBmiScore = i.getStringExtra("bmi_value");

        } catch (Exception e) {
            mBmiScore = null;
        }
        if (mCloriesRequired != null) {

            //this method takes care of displaying the decoview animations
            performDecoAnimations();

            //update text along with series item2(inside this method this
            //thread is delayed same as series item2)
            updateCalorieText();

        } else {
            updateBmiText();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

            }
        });

    }

    private void updateBmiText() {

        mBmiView.setVisibility(View.VISIBLE);
        mBmrView.setVisibility(View.GONE);
        int bmiScore = Integer.parseInt(mBmiScore);
        Random r = new Random();
        int low = 9;
        int high = 100;
        //**Random values before displaying the required value**//
        final int result = r.nextInt(high - low) + low;

        mBmiTickerView.setCharacterList(TickerUtils.getDefaultListForUSCurrency());

        //set the animation duration to two seconds
        mBmiTickerView.setAnimationDuration(1500);
        mBmiTickerView.setAlpha(1);

        mBmiTickerView.setText(String.valueOf(result));

        //****************************************************//
        //Update the required value in ticker motion
        mBmiTickerView.setText(String.valueOf(mBmiScore));
        if (bmiScore < 19) {
            mBmiText.setText(R.string.bmi_score_le19);
        } else if (bmiScore >= 19 && bmiScore <= 23) {
            mBmiText.setText(R.string.bmi_score_le23);
        } else {
            mBmiText.setText(R.string.bmi_score_ge23);
        }

    }


    private void performDecoAnimations() {
        mBmrView.setVisibility(View.VISIBLE);
        mBmiView.setVisibility(View.GONE);
        //deco view handling
        final DecoView decoView = (DecoView) findViewById(R.id.calorieView);

        // In order to display number of "Circles" on the screen
        // We need to create series item for each circle as follows
        // Here I would like to display two circles one is for background
        // Other one is to show the main circle(Indicates calories in this case)***//

        //background circle item1 color,circle strength, visibility
        SeriesItem backgroundCircleItem = createSeriesItem(Color.parseColor("#30000000"), 50f, true);

        //main circle item2
        SeriesItem mainItem = createSeriesItem(Color.parseColor("#E0F2F1"), 50f, false);

        //add each item to Deco View.
        int backIndex = decoView.addSeries(backgroundCircleItem);
        final int seriesIndex = decoView.addSeries(mainItem);

        //get reference of ticker view to update the calorie value
        //this view is used to display value in pedometer style
        mTickerView = (TickerView) findViewById(R.id.tickerview);

        //set character list for the ticker view
        mTickerView.setCharacterList(TickerUtils.getDefaultListForUSCurrency());

        //set the animation duration to two seconds
        mTickerView.setAnimationDuration(1500);


        decoView.executeReset();

        //asking deco view to animate the background circle
        startCircleItemAnimation(decoView, 50f, backIndex, 3000, 100);

        //this once is for little effect(small spiral animation before starting the main)
        decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setDuration(2000)
                .setDelay(1250)
                .setIndex(seriesIndex)
                .build());

        //asking deco view to animate main items
        startCircleItemAnimation(decoView, 50f, seriesIndex, 1500, 3250);

    }

    private void startCircleItemAnimation(DecoView decoView, float v, int backIndex, int duration, int delay) {
        decoView.addEvent(new DecoEvent.Builder(50f)
                .setIndex(backIndex)
                .setDuration(duration)
                .setDelay(delay)
                .build());

    }

    private SeriesItem createSeriesItem(int itemColor, float v, boolean visiblity) {
        return new SeriesItem.Builder(itemColor)
                .setRange(0, v, 0)
                .setInitialVisibility(visiblity)
                .build();

    }

    private void updateCalorieText() {
        final android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //running thread on UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Random r = new Random();
                        int low = 1000;
                        int high = 10000;
                        //**Random values before displaying the required value**//
                        final int result = r.nextInt(high - low) + low;

                        mTickerView.setText(String.valueOf(result));
                        //****************************************************//
                        //Update the required value in ticker motion
                        mTickerView.setText(String.valueOf(mCloriesRequired));

                    }
                });
            }
        }, 3250);//delaying till the series item2 starts, so that we can
        //perform series animation and value update at the same time
    }


}
