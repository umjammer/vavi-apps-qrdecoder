/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode.decoder;


/**
 * このクラスは受け取ったシンボルとマスクパターンから形式情報を
 * 生成する。
 *
 * @version 新規作成 2003/02/24(Mon) 石戸谷 顕太朗
 * 追加変更 2003/02/27(Tue) 石戸谷 顕太朗
 */
class QRFormInfo {

    final static String FORM_BCHG = "10100110111";
    final static String FORM_MASK = "101010000010010";

    /** コンストラクタ */
    public QRFormInfo() {
        errorCollectionLevel = VersionTable.ErrorCollectionLevel.L;
        maskCode = MaskDecorator.Mask.PATTERN0;
    }

    // エンコーダー

    /** 形式情報を作成し返す関数。 */
    public void initialize(Symbol sym, MaskDecorator.Mask pattern) {
        if (sym == null) {
            throw new IllegalArgumentException("invalid ErrorCollectionLevel");
        }
        formInfo.clear();

        errorCollectionLevel = sym.getErrorCollectionLevel();
        maskCode = pattern;

        BinaryString code = new BinaryString();
        BinaryString ec = new BinaryString();
        BinaryString pad = new BinaryString();
        BinaryString mask = new BinaryString(FORM_MASK);
        BCHECCodeGenerator gen = new BCHECCodeGenerator();

        code = switch (errorCollectionLevel) {
            case L -> new BinaryString("01");
            case M -> new BinaryString("00");
            case Q -> new BinaryString("11");
            case H -> new BinaryString("10");
        };

        switch (maskCode) {
            case PATTERN0:
                code.operatorPlusLet(new BinaryString("000"));
                break;
            case PATTERN1:
                code.operatorPlusLet(new BinaryString("001"));
                break;
            case PATTERN2:
                code.operatorPlusLet(new BinaryString("010"));
                break;
            case PATTERN3:
                code.operatorPlusLet(new BinaryString("011"));
                break;
            case PATTERN4:
                code.operatorPlusLet(new BinaryString("100"));
                break;
            case PATTERN5:
                code.operatorPlusLet(new BinaryString("101"));
                break;
            case PATTERN6:
                code.operatorPlusLet(new BinaryString("110"));
                break;
            case PATTERN7:
                code.operatorPlusLet(new BinaryString("111"));
                break;
        }

        if (code.equals("00000")) {
            formInfo = mask;
            return;
        }

        // 誤り訂正語の生成（codeに足しているのは10乗）
        ec = gen.execute(new BinaryString(code + "0000000000"), new BinaryString(FORM_BCHG));

        // 帰ってきたコードが10bitに満たなければ前を0で埋める。
        if (ec.GetLength() < 10) {
            for (int i = 0; i < 10 - ec.GetLength(); i++) {
                pad.add(false);
            }
            ec = pad.operatorPlus(ec);
        }

        formInfo = mask.operatorXor((code.operatorPlus(ec)));
    }

    /** 形式情報の取得 */
    public BinaryString getFormInfo() {
        return formInfo;
    }

    // デコーダー

    /** 形式情報の格納されたBinaryStringを読み込んでデコード。 */
    public void initialize(BinaryString str) {
        // TODO ; この位置に固有の処理を追加してください。
        formInfo = str;
        BinaryString mask = new BinaryString(FORM_MASK);
        BinaryString unmasked = str.xor(mask);

        // ECL (bits 0..1)
        boolean bit0 = unmasked.at(0);
        boolean bit1 = unmasked.at(1);
        if (!bit0 && bit1) {
            errorCollectionLevel = VersionTable.ErrorCollectionLevel.L; // "01"
        } else if (!bit0 && !bit1) {
            errorCollectionLevel = VersionTable.ErrorCollectionLevel.M; // "00"
        } else if (bit0 && bit1) {
            errorCollectionLevel = VersionTable.ErrorCollectionLevel.Q; // "11"
        } else {
            errorCollectionLevel = VersionTable.ErrorCollectionLevel.H; // "10"
        }

        // Mask (bits 2..4)
        int patternIndex = (unmasked.at(2) ? 4 : 0) | (unmasked.at(3) ? 2 : 0) | (unmasked.at(4) ? 1 : 0);
        maskCode = MaskDecorator.Mask.values()[patternIndex];
    }

    /** エラー訂正レベルの取得 */
    public VersionTable.ErrorCollectionLevel getECL() {
        return errorCollectionLevel;
    }

    /** マスクコードの取得 */
    public MaskDecorator.Mask getMaskCode() {
        return maskCode;
    }

    /**  */
    private VersionTable.ErrorCollectionLevel errorCollectionLevel;
    /**  */
    private MaskDecorator.Mask maskCode;
    /**  */
    private BinaryString formInfo;
}

