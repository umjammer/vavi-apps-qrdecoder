/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode;

import java.util.ArrayList;
import java.util.List;


/**
 * このクラスは入力されてきたCDataCodeWordをRSブロックに分けて格納
 * し、ErrorCodeGeneratorにRSブロックを渡し、ECブロックを取得する為
 * インターフェイスを用意します。
 *
 * @version 新規作成 2002/11/14(Sat) 石戸谷　顕太朗
 */
class Message {

    /** コンストラクタ */
    public Message() {
        numberOfBlocks = 0;
        data.clear();
        rsBlocks.clear();
        ecBlocks.clear();
    }

    /**
     * 配置用に並べ替えられたデータを受取り、RSブロックとECブロックに正しく分割し
     * 格納するメソッド。
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
        // とりあえずデータクリア。
        rsBlocks.clear();
        ecBlocks.clear();
        rsLength.clear();
        ecLength.clear();
        this.data = new BinaryString();

        // 初期化処理開始
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

        // データを突っ込む
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

    /** データコードワードの設定及びメッセージの初期化 */
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

    /** ECCodeWord をブロックの末尾に格納 */
    public void addECBlock(BinaryString ecwords) {
        if (ecwords == null) {
            throw new IllegalArgumentException("ecwords is NULL");
        }
        ecBlocks.add(ecwords);
        data.operatorPlusLet(ecwords);
    }

    /** indexビット目のビットを返す。 */
    public boolean get(int index) {
        if (data == null) {
            throw new IllegalArgumentException("Data is NULL");
        }
        return data.at(index);
    }

    /** Dataを配置用に並び替えて取得できるメソッド */
    public BinaryString getData() {
        BinaryString ret = new BinaryString();

        int block = 0;
        int index = 0;
        int c = 0;
        for (int i = 0; c < wholeDCW; i++) {
            if (index < rsBlocks.get(block).GetLength() / 8) {
                ret.operatorPlusLet(rsBlocks.get(block).GetSubByte(index, 1));
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
                ret.operatorPlusLet(ecBlocks.get(block).GetSubByte(index, 1));
                c++;
            }
            block = (i + 1) % numberOfBlocks;
            if (block == 0) {
                index++;
            }
        }
        return ret;
    }

    /** Dataメンバの取得メソッド */
    public BinaryString getPlainData() {
        return data;
    }

    /** データの長さを返す */
    public int getDataLength() {
        if (data == null) {
            throw new IllegalArgumentException("Data is NULL");
        }
        return data.GetMaxLength();
    }

    /** ブロックの数を返す */
    public int getNumberOfBlocks() {
        if (data == null) {
            throw new IllegalArgumentException("Data is NULL");
        }
        return numberOfBlocks;
    }

    /** index 番目のRSブロックの長さを返す。 */
    public int getRSLength(int index) {
        if (index >= numberOfBlocks) {
            throw new IllegalArgumentException("index overflow");
        }
        return rsLength.get(index);
    }

    /** index 番目のエラー訂正語ブロックの長さを返す */
    public int getECLength(int index) {
        if (index >= numberOfBlocks) {
            throw new IllegalArgumentException("index overflow");
        }
        return ecLength.get(index);
    }

    /** index 番目の RS ブロックを返す */
    public BinaryString getRSBlockAt(int index) {
        if (index >= numberOfBlocks) {
            throw new IllegalArgumentException("index overflow");
        }
        return rsBlocks.get(index);
    }

    /** 全てのデータコード語の数 */
    private int wholeDCW;

    /** 全てのエラー訂正語の数 */
    private int wholeECCW;

    /** RS ブロックの数 (ECブロックの数はRSブロックの数に同じ。) */
    private int numberOfBlocks;

    /** RS ブロック、ECブロックを連結して格納しているデータ */
    private BinaryString data = new BinaryString();

    /** RS ブロックの総コード語数 */
    private List<Integer> rsLength = new ArrayList<>();

    /** EC ブロックのコード語数 */
    private List<Integer> ecLength = new ArrayList<>();

    /** RS ブロック */
    private List<BinaryString> rsBlocks = new ArrayList<>();

    /** エラー訂正語のブロック */
    private List<BinaryString> ecBlocks = new ArrayList<>();
}
