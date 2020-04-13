package io.github.long76.androidxcolorpicker;

import androidx.annotation.ColorInt;

public interface AXColorPickerCallback {
    /**
     * Gets called whenever a user chooses a color from the ColorPicker, i.e., presses the
     * submit button.
     *
     * @param color Android Color int chosen
     * @since v2.0
     */

    void onColorChosen(@ColorInt int color);
}

