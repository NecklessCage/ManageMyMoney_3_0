package com.bupi.ha.mmm_3_0.helpers;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.bupi.ha.mmm_3_0.R;

/**
 * Created by Htet Aung on 7/7/2016.
 */
public class DateCursorAdapter extends SimpleCursorAdapter {
    private final LayoutInflater inflater;
    private Context context;
    private Context appContext;
    private int layout;
    private Cursor cursor;

    public DateCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to, 0);
        this.layout = layout;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.cursor = c;
    } // End of DateCursorAdapter.

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    } // End of newView.

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
    } // End of bindView.

    @Override
    public void setViewText(TextView v, String text) {
        if (v.getId() == R.id.text_view_list_item_date
                || v.getId() == R.id.checked_text_view_list_item_date
                ) { // Make sure it matches your time field
            // You may want to try/catch with NumberFormatException in case `text` is not a numeric value
            text = Helpers.dateFormat.format(Long.parseLong(text));
        } // End of if.
        v.setText(text);
    } // End of setViewText
} // End of class.
