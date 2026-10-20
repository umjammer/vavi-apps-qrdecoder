/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode;

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
 * This class provides a means to retrieve CSV data in int form.
 *
 * @version Created 2002/11/11(Mon) by Kentaro Ishitoya
 *          Implementation completed 2002/11/12(Tue) by Kentaro Ishitoya
 */
class CSVFileReader implements FileReader {

    /** Constructor */
    public CSVFileReader() {
        maxRow = 0;
        maxCol = 0;
    }

    /**
     * Function that reads the file into {@link FileReader#stringList} and then calls FormatData
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

    /** Function to return data as an array by row. */
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

    /** Function to identify data by row and column and return as int */
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

    /** Function to return the maximum number of rows */
    @Override
    public int getMaxRow() {
        return maxRow;
    }

    /** Function to return the maximum number of columns */
    @Override
    public int getMaxCol() {
        return maxCol;
    }

    /** Function to release */
    private void release() {
        if (data != null) {
            data.clear();
        }
        maxRow = 0;
        maxCol = 0;
    }

    //  Function to convert the sent comma-separated string into an array of ints.
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

    /** Stores data. */
    private List<List<Integer>> data = new ArrayList<>();

    /** Maximum number of rows */
    private int maxRow;

    /** Maximum number of columns */
    private int maxCol;
}
