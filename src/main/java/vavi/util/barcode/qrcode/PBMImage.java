/*
 * Copyright (c) 2002-2003 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


/**
 * This class handles PBM images
 *
 * @version Created 2003/02/27(Tue) by Kentaro Ishitoya
 */
class PBMImage {

    /** Initialization function. */
    public void initialize(BinaryImage temp) {
        if (temp == null) {
            throw new IllegalArgumentException("Image is NULL");
        }
        if (image != null) {
            image.clear();
        }
        image = temp;
    }

    /** Save to file in PBM format. */
    public boolean saveToFile(String fn, double scale /* = 1 */) throws IOException {
        if (fn == null) {
            throw new IllegalArgumentException("Image is NULL");
        }
        if (scale < 1) {
            throw new IllegalArgumentException("ts is smaller than 1");
        }
        int row = (int) (image.getMaxRow() * scale);
        int col = (int) (image.getMaxCol() * scale);
        Writer ofs;
        try {
            ofs = new FileWriter(fn);
        } catch (IOException e) {
            return false;
        }
        ofs.write("P1");
        ofs.write("\n");
        ofs.write(Integer.toString(row));
        ofs.write(" ");
        ofs.write(Integer.toString(col));
        ofs.write("\n");

        for (int y = 0; y < col; y++) {
            for (int x = 0; x < row; x++) {
                if (image.getPixel((int) (x / scale), (int) (y / scale))) {
                    ofs.write("1");
                } else {
                    ofs.write("0");
                }
                if (x != row - 1) {
                    ofs.write(" ");
                }
            }
            if (y != col - 1) {
                ofs.write("\n");
            }
        }
        ofs.close();
        return true;
    }

    /**
     * Function to read PBM file
     *
     * @throws IOException
     */
    public boolean loadFromFile(String fn) throws IOException {
        if (fn == null) {
            throw new IllegalArgumentException("Image is NULL");
        }
        image.clear();
        BufferedReader ifs;
        String str;
        int col;
        int row;
        int bit;

        try {
            ifs = new BufferedReader(new java.io.FileReader(fn));
        } catch (IOException e) {
            return false;
        }

        str = ifs.readLine(); // @@@ scanf
        if (str != "P1") {
            return false;
        }

        row = ifs.read(); // @@@
        col = ifs.read(); // @@@
        image.initialize(row, col, false);
        for (int y = 0; y < col; y++) {
            for (int x = 0; x < row; x++) {
                bit = ifs.read(); // @@@
                if (bit >= 1) {
                    image.putPixel(x, y, true);
                }
            }
        }

        ifs.close();
        return true;
    }

    /** Function to return BinaryImage */
    public BinaryImage getImage() {
        return image;
    }

    /** */
    private BinaryImage image;
}