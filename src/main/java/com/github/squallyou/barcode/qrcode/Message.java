/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode;

import java.util.ArrayList;
import java.util.List;


/**
 * This class stores the input CDataCodeWord divided into RS blocks,
 * passes the RS blocks to ErrorCodeGenerator, and provides an interface
 * to obtain EC blocks.
 *
 * @version Created 2002/11/14(Sat) by Kentaro Ishitoya
 */
class Message {

    /** Constructor */
    public Message() {
        numberOfBlocks = 0;
        data.clear();
        rsBlocks.clear();
        ecBlocks.clear();
    }

    /**
     * Method that receives data rearranged for placement, correctly divides it into RS blocks and EC blocks,
     * and stores them.
     */
    public void setData(BinaryString rawData, Symbol sym) {
        if (rawData == null) {
            throw new IllegalArgumentException("Data is NULL");
        }
        if (sym == null || sym.isPartial()) {
            throw new IllegalArgumentException("Invalid Symbol");
        }
        if (rawData.GetLength() != sym.getWholeCodeWords() * 8 + sym.getRemainderBits()) {
            throw new IllegalArgumentException("Invalid Data");
        }
        // Clear data for now.
        rsBlocks.clear();
        ecBlocks.clear();
        rsLength.clear();
        ecLength.clear();
        this.data = new BinaryString();

        // Start initialization process
        int b1 = sym.getRsBlock1();
        int b2 = sym.getRsBlock2();
        int dwb1 = sym.getRSBlock1DataCodeWords();
        int dwb2 = sym.getRsBlock2DataCodeWords();
        numberOfBlocks = b1 + b2;
        wholeECCW = sym.getECCodeWords();
        wholeDCW = sym.getWholeCodeWords() - wholeECCW;

        for (int i = 0; i < b1; i++) {
            BinaryString bs = new BinaryString();
            bs.SetMaxLengthByByte(dwb1);
            rsBlocks.add(bs);
            rsLength.add(sym.getRSBlock1WholeCodes());
            ecLength.add(sym.getRSBlock1WholeCodes() - dwb1);
            BinaryString ec = new BinaryString();
            ec.SetMaxLengthByByte(sym.getRSBlock1WholeCodes() - dwb1);
            ecBlocks.add(ec);
        }
        for (int i = 0; i < b2; i++) {
            BinaryString bs = new BinaryString();
            bs.SetMaxLengthByByte(dwb2);
            rsBlocks.add(bs);
            rsLength.add(sym.getRsBlock2WholeCodes());
            ecLength.add(sym.getRsBlock2WholeCodes() - dwb2);
            BinaryString ec = new BinaryString();
            ec.SetMaxLengthByByte(sym.getRsBlock2WholeCodes() - dwb2);
            ecBlocks.add(ec);
        }

        // Insert data
        int c = 0;
        int maxDCW = Math.max(dwb1, dwb2);
        for (int round = 0; round < maxDCW; round++) {
            for (int block = 0; block < numberOfBlocks; block++) {
                int dwb = (block < b1) ? dwb1 : dwb2;
                if (round < dwb) {
                    rsBlocks.get(block).append(rawData.GetSubByte(c, 1));
                    c++;
                }
            }
        }

        int ec1 = sym.getRSBlock1WholeCodes() - dwb1;
        int ec2 = (b2 > 0) ? (sym.getRsBlock2WholeCodes() - dwb2) : 0;
        int maxECCW = Math.max(ec1, ec2);
        for (int round = 0; round < maxECCW; round++) {
            for (int block = 0; block < numberOfBlocks; block++) {
                int ec = (block < b1) ? ec1 : ec2;
                if (round < ec) {
                    ecBlocks.get(block).append(rawData.GetSubByte(c, 1));
                    c++;
                }
            }
        }

        this.data = new BinaryString();
        for (int block = 0; block < numberOfBlocks; block++) {
            this.data.append(rsBlocks.get(block));
        }
    }

