/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode;

import java.io.IOException;


/**
 * This class provides mutual conversion between power representation and integer representation
 * using data received from the FileReader class passed from the client.
 *
 * @version Created 2002/11/13(Wed) by Kentaro Ishitoya
 *          Implementation completed 2002/11/14(Thu) by Kentaro Ishitoya
 */
class PowerTable extends Table {

    /** Constructor */
    public PowerTable() {
        file = null;
    }

    /**
     * Initializer that accepts a filename as String.
     * Accepts a FileReader class, initializes the File, and reads the file.
     *
     * @throws IOException
     */
    @Override
    public boolean initialize(String fn, FileReader temp) throws IOException {
        if (temp == null) {
            return false;
        }
        file = temp;
        file.loadFromFile(fn);
        return true;
    }

    /** Function to retrieve an integer from the table using degree as key. */
    public int convertPowerToInt(int p) {
        if (file == null) {
            throw new IllegalStateException("File is NULL");
        }
        p = p % 255;
        return file.getData(p, 0);
    }

    /** Function to retrieve a degree from the table using integer as key. */
    public int convertIntToPower(int i) {
        if (file == null) {
            throw new IllegalStateException("File is NULL");
        }
        if (i > 255) {
            return 0;
        }
        return file.getData(i, 1);
    }
}
