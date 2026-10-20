/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode;


/**
 * This class calculates penalty scores for masks applied to CQRCodeImage and
 * returns the mask specifier with the lowest penalty score as an unsigned int.
 *
 * @version 2002/02/14(Fri) by Kentaro Ishitoya
 */
class MaskSelector {

    final static int WEIGHT_Adjacent = 3;
    final static int WEIGHT_Block = 3;
    final static int WEIGHT_Pattern = 40;
    final static int WEIGHT_Dark = 10;

    /** Constructor */
    public MaskSelector() {
    }

    /** Returns the most appropriate mask specifier for the given QRCodeImage */
    public MaskDecorator.Mask rateMask(QRCodeImage target) {
        int rate;
        int least = 0;
        MaskDecorator.Mask index = null;
        BinaryImage temp;

        // Score each pattern.
        for (MaskDecorator.Mask i : MaskDecorator.Mask.values()) {
            rate = 0;
            temp = target.getImage(i);
            rate += rateMaskAdjacent(temp);
            rate += rateMaskBlock(temp);
            rate += rateMaskPattern(temp);
            rate += rateMaskDark(temp);
            if (rate < least || i == MaskDecorator.Mask.PATTERN0) {
                least = rate;
                index = i;
            }
            System.out.println(i + "        :");
            System.out.println("Adjacent : " + rateMaskAdjacent(temp));
            System.out.println("Block    : " + rateMaskBlock(temp));
            System.out.println("Pattern  : " + rateMaskPattern(temp));
            System.out.println("Dark     : " + rateMaskDark(temp));
            System.out.println("Rate     : " + rate);
            System.out.println();
        }
        return index;
    }

    /** Function to calculate penalty score for adjacent modules of the same color */
    private int rateMaskAdjacent(BinaryImage image) {
        int row = image.getMaxRow();
        int col = image.getMaxCol();
        boolean module;
        int adjacent;
        int rate = 0;
        for (int y = 0; y < row; y++) {
            adjacent = 0;
            for (int x = 0; x < row; x++) {
                module = image.getPixel(x, y);

                if (x + 1 >= row) {
                    continue;
                }

                if (image.getPixel(x + 1, y) != module) {
                    if (adjacent > 5) {
                        rate += adjacent - 5;
                    }
                    adjacent = 0;
                }
                adjacent++;
            }
        }
        for (int x = 0; x < row; x++) {
            adjacent = 0;
            for (int y = 0; y < col; y++) {
                module = image.getPixel(x, y);

                if (y + 1 >= col) {
                    continue;
                }

                if (image.getPixel(x, y + 1) != module) {
                    if (adjacent > 5) {
                        rate += adjacent - 5;
                    }
                    adjacent = 0;
                }
                adjacent++;
            }
        }
        return rate;
    }

    /** Calculate penalty score for modules of the same color block. */
    private int rateMaskBlock(BinaryImage image) {
        int row = image.getMaxRow();
        int col = image.getMaxCol();
        BinaryImage temp = new BinaryImage();
        temp.initialize(row, col, false);
        BinaryImage bimg = new BinaryImage();
        bimg.initialize(2, 2, true);
        boolean module;
        int block = 0;
        for (int x = 0; x < row; x++) {
            for (int y = 0; y < col; y++) {
                if (temp.getPixel(x, y)) {
                    continue;
                } // Already evaluated.

                module = image.getPixel(x, y);
                if (!module) {
                    continue;
                }

                if (x + 1 >= col || y + 1 >= row) {
                    continue;
                }        // Is the bottom-right pixel.
                if (image.getPixel(x + 1, y) != module) {
                    continue;
                }        // Right neighbor
                if (image.getPixel(x, y + 1) != module) {
                    continue;
                }        // Below
                if (image.getPixel(x + 1, y + 1) != module) {
                    continue;
                }        // Bottom-right
                // If all the same color, add penalty score.
                block++;
                temp.or(x, y, bimg);
            }
        }
        return WEIGHT_Block * block;
    }

    /** Calculate penalty score for module patterns with 1:1:3:1:1 ratio. */
    private int rateMaskPattern(BinaryImage image) {
        int row = image.getMaxRow();
        int col = image.getMaxCol();

//      boolean module;
        int rate = 0;
        // Scan in column direction
        for (int y = 0; y < col; y++) {
            for (int x = 0; x < row; x++) {
                if (image.getPixel(x, y) && (x + 6) < row) {
                    // 1:1:3:1:1 (dark:light:dark:light:dark) pixel pattern search
                    // image.GetPixel() == true is dark ==false is light

                    if (x != 0) {
                        if (image.getPixel(x - 1, y)) {
                            continue;
                        } // Is the previous one a dark module
                    }
                    if (image.getPixel(x + 1, y)) {
                        continue;
                    } // Is it a light module
                    if (!image.getPixel(x + 2, y)) {
                        continue;
                    } // Is it a dark module
                    if (!image.getPixel(x + 3, y)) {
                        continue;
                    } // Is it a dark module
                    if (!image.getPixel(x + 4, y)) {
                        continue;
                    } // Is it a dark module
                    if (image.getPixel(x + 5, y)) {
                        continue;
                    } // Is it a light module
                    if (!image.getPixel(x + 6, y)) {
                        continue;
                    } // Is it a dark module
                    if (x + 7 != row) {
                        if (image.getPixel(x + 7, y)) {
                            continue;
                        } // Is the next one a dark module
                    }
                    rate += WEIGHT_Pattern;
                }
            }
        }
        // Scan in column direction
        for (int x = 0; x < row; x++) {
            for (int y = 0; y < col; y++) {
                if (image.getPixel(x, y) && (y + 6) < col) {
                    // 1:1:3:1:1 (dark:light:dark:light:dark) pixel pattern search
                    // image.getPixel() == true is dark ==false is light

                    if (y != 0) {
                        if (image.getPixel(x, y - 1)) {
                            continue;
                        }    // Previous is a light module
                    }
                    if (image.getPixel(x, y + 1)) {
                        continue;
                    }    // Is it a light module
                    if (!image.getPixel(x, y + 2)) {
                        continue;
                    }    // Is it a dark module
                    if (!image.getPixel(x, y + 3)) {
                        continue;
                    }    // Is it a dark module
                    if (!image.getPixel(x, y + 4)) {
                        continue;
                    }    // Is it a dark module
                    if (image.getPixel(x, y + 5)) {
                        continue;
                    }    // Is it a light module
                    if (!image.getPixel(x, y + 6)) {
                        continue;
                    }    // Is it a dark module
                    if (y + 7 != col) {
                        if (image.getPixel(x, y + 7)) {
                            continue;
                        }    // Next is a dark module
                    }
                    rate += WEIGHT_Pattern;
                }
            }
        }
        return rate;
    }

    /** Calculate penalty score for the ratio of dark modules to the whole. */
    private int rateMaskDark(BinaryImage image) {
        int row = image.getMaxRow();
        int col = image.getMaxCol();

        int modules = 0;
        // Scan in column direction
        for (int y = 0; y < col; y++) {
            for (int x = 0; x < row; x++) {
                if (!image.getPixel(x, y)) {
                    modules++;
                }
            }
        }
        return (int) (Math.abs(((double) modules / (double) (row * col) * 100.0 - 50.0) / 5.0) * WEIGHT_Dark);
    }
}
