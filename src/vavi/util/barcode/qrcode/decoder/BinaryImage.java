/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode.decoder;

import java.util.ArrayList;
import java.util.List;


/**
 * ���Υ��饹�ϡ�'0'��'1'�����Ͳ����줿�����ǡ����򰷤��٤Υ��饹�Ǥ���
 *
 * @version	�������� 2002/12/16(Mon) �и�ë����ϯ
 */
class BinaryImage {

    /** ���󥹥ȥ饯�� */
    public BinaryImage() {
        data.clear();
    }

    /** ���ԡ����󥹥ȥ饯�� */
    public BinaryImage(final BinaryImage temp) {
        data.clear();
        List<List<Boolean>> v = temp.data;
        data.addAll(v);
        row = temp.row;
        col = temp.col;
    }
    
    /** �����黻�� */
    public final BinaryImage operatorLet(final BinaryImage right) {
        data.clear();
        List<List<Boolean>> v = right.data;
        data.addAll(v);
        row = right.row;
        col = right.col;
        return this;
    }

    /** ������ؿ���row * col ���礭�����ΰ����ݤ���Val�ǽ�������롣 */
    public void initialize(final int x, final int y, boolean val /* = false */) {
        col = x;
        row = y;
        List<List<Boolean>> Data = new ArrayList<List<Boolean>>(row);
        for (int r = 0; r < row; r++) {
            List<Boolean> cs = new ArrayList<Boolean>(col);
            Data.add(cs);
        }
    }

    /** ������ؿ����Ϥ��줿�󼡸�����ǽ��������ؿ��� */
    public void initialize(final List<List<Boolean>> v) {
        col = v.get(0).size();
        row = v.size();
        data = v;
    }

    /** ������� 1 �ԥ������ Value ���ͤǽ񤭴����롣 */
    public void putPixel(final int x, final int y, boolean val) {
        data.get(y).add(x, new Boolean(val));
    }

    /** ������� X, Y �ΰ��֤ˤ���ԥ�������ͤ������ */
    public boolean getPixel(final int x, final int y) {
        return data.get(y).get(x).booleanValue();
    }

    /** ��Ǽ����Ƥ��륤�᡼���κ���Ԥ��������ؿ��� */
    public int getMaxRow() {
        return row;
    }

    /** ��Ǽ����Ƥ��륤�᡼���κ���Ԥ��������ؿ��� */
    public int getMaxCol() {
        return col;
    }

    /** ������� X, Y �ΰ��֤ˤ���ԥ�������ͤ�ȿž�� */
    public void flip(final int x, final int y) {
        data.get(y).set(x, new Boolean(!data.get(y).get(x).booleanValue()));
    }

    /** Ϳ����줿�����v���ɤ�Ĥ֤� */
    public void fill(final int l, final int t, final int r, final int b, boolean v) {
        if (row < t || row < b || col < t || col < b) {
            throw new IllegalArgumentException("overflow");
        }
        for (int y = t; y < b; y++) {
            for (int x = l; x < r; x++) {
                putPixel(x, y, v);
            }
        }
    }

    /** �ǡ������˴������ꥢ����ؿ� */
    public void clear() {
        data.clear();
        row = 0;
        col = 0;
    }

    /** ���Ϥ��줿���᡼���ȳ�Ǽ����Ƥ��륤�᡼���� Or ��Ȥ��֤��� */
    public BinaryImage or(BinaryImage temp) {
        if (row != temp.row || col != temp.col) {
            throw new IllegalArgumentException("Size does not matched");
        }

        BinaryImage ret = new BinaryImage();
        ret.initialize(col, row, false);
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                ret.putPixel(x, y, getPixel(x, y) | temp.getPixel(x, y));
            }
        }
        return ret;
    }

    /**
     * ���Ϥ��줿��ʬ���᡼���ȳ�Ǽ����Ƥ��륤�᡼���� X, Y ���֤�Ʊ���礭������ʬ��
     * �� Or ��Ȥ��֤���
     */
    public BinaryImage or(final int sx, final int sy, BinaryImage temp) {
        if (row < sy + temp.row || col < sx + temp.col) {
            throw new IllegalArgumentException("Error : Overflow @ CBinaryImage::or x,y");
        }

        BinaryImage ret = this;
        for (int y = sy; y < sy + temp.row; y++) {
            for (int x = sx; x < sx + temp.col; x++) {
                ret.putPixel(x, y, getPixel(x, y) | temp.getPixel(x - sx, y - sy));
            }
        }
        return ret;
    }

    /** Data������ȿž�����֤��� */
    public BinaryImage not() {
        BinaryImage ret = new BinaryImage();
        ret.initialize(col, row, false);
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                ret.putPixel(x, y, !getPixel(x, y));
            }
        }
        return ret;
    }

    /** */
    private List<List<Boolean>> data;

    /** */
    private int row;

    /** */
    private int col;
}

/* */
