/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode.decoder;

import java.util.ArrayList;
import java.util.List;


/**
 * ���Υ��饹�� ExpressionTable ��������¿�༰���������Execute ���줿
 * ���줿�Ȥ������Ϥ���� RS �֥�å����饨�顼���������������֤���
 * ECCodeGenerator == ErrorCollectionCodeGenerator
 *
 * @version	�������� 2002/11/15(Sun) �и�ë������ϯ
 */
class ECCodeGenerator {
    /** */
    public ECCodeGenerator() {
    }

    /** ���顼�����ɤ���������ؿ� */
    public BinaryString execute(final int ec, final int wc, BinaryString block, final PowerTable power, final ExpressionTable exp) {
        if (power == null || exp == null) {
            throw new IllegalArgumentException("Table is NULL");
        }
        if (block == null) {
            throw new IllegalArgumentException("RSBlock is NULL");
        }
        List<Integer> gp = exp.getGenerateExp(ec);
        List<Integer> gx = new ArrayList<Integer>();
        for (int i = ec; i >= 0; i--) {
            gx.add(new Integer(i));
        }
        GaloisPolynomial g = new GaloisPolynomial(gp, gx, power, GaloisPolynomial.POW_INDICATION);

        List<Integer> fi = block.GetDataByUINTArray();
        List<Integer> fx = new ArrayList<Integer>();
        for (int i = wc - 1; i >= ec; i--) {
            fx.add(new Integer(i));
        }
        GaloisPolynomial f = new GaloisPolynomial(fi, fx, power, GaloisPolynomial.INT_INDICATION);
        return f.operatorModulo(g).getDataByBinaryString();
    }
}

/* */
