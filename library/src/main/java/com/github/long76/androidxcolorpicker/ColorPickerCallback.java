package com.github.long76.androidxcolorpicker;

import androidx.annotation.ColorInt;

public interface ColorPickerCallback {
    /**
     * Gets called whenever a user chooses a color from the ColorPicker, i.e., presses the
     * "Choose" button.
     *
     * @param color Color chosen
     */

    void onColorChosen(@ColorInt int color);
}

