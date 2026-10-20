/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode;

import java.util.ArrayList;
import java.util.List;


/**
 * This class obtains the generator polynomial from ExpressionTable and creates
 * error correction codewords from the input RS block when Execute is called.
 * ECCodeGenerator == Error Correction Code Generator
 *
 * @version Created 2002/11/15(Sun) by Kentaro Ishitoya
 */
class ECCodeGenerator {

    /** */
    public ECCodeGenerator() {
    }

    /** Function to generate error correction code */
    public BinaryString execute(int ec, int wc, BinaryString block, PowerTable power, ExpressionTable exp) {
        if (power == null || exp == null) {
            throw new IllegalArgumentException("Table is NULL");
        }
        if (block == null) {
            throw new IllegalArgumentException("RSBlock is NULL");
        }
        List<Integer> gp = exp.getGenerateExp(ec);
        List<Integer> gx = new ArrayList<>();
        for (int i = ec; i >= 0; i--) {
            gx.add(i);
        }
        GaloisPolynomial g = new GaloisPolynomial(gp, gx, power, GaloisPolynomial.POW_INDICATION);

        List<Integer> fi = block.GetDataByUINTArray();
        List<Integer> fx = new ArrayList<>();
        for (int i = wc - 1; i >= ec; i--) {
            fx.add(i);
        }
        GaloisPolynomial f = new GaloisPolynomial(fi, fx, power, GaloisPolynomial.INT_INDICATION);
        return f.operatorModulo(g).getDataByBinaryString();
    }
}
