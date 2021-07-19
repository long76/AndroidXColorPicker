package io.github.long76.androidxcolorpicker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.SeekBar;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.IntRange;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSeekBar;

import static io.github.long76.androidxcolorpicker.AXColorPickerFormatHelper.assertColorValueInRange;
import static io.github.long76.androidxcolorpicker.AXColorPickerFormatHelper.formatColorValues;

public class AXColorPicker extends Dialog implements AppCompatSeekBar.OnSeekBarChangeListener {

    private final Context context;

    private View colorView = null;
    private AppCompatSeekBar alphaSeekBar = null;
    private AppCompatSeekBar redSeekBar = null;
    private AppCompatSeekBar greenSeekBar = null;
    private AppCompatSeekBar blueSeekBar = null;
    private AppCompatEditText hexCode = null;
    private String text = null;
    private AXColorPickerCallback callback = null;
    private boolean ARGBFormat;
    private boolean autoClose;
    private boolean customButtonColor;
    private boolean customButtonTextSize;

    @Dimension(unit = 2)
    private float textSizeButton;

    @ColorInt
    private int backColorButton, textColorButton;

    @IntRange(from = 0, to = 255)
    private int alpha, red, green, blue;

    /**
     * Creator of the class. It will initialize the class with black color as default
     *
     * @param context The reference to the activity where the color picker is called
     * @since v2.0
     */
    public AXColorPicker(Context context) {
        super(context);

        this.context = context;

        if (context instanceof AXColorPickerCallback) {
            callback = (AXColorPickerCallback) context;
        }

        alpha = 255;
        red = 0;
        green = 0;
        blue = 0;

        ARGBFormat = false;
        autoClose = false;
        customButtonColor = false;
        customButtonTextSize = false;
    }

    /**
     * Creator of the class. It will initialize the class with the rgb color passed as default
     *
     * @param context The reference to the activity where the color picker is called
     * @param red     Red color for RGB values (0 - 255)
     * @param green   Green color for RGB values (0 - 255)
     * @param blue    Blue color for RGB values (0 - 255)
     *                <p>
     *                If the value of the colors it's not in the right range (0 - 255) it will
     *                be place at 0.
     * @since v2.0
     */
    public AXColorPicker(Context context,
                         @IntRange(from = 0, to = 255) int red,
                         @IntRange(from = 0, to = 255) int green,
                         @IntRange(from = 0, to = 255) int blue) {
        this(context);

        this.red = assertColorValueInRange(red);
        this.green = assertColorValueInRange(green);
        this.blue = assertColorValueInRange(blue);
    }

    /**
     * Creator of the class. It will initialize the class with the argb color passed as default
     *
     * @param context The reference to the activity where the color picker is called
     * @param alpha   Alpha value (0 - 255)
     * @param red     Red color for RGB values (0 - 255)
     * @param green   Green color for RGB values (0 - 255)
     * @param blue    Blue color for RGB values (0 - 255)
     *                <p>
     *                If the value of the colors it's not in the right range (0 - 255) it will
     *                be place at 0.
     *                <p>Set ARGBFormat to true</p>
     * @since v2.0
     */
    public AXColorPicker(Context context,
                         @IntRange(from = 0, to = 255) int alpha, @IntRange(from = 0, to = 255) int red,
                         @IntRange(from = 0, to = 255) int green, @IntRange(from = 0, to = 255) int blue) {
        this(context);

        this.alpha = assertColorValueInRange(alpha);
        this.red = assertColorValueInRange(red);
        this.green = assertColorValueInRange(green);
        this.blue = assertColorValueInRange(blue);

        ARGBFormat = true;
    }

    /**
     * Set text submit button.
     *
     * @param text Text button
     * @since v2.0
     */
    public void setTextButton(String text) {
        this.text = text;
    }

    /**
     * Get ARGB format flag value
     *
     * @return ARGB format value boolean (true/false)
     * @since v2.0
     */
    public boolean getARGBFormat() {
        return ARGBFormat;
    }

    /**
     * Set ARGB format flag
     *
     * @param ARGBFormat Show Alpha seekbar(true) or not(false)
     * @since v2.0
     */
    public void setARGBFormat(boolean ARGBFormat) {
        this.ARGBFormat = ARGBFormat;
    }

    /**
     * Get auto-close flag value
     *
     * @return Auto-close value boolean (true/false)
     * @since v2.0
     */
    public boolean getAutoClose() {
        return autoClose;
    }

