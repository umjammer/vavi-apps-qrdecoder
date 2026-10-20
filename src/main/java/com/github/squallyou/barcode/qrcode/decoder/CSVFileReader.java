/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode.decoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * このクラスは CSV のデータを int 形で取得する手段を提供します。
 *
 * @version 新規作成 2002/11/11(Mon) 石戸谷　顕太朗
 * 実装完了 2002/11/12(Tue) 石戸谷　顕太朗
 */
class CSVFileReader implements FileReader {

    /** コンストラクタ */
    public CSVFileReader() {
        maxRow = 0;
        maxCol = 0;
    }

    /**
     * ファイルを {@link FileReader#stringList} に読み込んだ上で、FormatData を呼び出す関数
     *
     * @throws IOException
     */
    @Override
    public void loadFromFile(String fn) throws IOException {
        if (data != null) {
            data.clear();
            maxRow = 0;
            maxCol = 0;
        } else {
            data = new ArrayList<>();
        }
        InputStream is;
        File f = new File(fn);
        if (f.exists()) {
            is = new FileInputStream(f);
        } else {
            is = CSVFileReader.class.getResourceAsStream(fn.startsWith("/") ? fn : "/" + fn);
            if (is == null) {
                is = CSVFileReader.class.getResourceAsStream(fn);
            }
            if (is == null) {
                is = CSVFileReader.class.getResourceAsStream("/com/github/squallyou/barcode/qrcode/decoder/" + fn);
            }
            if (is == null) {
                throw new java.io.FileNotFoundException("Resource not found: " + fn);
            }
        }
        try (BufferedReader ifs = new BufferedReader(new InputStreamReader(is))) {
            String str;
            while ((str = ifs.readLine()) != null) {
                if (!str.trim().isEmpty()) {
                    data.add(convertString(str));
                }
            }
        }
        maxRow = data.size();
        maxCol = data.isEmpty() ? 0 : data.get(0).size();
    }

    /** データを行単位の配列で返す関数。 */
    @Override
    public List<Integer> getData(int row) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data is NULL");
        }
        if (row >= maxRow) {
            throw new IllegalArgumentException("index over flow");
        }
        return data.get(row);
    }

    /** データを行、列から特定して int で返す関数 */
    @Override
    public int getData(int row, int col) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data is NULL");
        }
        if (row >= maxRow || col >= data.get(row).size()) {
            throw new IllegalArgumentException("index over flow");
        }
        return data.get(row).get(col);
    }

    /** 行の最大数を返す関数 */
    @Override
    public int getMaxRow() {
        return maxRow;
    }

    /** 列の最大数を返す関数 */
    @Override
    public int getMaxCol() {
        return maxCol;
    }

    /** 解放を行なう関数 */
    private void release() {
        if (data != null) {
            data.clear();
        }
        maxRow = 0;
        maxCol = 0;
    }

    //  送られてきたカンマ区切りの文字列を、intの配列にして返す関数。
    private List<Integer> convertString(String str) {
        if (str == null) {
            throw new IllegalArgumentException("str is NULL");
        }
        List<Integer> v = new ArrayList<>();

        StringTokenizer iss = new StringTokenizer(str, ", \t");
        while (iss.hasMoreTokens()) {
            v.add(Integer.parseInt(iss.nextToken()));
        }
        return v;
    }

    /** データを格納する。 */
    private List<List<Integer>> data = new ArrayList<>();

    /** 最大行数 */
    private int maxRow;

    /** 最大列数 */
    private int maxCol;
}