package com.example.coachroebuck.userexperienceexerciseandroid;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InfoView extends RelativeLayout implements IViewHolderSelection {

    RecyclerView recyclerView;
    private int rows;
    private int columns;
    private List<MyViewModel> list = new ArrayList<>();

    public InfoView(Context context) {
        super(context);
        init();
    }

    public InfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.info_view, this);
        this.recyclerView = findViewById(R.id.recyclerView);
    }

    public void bind(final int rows, final int columns) {
        this.rows = rows;
        this.columns = columns;


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), columns);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        for(int i = 0; i < (rows * columns); i++) {
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            list.add(new MyViewModel(false, color));
        }
        MyBinding binding = new MyBinding (this, list, rows, columns);
        recyclerView.setAdapter(binding);
        binding.notifyDataSetChanged();
    }

    @Override
    public int getScreenWidth() {
        return getMeasuredWidth();
    }

    @Override
    public int getScreenHeight() {
        return getMeasuredHeight();
    }

    @Override
    public void notifyItemChanged(MyViewModel myViewModel, boolean isExpanded) {
        MyBinding binding = (MyBinding)recyclerView.getAdapter();
        final boolean expanded = !binding.getSelectionState();
        final float pivotX = ((myViewModel.getPosition() % columns) * (float)(myViewModel.getWidth() + (myViewModel.getWidth() * 0.5))) / (float)getMeasuredWidth();
        final float pivotY = ((myViewModel.getPosition() / rows) * (float)(myViewModel.getHeight() + (myViewModel.getHeight() * 0.5))) / (float)getMeasuredHeight();
        binding.setSelectionState(expanded);
        final int x1 = expanded ? 1 : columns;
        final int x2 = expanded ? columns : 1;
        final int y1 = expanded ? 1 : rows;
        final int y2 = expanded ? rows : 1;
        scaleView(recyclerView, x1, x2, y1, y2, pivotX, pivotY);

//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), isExpanded ? 1 : columns);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        recyclerView.getAdapter().notifyItemRangeChanged(0, rows * columns);
    }

    private void scaleView(View v, float x1, float x2, float y1, float y2, float pivotXValue, float pivotYValue) {
        Animation anim = new ScaleAnimation(
                x1, x2, // Start and end values for the X axis scaling
                y1, y2, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, pivotXValue, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, pivotYValue); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(500);
        anim.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
            }
        });
        v.setAnimation(anim);
        v.startAnimation(anim);
    }
}