    /**
     * Set auto-close dialog
     *
     * @param autoClose Close dialog after submit button click(true) or not(false)
     * @since v2.0
     */
    public void setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
    }

    /**
     * Set OnColorPicker listener
     *
     * @param listener Method implement AXColorPickerCallback interface
     * @since v2.0
     */
    public void setOnColorChosenListener(AXColorPickerCallback listener) {
        callback = listener;
    }

    /**
     * Set color for submit button
     *
     * @param backColor Background color
     * @param textColor Text color
     * @since v2.0
     */
    public void setColorButton(@ColorInt int backColor, @ColorInt int textColor) {
        backColorButton = backColor;
        textColorButton = textColor;
        customButtonColor = true;
    }


    /**
     * Set color for submit button
     *
     * @param textSizeButton Text size in float(sp)
     * @since v2.0
     */
    public void setTextSizeButton(@Dimension(unit = 2) float textSizeButton) {
        this.textSizeButton = textSizeButton;
        customButtonTextSize = true;
    }

    /**
     * Simple onCreate function. Here there is the init of the GUI.
     *
     * @param savedInstanceState As usual ...
     * @since v2.0
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.axcolorpicker_layout_color_picker);

        colorView = findViewById(R.id.colorView);

        hexCode = findViewById(R.id.hexCode);
        AppCompatButton okButton = findViewById(R.id.okColorButton);

        if (text != null) {
            okButton.setText(text);
        }

        if (customButtonColor) {
            okButton.setBackgroundColor(backColorButton);
            okButton.setTextColor(textColorButton);
        }

        if (customButtonTextSize) {
            okButton.setTextSize(textSizeButton);
        }

        alphaSeekBar = findViewById(R.id.alphaSeekBar);
        redSeekBar = findViewById(R.id.redSeekBar);
        greenSeekBar = findViewById(R.id.greenSeekBar);
        blueSeekBar = findViewById(R.id.blueSeekBar);

        alphaSeekBar.setOnSeekBarChangeListener(this);
        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar.setOnSeekBarChangeListener(this);

        hexCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(ARGBFormat ? 8 : 6)});

        hexCode.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event.getAction() == KeyEvent.ACTION_DOWN &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                updateColorView(v.getText().toString());
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(hexCode.getWindowToken(), 0);

                return true;
            }
            return false;
        });

        okButton.setOnClickListener(view -> sendColor());
    }

    private void initUi() {
        colorView.setBackgroundColor(getColor());

        alphaSeekBar.setProgress(alpha);
        redSeekBar.setProgress(red);
        greenSeekBar.setProgress(green);
        blueSeekBar.setProgress(blue);

        if (!ARGBFormat) {
            alphaSeekBar.setVisibility(View.GONE);
        }

        hexCode.setText(ARGBFormat
                ? formatColorValues(alpha, red, green, blue)
                : formatColorValues(red, green, blue)
        );
    }

    private void sendColor() {
        if (callback != null)
            callback.onColorChosen(getColor());
        if (autoClose) {
            dismiss();
        }
    }

    /**
     * Method that synchronizes the color between the bars, the view, and the HEX code text.
     *
     * @param color Android int of color.
     * @since v2.0
     */
    public void setColor(@ColorInt int color) {
        alpha = Color.alpha(color);
        red = Color.red(color);
        green = Color.green(color);
        blue = Color.blue(color);
    }

    /**
     * Method that synchronizes the color between the bars, the view, and the HEX code text.
     *
     * @param input HEX Code of the color.
     * @since v2.0
     */
    private void updateColorView(String input) {
        try {
            final int color = Color.parseColor('#' + input);
            alpha = Color.alpha(color);
            red = Color.red(color);
            green = Color.green(color);
            blue = Color.blue(color);

            colorView.setBackgroundColor(getColor());

            alphaSeekBar.setProgress(alpha);
            redSeekBar.setProgress(red);
            greenSeekBar.setProgress(green);
            blueSeekBar.setProgress(blue);
        } catch (IllegalArgumentException ignored) {
            hexCode.setError(context.getResources().getText(R.string.axcolorpicker_errHex));
        }
    }

    /**
     * Method called when the user change the value of the bars. This sync the colors.
     *
     * @param seekBar  SeekBar that has changed
     * @param progress The new progress value
     * @param fromUser Whether the user is the reason for the method call
     * @since v2.0
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == R.id.alphaSeekBar) {
            alpha = progress;
        } else if (seekBar.getId() == R.id.redSeekBar) {
            red = progress;
        } else if (seekBar.getId() == R.id.greenSeekBar) {
            green = progress;
        } else if (seekBar.getId() == R.id.blueSeekBar) {
            blue = progress;
        }

        colorView.setBackgroundColor(getColor());

        //Setting the inputText hex color
        hexCode.setText(ARGBFormat
                ? formatColorValues(alpha, red, green, blue)
                : formatColorValues(red, green, blue)
        );
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    /**
     * Getter for the RED value of the RGB selected color
     *
     * @return RED Value Integer (0 - 255)
     * @since v2.0
     */
    public int getRed() {
        return red;
    }

    /**
     * Getter for the GREEN value of the RGB selected color
     *
     * @return GREEN Value Integer (0 - 255)
     * @since v2.0
     */
    public int getGreen() {
        return green;
    }

    /**
     * Getter for the BLUE value of the RGB selected color
     *
     * @return BLUE Value Integer (0 - 255)
     * @since v2.0
     */
    public int getBlue() {
        return blue;
    }

    /**
     * Getter for the ALPHA value of the ARGB selected color
     *
     * @return ALPHA Value Integer (0 - 255)
     * @since v2.0
     */
    public int getAlpha() {
        return alpha;
    }

    /**
     * Set color RED.
     *
     * @param red RED color for RGB values (0 - 255)
     *            <p>If the value of the colors it's not in the right range (0 - 255) it will
     *            be place at 0.</p>
     * @since v2.0
     */
    public void setRed(@IntRange(from = 0, to = 255) int red) {
        this.red = assertColorValueInRange(red);
    }

    /**
     * Set color GREEN.
     *
     * @param green GREEN color for RGB values (0 - 255)
     *              <p>If the value of the colors it's not in the right range (0 - 255) it will
     *              be place at 0.</p>
     * @since v2.0
     */
    public void setGreen(@IntRange(from = 0, to = 255) int green) {
        this.green = assertColorValueInRange(green);
    }

    /**
     * Set color BLUE.
     *
     * @param blue BLUE color for RGB values (0 - 255)
     *             <p>If the value of the colors it's not in the right range (0 - 255) it will
     *             be place at 0.</p>
     * @since v2.0
     */
    public void setBlue(@IntRange(from = 0, to = 255) int blue) {
        this.blue = assertColorValueInRange(blue);
    }

    /**
     * Set color ALPHA.
     *
     * @param alpha ALPHA value (0 - 255)
     *              <p>If the value of the colors it's not in the right range (0 - 255) it will
     *              be place at 0.</p>
     *              <p>Set ARGBFormat to true</p>
     * @since v2.0
     */
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        this.alpha = assertColorValueInRange(alpha);
        ARGBFormat = true;
    }

    /**
     * Set color in ARGB.
     *
     * @param alpha Alpha value (0 - 255)
     * @param red   Red color for RGB values (0 - 255)
     * @param green Green color for RGB values (0 - 255)
     * @param blue  Blue color for RGB values (0 - 255)
     *              <p>If the value of the colors it's not in the right range (0 - 255) it will
     *              be place at 0.</p>
     *              <p>Set ARGBFormat to true</p>
     * @since v2.0
     */
    public void setARGB(@IntRange(from = 0, to = 255) int alpha, @IntRange(from = 0, to = 255) int red,
                        @IntRange(from = 0, to = 255) int green, @IntRange(from = 0, to = 255) int blue) {
        this.alpha = assertColorValueInRange(alpha);
        this.red = assertColorValueInRange(red);
        this.green = assertColorValueInRange(green);
        this.blue = assertColorValueInRange(blue);
        ARGBFormat = true;
    }

    /**
     * Set color in RGB.
     *
     * @param red   Red color for RGB values (0 - 255)
     * @param green Green color for RGB values (0 - 255)
     * @param blue  Blue color for RGB values (0 - 255)
     *              <p>If the value of the colors it's not in the right range (0 - 255) it will
     *              be place at 0.</p>
     *              <p>Set ARGBFormat to false</p>
     * @since v2.0
     */
    public void setRGB(@IntRange(from = 0, to = 255) int red, @IntRange(from = 0, to = 255) int green,
                       @IntRange(from = 0, to = 255) int blue) {
        this.red = assertColorValueInRange(red);
        this.green = assertColorValueInRange(green);
        this.blue = assertColorValueInRange(blue);
        ARGBFormat = false;
    }

    /**
     * Getter for the color as Android Color class value.
     * <p>From Android Reference: The Color class defines methods for creating and converting color
     * ints. Colors are represented as packed ints, made up of 4 bytes: alpha, red, green, blue.
     * The values are unpremultiplied, meaning any transparency is stored solely in the alpha
     * component, and not in the color components.</p>
     *
     * @return Selected color as Android Color class value.
     * @since v2.0
     */
    public int getColor() {
        return ARGBFormat ? Color.argb(alpha, red, green, blue) : Color.rgb(red, green, blue);
    }

    @Override
    public void show() {
        super.show();
        initUi();
    }
}
