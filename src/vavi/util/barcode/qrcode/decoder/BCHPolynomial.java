/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */ 

package vavi.util.barcode.qrcode.decoder;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * ���Υ��饹�ϥ�����GF(2)���¿�༰��ɽ�����롣
 * ¿�༰��¿�༰�βû����軻�򥵥ݡ��Ȥ��롣
 *
 * @version	�������� 2003/02/20(Thu) �и�ë������ϯ
 */
class BCHPolynomial {
    /** */
    protected BCHPolynomial() {}

    /** ���ԡ����󥹥ȥ饯�� */
    public BCHPolynomial(final BCHPolynomial source){
        if (source == null) {
            throw new IllegalArgumentException("source is null");
        }
        if (!this.polynomial.isEmpty()) {
            this.polynomial.clear();
        }
        List<BCHMonomial> v;
        v = source.polynomial;
        Collections.<BCHMonomial>sort(v);
        this.polynomial.addAll(v);
    }

    /** ñ�������ˤȤ륳�󥹥ȥ饯�� */
    public BCHPolynomial(final BCHMonomial temp) {
        if (temp == null) {
            throw new IllegalArgumentException("source is null");
        }
        if (!this.polynomial.isEmpty()) {
            this.polynomial.clear();
        }
        this.polynomial.add(temp);
    }

    /** BinaryString������ˤȤäƽ�������륳�󥹥ȥ饯���� */
    public BCHPolynomial(final BinaryString str) {
        if (str == null) {
            throw new IllegalArgumentException("source is null");
        }
        if (!this.polynomial.isEmpty()) {
            this.polynomial.clear();
        }
        int max = str.GetLength();
        int n;
        for (int i = 0; i < max; i++) {
            n = max - i;
            if (str.at(i)) {
                this.polynomial.add(new BCHMonomial(n - 1));
            }
        }
        if (this.polynomial.isEmpty()) {
//          Initialized = false;
        } else {
            Collections.sort(this.polynomial);
        }
    }

    /** �ꥹ�Ȥ�����˼�륳�󥹥ȥ饯�� */
    private BCHPolynomial(final List temp) {
        // @@@
    }

    /** ��Υꥹ�Ȥ�����˼�륳�󥹥ȥ饯�� */
    private void initBCHMonomialList(final List<BCHMonomial> source) {
        if (source == null) {
            throw new IllegalArgumentException("source is null");
        }
        if (!this.polynomial.isEmpty()) {
            this.polynomial.clear();
        }
        Iterator<BCHMonomial> p = source.iterator();
        while (p.hasNext()) {
            int prep = this.polynomial.indexOf(p);
            if (prep == this.polynomial.size() - 1) {
                this.polynomial.add(p.next());
            }
        }
        Collections.sort(this.polynomial);
    }

    /** unsigned int�Υꥹ�Ȥ�����ˤȤäƽ�������륳�󥹥ȥ饯�� */
    private void initIntegerList(final List<Integer> source) {
        if (source.isEmpty()) {
            throw new IllegalArgumentException("right operand is null");
        }
        if (!this.polynomial.isEmpty()) {
            this.polynomial.clear();
        }
        for (Integer p : source) {
            int prep = this.polynomial.indexOf(new BCHMonomial(p));
            if (prep == this.polynomial.size() - 1) {
                this.polynomial.add(new BCHMonomial(p));
            }
        }
        Collections.sort(this.polynomial);
    }

    /** �����黻�Ҥ�¿����� */
    public final BCHPolynomial operatorLet(final BCHPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        if (!this.polynomial.isEmpty()) {
            this.polynomial.clear();
        }

        List<BCHMonomial> v;
        v = right.polynomial;
        this.polynomial.addAll(v);
        return this;
    }

    /** �û������黻�Ҥ�¿����� */
    public final BCHPolynomial operatorPlusLet(final BCHPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        this.polynomial = plus(this, right).polynomial;
        return this;
    }

    /** �軻�����黻�� */
    public final BCHPolynomial operatorMultiplyLet(final BCHPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        this.polynomial = multiply(this, right).polynomial;
        return this;
    }

    /** */
    public final BCHPolynomial operatorDivideLet(final BCHPolynomial right) {
        throw new UnsupportedOperationException();
    }
    
    /** ��;�����黻�Ҥ�¿����� */
    public final BCHPolynomial operatorModuloLet(final BCHPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        this.polynomial = remainder(this, right).polynomial;
        return this;
    }

