/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode;


/**
 * This class has the function of a filter that overlays BinaryImage data.
 * Depending on the selected mask code, data on the BinaryImage
 * is changed by mask operations.
 *
 * @version Created 2002/12/16(Mon) by Kentaro Ishitoya
 */
class MaskDecorator {

    /**
     * Enumeration of mask patterns (NOTMASKED is unlikely to be used)
     */
    enum Mask {
        PATTERN0,
        PATTERN1,
        PATTERN2,
        PATTERN3,
        PATTERN4,
        PATTERN5,
        PATTERN6,
        PATTERN7,
        NOTMASKED
    }

    /** */
    public MaskDecorator() {
        image = null;
    }

    /**
     * Initializer, sets reference to the image.
     */
    void Initialize(BinaryImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image does not initialized");
        }
        this.image = image;
    }

    /**
     * Function that filters and returns the pixel at position represented by x, y
     * with the pattern represented by {@link Mask}. If {@link Mask#NOTMASKED} is selected, raw data can be obtained.
     */
    boolean GetMaskedPixel(int x, int y, Mask mc /* = NOTMASKED */) {
        if (x > image.getMaxCol() || y > image.getMaxRow()) {
            throw new IllegalArgumentException("Index over flow");
        }
        boolean pixel = image.getPixel(x, y);
        // Apply mask processing to the obtained module and return.
        return switch (mc) {
            // The specification says to invert when (x + y) % 2 = 0 is true, but since we don't want to use the ! operator
            // return pixel as-is when the condition is true, and return inverted pixel when false.
            case PATTERN0 -> ((x + y) % 2) != 0 ? pixel : !pixel;
            case PATTERN1 -> (y % 2) != 0 ? pixel : !pixel;
            case PATTERN2 -> (x % 3) != 0 ? pixel : !pixel;
            case PATTERN3 -> ((x + y) % 3) != 0 ? pixel : !pixel;
            case PATTERN4 -> (((x / 3) + (y / 2)) % 2) != 0 ? pixel : !pixel;
            case PATTERN5 -> ((x * y) % 2 + (x * y) % 3) != 0 ? pixel : !pixel;
            case PATTERN6 -> (((x * y) % 2 + (x * y) % 3) % 2) != 0 ? pixel : !pixel;
            case PATTERN7 -> (((x + y) % 3 + (x + y) % 2) % 2) != 0 ? pixel : !pixel;
            default -> pixel;
        };
    }

    /**
     * Function that unmask and returns the pixel at position represented by x, y
     * with the pattern represented by {@link Mask}. If {@link Mask#NOTMASKED} is selected, raw data can be obtained.
     */
    boolean GetUnMaskedPixel(int x, int y, Mask mc /* = NOTMASKED */) {
        return GetMaskedPixel(x, y, mc);
    }

    /** Associated image */
    private BinaryImage image;
}
