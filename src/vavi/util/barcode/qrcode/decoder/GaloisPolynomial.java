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
 * ���Υ��饹�ϥ����� GF(2) ���¿�༰��ɽ�����롣
 * ¿�༰��¿�༰�βû����軻�򥵥ݡ��Ȥ��롣
 *
 * @version	�������� 2002/11/14(Thu) �и�ë������ϯ
 *          �ɲ��ѹ� 2002/12/01(Sun) �и�ë������ϯ
 *          �ɲ��ѹ� 2002/12/15(Sun) �и�ë������ϯ
 *          �ɲ��ѹ� 2002/02/22(Sun) �и�ë������ϯ
 */
class GaloisPolynomial {
    final static int POW_INDICATION = 0;
    final static int INT_INDICATION = 1;

    /** ���󥹥ȥ饯����ɬ�����������ʤ���Фʤ�ʤ��Τ� private °���� */
    private GaloisPolynomial() {
        table = null;
    }

    /** ���󥹥ȥ饯�� */
    public GaloisPolynomial(final GaloisPolynomial source) {
        if (!polynomial.isEmpty()) {
            polynomial.clear();
        }
        List<GaloisMonomial> v;
        v = source.polynomial;
        polynomial.addAll(v);
        table = source.table;
    }

    /** ñ�༰�������äƽ��������ؿ� */
    public GaloisPolynomial(final GaloisMonomial monomial, final PowerTable table) {
        if (monomial == null) {
            throw new IllegalArgumentException("m is null");
        }
        if (table == null) {
            throw new IllegalArgumentException("table is null");
        }
        if (!this.polynomial.isEmpty()) {
            this.polynomial.clear();
        }
        this.polynomial.add(new GaloisMonomial(monomial));
        this.table = table;
    }

    /** ñ�༰������������äƽ�������� */
    public GaloisPolynomial(final List<GaloisMonomial> v, final PowerTable table) {
        if (table == null) {
            throw new IllegalArgumentException("table is null");
        }
        if (!this.polynomial.isEmpty()) {
            this.polynomial.clear();
        }
        for (GaloisMonomial p : v) {
            if (p == null) {
                throw new IllegalArgumentException("This Monomiall is not Initialized");
            }
            this.polynomial.add(p);
        }
        this.table = table;
        Collections.sort(this.polynomial);
    }

    /** ����������ɽ���ޤ��Ϥ٤���ɽ��������ȡ���μ����������äƽ�������륳�󥹥ȥ饯���� */
    public GaloisPolynomial(final List<Integer> i, final List<Integer> v, final PowerTable temp, final int flag /* = POW_INDICATION */) {
        if (temp == null) {
            throw new IllegalArgumentException("table is null");
        }
        if (i.size() != v.size()) {
            throw new IllegalArgumentException("not match size");
        }
        if (!this.polynomial.isEmpty()) {
            this.polynomial.clear();
        }
        this.table = temp;
        Iterator<Integer> pv = v.iterator();

        if (flag == INT_INDICATION) {
            for (Integer pi : i) {
                this.polynomial.add(new GaloisMonomial(pi.intValue(), this.table.convertIntToPower(pi.intValue()), pv.next().intValue()));
            }
        } else if (flag == POW_INDICATION) {
            for (Integer pi : i) {
                this.polynomial.add(new GaloisMonomial(this.table.convertPowerToInt(pi.intValue()), pi.intValue(), pv.next().intValue()));
            }
        }
        Collections.sort(this.polynomial);
    }

    /**
     * {@link BinaryString} ��Ϳ����줿 code ��¿�༰�ˤ��ƽ�������륳�󥹥ȥ饯����
     * @param code ����ɽ���Ǥ���Ȳ��ꤹ��
     */
    public GaloisPolynomial(final BinaryString code, final PowerTable table) {
        if (table == null) {
            throw new IllegalArgumentException("table is null");
        }
        if ((code.GetLength() % 8) != 0) {
            throw new IllegalArgumentException("Code is not 8bit Blocks");
        }
        if (!this.polynomial.isEmpty()) {
            this.polynomial.clear();
        }
        this.table = table;
        int max = code.GetLength() / 8;
        for (int i = 0; i < max; i++) {
            this.polynomial.add(new GaloisMonomial(code.getSubByte(i), this.table.convertIntToPower(code.getSubByte(i)), i));
        }
        Collections.sort(this.polynomial);
    }
    
