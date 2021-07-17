# AndroidXColorPicker

![GitHub release (latest by date)](https://img.shields.io/github/v/release/long76/AndroidXColorPicker)
![GitHub](https://img.shields.io/github/license/long76/AndroidXColorPicker)
![Android CI](https://github.com/long76/AndroidXColorPicker/workflows/Android%20CI/badge.svg)
![Maven Central](https://img.shields.io/maven-central/v/io.github.long76/androidxcolorpicker)

Color Picker based on [repo](https://github.com/Pes8/android-material-color-picker-dialog) and updated to AndroidX.

## Usage

### Requirements

minSdkVersion `21`

### build.gradle

```groovy
implementation 'io.github.long76:androidxcolorpicker:3.0'
```

## Features

* Color Picker Dialog
* Color Picker Preference

## Example

Color Picker Dialog

```java
AXColorPicker colorPicker = new AXColorPicker(context);
colorPicker.setColor(Color.BLACK);
colorPicker.setAutoClose(true);
colorPicker.setOnColorChosenListener(color -> {
             Timber.d("Color %d choosen", color);
         });
colorPicker.show();
```

Color Picker Preference

### settings.xml

```xml
<io.github.long76.androidxcolorpicker.AXColorPickerPreference
    app:AXColorPickerPrefName="preferences"
    app:AXColorPickerPrefNameTextColor="TextColor"
    app:AXColorPickerPrefNameBackgroundColor="ElementColor"
    app:key="TextColor"
    app:summary="Select color text"
    app:title="Color text"
    app:defaultValue="@color/colorText">
</io.github.long76.androidxcolorpicker.AXColorPickerPreference>
```

## License

MIT License

Copyright (c) 2017 Simone Pessotto (<http://www.simonepessotto.it>)

Copyright (c) 2020 long76

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
