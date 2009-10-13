/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */ 

package vavi.util.barcode.qrcode.decoder;


/**
 * ���Υ��饹�ϥ����� GF(2) ���ñ�༰��ɽ�����롣
 * ñ�༰��ñ�༰�βû����軻�򥵥ݡ��Ȥ��������Ȥΰ٤���ӱ黻�Ҥ�
 * ���ݡ��Ȥ��롣
 * ñ�༰<code>a ^ A * x ^ X</code> ��ɽ������ {@link GaloisMonomia} l�ϡ�
 * {@link #integer}<code> = a ^ A</code>������ɽ��
 * {@link #power}<code> = a ^ A</code>(�٤���ɽ��)
 * {@link #valPower}<code> = X</code>(x�μ���)
 * ��ǡ����Ȥ��ƻ��ġ�
 *
 * @version	�������� 2002/11/14(Thu) �и�ë������ϯ
 */
class GaloisMonomial implements Comparable<GaloisMonomial> {
    
    /**
     * �ǥե���ȥ��󥹥ȥ饯����
     * private °���ʤΤǡ������Ĥ��Υ��󥹥ȥ饯���Ǥʤ���Х��饹������ϥ��󥹥�
     * �󥹤�����Ǥ��ʤ���
     */
    private GaloisMonomial() {
        integer = 0;
        power = 0;
        valPower = 0;
    }
    
    /** ¿�༰���饹�⤫��ϡ��ƤӽФ�����Ĥ��롣 */
    public GaloisMonomial(final GaloisMonomial source) {
        integer = source.getInteger();
        power = source.getPower();
        valPower = source.getValPower();
    }

    /** ���٤ƤΥǡ���������ǤȤäƽ�������륳�󥹥ȥ饯���� */
    public GaloisMonomial(int integer, int power, int valPower){
        if (integer > 255) {
            throw new IllegalArgumentException("Integer > 255");
        }
        this.integer = integer;
        this.power = power % 255;
        this.valPower = valPower % 255;
    }
    
    /** �����黻�� */
    public final GaloisMonomial operatorLet(final GaloisMonomial right){
        integer = right.getInteger();
        power = right.getPower();
        valPower = right.getValPower();
        return this;
    }

    /** ��μ��� {@link #valPower} ����Ӥ��� */
    public boolean operatorLessThan(final GaloisMonomial right){
        if (valPower < right.getValPower()) {
            return true;
        }
        return false;
    }
    
    /** ��μ��� {@link #valPower} ����Ӥ��� */
    public boolean operatorGreaterThan(final GaloisMonomial right){
        if (valPower > right.getValPower()) {
            return true;
        }
        return false;
    }

    /** ��μ��� {@link #ValPower} ����Ӥ��� */
    public boolean operatorEqual(final GaloisMonomial right){
        if (valPower == right.getValPower()) {
            return true;
        }
        return false;
    }
    
    /** GF(2) ��βû��򤷤ޤ��� */
    public GaloisMonomial plus(final GaloisMonomial right, final PowerTable table) {
        // �ɤ��餫�ιब 0 �Ǥ�����ϡ�0 �Ǥʤ����ι���֤���
        if (this == null) {
            return right;
        }
        if (right == null) {
            return this;
        }
        
        if (valPower != right.getValPower()) {
            throw new IllegalArgumentException("Error : Cant plus diffarent valpower@CGaloisMonomial::Plus");
        }
        int rinteger = integer ^ right.getInteger();
        int rpower = table.convertIntToPower(rinteger);
        GaloisMonomial ret = new GaloisMonomial(rinteger, rpower, valPower);
        
        if (rinteger == 0) {
            ret = null;
        }
        return ret;
    }
    
    /** GF(2) ��ξ軻�򤷤ޤ��� */
    public GaloisMonomial multiply(final GaloisMonomial right, final PowerTable table) {
        // �ɤ��餫�ιब 0 �Ǥ�����ϡ�0 �ι���֤���
        if (this == null || right == null) {
            return new GaloisMonomial();
        }
        int resultPower = (power + right.getPower()) % 255;
        int resultInteger = table.convertPowerToInt(resultPower);
        int resultValPower = (valPower + right.getValPower()) % 255;
        return new GaloisMonomial(resultInteger, resultPower, resultValPower);
    }
    
    /** ����������ɽ�����֤� */
    public int getInteger() {
        return integer;
    }

    /** �����Τ٤���ɽ�����֤� */
    public int getPower() {
        return power;
    }

    /** ��μ������֤��� */
    public int getValPower() {
        return valPower;
    }

    /** ����������ɽ�� */
    private int integer;

    /** �����Τ٤���ɽ�� */
    private int power;

    /** ��μ��� */
    private int valPower;

    /** */
    public int compareTo(GaloisMonomial target) {
        return valPower - target.getValPower(); // TODO check
    }
}

/* */
