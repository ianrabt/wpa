package net.ianrabt.wpa.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import net.ianrabt.wpa.R;

public class HeaderViewDecoration extends RecyclerView.ItemDecoration {

    private View mLayout;
    private TextView mText;

    public HeaderViewDecoration(final Context context, RecyclerView parent, @LayoutRes int resId, String day) {
        // inflate and measure the layout
        mLayout = LayoutInflater.from(context).inflate(resId, parent, false);
        mLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mText = (TextView) mLayout.findViewById(R.id.day_text_view);
        mText.setText(day);
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        // layout basically just gets drawn on the reserved space on top of the first view
        mLayout.layout(parent.getLeft(), 0, parent.getRight(), mLayout.getMeasuredHeight());
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (parent.getChildAdapterPosition(view) == 0) {
                c.save();
                final int height = mLayout.getMeasuredHeight();
                final int top = view.getTop() - height;
                c.translate(270, top+50);
                mLayout.draw(c);
                c.restore();
                break;
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.set(0, mLayout.getMeasuredHeight(), 0, 0);
        } else {
            outRect.setEmpty();
        }
    }
}
