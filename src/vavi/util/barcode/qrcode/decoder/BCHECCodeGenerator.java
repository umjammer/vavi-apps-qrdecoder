/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode.decoder;


/**
 * ���Υ��饹�ϡ����Ϥ��줿�����Ф���BCH�����������롣
 *
 * @version	�������� 2003/02/22(Sun) �и�ë������ϯ
 */
class BCHECCodeGenerator {
    /** ���󥹥ȥ饯�� */
    public BCHECCodeGenerator() {}

    /** BCH������Ϥ���ؿ���Mode�ˤ�ä�����¿�༰���ѹ����롣 */
    BinaryString execute(final BinaryString codeWord, final BinaryString exp) {
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

/* */
