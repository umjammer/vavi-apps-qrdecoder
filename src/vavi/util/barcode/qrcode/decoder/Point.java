/*
 * Copyright (c) 2000-2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode.decoder;


/**
 * ���Υ��饹��X��Y���󼡸��κ�ɸ���Ǽ���륯�饹�Ǥ���
 * X, Y �ϸ�������Ƥ��ޤ������ѹ����ʤ��ǲ�������������
 *
 * @version	�������� 2000/06/26 �и�ë����ϯ
 *          �ɲ��ѹ� 2002/12/08 �и�ë����ϯ
 *			 CObject����ηѾ����ä���
 *			 Windows��RECT��¤�ΤȤθߴ������ӽ�
 */
class Point {
    /** */
    public Point(final int tempX /* = 0 */, final int tempY /* = 0 */) {
        setPoint(tempX, tempY);
    }

    /** */
    public Point(final Point temp) {
        setPoint(temp);
    }
    
    /** + �黻�Ҥ�¿����� */
    public Point operatorPlus(final Point right) {
        return new Point(x + right.x, y + right.y);
    }

    /** -�黻�Ҥ�¿����� */
    public Point operatorMinus(final Point right) {
        return new Point(x - right.x, y - right.y);
    }

    /** �� �黻�Ҥ�¿����� */
    public final Point operatorLet(final Point right) {
        setPoint(right.x, right.y);
        return this;
    }

    /** += �黻�Ҥ�¿����� */
    public final Point operatorPlusLet(final Point right) {
        addPoint(right);
        return this;
    }

    /** -= �黻�Ҥ�¿����� */
    public final Point operatorMinusLet(final Point right) {
        subPoint(right);
        return this;
    }

    /** () �黻�Ҥ�¿����� */
    public final Point operatorFunction(final int TempX, final int TempY) {
        setPoint(TempX, TempY);
        return this;
    }

    /** < �黻�Ҥ�¿����� */
    public boolean operatorLessThan(final Point right) {
        if (x < right.x && y < right.y) {
            return true;
        }
        return false;
    }

    /** > �黻�Ҥ�¿����� */
    public boolean operatorGreaterThan(final Point right) {
        if (x > right.x && y > right.y) {
            return true;
        }
        return false;
    }

    /** ==�黻�Ҥ�¿����� */
    public boolean operatorEqual(final Point right) {
        if (x == right.x && y == right.y) {
            return true;
        }
        return false;
    }

    /** != �黻�Ҥ�¿����� */
    public boolean operatorNotEqual(final Point right) {
        if (x != right.x || y != right.y) {
            return true;
        }
        return false;
    }

    /** <= �黻�Ҥ�¿����� */
    public boolean operatorLessEqual(final Point right) {
        if (x <= right.x && y <= right.y) {
            return true;
        }
        return false;
    }

    /** >= �黻�Ҥ�¿����� */
    public boolean operatorGreaterEqual(final Point right) {
        if (x >= right.x && y >= right.y) {
            return true;
        }
        return false;
    }
    
    /** */
    public int x;
    /** */
    public int y;

    /** �ݥ���ȤΥ��åȴؿ� */
    private void setPoint(final Point temp) {
        x = temp.x;
        y = temp.y;
    }

    /** �ݥ���ȤΥ��åȴؿ� */
    private void setPoint(final int tempX, final int tempY) {
        x = tempX;
        y = tempY;
    }

    /** �ݥ���Ȥ򤿤��ؿ� */
    private void addPoint(final Point temp) {
        x += temp.x;
        y += temp.y;
    }

    /** �ݥ���Ȥ�����ؿ� */
    private void subPoint(final Point temp) {
        x -= temp.x;
        y -= temp.y;
    }
}
