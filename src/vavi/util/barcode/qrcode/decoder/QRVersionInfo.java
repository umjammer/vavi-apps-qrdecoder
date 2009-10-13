/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved. 
 */

package vavi.util.barcode.qrcode.decoder;


/**
 * ���Υ��饹�ϼ�����ä�����ܥ뤫�鷿�־�����������롣
 *
 * @version	�������� 2003/02/24(Mon) �и�ë������ϯ
 *          �ɲ��ѹ� 2003/02/27(Tue) �и�ë������ϯ
 */
class QRVersionInfo {
    /** */
    final String VERSION_BCHG = "1111100100101";

    /** ���󥹥ȥ饯�� */
    public QRVersionInfo() {
        version = 0;
    }

    /** ���־����������֤��᥽�å� */
    public void initialize(final Symbol symbol) {
        if (symbol == null) {
            throw new IllegalArgumentException("invalid ECL");
        }

        version = symbol.getVersion();

        if (version < 7) {
            versionInfo = new BinaryString("000000000000000000");
            return;
        }

        if (versionInfo != null) {
            versionInfo.clear();
        }

        BinaryString code = new BinaryString(version, 6);
        BinaryString ec;
        BinaryString pad = new BinaryString();
        BCHECCodeGenerator gen = new BCHECCodeGenerator();

        // �������������� (code��­���Ƥ���Τ�12��)
        ec = gen.execute(code.add(new BinaryString("000000000000")), new BinaryString(VERSION_BCHG));

        // ���äƤ��������ɤ� 10 bit �������ʤ�������� 0 �����롣
        if (ec.GetLength() < 12) {
            for (int i = 0; i < 12 - ec.GetLength(); i++) {
                pad.add(false);
            }
            ec = pad.operatorPlus(ec);
        }

        versionInfo = code.operatorPlus(ec);
    }

    /** ���־������� */
    public BinaryString getVersionInfo() {
        return versionInfo;
    }

    // �ǥ���������

    /** ���־�������ꤷ�Ƥ��� String ��ǥ����� */
    public void initialize(final BinaryString String) {
        // TODO ; ���ΰ��֤˸�ͭ�ν������ɲä��Ƥ���������
    }

    /** ���֤���� */
    public int getVersion() {
        return version;
    }
    
    /** */
    private int version;

    /** */
    private BinaryString versionInfo;
}

/* */
