/*
 *  Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */ 

package vavi.util.barcode.qrcode.decoder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * ���Υ��饹�ϡ�'0'��'1'��ɽ�����Х��ʥ�ݥǡ�������ؤ˰����٤�
 * ���줿���饹�Ǥ���10�ʢ�2�ʡ�2�ʢ�10�ʤ�����Ѵ����ڤ�Ϣ������
 * ���ݡ��Ȥ��Ƥ��ޤ���
 * ��ջ��� ���Υ��饹��Borland C++Builder�ǻ��Ѥ����������˥����ǥ��󥰤�
 * ��Ƥ��ޤ���Microsoft Visual C++�ǻ��Ѥ�����ˤϡ��ʲ�����դ�
 * ���äƽ񤭴����Ƥ���������
 * ��pragma ��ä���
 * ��vcl.h ���󥯥롼��ʸ��ä���
 * ��GetDataByAnsiString�᥽�åɤ�ä���
 * ��Exception�ط�
 * �ʾ��VC�ǥ���ѥ���Ǥ���褦�ˤʤ�ޤ���
 * 
 * @version	�������� 2002/11/08(Fri) �и�ë����ϯ
 *          ������λ 2002/11/09(Sat) �и�ë����ϯ
 *          �ɲ��ѹ� 2002/11/10(Sun) �и�ë����ϯ
 *          �ɲ��ѹ� 2002/11/13(Wed) �и�ë����ϯ
 *          �ɲ��ѹ� 2002/12/12(Thu) �и�ë����ϯ
 *          �ɲ��ѹ� 2003/02/24(Mon) �и�ë����ϯ
 *          �ɲ��ѹ� 2003/02/25(Tue) �и�ë����ϯ
 */
class BinaryString {
 
    /** �ǥե���ȥ��󥹥ȥ饯�� */
    public BinaryString() {
        this.length = 0;
        maxLength = 0;
    }

    /** ���ԡ����󥹥ȥ饯�� */
    public BinaryString(BinaryString temp) {
        if (!data.isEmpty()) {
            data.clear();
        }
        if (temp.GetDataByBoolArray() == null) {
            return;
        }
        this.length = temp.GetLength();
        maxLength = temp.GetMaxLength();
        List<Boolean> v = temp.GetDataByBoolArray();
        data.addAll(v);
    }

    /** List ������ˤȤäƽ�������륳�󥹥ȥ饯�� */
    public BinaryString(final List temp) {
        // @@@ List<��> �ǿ���ʬ��
    }

    /** Boolean �����������ˤȤäƽ�������륳�󥹥ȥ饯�� */
    private void initBooleanList(final List<Boolean> temp) {
        if (!data.isEmpty()) {
            data.clear();
        }
        if (temp.size() == 0) {
            return;
        }
        this.length = temp.size();
        maxLength = temp.size();
        data.addAll(temp);
    }

    /**
     * char* �ǽ�������륳�󥹥ȥ饯����
     * "0101110"�Τ褦��'0'��'1'�Τߤǹ������줿ʸ�����BinaryString���Ѵ����롣
     * '0'��'1'�ʳ���ʸ�����Ѵ���˸��줿�顢���Ǥ����㳰���ꤲ�롣
     */
    public BinaryString(byte[] tch) {
        if (!data.isEmpty()) {
            data.clear();
        }
        String temp = new String(tch);
        if (temp.length() == 0) {
            return;
        }
        this.length = temp.length();
        maxLength = temp.length();

        for (int i = 0; i < temp.length(); i++) {
            char p = temp.charAt(i);
            if (p == '0') {
                data.add(Boolean.FALSE);
            } else if (p == '1') {
                data.add(Boolean.TRUE);
            } else {
                release();
                throw new IllegalArgumentException("Can't convert from char*");
            }
            p++;
        }
    }

    /**
     * string �ǽ�������륳�󥹥ȥ饯����
     * "0101110"�Τ褦��'0'��'1'�Τߤǹ������줿ʸ�����BinaryString���Ѵ����롣
     * '0'��'1'�ʳ���ʸ�����Ѵ���˸��줿�顢���Ǥ����㳰���ꤲ�롣
     */
    public BinaryString(String temp) {
        if (!data.isEmpty()) {
            data.clear();
        }
        if (temp == null) {
            return;
        }
        this.length = temp.length();
        maxLength = temp.length();

        for (int p = 0; p < length; p++) {
            if (temp.charAt(p) == '0') {
                data.add(Boolean.FALSE);
            } else if (p == '1') {
                data.add(Boolean.TRUE);
            } else {
                release();
                throw new IllegalArgumentException("Can't convert from string");
            }
        }
    }

