/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode;


/**
 * このクラスは、入力された符号に対してBCH符号を生成する。
 *
 * @version 新規作成 2003/02/22(Sun) 石戸谷　顕太朗
 */
class BCHECCodeGenerator {

    /** コンストラクタ */
    public BCHECCodeGenerator() {
    }

    /** BCH符号を出力する関数。Modeによって生成多項式を変更する。 */
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