    /** Set data codeword and initialize message */
    public void setDataCodeWord(DataCodeWord data, Symbol sym) {
        if (data == null) {
            throw new IllegalArgumentException("DataCodeWord is NULL");
        }
        int b1 = sym.getRsBlock1();
        int b2 = sym.getRsBlock2();
        int dwb1 = sym.getRSBlock1DataCodeWords();
        int dwb2 = sym.getRsBlock2DataCodeWords();
        int pos = 0;

        this.data.SetMaxLengthByByte(sym.getWholeCodeWords());
        this.data = data.getDataCodeWord();
        numberOfBlocks = b1 + b2;
        wholeECCW = sym.getECCodeWords();
        wholeDCW = sym.getWholeCodeWords() - wholeECCW;

        for (int i = 0; i < b1; i++) {
            rsBlocks.add(data.getSubDataCodeWord(pos, dwb1));
            rsLength.add(sym.getRSBlock1WholeCodes());
            ecLength.add(sym.getRSBlock1WholeCodes() - dwb1);
            pos += dwb1;
        }
        for (int i = 0; i < b2; i++) {
            rsBlocks.add(data.getSubDataCodeWord(pos, dwb2));
            rsLength.add(sym.getRsBlock2WholeCodes());
            ecLength.add(sym.getRsBlock2WholeCodes() - dwb2);
            pos += dwb2;
        }
    }

    /** Store ECCodeWord at the end of the block */
    public void addECBlock(BinaryString ecwords) {
        if (ecwords == null) {
            throw new IllegalArgumentException("ecwords is NULL");
        }
        ecBlocks.add(ecwords);
        data.append(ecwords);
    }

    /** Returns the bit at the index bit. */
    public boolean get(int index) {
        if (data == null) {
            throw new IllegalArgumentException("Data is NULL");
        }
        return data.at(index);
    }

    /** Method to rearrange and retrieve Data for placement */
    public BinaryString getData() {
        BinaryString ret = new BinaryString();

        int block = 0;
        int index = 0;
        int c = 0;
        for (int i = 0; c < wholeDCW; i++) {
            if (index < rsBlocks.get(block).GetLength() / 8) {
                ret.append(rsBlocks.get(block).GetSubByte(index, 1));
                c++;
            }
            block = (i + 1) % numberOfBlocks;
            if (block == 0) {
                index++;
            }
        }
        block = 0;
        index = 0;
        c = 0;
        for (int i = 0; c < wholeECCW; i++) {
            if (index < ecBlocks.get(block).GetLength() / 8) {
                ret.append(ecBlocks.get(block).GetSubByte(index, 1));
                c++;
            }
            block = (i + 1) % numberOfBlocks;
            if (block == 0) {
                index++;
            }
        }
        return ret;
    }

    /** Method to get the Data member */
    public BinaryString getPlainData() {
        return data;
    }

    /** Returns the length of data */
    public int getDataLength() {
        if (data == null) {
            throw new IllegalArgumentException("Data is NULL");
        }
        return data.GetMaxLength();
    }

    /** Returns the number of blocks */
    public int getNumberOfBlocks() {
        if (data == null) {
            throw new IllegalArgumentException("Data is NULL");
        }
        return numberOfBlocks;
    }

    /** Returns the length of the index-th RS block. */
    public int getRSLength(int index) {
        if (index >= numberOfBlocks) {
            throw new IllegalArgumentException("index overflow");
        }
        return rsLength.get(index);
    }

    /** Returns the length of the index-th error correction codeword block */
    public int getECLength(int index) {
        if (index >= numberOfBlocks) {
            throw new IllegalArgumentException("index overflow");
        }
        return ecLength.get(index);
    }

    /** Returns the index-th RS block */
    public BinaryString getRSBlockAt(int index) {
        if (index >= numberOfBlocks) {
            throw new IllegalArgumentException("index overflow");
        }
        return rsBlocks.get(index);
    }

    /** Total number of data codewords */
    private int wholeDCW;

    /** Total number of error correction codewords */
    private int wholeECCW;

    /** Number of RS blocks (number of EC blocks is the same as the number of RS blocks) */
    private int numberOfBlocks;

    /** Data that stores concatenated RS blocks and EC blocks */
    private BinaryString data = new BinaryString();

    /** Total number of codewords of RS blocks */
    private List<Integer> rsLength = new ArrayList<>();

    /** Number of codewords of EC blocks */
    private List<Integer> ecLength = new ArrayList<>();

    /** RS blocks */
    private List<BinaryString> rsBlocks = new ArrayList<>();

    /** Error correction codeword blocks */
    private List<BinaryString> ecBlocks = new ArrayList<>();
}
