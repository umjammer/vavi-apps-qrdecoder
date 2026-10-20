/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode;

import java.io.IOException;


/**
 * This class is an abstract base class for providing an interface to derived classes
 * for accessing data read from a file.
 *
 * @version Created 2002/11/13(Wed) by Kentaro Ishitoya
 */
abstract class Table {

    public Table() {
    }

    abstract boolean initialize(String fileName, FileReader file) throws IOException;

    protected FileReader file;
}