    /** 8bit������եǡ������Ϥ��줿���Υ��󥹥ȥ饯�� */
    public BinaryString(byte temp) {
        if (!data.isEmpty()) {
            data.clear();
        }
        this.length = 1 * 8;
        maxLength = 1 * 8;
        int offset = (int) Math.pow(2, length - 1);
        for (int i = 0; i < length; i++) {
            //���ֺ��ΥӥåȤ���Ф���
            if (((temp << i) & offset) != 0) {
                data.add(Boolean.TRUE);
            } else {
                data.add(Boolean.FALSE);
            }
        }
    }

    /** 16bit��������������Ϥ��줿���Υ��󥹥ȥ饯�� */
    public BinaryString(short temp) {
        if (!data.isEmpty()) {
            data.clear();
        }
        this.length = 2 * 8;
        maxLength = 2 * 8;
        int offset = (int) Math.pow(2, length - 1);
        for (int i = 0; i < length; i++) {
            //���ֺ��ΥӥåȤ���Ф���
            if (((temp << i) & offset) != 0) {
                data.add(Boolean.TRUE);
            } else {
                data.add(Boolean.FALSE);
            }
        }
    }

    /** 32bit��������������Ϥ��줿���Υ��󥹥ȥ饯�� */
    public BinaryString(int temp) {
        if (!data.isEmpty()) {
            data.clear();
        }
        this.length = 4 * 8;
        maxLength = 4 * 8;
        int offset = (int) Math.pow(2, length - 1);
        for (int i = 0; i < length; i++) {
            //���ֺ��ΥӥåȤ���Ф���
            if (((temp << i) & offset) != 0) {
                data.add(Boolean.TRUE);
            } else {
                data.add(Boolean.FALSE);
            }
        }
    }

    /** 32bit��������������Ϥ��줿���Υ��󥹥ȥ饯�� */
    public BinaryString(long temp) {
        if (!data.isEmpty()) {
            data.clear();
        }
        this.length = 4 * 8;
        maxLength = 4 * 8;
        int offset = (int) Math.pow(2, length - 1);
        for (int i = 0; i < length; i++) {
            //���ֺ��ΥӥåȤ���Ф���
            if (((temp << i) & offset) != 0) {
                data.add(Boolean.TRUE);
            } else {
                data.add(Boolean.FALSE);
            }
        }
    }
    
    /** 8bit�����̵�������Ϥ��줿���Υ��󥹥ȥ饯�� */
//    public BinaryString(unsigned char data) {
//        if (!Data.empty()) {
//            Data.clear();
//        }
//        Length = sizeof(unsigned char) * 8;
//        MaxLength = sizeof(unsigned char) * 8;
//        int offset = pow(2, Length - 1);
//        for (int i = 0; i < Length; i++) {
//            // ���ֺ��ΥӥåȤ���Ф���
//            if ((temp << i) & offset) {
//                Data.add(true);
//            } else {
//                Data.add(false);
//            }
//        }
//    }
    
    /** 16bit�����̵�������Ϥ��줿���Υ��󥹥ȥ饯�� */
//    public BinaryString(unsigned short data) {
//        if (!Data.empty()) {
//            Data.clear();
//        }
//        Length = sizeof(unsigned short) * 8;
//        MaxLength = sizeof(unsigned short) * 8;
//        unsigned int offset = pow(2, Length - 1);
//        for (unsigned int i = 0; i < Length; i++) {
//            // ���ֺ��ΥӥåȤ���Ф���
//            if ((temp << i) & offset) {
//                Data.push_back(true);
//            } else {
//                Data.push_back(false);
//            }
//        }
//    }

    /** 32bit�����̵�������Ϥ��줿���Υ��󥹥ȥ饯�� */
//    public BinaryString(unsigned int data) {
//        if (!Data.empty()) {
//            Data.clear();
//        }
//        Length = sizeof(unsigned int) * 8;
//        MaxLength = sizeof(unsigned int) * 8;
//        unsigned int offset = pow(2, Length - 1);
//        for (unsigned int i = 0; i < Length; i++) {
//            //  ���ֺ��ΥӥåȤ���Ф���
//            if ((temp << i) & offset) {
//                Data.push_back(true);
//            } else {
//                Data.push_back(false);
//            }
//        }
//    }

