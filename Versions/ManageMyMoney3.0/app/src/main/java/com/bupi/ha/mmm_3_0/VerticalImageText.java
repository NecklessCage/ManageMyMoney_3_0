package com.bupi.ha.mmm_3_0;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * TODO: document your custom view class.
 */
public class VerticalImageText extends LinearLayout {
    /**
     * Default values
     */
    public final int SEPARATOR_HEIGHT = 10;
    /**
     * The Views
     */
    private ImageView imageView;
    private View separator;
    private TextView textView;
    private Context context;

    /**
     * Constructors
     */
    public VerticalImageText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        // Get attribute values
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.ImageTextButton, 0, 0);
        Drawable image = attr.getDrawable(R.styleable.ImageTextButton_drawable);
        float height = attr.getDimension(R.styleable.ImageTextButton_separation, SEPARATOR_HEIGHT);
        String text = attr.getString(R.styleable.ImageTextButton_text);
        int bg_color = attr.getColor(R.styleable.ImageTextButton_backgroundColor, 0);
        int text_color = attr.getColor(R.styleable.ImageTextButton_textColor, 0);
        float text_size = attr.getDimensionPixelSize(R.styleable.ImageTextButton_textSize, 10);

        attr.recycle();

        // Layout params
        this.setOrientation(LinearLayout.VERTICAL);

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.vertical_image_text_button, this, true);

        // Set the attributes to the views
        imageView = (ImageView) getChildAt(0);
        imageView.setImageDrawable(image);
        imageView.setBackgroundColor(bg_color);

        // Separator is always transparent
        separator = getChildAt(1);
        separator.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, // width
                (int) height)); // height

        textView = (TextView) getChildAt(2);
        textView.setText(text);
        textView.setBackgroundColor(bg_color);
        textView.setTextColor(text_color);
        textView.setTextSize(text_size);
    }

    public VerticalImageText(Context context) {
        this(context, null);
    }

    /**
     * Setters
     */
    public void setText(String text) {
        textView.setText(text);
    }

    public void addButtonBorders(Drawable drawable) {
        // Set the background
        this.setBackground(drawable);
        // Change background colors for the child views
        imageView.setBackgroundColor(getResources().getColor(R.color.primary));
        textView.setBackgroundColor(getResources().getColor(R.color.primary));
    }
}
