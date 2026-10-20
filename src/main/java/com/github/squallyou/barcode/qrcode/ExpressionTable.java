/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode;

import java.util.ArrayList;
import java.util.List;


/**
 * This class returns a CGaloisPolynomial based on the Version and error level
 * passed from the client, using data received from the CFileReader class.
 *
 * @version Created 2002/12/14(Sat) by Kentaro Ishitoya
 */
class ExpressionTable extends Table {

    /** */
    public ExpressionTable() {
        file = null;
    }

    /** Initialization function */
    @Override
    public boolean initialize(String FileName, FileReader temp) {
        try {
            if (temp == null) {
                return false;
            }
            file = temp;
            file.loadFromFile(FileName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Function to return an array of the degrees of coefficients of the generator polynomial for ECCodeWords from data. */
    List<Integer> getGenerateExp(int ECCodeWords) {
        int index = 0;
        boolean isFound = false;
        for (int i = 0; i < file.getMaxRow(); i++) {
            if (file.getData(i, 0) == ECCodeWords) {
                index = i;
                isFound = true;
                break;
            }
        }

        if (!isFound) {
            throw new IllegalArgumentException("ECCount is not Exist");
        }

        List<Integer> gp = new ArrayList<>();
//    List<Integer> gx;
        for (int i = 1; i <= ECCodeWords + 1; i++) {
            gp.add(file.getData(index, i));
        }
        return gp;
    }
}
