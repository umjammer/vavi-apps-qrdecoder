/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode;


/**
 * This class generates BCH codes for the entered codeword.
 *
 * @version Created 2003/02/22(Sun) by Kentaro Ishitoya
 */
class BCHECCodeGenerator {

    /** Function to output BCH code. Changes the generator polynomial based on Mode. */
    BinaryString execute(BinaryString codeWord, BinaryString exp) {
        if (codeWord == null) {
            throw new IllegalArgumentException("codeword is NULL");
        }
        if (exp == null) {
            throw new IllegalArgumentException("codeword is NULL");
        }
        BCHPolynomial g = new BCHPolynomial(exp);
        BCHPolynomial i = new BCHPolynomial(codeWord);
        return i.operatorModulo(g).getDataByBinaryString();
    }
}
