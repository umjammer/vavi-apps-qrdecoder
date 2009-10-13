/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode.decoder;

import java.util.List;


/**
 * ���Υ��饹�����Ϥ���Ƥ���CDataCodeWord��RS�֥�å���ʬ���Ƴ�Ǽ
 * ����ErrorCodeGenerator��RS�֥�å����Ϥ���EC�֥�å�����������
 * ���󥿡��ե��������Ѱդ��ޤ���
 *
 * @version �������� 2002/11/14(Sat) �и�ë������ϯ
 */
class Message {

    /** ���󥹥ȥ饯�� */
    public Message() {
        numberOfBlocks = 0;
        data.clear();
        rsBlocks.clear();
        ecBlocks.clear();
    }

    /**
     * �����Ѥ��¤��ؤ���줿�ǡ��������ꡢRS�֥�å���EC�֥�å���������ʬ�䤷
     * ��Ǽ����᥽�åɡ�
     */
    public void setData(final BinaryString data, final Symbol sym) {
        if (data == null) {
            throw new IllegalArgumentException("Data is NULL");
        }
        if (sym == null && sym.isPartial()) {
            throw new IllegalArgumentException("Invalid Symbol");
        }
        if (data.GetLength() != sym.getWholeCodeWords() * 8 + sym.getRemainderBits()) {
            throw new IllegalArgumentException("Invalid Data");
        }
        // �Ȥꤢ�����ǡ������ꥢ��
        rsBlocks.clear();
        ecBlocks.clear();
        rsLength.clear();
        ecLength.clear();
        data.clear();

        // �������������
        int b1	= sym.getRsBlock1();
        int b2	= sym.getRsBlock2();
        int dwb1	= sym.getRSBlock1DataCodeWords();
        int dwb2	= sym.getRsBlock2DataCodeWords();
        int block = 0;
        int index = 0;
        int c = 0;
        data.SetMaxLengthByByte(sym.getWholeCodeWords());
        numberOfBlocks = b1 + b2;
        wholeECCW = sym.getECCodeWords();
        wholeDCW = sym.getWholeCodeWords() - wholeECCW;
        BinaryString bs = new BinaryString();

        for (int i = 0; i < b1; i++) {
            bs.SetMaxLengthByByte(dwb1);
            rsBlocks.add(bs);
            rsLength.add(new Integer(sym.getRSBlock1WholeCodes()));
            ecLength.add(new Integer(sym.getRSBlock1WholeCodes() - dwb1));
        }
        for (int i = 0; i < b2; i++) {
            bs.SetMaxLengthByByte(dwb2);
            rsBlocks.add(bs);
            rsLength.add(new Integer(sym.getRsBlock2WholeCodes()));
            ecLength.add(new Integer(sym.getRsBlock2WholeCodes() - dwb2));
        }

        // �ǡ������ͤù���
        for (int i = 0; i < wholeDCW; i++) {
            if (index < rsBlocks.get(block).GetLength() / 8) {
                rsBlocks.get(block).operatorPlusLet(data.GetSubByte(c, 1));
                data.operatorPlusLet(data.GetSubByte(c, 1));
                c++;
            }
            block = (i + 1) % numberOfBlocks;
            if (block == 0) {
                index++;
            }
        }
        block = 0;
        index = 0;
        for (int i = 0;i < wholeECCW; i++) {
            if (index < ecBlocks.get(block).GetLength() / 8) {
                ecBlocks.get(block).operatorPlusLet(data.GetSubByte(c, 1));
                data.operatorPlusLet(data.GetSubByte(c, 1));
                c++;
            }
            block = (i + 1) % numberOfBlocks;
            if (block == 0) {
                index++;
            }
        }
    }
    
    /** �ǡ��������ɥ�ɤ�����ڤӥ�å������ν���� */
    public void setDataCodeWord(final DataCodeWord data, final Symbol sym) {
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
            rsLength.add(new Integer(sym.getRSBlock1WholeCodes()));
            ecLength.add(new Integer(sym.getRSBlock1WholeCodes() - dwb1));
            pos += dwb1;
        }
        for (int i = 0; i < b2; i++) {
            rsBlocks.add(data.getSubDataCodeWord(pos, dwb2));
            rsLength.add(new Integer(sym.getRsBlock2WholeCodes()));
            ecLength.add(new Integer(sym.getRsBlock2WholeCodes() - dwb2));
            pos += dwb2;
        }
    }

    /** ECCodeWord ��֥�å��������˳�Ǽ */
    public void addECBlock(BinaryString ecwords) {
        if (ecwords == null) {
            throw new IllegalArgumentException("ecwords is NULL");
        }
        ecBlocks.add(ecwords);
        data.operatorPlusLet(ecwords);
    }

    /** index�ӥå��ܤΥӥåȤ��֤��� */
    public boolean get(final int index) {
        if (data == null) {
            throw new IllegalArgumentException("Data is NULL");
        }
        return data.at(index);
    }

    /** Data�������Ѥ��¤��ؤ��Ƽ����Ǥ���᥽�å� */
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

    /** Data���Фμ����᥽�å� */
    public BinaryString getPlainData() {
        return data;
    }

    /** �ǡ�����Ĺ�����֤� */
    public int getDataLength() {
        if (data == null) {
            throw new IllegalArgumentException("Data is NULL");
        }
        return data.GetMaxLength();
    }

    /** �֥�å��ο����֤� */
    public int getNumberOfBlocks() {
        if (data == null) {
            throw new IllegalArgumentException("Data is NULL");
        }
        return numberOfBlocks;
    }

    /** index ���ܤ�RS�֥�å���Ĺ�����֤��� */
    public int getRSLength(final int index) {
        if (index >= numberOfBlocks) {
            throw new IllegalArgumentException("index overflow");
        }
        return rsLength.get(index).intValue();
    }

    /** index ���ܤΥ��顼������֥�å���Ĺ�����֤� */
    public int getECLength(final int index) {
        if (index >= numberOfBlocks) {
            throw new IllegalArgumentException("index overflow");
        }
        return ecLength.get(index).intValue();
    }

    /** index ���ܤ� RS �֥�å����֤� */
    public BinaryString getRSBlockAt(final int index) {
        if (index >= numberOfBlocks) {
            throw new IllegalArgumentException("index overflow");
        }
        return rsBlocks.get(index);
    }

    /** ���ƤΥǡ��������ɸ�ο� */
    private int wholeDCW;

    /** ���ƤΥ��顼������ο� */
    private int wholeECCW;

    /** RS �֥�å��ο� (EC�֥�å��ο���RS�֥�å��ο���Ʊ����) */
    private int numberOfBlocks;

    /** RS �֥�å���EC�֥�å���Ϣ�뤷�Ƴ�Ǽ���Ƥ���ǡ��� */
    private BinaryString data;

    /** RS �֥�å��������ɸ�� */
    private List<Integer> rsLength;

    /** EC �֥�å��Υ����ɸ�� */
    private List<Integer> ecLength;

    /** RS �֥�å� */
    private List<BinaryString> rsBlocks;

    /** ���顼������Υ֥�å� */
    private List<BinaryString> ecBlocks;
}

/* */