    /** �û��黻�Ҥ�¿����� */
    public BCHPolynomial operatorPlus(final BCHPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        return plus(this, right);
    }

    /** �軻�黻�Ҥ�¿����� */
    public BCHPolynomial operatorMultiply(final BCHPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        return multiply(this, right);
    }

    /** ��;�黻�Ҥ�¿����� */
    public BCHPolynomial operatorModulo(final BCHPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        return remainder(this, right);
    }

    /** index�ǻ��ꤵ�����֤ι�򵢤� */
    public BCHMonomial get(int index) {
        return polynomial.get(index);
    }

    /** �ǡ�����ʸ����Ǽ��� */
    public String toString() {
        StringWriter buffer = new StringWriter();

        int count = 0;
        for (BCHMonomial monomial : polynomial) {
            if (monomial.getPower() == 0) {
                buffer.write("1");
            } else if (monomial.getPower() == 1) {
                buffer.write("X");
            } else {
                buffer.write("X^");
                buffer.write(monomial.getPower());
            }
            count++;
            if (count < polynomial.size() - 1) {
                buffer.write(" + ");
            }
        }
        return buffer.toString();
    }
    
    /** �ǡ�����unsigned int������Ǽ��� */
    public List<Integer> getDataByUINTArray() {
        List<Integer> ret = new ArrayList<Integer>();
        for (BCHMonomial p : polynomial) {
            ret.add(new Integer(p.getPower()));
        }
        return ret;
    }

    /** �ǡ�����BinaryString�Ǽ��� */
    public BinaryString getDataByBinaryString() {
        int temp = 0;
        for (BCHMonomial p : polynomial) {
            temp += Math.pow(2, p.getPower());
        }
        return new BinaryString(temp, polynomial.get(0).getPower() + 1);
    }

    /**
     * �ºݤ˲û���������ؿ���
     * Ʊ�����ι��ä���
     */
    private BCHPolynomial plus(final BCHPolynomial a, final BCHPolynomial b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("parameters must not be null");
        }
        List<BCHMonomial> ret = a.polynomial;

        for (BCHMonomial pb : b.polynomial) {
            int pret = ret.indexOf(pb);
            if (pret == ret.size() - 1) {
                ret.add(pb);
            } else {
                ret.remove(pret);
            }
        }
        return new BCHPolynomial(ret);
    }

    /**
     * �ºݤ˾軻��������ؿ���
     * ��꤬ñ�༰�Ǥʤ��¤�Ϸ׻����ʤ����㳰��Ф���������μ�����­��������
     */
    private BCHPolynomial multiply(final BCHPolynomial a, final BCHPolynomial b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("parameters must not be null");
        }
        if (!(a.polynomial.size() == 1 || b.polynomial.size() == 1)) {
            throw new IllegalArgumentException("cannot multiply polynomial and polynomial");
        }
        List<BCHMonomial> ret = new ArrayList<BCHMonomial>();
        Iterator<BCHMonomial> p;
        BCHMonomial m;
        if (a.polynomial.size() == 1) {
            m = a.polynomial.get(0);
            p = b.polynomial.iterator();
        } else {
            m = b.polynomial.get(0);
            p = a.polynomial.iterator();
        }
        while (p.hasNext()) {
            ret.add(p.next().operatorMultiply(m));
        }
        return new BCHPolynomial(ret);
    }

    /**
     * �ºݤ˾�;�����ؿ���
     * Galois�Ȥϰ㤤�����̤ν�����;����롣
     */
    private BCHPolynomial remainder(final BCHPolynomial g, final BCHPolynomial i) {
        if (g == null || i == null) {
            throw new IllegalArgumentException("parameters must not be null");
        }
        if (g.get(0).getPower() > i.get(0).getPower()) {
            throw new IllegalArgumentException("Invalid Paramiter");
        }

//      BCHMonomial m;
        BCHPolynomial ret = i;
//      List<BCHMonomial> pret;
        BCHMonomial pi = i.polynomial.get(0);
        BCHMonomial pg = g.polynomial.get(0);
        int n = pi.getPower() - pg.getPower();

        while (true) {
            ret = ret.operatorPlus(g.operatorMultiply(new BCHPolynomial(new BCHMonomial(n))));
            pi = ret.polynomial.get(0);
            if (pi.getPower() < pg.getPower()) {
                break;
            }
            n = pi.getPower() - pg.getPower();
        }
        return ret;
    }
    
    /** */
    private List<BCHMonomial> polynomial;
}

/* */
