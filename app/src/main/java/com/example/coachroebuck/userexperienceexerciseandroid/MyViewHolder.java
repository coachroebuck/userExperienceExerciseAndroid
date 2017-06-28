package com.example.coachroebuck.userexperienceexerciseandroid;

import android.support.v7.widget.RecyclerView;
import android.view.View;

class MyViewHolder
        extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private final IViewHolderSelection callback;
    private MyViewModel myViewModel;

    MyViewHolder(final View view,
                 final IViewHolderSelection callback) {
        super(view);
        this.callback = callback;
        view.setOnClickListener(this);
    }

    void bind(final MyViewModel myViewModel) {

        final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams)this.itemView.getLayoutParams();

        this.myViewModel = myViewModel;
        this.itemView.setBackgroundColor(myViewModel.getBackgroundColor());

        if(myViewModel.getWidth() == 0) {
            myViewModel.setWidth(lp.width);
            myViewModel.setHeight(lp.height);
            myViewModel.setLeft(this.itemView.getLeft());
            myViewModel.setTop(this.itemView.getTop());
            myViewModel.setRight(this.itemView.getRight());
            myViewModel.setBottom(this.itemView.getBottom());
        }
    }

    void hide() {
        final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams)this.itemView.getLayoutParams();
        lp.width = 0;
        lp.height = 0;
        this.itemView.setLeft(0);
        this.itemView.setTop(0);
        this.itemView.setRight(0);
        this.itemView.setBottom(0);
    }

    void show() {
        final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams)this.itemView.getLayoutParams();
        lp.width = myViewModel.getWidth();
        lp.height = myViewModel.getHeight();
        this.itemView.setLeft(myViewModel.getLeft());
        this.itemView.setTop(myViewModel.getTop());
        this.itemView.setRight(myViewModel.getRight());
        this.itemView.setBottom(myViewModel.getBottom());
    }

    void fullScreen() {
        final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams)this.itemView.getLayoutParams();
        final int width = this.callback.getScreenWidth();
        final int height = this.callback.getScreenHeight();
        lp.width = width;
        lp.height = height;
        this.itemView.setLeft(0);
        this.itemView.setTop(0);
        this.itemView.setRight(width);
        this.itemView.setBottom(height);
    }

    @Override
    public void onClick(View view) {
        this.callback.notifyItemChanged(myViewModel, !myViewModel.isExpanded());
//        myViewModel.setExpanded(!myViewModel.isExpanded());
//        this.callback.notifyItemChanged(myViewModel.isExpanded());
    }
}
