package io.github.long76.androidxcolorpicker;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

public class AXColorPickerPreference extends Preference
        implements Preference.OnPreferenceClickListener,
        AXColorPickerCallback {

    private View view;
    private AXColorPicker AXColorPicker;
    private String text;
    private String prefName;
    private String prefNameTextColor;
    private String prefNameBackgroundColor;
    private boolean alphaEnabled;
    private boolean customButtonColor;
    private float density = 0;
    private Bitmap.Config bitmapConfig = Bitmap.Config.RGB_565;

    @ColorInt
    private int value = Color.BLACK;

    @ColorInt
    private int textColor;

    @ColorInt
    private int backgroundColor;

    @Dimension(unit = 2)
    private float textSize;


    public AXColorPickerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public AXColorPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public AXColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AXColorPickerPreference(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        density = getContext().getResources().getDisplayMetrics().density;
        setOnPreferenceClickListener(this);
        if (attrs != null) {
            final TypedArray typedArray = getContext().obtainStyledAttributes(
                    attrs, R.styleable.AXColorPickerPreference
            );
            try {
                alphaEnabled = typedArray.getBoolean(
                        R.styleable.AXColorPickerPreference_AXColorPickerAlphaEnabled,
                        false
                );

                textColor = typedArray.getColor(
                        R.styleable.AXColorPickerPreference_AXColorPickerTextColor,
                        Integer.MAX_VALUE
                );

                backgroundColor = typedArray.getColor(
                        R.styleable.AXColorPickerPreference_AXColorPickerBackgroundColor,
                        Integer.MAX_VALUE
                );

                text = typedArray.getString(
                        R.styleable.AXColorPickerPreference_AXColorPickerText
                );

                prefName = typedArray.getString(
                        R.styleable.AXColorPickerPreference_AXColorPickerPrefName
                );

                prefNameTextColor = typedArray.getString(
                        R.styleable.AXColorPickerPreference_AXColorPickerPrefNameTextColor
                );

                prefNameBackgroundColor = typedArray.getString(
                        R.styleable.AXColorPickerPreference_AXColorPickerPrefNameBackgroundColor
                );

                textSize = typedArray.getFloat(
                        R.styleable.AXColorPickerPreference_AXColorPickerTextSize,
                        14);
            } finally {
                typedArray.recycle();
            }
        }
        if (text == null) {
            text = context.getResources().getString(R.string.axcolorpicker_btnSelectColor);
        }
        if (textColor != Integer.MAX_VALUE && backgroundColor != Integer.MAX_VALUE)
            customButtonColor = true;
        if (alphaEnabled) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
            AXColorPicker = new AXColorPicker(context, 0, 0, 0, 0);
        } else
            AXColorPicker = new AXColorPicker(context);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        AXColorPicker.setColor(value);
        AXColorPicker.setAutoClose(true);
        AXColorPicker.setOnColorChosenListener(this);
        AXColorPicker.setTextSizeButton(textSize);
        AXColorPicker.setTextButton(text);
        if (customButtonColor) {
            AXColorPicker.setColorButton(backgroundColor, textColor);
        } else if (prefName != null) {
            SharedPreferences preferences = getContext().getSharedPreferences(prefName, Context.MODE_PRIVATE);
            if (prefNameBackgroundColor != null && prefNameTextColor != null) {
                AXColorPicker.setColorButton(
                        preferences.getInt(prefNameBackgroundColor, -1),
                        preferences.getInt(prefNameTextColor, -1)
                );
            }
        }
        AXColorPicker.show();
        return true;
    }

    @Override
    public void onColorChosen(int color) {
        if (isPersistent()) {
            persistInt(color);
        }
        value = color;
        setPreviewColor();
        if (getOnPreferenceChangeListener() != null)
            getOnPreferenceChangeListener().onPreferenceChange(this, color);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        view = holder.itemView;
        setPreviewColor();
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getColor(index, Color.BLACK);
    }

    @Override
    protected void onSetInitialValue(Object defaultValue) {
        onColorChosen(defaultValue != null ? (Integer) defaultValue : getPersistedInt(value));
    }

    private void setPreviewColor() {
        if (view == null) return;
        ImageView iView = new ImageView(getContext());
        LinearLayout widgetFrameView = view.findViewById(android.R.id.widget_frame);
        if (widgetFrameView == null) return;
        widgetFrameView.setVisibility(View.VISIBLE);
        widgetFrameView.setPadding(
                widgetFrameView.getPaddingLeft(),
                widgetFrameView.getPaddingTop(),
                (int) (density * 8),
                widgetFrameView.getPaddingBottom()
        );
        // remove already create preview image
        widgetFrameView.removeAllViews();
        widgetFrameView.addView(iView);
        widgetFrameView.setMinimumWidth(0);
        iView.setImageBitmap(getPreviewBitmap());
    }

    private Bitmap getPreviewBitmap() {
        int d = (int) (density * 31); //30dip
        Bitmap bm = Bitmap.createBitmap(d, d, bitmapConfig);
        int w = bm.getWidth();
        int h = bm.getHeight();
        int c;
        for (int i = 0; i < w; i++) {
            for (int j = i; j < h; j++) {
                c = i <= 1 || i >= w - 2 || j >= h - 2 ? Color.GRAY : value;
                bm.setPixel(i, j, c);
                if (i != j) {
                    bm.setPixel(j, i, c);
                }
            }
        }

        return bm;
    }
}
