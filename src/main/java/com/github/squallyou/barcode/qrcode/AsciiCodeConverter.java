/*
 * Copyright (c) 2002-2003 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode;

import java.util.ArrayList;
import java.util.List;


/**
 * This class converts the given string into a bit sequence divided by 8 bytes
 *
 * @version Created 2002/11/25(Sun) by Manabu Shibata
 *          Modified 2003/02/25(Tue) by Kentaro Ishitoya
 */
class AsciiCodeConverter implements CharacterCodeConverter {

    /** Constructor */
    public AsciiCodeConverter() {
    }

    /** Convert to BinaryString */
    @Override
    public BinaryString convert(String data) {
        List<Byte> v = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            v.add((byte) data.charAt(i));
        }
        return new BinaryString(v);
    }

    /** Convert to BinaryString */
    @Override
    public BinaryString convert(byte[] data) {
        List<Byte> v = new ArrayList<>();
        for (byte datum : data) {
            v.add(datum);
        }
        return new BinaryString(v);
    }

    /** Convert to BinaryString */
    @Override
    public BinaryString convert(List<Byte> data) {
        return new BinaryString(data);
    }

    /** Convert from BinaryString to string */
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
