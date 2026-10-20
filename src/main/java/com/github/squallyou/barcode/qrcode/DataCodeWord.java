/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode;


/**
 * A class that receives the character mode indicator, character count indicator, and string,
 * concatenates them, and stores them in a format suitable as data codewords for QRCode.
 *
 * @version Created 2002/12/12(Thu) by Kentaro Ishitoya
 *          Modified 2002/12/14(Sat) by Kentaro Ishitoya
 */
class DataCodeWord {

    /** Function to initialize DataCodeWord */
    public void setDataCodeWord(BinaryString mode, BinaryString count, BinaryString str, Symbol sym) {
        if (count == null || mode == null || str == null) {
            throw new IllegalArgumentException("Invalid arg");
        }
        charCount = count;
        this.mode = mode;
        string = str;
        dataCodeWord.operatorPlusLet(mode);
        dataCodeWord.operatorPlusLet(charCount);
        dataCodeWord.operatorPlusLet(string);
        padDataCodeWord(sym);
    }

    /** Function to get the character count indicator */
    public BinaryString getCharCount() {
        return charCount;
    }

    /** Function to get the character mode indicator */
    public BinaryString getMode() {
        return mode;
    }

    /** Function to get the string */
    public BinaryString getString() {
        return string;
    }

    /** Function to get the DataCodeWord */
    public BinaryString getDataCodeWord() {
        return dataCodeWord;
    }

    /** Function to return count data codewords from index */
    public BinaryString getSubDataCodeWord(int index, int count) {
        return dataCodeWord.GetSubByte(index, count);
    }

    /**
     * Function to concatenate padding bits and padding words to the end of the data code
     * concatenated in the order of mode indicator, character count indicator, and string.
     */
    private void padDataCodeWord(Symbol sym) {
        if (charCount == null || mode == null || string == null) {
            throw new IllegalArgumentException("One or more Data was not Initialized");
        }
        int wCodeWord = sym.getDataCodeWords();
        dataCodeWord.SetMaxLengthByByte(wCodeWord);

        // Add terminator pattern (0000), exit if symbol capacity is filled
        if (dataCodeWord.GetLength() < wCodeWord * 8) {
            int end = 8 - dataCodeWord.GetLength() % 8;
            for (int i = 0; i < end && i < 4; i++) {
                dataCodeWord.add(false);
            }
        } else {
            return;
        }

        // Add padding bits (0), exit if symbol capacity is filled
        if (dataCodeWord.GetLength() < wCodeWord * 8) {
            int remaind = dataCodeWord.GetLength() % 8;
            if (remaind != 0) {
                int padbit = 8 - remaind;
                for (int i = 0; i < padbit; i++) {
                    dataCodeWord.add(false);
                }
            }
        } else {
            return;
        }

        // Add padding codewords (11101100 and 00010001), exit if symbol capacity is filled
        if (dataCodeWord.GetLength() / 8 < wCodeWord) {
            int padword = wCodeWord - dataCodeWord.GetLength() / 8;
            for (int i = 0; i < padword; i++) {
                if ((i % 2) != 0) {
                    dataCodeWord.operatorPlusLet(new BinaryString("00010001"));
                } else {
                    dataCodeWord.operatorPlusLet(new BinaryString("11101100"));
                }
            }

        } else {
            return;
        }
    }

    /** */
    private BinaryString charCount;
    /** */
    private BinaryString mode;
    /** */
    private BinaryString string;
    /** */
    private BinaryString dataCodeWord;
}
