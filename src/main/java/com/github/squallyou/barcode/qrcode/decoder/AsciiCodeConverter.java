/*
 * Copyright (c) 2002-2003 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode.decoder;

import java.util.ArrayList;
import java.util.List;


/**
 * このクラスは、与えられた文字列を8バイトで区切られたビット列に直すクラス
 *
 * @version 新規作成 2002/11/25(Sut) 柴田 真武
 * 追加変更 2003/02/25(Tue) 石戸谷 顕太朗
 */
class AsciiCodeConverter implements CharacterCodeConverter {

    /** コンストラクタ */
    public AsciiCodeConverter() {
    }

    /** BinaryString に変換する */
    @Override
    public BinaryString convert(String data) {
        List<Byte> v = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            v.add((byte) data.charAt(i));
        }
        return new BinaryString(v);
    }

    /** BinaryString に変換する */
    @Override
    public BinaryString convert(byte[] data) {
        List<Byte> v = new ArrayList<>();
        for (byte datum : data) {
            v.add(datum);
        }
        return new BinaryString(v);
    }

    /** BinaryString に変換する */
    @Override
    public BinaryString convert(List<Byte> data) {
        return new BinaryString(data);
    }

    /** BinaryString から文字列に変換する */
    public String convertToString(BinaryString data, int bitOffset, int charCount) {
        byte[] bytes = new byte[charCount];
        for (int i = 0; i < charCount; i++) {
            int b = 0;
            for (int j = 0; j < 8; j++) {
                if (data.at(bitOffset + i * 8 + j)) {
                    b |= (1 << (7 - j));
                }
            }
            bytes[i] = (byte) b;
        }
        return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
    }
}
