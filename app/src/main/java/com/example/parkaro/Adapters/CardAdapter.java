package com.example.parkaro.Adapters;

import android.content.Context;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.parkaro.Activities.Dashboard;
import com.example.parkaro.Models.cardData;
import com.example.parkaro.R;

import java.util.List;

public class CardAdapter extends PagerAdapter{
    Context context;
    LayoutInflater layoutinflater;
    public CardView cardView;
    cardData cardData = new cardData();
    TextView hrs, fare;

    //constructor of adapter class
    public CardAdapter( Context context ) {

        this.context = context;
    }

    //init array of layouts
    int[] back = {R.layout.card_layout1, R.layout.card_layout2, R.layout.card_layout3, R.layout.card_layout4, R.layout.card_layout5};


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
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutinflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view =  layoutinflater.inflate(back[position], container, false);

        TextView place_name = view.findViewById(R.id.card_text);
        cardData = ((Dashboard)context).setPlaceName();
        if(cardData.getPlaceName() !=null)
        {
            place_name.setText(cardData.getPlaceName());
        }
        Button button = view.findViewById(R.id.proceed_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof Dashboard){
                    ((Dashboard)context).proceedButtonCall(Boolean.TRUE, hrs.getText().toString(),fare.getText().toString());
                }
            }
        });
        container.addView(view);
        return view;
    }

    private View mCurrentView;

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentView = (View)object;
        hrs = mCurrentView.findViewById(R.id.min_hrs);
        fare = mCurrentView.findViewById(R.id.fare);
    }

    public String setHrs(){
        return hrs.getText().toString();
    }
    public String setFare(){
        return fare.getText().toString();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    public void destroyItem(ViewGroup container, int Position, Object object) {
        container.removeView((ConstraintLayout) object);
    }


}