    /** �����黻�� */
    public final GaloisPolynomial operatorLet(final GaloisPolynomial right){
        if (right == null) {
            throw new IllegalArgumentException("right is null");
        }
        if (!polynomial.isEmpty()) {
            polynomial.clear();
        }
        List<GaloisMonomial> v;
        v = right.polynomial;
        polynomial.addAll(v);
        table = right.table;
        Collections.sort(polynomial);
        return this;
    }

    /** �û������黻�� */
    public final GaloisPolynomial operatorPlusLet(final GaloisPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right is null");
        }
        List<GaloisMonomial> v = plus(polynomial, right.polynomial);
        polynomial.clear();
        polynomial.addAll(v);
        return this;
    }

    /** �軻�����黻�� */
    public final GaloisPolynomial operatorMultiplyLet(final GaloisPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right is null");
        }
        List<GaloisMonomial> v = multiply(polynomial, right.polynomial);
        polynomial.clear();
        polynomial.addAll(v);
        return this;
    }

    /** ��;�����黻�� */
    public final GaloisPolynomial operatorModuloLet(final GaloisPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right is null");
        }
        GaloisPolynomial v = remainder(this, right);
        polynomial.clear();
        polynomial.addAll(v.polynomial);
        return this;
    }

    /** �û��黻�� */
    public GaloisPolynomial operatorPlus(final GaloisPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right is null");
        }

        return new GaloisPolynomial(plus(polynomial, right.polynomial), table);
    }

    /** �軻�黻�� */
    public GaloisPolynomial operatorMultiply(final GaloisPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right is null");
        }

        return new GaloisPolynomial(multiply(polynomial, right.polynomial), table);
    }

    /** �軻�黻�� */
    public GaloisPolynomial operatorModulo(final GaloisPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right is null");
        }

        return remainder(this, right);
    }
    
    /** index ��ɽ�������֤ι���֤� */
    public GaloisMonomial get(final int index) {
        if (index > polynomial.size()) {
            throw new IllegalArgumentException("invalid index");
        }
        return polynomial.get(index);
    }

    /** ��Ǽ����Ƥ���¿�༰������ɽ����¿�༰��ʸ����ˤ����֤� */
    public String toStringIntIndication() {
        StringWriter buffer = new StringWriter();
        int count = 0;
        for (GaloisMonomial monomial : polynomial) {
            if (monomial.getInteger() != 1) {
                buffer.write(monomial.getInteger());
            } else if (monomial.getValPower() == 0) {
                buffer.write(monomial.getInteger());
            }

            if (monomial.getValPower() == 1) {
                buffer.write("X");
            } else if (monomial.getValPower() != 0) {
                buffer.write("X^");
                buffer.write(monomial.getValPower());
            }
            count++;
            if (count != polynomial.size() - 1) {
                buffer.write(" + ");
            }
        }
        return buffer.toString();
    }

    /** ��Ǽ����Ƥ���¿�༰��٤�ɽ����¿�༰��ʸ����ˤ����֤� */
    public String toStringPowIndication() {
        StringWriter buffer = new StringWriter();

        int count = 0;
        for (GaloisMonomial monomial : polynomial) {
            if (monomial.getPower() == 1) {
                buffer.write("A");
            } else if (monomial.getPower() != 0) {
                buffer.write("A^");
                buffer.write(monomial.getPower());
            }
            if (monomial.getValPower() == 1) {
                buffer.write("X");
            } else if (monomial.getValPower() != 0) {
                buffer.write("X^");
                buffer.write(monomial.getValPower());
            }
            count++;
            if (count < polynomial.size() - 1) {
                buffer.write(" + ");
            }
        }
        return buffer.toString();
    }

    /** ��Ǽ����Ƥ���¿�༰�򷸿�ɽ����unsigned int������ˤ����֤��� */
    public List<Integer> getDataByIntArray() {
        List<Integer> v = new ArrayList<Integer>();
        for (GaloisMonomial monomial : polynomial) {
            v.add(new Integer(monomial.getInteger()));
        }
        return v;
    }

    /** ��Ǽ����Ƥ���¿�༰�򷸿�ɽ����BinaryString�ˤ����֤��� */
    public BinaryString getDataByBinaryString() {
        BinaryString bs = new BinaryString();
        for (GaloisMonomial monomial : polynomial) {
            bs.operatorPlusLet(new BinaryString(monomial.getInteger()));
        }
        return bs;
    }

    /**
     * �����Ǳ黻��Ԥʤ��᥽�å�
     * �ºݤ˲û���Ԥʤ��ؿ�
     */
    private List<GaloisMonomial> plus(final List<GaloisMonomial> monomialA, final List<GaloisMonomial> monomialB) {
        if (monomialA.isEmpty() || monomialB.isEmpty()) {
            throw new IllegalArgumentException("Error : Invalid Paramiter @ CGaloisPolynomial::Multi");
        }

        List<GaloisMonomial> result = new ArrayList<GaloisMonomial>();

        int max;
        // ��Ĥ�¿�༰�桢x�κǹ⼡�������
        if (monomialA.get(0).getValPower() > monomialB.get(0).getValPower()) {
            max = monomialA.get(0).getValPower();
        } else {
            max = monomialB.get(0).getValPower();
        }

        // monomial1 �����¸�ߤ� monomial2 �����¸�ߤ��ʤ��ࡢ���εդξ������������롣
        // ξ����¸�ߤ����ξ�硢­���Ƥ�������������롣
        for (int i = 0; i <= max; i++) {
            int n = max - i;
            GaloisMonomial monomialA2 = monomialA.get(new GaloisMonomial(1, 0, n).getInteger());
            GaloisMonomial monomialB2 = monomialB.get(new GaloisMonomial(1, 0, n).getInteger());

            if (monomialA2 != monomialA.get(monomialA.size() - 1) && monomialB2 == monomialB.get(monomialB.size() - 1)) {
                result.add(monomialA2);
            } else if (monomialB2 != monomialB.get(monomialB.size() - 1) && monomialA2 == monomialA.get(monomialA.size() - 1)) {
                result.add(monomialB2);
            } else if (monomialA2 != monomialA.get(monomialA.size() - 1) && monomialB2 != monomialB.get(monomialB.size() - 1)) {
                if (monomialA2.plus(monomialB2, table) != null) {
                    result.add((monomialA2).plus(monomialB2, table));
                }
            }
        }
        Collections.sort(result);
        return result;
    }

    /** �ºݤ˾軻��Ԥʤ��ؿ� */
    private List<GaloisMonomial> multiply(final List<GaloisMonomial> a, final List<GaloisMonomial> b) {
        if (a.isEmpty() || b.isEmpty()) {
            throw new IllegalArgumentException("Invalid Paramiter");
        }
        List<GaloisMonomial> ret = new ArrayList<GaloisMonomial>();
        List<GaloisMonomial> temp = new ArrayList<GaloisMonomial>();
        GaloisMonomial m;
        GaloisMonomial t;

        // A �Τ��줾��ι��B�ι��ݤ���
        for (GaloisMonomial pb : b) {
            for (GaloisMonomial pa : a) {
                temp.add(pa.multiply(pb, table));
            }
        }
        Collections.sort(temp);
        Iterator<GaloisMonomial> ptemp = temp.iterator();

        // Ʊ�����ι��ꥹ�ȥ��åפ����­���������ˤ��롣
        while (ptemp.hasNext()) {
            int num = temp.indexOf(ptemp);
            if (num != 0) {
                m = ptemp.next();
                if (num == 1) {
                    ret.add(m);
                } else {
                    for (int i = 0; i < num - 1; i++) {
                        if ((t = m.plus(ptemp.next(), table)) != null) {
                            m = t;
                        }
                    }
                    ret.add(m);
                }
            }
        }
        Collections.sort(ret);
        return ret;
    }

    /**
     * �ºݤ˾�;����ޤ���
     * @param i ����¿�༰
     * @param g ������¿�༰ (���դˤ����ư���ޤ���)
     */
    private GaloisPolynomial remainder(final GaloisPolynomial i, final GaloisPolynomial g) {
        if (i == null || g == null) {
            throw new IllegalArgumentException("Invalid Paramiter");
        }

        GaloisMonomial a = i.get(0);
        int max = g.get(0).getValPower();
        int num = a.getValPower() - g.get(0).getValPower();
        GaloisPolynomial r = new GaloisPolynomial(new GaloisMonomial(a.getInteger(), a.getPower(), num), table);
        GaloisPolynomial f = g.operatorMultiply(r).operatorPlus(i);

        for (int n = num - 1; n >= 0; n--) {
            a = f.get(0);
            r = new GaloisPolynomial(new GaloisMonomial(a.getInteger(), a.getPower(), n), table);
            f = g.operatorMultiply(r).operatorPlus(f);

        }

        for (int ii = max - 1; ii >= 0; ii--) {
            int p = f.polynomial.indexOf(new GaloisMonomial(0, 0, ii));
            if (p != f.polynomial.size() - 1) {
                f.polynomial.add(new GaloisMonomial(0, 0, ii));
            }
        }
        return f;
    }

    /** �ǡ��� */
    private List<GaloisMonomial> polynomial;

    /** */
    private PowerTable table;
}

/* */
