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
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;

import static io.github.long76.androidxcolorpicker.ColorFormatHelper.assertColorValueInRange;
import static io.github.long76.androidxcolorpicker.ColorFormatHelper.formatColorValues;

public class ColorPicker extends Dialog implements SeekBar.OnSeekBarChangeListener {

    private Context context;

    private View colorView = null;
    private SeekBar alphaSeekBar = null;
    private SeekBar redSeekBar = null;
    private SeekBar greenSeekBar = null;
    private SeekBar blueSeekBar = null;
    private EditText hexCode = null;
    private Button okButton = null;
    private int backColorButton, textColorButton;
    private int alpha, red, green, blue;
    private ColorPickerCallback callback = null;

    private boolean withAlpha;
    private boolean autoClose;
    private boolean buttonColor;

    /**
     * Creator of the class. It will initialize the class with black color as default
     *
     * @param context The reference to the activity where the color picker is called
     */
    public ColorPicker(Context context) {
        super(context);

        this.context = context;

        if (context instanceof ColorPickerCallback) {
            callback = (ColorPickerCallback) context;
        }

        alpha = 255;
        red = 0;
        green = 0;
        blue = 0;

        withAlpha = false;
        autoClose = false;
        buttonColor = false;
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
     */
    public ColorPicker(Context context,
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
     * @since v1.1.0
     */
    public ColorPicker(Context context,
                       @IntRange(from = 0, to = 255) int alpha,
                       @IntRange(from = 0, to = 255) int red,
                       @IntRange(from = 0, to = 255) int green,
                       @IntRange(from = 0, to = 255) int blue) {
        this(context);

        this.alpha = assertColorValueInRange(alpha);
        this.red = assertColorValueInRange(red);
        this.green = assertColorValueInRange(green);
        this.blue = assertColorValueInRange(blue);

        withAlpha = true;
    }

    /**
     * Enable auto-dismiss for the dialog
     */
    public void enableAutoClose() {
        autoClose = true;
    }

    /**
     * Disable auto-dismiss for the dialog
     */
    public void disableAutoClose() {
        autoClose = false;
    }

    public void setCallback(ColorPickerCallback listener) {
        callback = listener;
    }

    /**
     * Set color for submit button
     */
    public void setColorButton(int backColor, int textColor) {
        backColorButton = backColor;
        textColorButton = textColor;
        buttonColor = true;
    }

    /**
     * Simple onCreate function. Here there is the init of the GUI.
     *
     * @param savedInstanceState As usual ...
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.axcolorpicker_layout_color_picker);

        colorView = findViewById(R.id.colorView);

        hexCode = findViewById(R.id.hexCode);
        okButton = findViewById(R.id.okColorButton);

        if (buttonColor) {
            okButton.setBackgroundColor(backColorButton);
            okButton.setTextColor(textColorButton);
        }

        alphaSeekBar = findViewById(R.id.alphaSeekBar);
        redSeekBar = findViewById(R.id.redSeekBar);
        greenSeekBar = findViewById(R.id.greenSeekBar);
        blueSeekBar = findViewById(R.id.blueSeekBar);

        alphaSeekBar.setOnSeekBarChangeListener(this);
        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar.setOnSeekBarChangeListener(this);

        hexCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(withAlpha ? 8 : 6)});

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

        if (!withAlpha) {
            alphaSeekBar.setVisibility(View.GONE);
        }

        hexCode.setText(withAlpha
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
        hexCode.setText(withAlpha
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
     * Getter for the ALPHA value of the ARGB selected color
     *
     * @return ALPHA Value Integer (0 - 255)
     * @since v1.1.0
     */
    public int getAlpha() {
        return alpha;
    }

    /**
     * Getter for the RED value of the RGB selected color
     *
     * @return RED Value Integer (0 - 255)
     */
    public int getRed() {
        return red;
    }

    /**
     * Getter for the GREEN value of the RGB selected color
     *
     * @return GREEN Value Integer (0 - 255)
     */
    public int getGreen() {
        return green;
    }

    /**
     * Getter for the BLUE value of the RGB selected color
     *
     * @return BLUE Value Integer (0 - 255)
     */
    public int getBlue() {
        return blue;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public void setARGB(int alpha, int red, int green, int blue) {
        this.alpha = alpha;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public void setRGB(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * Getter for the color as Android Color class value.
     * <p>
     * From Android Reference: The Color class defines methods for creating and converting color
     * ints.
     * Colors are represented as packed ints, made up of 4 bytes: alpha, red, green, blue.
     * The values are unpremultiplied, meaning any transparency is stored solely in the alpha
     * component, and not in the color components.
     *
     * @return Selected color as Android Color class value.
     */
    public int getColor() {
        return withAlpha ? Color.argb(alpha, red, green, blue) : Color.rgb(red, green, blue);
    }

    @Override
    public void show() {
        super.show();
        initUi();
    }
}