    /** 32bit�����̵�������Ϥ��줿���Υ��󥹥ȥ饯�� */
//    public BinaryString(unsigned long data) {
//        if (!Data.empty()) {
//            Data.clear();
//        }
//        Length = sizeof(unsigned long) * 8;
//        MaxLength = sizeof(unsigned long) * 8;
//        int offset = pow(2, Length - 1);
//        for (int i = 0; i < Length; i++) {
//            //���ֺ��ΥӥåȤ���Ф���
//            if ((temp << i) & offset) {
//                Data.add(true);
//            } else {
//                Data.add(false);
//            }
//        }
//    }

    /** 8bits�����̵��������unsigned char�Υ٥������������륳�󥹥ȥ饯�� */
    private void initByteList(final List<Byte> temp) {
        if (!data.isEmpty()) {
            data.clear();
        }

        int size = 1 * 8;
        int offset = (int) Math.pow(2, size - 1);
        this.length = temp.size() * size;
        maxLength = temp.size() * size;

        for (Byte p : temp) {
            for (int i = 0; i < size; i++) {
                // ���ֺ��ΥӥåȤ���Ф���
                if (((p.byteValue() << i) & offset) != 0) {
                    data.add(Boolean.TRUE);
                } else {
                    data.add(Boolean.FALSE);
                }
            }
        }
    }

    /** 16bits�����̵��������unsigned short�Υ٥������������륳�󥹥ȥ饯�� */
    private void initShortList(final List<Short> temp) {
        if (!data.isEmpty()) {
            data.clear();
        }
        
        int size = 2 * 8;
        int offset = (int) Math.pow(2, size - 1);
        this.length = temp.size() * size;
        maxLength = temp.size() * size;
        
        for (Short p : temp) {
            for (int i = 0; i < size; i++) {
                // ���ֺ��ΥӥåȤ���Ф���
                if (((p.shortValue() << i) & offset) != 0) {
                    data.add(Boolean.TRUE);
                } else {
                    data.add(Boolean.FALSE);
                }
            }
        }
    }

    /** 32bits�����̵��������unsigned int�Υ٥������������륳�󥹥ȥ饯�� */
    private void initIntegerList(final List<Integer> temp) {
        if (!data.isEmpty()) {
            data.clear();
        }

        int size = 4 * 8;
        int offset = (int) Math.pow(2, size - 1);
        this.length = temp.size() * size;
        maxLength = temp.size() * size;

        for (Integer p : temp) {
            for (int i = 0; i < size; i++) {
                // ���ֺ��ΥӥåȤ���Ф���
                if (((p.intValue() << i) & offset) != 0) {
                    data.add(Boolean.TRUE);
                } else {
                    data.add(Boolean.FALSE);
                }
            }
        }
    }

    /** 32bits�����̵��������unsigned long�Υ٥������������륳�󥹥ȥ饯�� */
    private void initLongList(final List<Long> temp) {
        if (!data.isEmpty()) {
            data.clear();
        }

        int size = 8 * 8;
        int offset = (int) Math.pow(2, size - 1);
        length = temp.size() * size;
        maxLength = temp.size() * size;

        for (Long p : temp) {
            for (int i = 0; i < size; i++) {
                // ���ֺ��ΥӥåȤ���Ф���
                if (((p.longValue() << i) & offset) != 0) {
                    data.add(Boolean.TRUE);
                } else {
                    data.add(Boolean.FALSE);
                }
            }
        }
    }

    /** Ĺ������ꤷ�� Data ���Ǽ���� */
    public BinaryString(int temp, int length) {
        if (!data.isEmpty()) {
            data.clear();
        }
        if (length == 0) {
            length = 0;
            maxLength = 0;
            return ;
        }
        this.length = length;
        maxLength = length;
        int offset = (int) Math.pow(2, length - 1);
        for (int i = 0; i < length; i++) {
            // ���ֺ��ΥӥåȤ���Ф���
            if (((temp << i) & offset) != 0) {
                data.add(Boolean.TRUE);
            } else {
                data.add(Boolean.FALSE);
            }
        }
    }

    /** ����黻�Ҥ�¿����� */
    public boolean operatorEqual(final BinaryString right) {
        if (right == null) {
            throw new IllegalArgumentException("right is NULL");
        }
        if (right.GetLength() != length) {
            return false;
        }
        Iterator<Boolean> p = data.iterator();
        Iterator<Boolean> rp = right.data.iterator();
        while (p.hasNext() && rp.hasNext()) {
            if (p.next() != rp.next()) {
                return false;
            }
        }
        return true;
    }

