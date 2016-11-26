package com.example.c5234873.samplerevealeffect;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pools;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Config;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class ResultsActivity extends AppCompatActivity {
    TickerView mTickerView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //this method takes of displaying the decoview animtions
        performDecoAnimations();

        //update text along with seriesitem2(inside this method this
        //thread is delayed same as seriesitem2)
        updateText();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

            }
        });

    }

    private void performDecoAnimations() {
        //deco view handling
        final DecoView decoView = (DecoView) findViewById(R.id.calorieView);

        // In order to display number of "Circles" on the screen
        // We need to create series item for each circle as follows
        // Here I would like to display two circles one is for background
        // Other one is to show the main circle(Indicates calories in this case)***//

        //background circle item1 color,circle strength, visibility
        SeriesItem backgroundCircleItem = createSeriesItem(Color.parseColor("#FFE2E2E2"), 50f, true);

        //main circle item2
        SeriesItem mainItem = createSeriesItem(Color.parseColor("#FFFF8800"), 50f, false);

        //add each item to Deco View.
        int backIndex = decoView.addSeries(backgroundCircleItem);
        final int seriesIndex = decoView.addSeries(mainItem);

        //get reference of ticker view to update the calorie value
        //this view is used to display value in pedometer style
        mTickerView = (TickerView) findViewById(R.id.tickerview);

        //set character list for the ticker view
        mTickerView.setCharacterList(TickerUtils.getDefaultListForUSCurrency());

        //set the animation duration to two seconds
        mTickerView.setAnimationDuration(2000);


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
        startCircleItemAnimation(decoView, 50f, seriesIndex, 2000, 3250);

    }

    private void startCircleItemAnimation(DecoView decoView, float v, int backIndex, int duration, int delay) {
        decoView.addEvent(new DecoEvent.Builder(50f)
                .setIndex(backIndex)
                .setDuration(duration)
                .setDelay(delay)
                .build());

    }

    private SeriesItem createSeriesItem(int itemColor, float v, boolean visiblity) {
        return  new SeriesItem.Builder(itemColor)
                .setRange(0, v, 0)
                .setInitialVisibility(visiblity)
                .build();


    }

    private void updateText() {
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
                        mTickerView.setText(String.valueOf(result + 10));
                        mTickerView.setText(String.valueOf(result + 20));
                        //****************************************************//

                        //Update the required value in ticker motion
                        mTickerView.setText(String.valueOf(2964));

                    }
                });
            }
        }, 3250);//delaying till the series item2 starts, so that we can
        //perform series animation and value update at the same time
    }


}
