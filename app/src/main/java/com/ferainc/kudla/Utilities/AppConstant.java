package com.ferainc.kudla.Utilities;
import java.util.Arrays;
import java.util.List;

public class AppConstant {

    // Number of columns of Grid View
    public static final int NUM_OF_COLUMNS = 5;

    // Gridview image padding
    public static final int GRID_PADDING = 8; // in dp

    // SD card image directory
    //public static final String PHOTO_ALBUM = "androidhive";
    public static final String PHOTO_ALBUM = "/storage/emulated/0/WhatsApp/Media";

    // supported file formats
    public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg", "png");

    /* Background image size
    xxxhdpi: 1280x1920 px
    xxhdpi: 960x1600 px
    xhdpi: 640x960 px
    hdpi: 480x800 px
    mdpi: 320x480 px
    ldpi: 240x320 px */

    /* Launcher icon size
    Google play: 512x512 px
    xxxhdpi: 192x192 px  (640 dpi)
    xxhdpi: 144x144 px  (480 dpi)
    xhdpi: 96x96 px (320 dpi)
    hdpi: 72x72 px  (240 dpi)
    mdpi: 48x48 px  (160 dpi)
    ldpi: 36x36 px  (120 dpi)*/

     /* Tab bar icon size
     xxxhdpi: 128x128 px
    xxhdpi: 96x96 px
    xhdpi: 64x64 px
    hdpi: 48x48 px
    mdpi: 32x32 px
    ldpi: 32x32 px */

    /* Colours
        #8A6EFF
        #BEA2FF
        #8495FF
        #5769FF
        #FF41D3
        #21759b
     */

    //copperplate font for logo

    /* Firebase Storage public access
    service firebase.storage {
  match /b/kudla-53b1f.appspot.com/o {
    match /{allPaths=**} {
      allow read, write;
            }
        }
    }*/

}