package com.example.parkaro.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.parkaro.Adapters.SliderAdapter;
import com.example.parkaro.R;

public class MainActivity extends AppCompatActivity {

    private ViewPager mslider_view;
    private LinearLayout mdots_layout;
    private TextView mdots[];
    private SliderAdapter slideradapter;
    private Button buttonNext;
    private Button buttonStart;
    private int mCurrentPage = 0;
    private Animation buttonstart_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize variables
        mslider_view = (ViewPager) findViewById(R.id.slider_view);
        mdots_layout = (LinearLayout) findViewById(R.id.dots_layout);
        buttonNext = findViewById(R.id.button_next);
        buttonStart = findViewById(R.id.get_started);
        slideradapter = new SliderAdapter(this);


        mslider_view.setAdapter(slideradapter);
        addDotsIndicator(0);//setting argument value
        mslider_view.addOnPageChangeListener(viewListener);

        //chec if intro opend before using shared preferences


        //moving to next screen after button clicking
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCurrentPage = mslider_view.getCurrentItem();
                if (mCurrentPage < 3) {
                    mCurrentPage++;
                    mslider_view.setCurrentItem(mCurrentPage);
                    buttonNext.setBackgroundResource(R.color.colorgreen);
                }
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login_activity = new Intent(getApplicationContext(), Login.class);
                startActivity(login_activity);
                savePrefData();
                finish();
            }
        });

       if (restorePrefData()) {

            Intent login_signup_activity = new Intent(getApplicationContext(), Login.class);
            startActivity(login_signup_activity);
            finish();

        }

    }

    private boolean restorePrefData() {
        SharedPreferences pref = this.getSharedPreferences("preferences", this.MODE_PRIVATE);
        boolean temp = pref.getBoolean("IntroOpenedBefore", false);
        return temp;

    }

    private void savePrefData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("preferences", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("IntroOpenedBefore", true);
        editor.commit();
    }


    public void addDotsIndicator(int position1) {
        mdots = new TextView[3];
        mdots_layout.removeAllViews();

        //set dots color and visiblity of buttons and dots
        for (int i = 0; i < mdots.length; i++) {
            mdots[i] = new TextView(this);
            mdots[i].setText(Html.fromHtml("&#8226", Html.FROM_HTML_MODE_LEGACY));
            mdots[i].setTextSize(35);
            mdots[i].setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            mdots_layout.addView(mdots[i]);

        }
        if (mdots.length > 0) {
            if (position1 == 0) {
                buttonNext.setVisibility(View.VISIBLE);
                buttonStart.setVisibility(View.INVISIBLE);
                mdots_layout.setVisibility(View.VISIBLE);
                mdots[position1].setTextColor(ContextCompat.getColor(this, R.color.colorgreen));
            } else if (position1 == 1) {// middle page
                mdots[position1].setTextColor(ContextCompat.getColor(this, R.color.colorgreen));//setting currently selected dot to white
                buttonNext.setVisibility(View.VISIBLE);
                buttonStart.setVisibility(View.INVISIBLE);
                mdots_layout.setVisibility(View.VISIBLE);
            } else {
                mdots[position1].setTextColor(ContextCompat.getColor(this, R.color.colorgreen));
                buttonstart_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start_button_anim);
                buttonstart_anim.setRepeatMode(Animation.INFINITE);
                buttonNext.setVisibility(View.INVISIBLE);
                buttonStart.setVisibility(View.VISIBLE);
                mdots_layout.setVisibility(View.INVISIBLE);
                buttonStart.setAnimation(buttonstart_anim);

            }

        }
    }

    //view pager functions
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);//calling indicator fun
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}