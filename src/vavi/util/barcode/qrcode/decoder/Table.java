/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode.decoder;

import java.io.IOException;


/**
 * ���Υ��饹�ϥե����뤫���ɤ߹�����ǡ����˥�����������٤Υ���
 * �ե�������Ƴ�Х��饹���󶡤���٤���ݴ��쥯�饹�Ǥ���
 *
 * @version	�������� 2002/11/13(Wed) �и�ë������ϯ
 */
abstract class Table {
    public Table() {}

    abstract boolean initialize(final String fileName, FileReader file) throws IOException;

    protected FileReader file;
}

/* */
