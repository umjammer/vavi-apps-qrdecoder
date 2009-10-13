/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode.decoder;

import java.util.ArrayList;
import java.util.List;


/**
 * ���Υ��饹��CFileReader���饹���������ä��ǡ����򥯥饤�����
 * �����Ϥ��줿��Version�ȥ��顼��٥�ˤ�ä�CGaloisPlynomial�Ȥ���
 * �֤���
 *
 * @version	�������� 2002/12/14(Sat) �и�ë������ϯ
 */
class ExpressionTable extends Table {
    /** */
    public ExpressionTable() {
        file = null;
    }
    
    /** ������ؿ� */
    public boolean initialize(final String FileName, FileReader temp) {
        try {
            if (temp == null) {
                return false;
            }
            file = temp;
            file.loadFromFile(FileName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** �ǡ�������ECCodeWord���Ф�������¿�༰�ι�η����μ���������ˤ����֤��ؿ��� */
    List<Integer> getGenerateExp(final int ECCodeWords) {
        int index = 0;
        boolean isFound = false;
        for (int i = 0; i < file.getMaxRow(); i++) {
            if (file.getData(i, 0) == ECCodeWords) {
                index = i;
                isFound = true;
                break;
            }
        }

        if (!isFound) {
            throw new IllegalArgumentException("ECCount is not Exist");
        }

        List<Integer> gp = new ArrayList<Integer>();
//	List<Integer> gx;
        for (int i = 1; i <= ECCodeWords + 1; i++) {
            gp.add(new Integer(file.getData(index, i)));
        }
        return gp;
    }
}

/* */