    /** ������黻�Ҥ�¿����� */
    public boolean operatorNotEqual(final BinaryString right) {
        if (right == null) {
            throw new IllegalArgumentException("right is NULL");
        }
        if (right.GetLength() != length) {
            return true;
        }
        Iterator<Boolean> p = data.iterator();
        Iterator<Boolean> rp = right.data.iterator();
        while (p.hasNext() && rp.hasNext()) {
            if (p.next() != rp.next()) {
                return true;
            }
        }
        return false;
    }

    /** �����黻�Ҥ�¿�������C++ �ʳ��Ǽ���������� Copy ���Υ᥽�åɤ��롣 */
    public final BinaryString operatorPlus(final BinaryString right) {
        if (!data.isEmpty()) {
            data.clear();
        }
        if (right.GetDataByBoolArray().isEmpty()) {
            throw new IllegalArgumentException("Data is NULL");
        }

        List<Boolean> v = right.GetDataByBoolArray();
        data.addAll(v);
        length = v.size();
        maxLength = v.size();
        return this;
    }

    /**
     * �û��黻�Ҥ�¿������ʥХ��ʥ�ʸ�����Ϣ��黻�ҡ�
     * ��ǰŪ�ˤ�Perl��"0001" . "0101"��"00010101"��Ϣ�뤵��봶����
     */
    public final BinaryString operatorXor(final BinaryString right) {
        if (right.GetDataByBoolArray().isEmpty()) {
            throw new IllegalArgumentException("Data is NULL");
        }

        List<Boolean> temp = data;
        List<Boolean> v = right.GetDataByBoolArray();
        temp.addAll(v);
        return new BinaryString(temp);
    }

    /** �û������黻�Ҥ�¿���������̣�ϲû��黻�ҤȤۤ�Ʊ���� */
    public final BinaryString operatorLet(final BinaryString right) {
        if (right.GetDataByBoolArray() == null) {
            throw new IllegalArgumentException("Data is NULL");
        }

        List<Boolean> v = right.GetDataByBoolArray();
        data.addAll(v);
        this.length = data.size();
        return this;
    }

    /** OR �黻�Ҥ�¿������� */
    public final BinaryString operatorPlusLet(final BinaryString right) {
        if (right.GetDataByBoolArray() == null) {
            throw new IllegalArgumentException("Data is NULL");
        }
        if (right.GetLength() != length) {
            throw new IllegalArgumentException("Length is not match");
        }
        List<Boolean> v = new ArrayList<Boolean>();
        Iterator<Boolean> p = data.iterator();
        Iterator<Boolean> rp = right.data.iterator();
        while (p.hasNext() && rp.hasNext()) {
            v.add(new Boolean(p.next().booleanValue() ^ rp.next().booleanValue()));
        }
        return new BinaryString(v);
    }

    /** XOR �����黻�Ҥ�¿����� */
    public final BinaryString operatorXorLet(final BinaryString right) {
        if (right.GetDataByBoolArray() == null) {
            throw new IllegalArgumentException("Data is NULL");
        }
        if (right.GetLength() != length) {
            throw new IllegalArgumentException("Length is not match");
        }
        Iterator<Boolean> p = data.iterator();
        Iterator<Boolean> rp = right.data.iterator();
        while (p.hasNext() && rp.hasNext()) {
            Boolean b = p.next();
            b = new Boolean(b.booleanValue() ^ rp.next().booleanValue()); // @@@
        }
        return this;
    }

    /**
     * () �黻�Ҥ�¿�������
     * index (�ӥå�) �Ǥ���蘆��륪�ե��åȤ��顢count �ӥå�ȴ���Ф����֤���
     * �ΰ��Ϥ߽Ф�����㳰���ꤲ�롣
     * GetSubBit��Ʊ��
     */
    public final BinaryString operatorFunction(final int index, final int count) {
        if (data.isEmpty() || index > length || index + count > length) {
            throw new IllegalArgumentException("index overflow");
        }
        List<Boolean> temp = new ArrayList<Boolean>(count);
        temp.addAll(data.subList(index, index + count));
        return new BinaryString(temp);
    }

    /**
     * [] �黻�Ҥ�¿�����
     * index��Ϳ�����륤��ǥå�����bit��ҤȤĤ����֤���
     * �ΰ��Ϥ߽Ф�����㳰���ꤲ�롣
     * At��Ʊ��
     */
    public final boolean operatorArray(final int index) {
        if (data.isEmpty() || index > length) {
            throw new IllegalArgumentException("index overflow");
        }
        return data.get(index).booleanValue();
    }

