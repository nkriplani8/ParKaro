package com.example.parkaro.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.parkaro.R;

public class SliderAdapter extends PagerAdapter {

        Context context;
        LayoutInflater layoutinflater;

        //constructor of adapter class
        public SliderAdapter(Context context) {
            this.context = context;
        }

        //init array of layouts
        int[] back = {R.layout.slide_layout1, R.layout.slide_layout2, R.layout.slide_layout3};


        @Override
        public int getCount() {
            return back.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == (ConstraintLayout) object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutinflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view = layoutinflater.inflate(back[position], container, false);
            container.addView(view);
            return view;
        }

        public void destroyItem(ViewGroup container, int Position, Object object) {
            container.removeView((ConstraintLayout) object);
        }

    }


