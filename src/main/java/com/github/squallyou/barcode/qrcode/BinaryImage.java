/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode;

import java.util.ArrayList;
import java.util.List;


/**
 * This class is for handling binary image data represented by '0' and '1'.
 *
 * @version Created 2002/12/16(Mon) by Kentaro Ishitoya
 */
class BinaryImage {

    /** Constructor */
    public BinaryImage() {
        data = new ArrayList<>();
    }

    /** Copy constructor */
    public BinaryImage(BinaryImage temp) {
        data = new ArrayList<>();
        if (temp.data != null) {
            for (List<Boolean> r : temp.data) {
                data.add(new ArrayList<>(r));
            }
        }
        row = temp.row;
        col = temp.col;
    }

    /** Assignment operator */
    public final BinaryImage operatorLet(BinaryImage right) {
        data.clear();
        if (right.data != null) {
            for (List<Boolean> r : right.data) {
                data.add(new ArrayList<>(r));
            }
        }
        row = right.row;
        col = right.col;
        return this;
    }

    /** Initialization function, allocates a region of size row * col and initializes with Val. */
    public void initialize(int x, int y, boolean val /* = false */) {
        col = x;
        row = y;
        data = new ArrayList<>(row);
        for (int r = 0; r < row; r++) {
            List<Boolean> cs = new ArrayList<>(col);
            for (int c = 0; c < col; c++) {
                cs.add(val);
            }
            data.add(cs);
        }
    }

    /** Initialization function, initializes with the passed 2D array. */
    public void initialize(List<List<Boolean>> v) {
        col = v.get(0).size();
        row = v.size();
        data = new ArrayList<>();
        for (List<Boolean> r : v) {
            data.add(new ArrayList<>(r));
        }
    }

    /** Rewrites one pixel in the image with the value Value. */
    public void putPixel(int x, int y, boolean val) {
        data.get(y).set(x, val);
    }

    /** Gets the value of the pixel at position X, Y in the image. */
    public boolean getPixel(int x, int y) {
        return data.get(y).get(x);
    }

    /** Function to get the maximum row of the stored image. */
    public int getMaxRow() {
        return row;
    }

    /** Function to get the maximum column of the stored image. */
    public int getMaxCol() {
        return col;
    }

    /** Inverts the value of the pixel at position X, Y in the image. */
    public void flip(int x, int y) {
        data.get(y).set(x, !data.get(y).get(x));
    }

    /** Fills the given rectangle with v */
    public void fill(int l, int t, int r, int b, boolean v) {
        if (row < t || row < b || col < l || col < r) {
            throw new IllegalArgumentException("overflow");
        }
        for (int y = t; y < b; y++) {
            for (int x = l; x < r; x++) {
                putPixel(x, y, v);
            }
        }
    }

    /** Function to discard and clear data */
    public void clear() {
        data.clear();
        row = 0;
        col = 0;
    }

    /** Returns the OR of the input image and the stored image. */
    public BinaryImage or(BinaryImage temp) {
        if (row != temp.row || col != temp.col) {
            throw new IllegalArgumentException("Size does not matched");
        }

        BinaryImage ret = new BinaryImage();
        ret.initialize(col, row, false);
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                ret.putPixel(x, y, getPixel(x, y) | temp.getPixel(x, y));
            }
        }
        return ret;
    }

    /**
     * Returns the OR of the input partial image and the same-sized portion at position X, Y of the stored image.
     */
    public BinaryImage or(int sx, int sy, BinaryImage temp) {
        if (row < sy + temp.row || col < sx + temp.col) {
            throw new IllegalArgumentException("Error : Overflow @ CBinaryImage::or x,y");
        }

        BinaryImage ret = this;
        for (int y = sy; y < sy + temp.row; y++) {
            for (int x = sx; x < sx + temp.col; x++) {
                ret.putPixel(x, y, getPixel(x, y) | temp.getPixel(x - sx, y - sy));
            }
        }
        return ret;
    }

    /** Inverts all Data and returns. */
    public BinaryImage not() {
        BinaryImage ret = new BinaryImage();
        ret.initialize(col, row, false);
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                ret.putPixel(x, y, !getPixel(x, y));
            }
        }
        return ret;
    }

    /** */
    private List<List<Boolean>> data = new ArrayList<>();

    /** */
    private int row;

    /** */
    private int col;
}
