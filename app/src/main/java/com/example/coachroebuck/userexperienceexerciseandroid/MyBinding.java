package com.example.coachroebuck.userexperienceexerciseandroid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

class MyBinding
        extends RecyclerView.Adapter<MyViewHolder> {

    private final IViewHolderSelection callback;
    private final List<MyViewModel> list;
    private final int rows;
    private final int columns;
    private boolean isExpanded = false;

    MyBinding(IViewHolderSelection callback,
              final List<MyViewModel> list,
              final int rows,
              final int columns) {
        this.callback = callback;
        this.list = list;
        this.rows = rows;
        this.columns = columns;
    }

    void setSelectionState(final boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder,
                        parent,
                        false);

        final int width = parent.getMeasuredWidth() / columns;
        final int height = parent.getMeasuredHeight() / rows;
        inflatedView.setMinimumHeight(isExpanded ? 0 : height);
        inflatedView.setMinimumWidth(isExpanded ? 0 : width);
        inflatedView.getLayoutParams().width = width;
        inflatedView.getLayoutParams().height = height;
        return new MyViewHolder(inflatedView, callback);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        list.get(position).setPosition(position);

        holder.bind(list.get(position));

        if(isExpanded && list.get(position).isExpanded()) {
            holder.fullScreen();
        }
        else if(isExpanded) {
            holder.hide();
        }
        else if(!isExpanded){
            holder.show();
        }
    }

    @Override
    public int getItemCount() {
        return rows * columns;
    }

    public boolean getSelectionState() {
        return this.isExpanded;
    }
}
