/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */ 

package vavi.util.barcode.qrcode.decoder;

import java.io.IOException;
import java.util.List;


/**
 * ���Υ��饹�Ϲԡ���ǹ������줿�ե�������ɤ߹���ǡ��ǡ����μ���
 * ���ݡ��Ȥ��뤿��Τ�ΤǤ���
 * ��˥ơ��֥륯�饹�إǡ��������������󶡤��륤�󥿡��ե������Ȥ�
 * �롢��ݴ��쥯�饹�Ǥ���
 *
 * @version	�������� 2002/11/11 �и�ë����ϯ
 */
interface FileReader {
    /** �ɤ߹��ߥ��󥿡��ե����� 
     * @throws IOException*/
    void loadFromFile(String fileName) throws IOException;
    /** �ǡ����������󥿡��ե����� */
    List<Integer> getData(int row);
    /** �ǡ����������󥿡��ե����� */
    int getData(int row, int col);
    /** ����Ԥμ��� */
    int getMaxRow();
    /** ������μ��� */
    int getMaxCol();
}

/* */