    /**
     * �ǡ�����stringʸ������֤��ؿ�
     * "01011111"�Ȥ����褦��'0'��'1'�ǹ��������ʸ������֤���
     */
    public String GetDataByString() {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Data NULL");
        }
        String str = "";
        for (Boolean p : data) {
            if (p.booleanValue()) {
                str += "1";
            } else {
                str += "0";
            }
        }
        return str;
    }

    /** bool ������ǡ��ǡ������֤��ؿ� () �黻�� */
    public List<Boolean> GetDataByBoolArray() {
        return data;
    }

    /**
     * unsigned int������ǥǡ������֤��ؿ���
     * 1�Х��ȤŤ�������ľ������Τ�����˳�Ǽ�����֤��ޤ���
     */
    public List<Integer> GetDataByUINTArray() {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Data NULL");
        }
        List<Integer> v = new ArrayList<Integer>();
        int temp;
        for (Boolean p : data) {
            temp = 0;
            for (int i = 7; i >= 0; i--) {
                if (p.booleanValue()) {
                    temp += Math.pow(2, i);
                }
            }
            v.add(new Integer(temp));
        }
        return v;
    }

    /**
     * UCHAR������ǥǡ������֤��ؿ���
     * 1�Х��ȤŤ�������ľ������Τ�����˳�Ǽ�����֤��ޤ���
     */
    public List<Byte> GetDataByUCHARArray() {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Data NULL");
        }
        List<Byte> v = new ArrayList<Byte>();
        byte temp = 0;
        for (Boolean p : data) {
            for (int i = 7; i >= 0; i--) {
                if (p.booleanValue()) {
                    temp += Math.pow(2, i);
                }
            }
            v.add(new Byte(temp));
        }
        return v;
    }

    /** Length ���֤���Length �ϥӥåȡ� */
    public int GetLength() {
        return length;
    }

    /** MaxLength ��ӥå�ñ�̤��֤��� */
    public int GetMaxLength() {
        return maxLength;
    }

    /** MaxLength��Х���ñ�̤��֤��� */
    public int GetMaxLengthByByte() {
        return maxLength / 8;
    }

    /** MaxLength�򥻥åȤ��롣�ӥå�ñ�̡� */
    public void SetMaxLength(int len) {
        maxLength = len;
    }

    /** maxLength ��Х���ñ�̤ǥ��åȤ��롣 */
    public void SetMaxLengthByByte(int len) {
        maxLength = len * 8;
    }

    /** index �ǻ��ꤵ�줿���ե��åȤ��顢count �Ĥ� bit ����Ф����֤��� */
    public BinaryString GetSubBit(final int index, final int count) {
        if (data.isEmpty() || index > length || index + count > length) {
            throw new IllegalArgumentException("index overflow");
        }
        List<Boolean> temp = new ArrayList<Boolean>(count);
        temp.addAll(data.subList(index, index + count));
        return new BinaryString(temp);
    }

    /** index �ǻ��ꤵ�줿���֤ΥХ��Ȥ� unsigned int �ˤʤ������֤��ؿ��� */
    public int getSubByte(final int index) {
        if (data.isEmpty() || (index * 8) >= length) {
            throw new IllegalArgumentException("index overflow");
        }
        int temp = 0;
        int c = 0;
        for (int i = 7; index * 8 + c < data.size() && i >= 0; i--) {
            Boolean p = data.get(index * 8 + c);
            if (p.booleanValue()) {
                temp += Math.pow(2, i);
            }
            c++;
        }
        return temp;
    }

    /** index�ǻ��ꤵ�줿���֤ΥХ��Ȥ�unsigned int�ˤʤ������֤��ؿ��� */
    public char GetSubByteByUCHAR(final int index) {
        if (data.isEmpty() || (index * 8) >= length) {
            throw new IllegalArgumentException("index overflow");
        }
        char temp = 0;
        int c = 0;
        for (int i = 7; index * 8 + c < data.size() && i >= 0; i--) {
            Boolean p = data.get(index * 8 + c);
            if (p.booleanValue()) {
                temp += Math.pow(2, i);
            }
            c++;
        }
        return temp;
    }

    /**
     * index �ǻ��ꤵ�줿���֤��� Byte ʬ�� unsigned int ������ˤ����֤��ؿ���
     * 8 Bit �� 1 �Ĥ� unsigned int
     */
    public List<Integer> GetSubByteByUINTArray(final int index, final int byte_) {
        if (data.isEmpty() || index >= length || index + byte_ * 8 > length) {
            throw new IllegalArgumentException("index overflow");
        }
        List<Integer> v = new ArrayList<Integer>();
        int temp = 0;
        int count = 0;
        int c = 0;
        while (index * 8 + c < data.size() && count < (index + byte_) * 8) {
            for (int i = 7; i >= 0; i--) {
                Boolean p = data.get(index * 8 + c);
                if (p.booleanValue()) {
                    temp += Math.pow(2, i);
                }
                c++;
            }
            v.add(new Integer(temp));
            count++;
        }
        return v;
    }

    /**
     * index �ǻ��ꤵ�줿���֤��� Byte ʬ�� UCHAR ������ˤ����֤��ؿ���
     * 8 Bit �� 1 �ĤΥ֥�å�
     */
    public List<Byte> GetSubByteByUCHARArray(final int index, final int byte_) {
        if (data.isEmpty() || index >= length || index + byte_ * 8 > length) {
            throw new IllegalArgumentException("index overflow");
        }
        List<Byte> v = new ArrayList<Byte>();
        int temp = 0;
        int count = 0;
        int c = 0;
        while (index * 8 + c < data.size() && count < (index + byte_) * 8) {
            for (int i = 7; i >= 0; i--) {
                Boolean p = data.get(index * 8 + c);
                if (p.booleanValue()) {
                    temp += Math.pow(2, i);
                }
                c++;
            }
            v.add(new Byte((byte) (temp & 0xff)));
            count++;
        }
        return v;
    }
    
    /** index�ǻ��ꤵ�줿���֤���Byteʬ��Х��ʥꥹ�ȥ�󥰤ˤ����֤��ؿ��� */
    public BinaryString GetSubByte(final int index, final int byte_) {
        if (data.isEmpty() || index > length || index + byte_ * 8 > length) {
            throw new IllegalArgumentException("Error : index overflow");
        }
        List<Boolean> v = new ArrayList<Boolean>();
        v.addAll(data.subList(index * 8, index * 8 + byte_ * 8));
        return new BinaryString(v);
    }

    /**
     * �桼�ƥ���ƥ���
     * �ǡ����򥯥ꥢ����ؿ���
     * �����ޤǤ�ǡ����򥯥ꥢ��������ǡ�maxLength �ϥ��ꥢ���ʤ���
     */
    public void clear() {
        if (!data.isEmpty()) {
            data.clear();
        }
        length = 0;
    }

    /** []�ؿ���Ʊ�� index ���ΰ褫��Ϥ߽ФƤ�������㳰���ꤲ�롣 */
    public boolean at(final int index) {
        if (data.isEmpty() || index > length) {
            throw new IllegalArgumentException("index overflow");
        }
        return data.get(index).booleanValue();
    }

    /** �ܱ黻�Ҥ�Ʊ���� */
    public final BinaryString add(BinaryString temp) {
        if (temp.GetDataByBoolArray() == null) {
            throw new IllegalArgumentException("index overflow");
        }
        if (length + temp.GetLength() > maxLength) {
            throw new IllegalArgumentException("Data Overflow");
        }

        data.addAll(temp.GetDataByBoolArray());
        length = data.size();
        return this;
    }

    /** index���ܤΥӥåȤ�ȿž�����롣 */
    void flip(int index) {
        if (data.isEmpty() || index >= length) {
            throw new IllegalArgumentException("Error : index overflow @CBinaryString::Flip");
        }
        data.set(index, new Boolean(!data.get(index).booleanValue()));
    }

    /** 1bit������ΰ��ָ�������롣 */
    void add(boolean bit) {
        this.length += 1;
        data.add(new Boolean(bit));
    }

    /**
     * �ǡ����β����򤹤�ؿ���
     * ���٤ƤΥǡ������Ф��ͤ�ꥻ�åȤ��롣
     */
    private void release() {
        if (!data.isEmpty()) {
            data.clear();
        }
        this.length = 0;
        maxLength = 0;
    }

    /** �ǡ��� */
    private List<Boolean> data;

    /** �����ߤ�Ĺ�� (�ǡ��������äƤ�Ȥ���ޤǤ�Ĺ��) */
    private int length;

    /** �����Ĺ�� */
    private int maxLength;
}