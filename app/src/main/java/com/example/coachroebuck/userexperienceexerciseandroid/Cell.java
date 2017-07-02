package com.example.coachroebuck.userexperienceexerciseandroid;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Random;

public class Cell extends RelativeLayout
        implements View.OnClickListener, View.OnTouchListener {
    private LinearLayout rootLinearLayout;
    private int rows;
    private int columns;
    private Cell instance = this;
    private Cell containingCell;
    private Cell parentCell;
    private GestureDetector detector;
    private int position = -1;
    private int zOrder = 0;
    private float pivotX = 0;
    private float pivotY = 0;
    private OnSwipeListener.Direction direction = OnSwipeListener.Direction.none;

    public Cell(Context context) {
        super(context);
        init();
        containingCell = null;
    }

    public Cell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        containingCell = null;
    }

    public Cell(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        containingCell = null;
    }

    private void init() {
        inflate(getContext(), R.layout.info_view, this);
        this.rootLinearLayout = findViewById(R.id.rootLinearLayout);
        setOnClickListener(this);
        setOnTouchListener(this);

        detector = new GestureDetector(getContext(), new OnSwipeListener() {

            @Override
            public boolean onSwipe(Direction direction){
                instance.direction = direction;
                return direction != Direction.none;
            }
        });
    }

    public void bind(final int rows, final int columns) {
        this.rows = rows;
        this.columns = columns;

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
                final int index = (i * rows) + j;
                Cell childCell = addCell(this, index);
                childCell.setParentCell(parentCell);
                linearLayout.addView(childCell);
            }

            rootLinearLayout.addView(linearLayout);
        }
    }

    @Override
    public void onClick(View view) {
        if(containingCell != null) {
            containingCell.expand(getMeasuredWidth(),
                    getMeasuredHeight(),
                    position);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        final boolean result = detector.onTouchEvent(motionEvent);

        if (motionEvent.getAction() == MotionEvent.ACTION_UP
                || motionEvent.getAction() == MotionEvent.ACTION_POINTER_UP) {
            if (direction == OnSwipeListener.Direction.up
                    && parentCell != null
                    && containingCell != null) {
                if(containingCell != null) {
                    containingCell.collapseFromFullScreen(getMeasuredWidth(),
                            getMeasuredHeight(),
                            position);
                }
                if(parentCell != null) {
                    parentCell.collapse(getMeasuredWidth(),
                            getMeasuredHeight(),
                            position);
                }
//                final int x1 = 1;
//                final int x2 = 1 / containerCell.getColumns();
//                final int y1 = 1;
//                final int y2 = 1 / containerCell.getRows();
//                final int width = containerCell.getMeasuredWidth();
//                final int height = containerCell.getMeasuredHeight();
//                pivotX = ((position % containerCell.getColumns()) * (float) (width + (width * 0.5))) / (float) getMeasuredWidth();
//                pivotY = ((position / containerCell.getRows()) * (float) (height + (height * 0.5))) / (float) getMeasuredHeight();
//                scaleView(containerCell, x1, x2, y1, y2, pivotX, pivotY);

                if(parentCell != null) {
                    parentCell.collapse(getMeasuredWidth(),
                            getMeasuredHeight(),
                            position);
                }
            } else if (direction == OnSwipeListener.Direction.down
                    && containingCell != null) {
                containingCell.expand(getMeasuredWidth(),
                        getMeasuredHeight(),
                        position);
            }

            direction = OnSwipeListener.Direction.none;
        }

        return result;
    }

    private void expand(final int width,
                        final int height,
                        final int position) {
        final int x1 = 1;
        final int x2 = columns;
        final int y1 = 1;
        final int y2 = rows;
        pivotX = ((position % columns) * (float)(width + (width * 0.5))) / (float)getMeasuredWidth();
        pivotY = ((position / rows) * (float)(height + (height * 0.5))) / (float)getMeasuredHeight();
        scaleView(this, x1, x2, y1, y2, pivotX, pivotY, null);

        Cell childCell = addCell(null, position);
        childCell.setParentCell(this);
        childCell.bind(rows, columns);
        childCell.setPivotX(pivotX);
        childCell.setPivotY(pivotY);
        childCell.setzOrder(zOrder + 1);
        ConstraintLayout viewParent = (ConstraintLayout)this.getParent();
        viewParent.addView(childCell);
        scaleView(childCell,
                1/columns,
                1,
                1/rows,
                1,
                pivotX, pivotY, null);

        if(containingCell != null) {
            containingCell.expand();
        }
    }

    private void expand() {
        setVisibility(GONE);
    }

    private void collapse() {
        collapse(getMeasuredWidth(),
                getMeasuredHeight(),
                position);
    }

    private void collapseFromFullScreen(final int width,
                                        final int height,
                                        final int position) {
        final int x1 = 1;
        final int x2 = 1/columns;
        final int y1 = 1;
        final int y2 = 1/rows;
        pivotX = ((position % columns) * (float)(width + (width * 0.5))) / (float)getMeasuredWidth();
        pivotY = ((position / rows) * (float)(height + (height * 0.5))) / (float)getMeasuredHeight();
        scaleView(this, x1, x2, y1, y2, pivotX, pivotY,
                new Animation.AnimationListener(){
                    @Override
                    public void onAnimationStart(Animation arg0) {
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        ConstraintLayout constraintLayout = (ConstraintLayout)getParent();
                        constraintLayout.removeView(instance);
                    }
                });

        if(containingCell != null
                && containingCell.getVisibility() == VISIBLE) {
            containingCell.collapse();
        }if(containingCell != null) {
            containingCell.setVisibility(VISIBLE);
        }
    }

    private void collapse(final int width,
                          final int height,
                          final int position) {
        final int x1 = columns;
        final int x2 = 1;
        final int y1 = rows;
        final int y2 = 1;
        pivotX = ((position % columns) * (float)(width + (width * 0.5))) / (float)getMeasuredWidth();
        pivotY = ((position / rows) * (float)(height + (height * 0.5))) / (float)getMeasuredHeight();
        scaleView(this, x1, x2, y1, y2, pivotX, pivotY, null);

        if(containingCell != null
                && containingCell.getVisibility() == VISIBLE) {
            containingCell.collapse();
        }if(containingCell != null) {
            containingCell.setVisibility(VISIBLE);
        }
    }

    private Cell addCell(final Cell containingCell, final int position) {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT,
                1.0f
        );

        final Random rnd = new Random();
        final int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        Cell childCell = new Cell(getContext());
        childCell.setLayoutParams(param);
        childCell.setBackgroundColor(color);
        childCell.setPosition(position);
        childCell.setzOrder(zOrder);
        childCell.setContainingCell(containingCell);

        return childCell;
    }

    private void scaleView(View v,
                           final float x1,
                           final float x2,
                           final float y1,
                           final float y2,
                           final float pivotXValue,
                           final float pivotYValue,
                           final Animation.AnimationListener animationListener) {
        Animation anim = new ScaleAnimation(
                x1, x2, // Start and end values for the X axis scaling
                y1, y2, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, pivotXValue, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, pivotYValue); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(500);
        anim.setAnimationListener(animationListener);
        v.setAnimation(anim);
        v.startAnimation(anim);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setzOrder(int zOrder) {
        this.zOrder = zOrder;
    }

    public void setContainingCell(Cell containingCell) {
        this.containingCell = containingCell;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Cell getParentCell() {
        return parentCell;
    }

    public void setParentCell(Cell parentCell) {
        this.parentCell = parentCell;
    }
}
