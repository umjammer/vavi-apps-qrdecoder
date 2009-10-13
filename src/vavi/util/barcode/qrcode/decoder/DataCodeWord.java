/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */ 

package vavi.util.barcode.qrcode.decoder;


/**
 * ʸ���⡼�ɻؼ��ҡ�ʸ�����ؼ��ҡ�ʸ��������ä�Ϣ�뤷QRCode��
 * �ǡ�����Ȥ��Ƥ��κۤ������Ƴ�Ǽ���륯�饹��
 *
 * @version �������� 2002/12/12(Thu) �и�ë������ϯ
 *          �ɲ��ѹ� 2002/12/14(Sat) �и�ë������ϯ
 */
class DataCodeWord {
    /** */
    public DataCodeWord() {
        charCount.clear();
        mode.clear();
        string.clear();
        dataCodeWord.clear();
    }

    /** DataCodeWord �ν�����򤹤�ؿ� */
    public void setDataCodeWord(final BinaryString mode, final BinaryString count, final BinaryString str, final Symbol sym) {
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
    
    /** ʸ�����ؼ��Ҥμ����ؿ� */
    public BinaryString getCharCount() {
        return charCount;
    }

    /** ʸ���⡼�ɻؼ��Ҥμ����ؿ� */
    public BinaryString getMode() {
        return mode;
    }

    /** ʸ����μ����ؿ� */
    public BinaryString getString() {
        return string;
    }

    /** DataCodeWord�μ����ؿ� */
    public BinaryString getDataCodeWord() {
        return dataCodeWord;
    }

    /** index ���� count �ĤΥǡ�������֤��ؿ� */
    public BinaryString getSubDataCodeWord(final int index, final int count) {
        return dataCodeWord.GetSubByte(index, count);
    }

    /**
     * �⡼�ɻؼ��ҡ�ʸ�����ؼ��ҡ�ʸ����ν�˷�礵�줿�ǡ��������ɤ������������
     * �ӥåȵڤ�������ɤ�Ϣ�뤹��ؿ���
     */
    private void padDataCodeWord(final Symbol sym) {
        if (charCount == null || mode == null || string == null) {
            throw new IllegalArgumentException("One or more Data was not Initialized");
        }
        int wcodeword = sym.getDataCodeWords();
        dataCodeWord.SetMaxLengthByByte(wcodeword);
        
        // ��ü�ѥ�����(0000)���ղá�����ܥ����̤��������Ƥ����齪λ
        if (dataCodeWord.GetLength() < wcodeword * 8) {
            int end = 8 - dataCodeWord.GetLength() % 8;
            for (int i = 0; i < end && i < 4; i++) {
                dataCodeWord.add(false);
            }
        } else {
            return ;
        }
        
        // �����ӥå�(0)���ղá�����ܥ����̤��������Ƥ����齪λ
        if (dataCodeWord.GetLength() < wcodeword * 8) {
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
        
        // ����𥳡��ɸ�(11101100�ڤ�00010001)���ղá�����ܥ����̤��������Ƥ����齪λ
        if (dataCodeWord.GetLength() / 8 < wcodeword) {
            int padword = wcodeword - dataCodeWord.GetLength() / 8;
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