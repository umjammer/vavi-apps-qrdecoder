/*
 * Copyright (c) 2002-2003 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode.decoder;

import java.util.ArrayList;
import java.util.List;


/**
 * ���Υ��饹�ϡ�Ϳ����줿ʸ�����8�Х��ȤǶ��ڤ�줿�ӥå����ľ�����饹
 *
 * @version	�������� 2002/11/25(Sut) ���� ����
 *          �ɲ��ѹ� 2003/02/25(Tue) �и�ë ����ϯ
 */
class AsciiCodeConverter implements CharacterCodeConverter {
    /** ���󥹥ȥ饯�� */
    public AsciiCodeConverter() {
    }

    /** BinaryString ���Ѵ����� */
    public BinaryString convert(final String data) {
        List<Byte> v = new ArrayList<Byte>();
        for (int i = 0; i < data.length(); i++) {
            v.add(new Byte((byte) data.charAt(i)));
        }
        return new BinaryString(v);
    }

    /** BinaryString ���Ѵ����� */
    public BinaryString convert(final byte[] data) {
        List<Byte> v = new ArrayList<Byte>();
        for (int i = 0; i < data.length; i++ ) {
            v.add(new Byte(data[i]));
        }
        return new BinaryString(v);
    }

    /** BinaryString ���Ѵ����� */
    public BinaryString convert(final List<Byte> data) {
        return new BinaryString(data);
    }
}

/* */
