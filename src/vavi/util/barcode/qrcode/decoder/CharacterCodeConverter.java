/*
 * Copyright (c) 2002-2003 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode.decoder;

import java.util.List;


/**
 * ���Υ��饹�ϡ�Ϳ����줿ʸ�����ӥå����ľ�����饹����ݴ��쥯�饹
 *
 * @version	�������� 2002/11/25(Sut) ���ġ�����
 *          �ɲ��ѹ� 2003/02/25(Tue) �и�ë������ϯ
 */
interface CharacterCodeConverter {

    BinaryString convert(final String data);
    BinaryString convert(final byte[] data);
    BinaryString convert(final List<Byte> data);
}

/* */
