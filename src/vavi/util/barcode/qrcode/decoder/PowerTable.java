/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode.decoder;

import java.io.IOException;


/**
 * ���Υ��饹�� FileReader ���饹���������ä��ǡ����򥯥饤�����
 * �����Ϥ��줿���٤���ɽ��������ɽ��������Ѵ����󶡤��륯�饹�Ǥ���
 *
 * @version	�������� 2002/11/13(Wed) �и�ë ����ϯ
 *          ������λ 2002/11/14(Thu) �и�ë ����ϯ
 */
class PowerTable extends Table {

    /** ���󥹥ȥ饯�� */
    public PowerTable() {
        file = null;
    }

    /**
     * String�ǥե�����̾�������륤�˥���饤����
     * FileReader���饹�������ä�File�����������ե�������ɤ߹��ࡣ
     * @throws IOException
     */
    public boolean initialize(final String fn, FileReader temp) throws IOException {
        if (temp == null) {
            return false;
        }
        file = temp;
        file.loadFromFile(fn);
        return true;
    }

    /** �ơ��֥뤫�鼡���򥭡�����������Ф��ؿ��� */
    public int convertPowerToInt(int p) {
        if (file == null) {
            throw new IllegalStateException("File is NULL");
        }
        p = p % 255;
        return file.getData(p, 0);
    }

    /** �ơ��֥뤫�������򥭡��˼�������Ф��ؿ��� */
    public int convertIntToPower(int i) {
        if (file == null) {
            throw new IllegalStateException("File is NULL");
        }
        if (i > 255) {
            return 0;
        }
        return file.getData(i, 1);
    }
}

/* */
