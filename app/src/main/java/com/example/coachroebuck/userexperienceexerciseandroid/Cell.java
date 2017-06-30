package com.example.coachroebuck.userexperienceexerciseandroid;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cell extends RelativeLayout
        implements View.OnClickListener, View.OnTouchListener, ICellSelection {
    private ICellSelection callback;
    private LinearLayout rootLinearLayout;
    private int rows;
    private int columns;
    private boolean isAnimating = false;
    private Cell instance = this;
    private boolean isExpanded = false;
    private GestureDetector detector;
    private int position = -1;
    private int zOrder = 0;
    private List<LinearLayout> linearLayouts;
    private Cell selectedCell;
    private float pivotX = 0;
    private float pivotY = 0;

    public Cell(Context context) {
        super(context);
        init();
    }

    public Cell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Cell(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.info_view, this);
        this.rootLinearLayout = findViewById(R.id.rootLinearLayout);
        setOnClickListener(this);
        setOnTouchListener(this);

        detector = new GestureDetector(getContext(), new OnSwipeListener() {

            @Override
            public boolean onSwipe(Direction direction){
                // Possible implementation
                if(direction == Direction.left|| direction == Direction.right) {
                    // Do something COOL like animation or whatever you want
                    // Refer to your view if needed using a global reference
                    return true;
                }
                else if(direction == Direction.up && isExpanded) {
                    if(callback != null) {
                        callback.onSelectToCollapse(instance);
                    }
                    return true;
                }
                else if(direction == Direction.down && !isExpanded) {
                    if(callback != null) {
                        callback.onSelectToExpand(instance);
                    }
                    return true;
                }

                return false;
            }
        });
    }

    public void bind(final int rows, final int columns) {
        this.rows = rows;
        this.columns = columns;

        linearLayouts = new ArrayList<>();

        for(int i = 0; i < rows; i++) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT,
                    1.0f
            );

            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setLayoutParams(param);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            for(int j = 0; j < columns; j++) {
                final Random rnd = new Random();
                final int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                final int index = (i * rows) + j;
                Cell view = new Cell(getContext());
                view.setLayoutParams(param);
                view.setBackgroundColor(color);
                view.setPosition(index);
                view.setzOrder(zOrder + 1);
                view.setCallback(this);
                linearLayout.addView(view);
            }

            rootLinearLayout.addView(linearLayout);
            linearLayouts.add(linearLayout);
        }
    }

    @Override
    public void onClick(View view) {
        if(this.callback != null && !this.isExpanded) {
            this.callback.onSelectToExpand(this);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.d(this.getClass().getCanonicalName(), "onTouch: " + motionEvent.getAction()
                + "; " + motionEvent.getButtonState());
        return detector.onTouchEvent(motionEvent);
    }

    @Override
    public void onSelectToCollapse(final Cell cell) {
        this.selectedCell = null;
//        cell.setListener(false);
        updateForCollapsingOrExpanding(cell, false);
        cell.removeAllViews();
    }

    @Override
    public void onSelectToExpand(final Cell cell) {
        this.selectedCell = cell;
        updateForCollapsingOrExpanding(cell, true);
//        cell.setListener(true);
//        infoView.bind(rows, columns);
    }

    private void updateForCollapsingOrExpanding(final Cell cell,
                                                final boolean isExpanding) {
        if(!isAnimating) {
            isAnimating = true;
            isExpanded = isExpanding;
            final int x1 = isExpanded ? 1 : columns;
            final int x2 = isExpanded ? columns : 1;
            final int y1 = isExpanded ? 1 : rows;
            final int y2 = isExpanded ? rows : 1;
            if(isExpanded) {
                pivotX = ((cell.getPosition() % columns) * (float)(cell.getMeasuredWidth() + (cell.getMeasuredWidth() * 0.5))) / (float)getMeasuredWidth();
                pivotY = ((cell.getPosition() / rows) * (float)(cell.getMeasuredHeight() + (cell.getMeasuredHeight() * 0.5))) / (float)getMeasuredHeight();
            }
            scaleView(this, x1, x2, y1, y2, pivotX, pivotY);
        }
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
                preScale();
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                isAnimating = false;
                postScale();
            }
        });
        v.setAnimation(anim);
        v.startAnimation(anim);
    }

    private void preScale() {
        if(!isExpanded)  {
            updateVisibility();
        }
    }

    private void postScale() {
        if (isExpanded) {
            updateVisibility();
        }
    }

    private void updateVisibility() {
        for (LinearLayout linearLayout : linearLayouts) {
            if(this.selectedCell == null) {
                linearLayout.setVisibility(VISIBLE);
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    Cell nextCell = (Cell) linearLayout.getChildAt(i);
                    nextCell.setExpanded(false);
                    linearLayout.getChildAt(i).setVisibility(VISIBLE);
                }
            }
            else if (!this.selectedCell.getParent().equals(linearLayout)) {
                linearLayout.setVisibility(isExpanded ? GONE : VISIBLE);
            } else {
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    Cell nextCell = (Cell) linearLayout.getChildAt(i);
                    nextCell.setExpanded(isExpanded);
                    if (!linearLayout.getChildAt(i).equals(this.selectedCell)) {
                        linearLayout.getChildAt(i).setVisibility(isExpanded ? GONE : VISIBLE);
                    }
                }
            }
        }
    }

    public void setCallback(ICellSelection callback) {
        this.callback = callback;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getzOrder() {
        return zOrder;
    }

    public void setzOrder(int zOrder) {
        this.zOrder = zOrder;
    }

    public void setExpanded(boolean expanded) {
        this.isExpanded = expanded;
    }

    public boolean isExpanded() {
        return isExpanded;
    }
}
