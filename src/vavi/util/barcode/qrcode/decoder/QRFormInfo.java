/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode.decoder;


/**
 * ���Υ��饹�ϼ�����ä�����ܥ�ȥޥ����ѥ����󤫤���������
 * �������롣
 *
 * @version	�������� 2003/02/24(Mon) �и�ë ����ϯ
 *          �ɲ��ѹ� 2003/02/27(Tue) �и�ë ����ϯ
 */
class QRFormInfo {
    final static String FORM_BCHG = "10100110111";
    final static String FORM_MASK = "101010000010010";

    /** ���󥹥ȥ饯�� */
    public QRFormInfo() {
        errorCollectionLevel = VersionTable.ErrorCollectionLevel.L;
        maskCode = MaskDecorator.Mask.PATTERN0;
    }

    // ���󥳡�����

    /** ���������������֤��ؿ��� */
    public void initialize(final Symbol sym, MaskDecorator.Mask pattern) {
        if (sym == null) {
            throw new IllegalArgumentException("invalid ErrorCollectionLevel");
        }
        formInfo.clear();

        errorCollectionLevel = sym.getErrorCollectionLevel();
        maskCode = pattern;

        BinaryString code = new BinaryString();
        BinaryString ec = new BinaryString();
        BinaryString pad = new BinaryString();
        BinaryString mask = new BinaryString(FORM_MASK);
        BCHECCodeGenerator gen = new BCHECCodeGenerator();

        switch (errorCollectionLevel) {
        case L:
            code = new BinaryString("01");
            break;
        case M:
            code = new BinaryString("00");
            break;
        case Q:
            code = new BinaryString("11");
            break;
        case H:
            code = new BinaryString("10");
            break;
        }
        
        switch (maskCode) {
        case PATTERN0:
            code.operatorPlusLet(new BinaryString("000"));
            break;
        case PATTERN1:
            code.operatorPlusLet(new BinaryString("001"));
            break;
        case PATTERN2:
            code.operatorPlusLet(new BinaryString("010"));
            break;
        case PATTERN3:
            code.operatorPlusLet(new BinaryString("011"));
            break;
        case PATTERN4:
            code.operatorPlusLet(new BinaryString("100"));
            break;
        case PATTERN5:
            code.operatorPlusLet(new BinaryString("101"));
            break;
        case PATTERN6:
            code.operatorPlusLet(new BinaryString("110"));
            break;
        case PATTERN7:
            code.operatorPlusLet(new BinaryString("111"));
            break;
        }

        if (code.equals("00000")) {
            formInfo = mask;
            return ;
        }

        // ����������������code��­���Ƥ���Τ�10���
        ec = gen.execute(new BinaryString(code + "0000000000"), new BinaryString(FORM_BCHG));

        // ���äƤ��������ɤ�10bit�������ʤ��������0�����롣
        if (ec.GetLength() < 10) {
            for (int i = 0; i < 10 - ec.GetLength(); i++) {
                pad.add(false);
            }
            ec = pad.operatorPlus(ec);
        }

        formInfo = mask.operatorXor((code.operatorPlus(ec)));
    }

    /** ��������μ��� */
    public BinaryString getFormInfo() {
        return formInfo;
    }

    // �ǥ�������

    /** ��������γ�Ǽ���줿BinaryString���ɤ߹���ǥǥ����ɡ� */
    public void initialize(final BinaryString str) {
        // TODO ; ���ΰ��֤˸�ͭ�ν������ɲä��Ƥ���������
    }

    /** ���顼������٥�μ��� */
    public VersionTable.ErrorCollectionLevel getECL() {
        return errorCollectionLevel;
    }

    /** �ޥ��������ɤμ��� */
    public MaskDecorator.Mask getMaskCode() {
        return maskCode;
    }

    /** */
    private VersionTable.ErrorCollectionLevel errorCollectionLevel;
    /** */
    private MaskDecorator.Mask maskCode;
    /** */
    private BinaryString formInfo;
}

/* */

