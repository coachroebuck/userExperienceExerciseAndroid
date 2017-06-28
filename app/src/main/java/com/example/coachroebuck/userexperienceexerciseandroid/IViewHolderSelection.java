package com.example.coachroebuck.userexperienceexerciseandroid;

interface IViewHolderSelection {
    int getScreenWidth();
    int getScreenHeight();
    void notifyItemChanged(MyViewModel myViewModel, boolean isExpanded);
}
