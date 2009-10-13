/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */ 

package vavi.util.barcode.qrcode.decoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * ���Υ��饹�� CSV �Υǡ����� int ���Ǽ���������ʤ��󶡤��ޤ���
 *
 * @version	�������� 2002/11/11(Mon) �и�ë������ϯ
 *          ������λ 2002/11/12(Tue) �и�ë������ϯ
 */
class CSVFileReader implements FileReader {
    /** ���󥹥ȥ饯�� */
    public CSVFileReader() {
        maxRow = 0;
        maxCol = 0;
    }

    /** �ե������ {@link FileReader#stringList} ���ɤ߹������ǡ�FormatData ��ƤӽФ��ؿ� 
     * @throws IOException*/
    public void loadFromFile(String fn) throws IOException {
        try {
            if (data != null) {
                data.clear();
                maxRow = 0;
                maxCol = 0;
            }
            BufferedReader ifs = new BufferedReader(new java.io.FileReader(fn));
            while (ifs.ready()) {
                String str = ifs.readLine(); 
                data.add(convertString(str));
            }
            maxRow = data.size();
            maxCol = data.get(0).size();
            ifs.close();
        } finally {
            release();
        }
    }

    /** �ǡ������ñ�̤�������֤��ؿ��� */
    public List<Integer> getData(int row) {
        try {
            if (data.isEmpty()) {
                throw new IllegalArgumentException("Data is NULL");
            }
            if (row > maxRow) {
                throw new IllegalArgumentException("index over flow");
            }
            return data.get(row);
        } finally {
            release();
        }
    }
    
    /** �ǡ�����ԡ��󤫤����ꤷ�� int ���֤��ؿ� */
    public int getData(int row, int col) {
        try {
            if (data.isEmpty()) {
                throw new IllegalArgumentException("Data is NULL");
            }
            if (row > maxRow || col > data.get(row).size()) {
                throw new IllegalArgumentException("index over flow");
            }
            return data.get(row).get(col).intValue();
        } finally {
            release();
        }
    }

    /** �Ԥκ�������֤��ؿ� */
    public int getMaxRow() {
        return maxRow;
    }

    /** ��κ�������֤��ؿ� */
    public int getMaxCol() {
        return maxCol;
    }

    /** ������Ԥʤ��ؿ� */
    private void release() {
        if (data != null) {
            data.clear();
        }
        maxRow = 0;
        maxCol = 0;
    }

    //  �����Ƥ�������޶��ڤ��ʸ�����int������ˤ����֤��ؿ���
    private List<Integer> convertString(String str) {
        try {
            if (str == null) {
                throw new IllegalArgumentException("str is NULL");
            }
            List<Integer> v = new ArrayList<Integer>();;

            StringTokenizer iss = new StringTokenizer(str, ", \t");
            while (iss.hasMoreTokens()) {
                v.add(new Integer(iss.nextToken()));
            }
            return v;
        } finally {
            release();
        }
    }

    /** �ǡ������Ǽ���롣 */
    private List<List<Integer>> data;

    /** ����Կ� */
    private int maxRow;

    /** ������� */
    private int maxCol;
}