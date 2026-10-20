/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode;

import java.io.IOException;
import java.util.List;


/**
 * This class is for reading files composed of rows and columns and supporting data retrieval.
 * It is an abstract base class that serves as an interface providing data access mainly to table classes.
 *
 * @version Created 2002/11/11 by Kentaro Ishitoya
 */
interface FileReader {

    /**
     * Read interface
     *
     * @throws IOException
     */
    void loadFromFile(String fileName) throws IOException;

    /** Data retrieval interface */
    List<Integer> getData(int row);

    /** Data retrieval interface */
    int getData(int row, int col);

    /** Get maximum row */
    int getMaxRow();

    /** Get maximum column */
    int getMaxCol();
}
