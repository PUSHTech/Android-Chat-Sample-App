package com.pushtech.pushchat.androidapplicationexample.chat.contacts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by goda87 on 5/09/14.
 */
public class CheckableLinearLayout extends LinearLayout implements Checkable {
    private boolean isChecked;
    private List<Checkable> checkableViews = new ArrayList<Checkable>();

    public CheckableLinearLayout(Context context) {
        super(context);
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // @see android.widget.Checkable#isChecked()
    public boolean isChecked() {
        return isChecked;
    }

    // @see android.widget.Checkable#setChecked(boolean)
    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        for (Checkable c : checkableViews) {
            // Pass the information to all the child Checkable widgets
            c.setChecked(isChecked);
        }
    }

    // @see android.widget.Checkable#toggle()
    public void toggle() {
        this.isChecked = !this.isChecked;
        for (Checkable c : checkableViews) {
            // Pass the information to all the child Checkable widgets
            c.toggle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final int childCount = this.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            findCheckableChildren(this.getChildAt(i));
        }
    }

    /**
     * Add to our checkable list all the children of the view that implement the
     * interface Checkable
     */
    private void findCheckableChildren(View v) {
        if (v instanceof Checkable) {
            this.checkableViews.add((Checkable) v);
        }
        if (v instanceof ViewGroup) {
            final ViewGroup vg = (ViewGroup) v;
            final int childCount = vg.getChildCount();
            for (int i = 0; i < childCount; ++i) {
                findCheckableChildren(vg.getChildAt(i));
            }
        }
    }
}