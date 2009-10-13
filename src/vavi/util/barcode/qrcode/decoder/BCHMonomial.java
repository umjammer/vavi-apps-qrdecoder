/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode.decoder;


/**
 * ���Υ��饹�ϥ����� GF(2) ���ñ�༰��ɽ�����롣
 * ñ�༰��ñ�༰�βû����軻�򥵥ݡ��Ȥ��������Ȥΰ٤���ӱ黻�Ҥ�
 * ���ݡ��Ȥ��롣
 * ñ�༰ * x ^ X ��ɽ������ {@link BCHMonomial} �ϡ�
 * {@link #valPower} = X (x�μ���)
 * ������ǡ����Ȥ��ƻ��ġ�
 *
 * @version	�������� 2003/02/20(Thu) �и�ë������ϯ
 */
class BCHMonomial implements Comparable<BCHMonomial> {
    /** ���󥹥ȥ饯�� */
    private BCHMonomial() {
        power = 0;
    }
    
    /** */
    public BCHMonomial(final BCHMonomial source) {
        if (source == null) {
            throw new IllegalArgumentException("source is null");
        }
        power = source.power;
    }
    
    /** */
    public BCHMonomial(final int power) {
        this.power = power;
    }

    /** = �黻�Ҥ�¿����� */
    public final BCHMonomial operatorLet(final BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        if (right == this) {
            throw new IllegalArgumentException("right is same to this");
        }
        power = right.power;
        return this;
    }

    /**
     * �軻�����黻�Ҥ�¿�����
     * ������­������
     */
    public final BCHMonomial operatorMultiplyLet(final BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        power += right.power;
        return this;
    }

    /**
     * �û������黻�Ҥ�¿�����
     * ������Ʊ���ʤ顢0 �ˤʤ� (xor ��Ȥ�Ȥ�������)
     */
    public final BCHMonomial operatorPlusLet(final BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        power ^= right.power;
        return this;
    }

    /*
     * ���������黻�Ҥ�¿�����
     * ���������
     */
    public final BCHMonomial operatorDivideLet(final BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        power -= right.power;
        return this;
    }

    /**
     * �軻�黻�Ҥ�¿�����
     * ������­������
     */
    public BCHMonomial operatorMultiply(final BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        power += right.power;
        return this;
    }

    /**
     * �û��黻�Ҥ�¿�����
     * ������Ʊ���ʤ顢0 �ˤʤ��XOR��Ȥ�Ȥ������ȡ�
     */
    public BCHMonomial operatorPlus(final BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        power ^= right.power;
        return this;
    }

    /** 
     * �����黻�Ҥ�¿�����
     * ���������
     */
    public BCHMonomial operatorDivide(final BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        power -= right.power;
        return this;
    }

    /** ��ӱ黻�Ҥ�¿����� */
    public boolean operatorLessThan(final BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        if (power < right.power) {
            return true;
        }
        return false;
    }

    /** ��ӱ黻�Ҥ�¿����� */
    public boolean operatorGreaterThan(final BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        if (power > right.power) {
            return true;
        }
        return false;
    }

    /** ����黻�Ҥ�¿����� */
    public boolean operatorEqual(final BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        if (power == right.power) {
            return true;
        }
        return false;
    }
    
    /** ������黻�Ҥ�¿����� */
    public boolean operatorNotEqual(final BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        if (power != right.power) {
            return true;
        }
        return false;
    }

    /** �������֤� */
    public final int getPower() {
        return power;
    }
    
    /** */
    private int power;

    /** */
    public int compareTo(BCHMonomial target) {
        return power - target.power;
    }
}

/* */
