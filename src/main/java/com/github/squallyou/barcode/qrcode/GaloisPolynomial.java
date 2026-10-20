/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * This class represents a polynomial over the Galois field GF(2).
 * It supports addition and multiplication of polynomials.
 *
 * @version Created 2002/11/14(Thu) by Kentaro Ishitoya
 *          Modified 2002/12/01(Sun) by Kentaro Ishitoya
 *          Modified 2002/12/15(Sun) by Kentaro Ishitoya
 *          Modified 2002/02/22(Sun) by Kentaro Ishitoya
 */
class GaloisPolynomial {

    final static int POW_INDICATION = 0;
    final static int INT_INDICATION = 1;

    /** Constructor. Must always be initialized, so it is private. */
    private GaloisPolynomial() {
        table = null;
    }

    /** Constructor */
    public GaloisPolynomial(GaloisPolynomial source) {
        if (!polynomial.isEmpty()) {
            polynomial.clear();
        }
        List<GaloisMonomial> v;
        v = source.polynomial;
        polynomial.addAll(v);
        table = source.table;
    }

    /** Function to initialize by receiving a monomial */
    public GaloisPolynomial(GaloisMonomial monomial, PowerTable table) {
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

    /** Initialize by receiving an array of monomials */
    public GaloisPolynomial(List<GaloisMonomial> v, PowerTable table) {
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

    /** Constructor that takes an array of integer or power representation of coefficients and degree of terms and initializes. */
    public GaloisPolynomial(List<Integer> i, List<Integer> v, PowerTable temp, int flag /* = POW_INDICATION */) {
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
                this.polynomial.add(new GaloisMonomial(pi, this.table.convertIntToPower(pi), pv.next()));
            }
        } else if (flag == POW_INDICATION) {
            for (Integer pi : i) {
                this.polynomial.add(new GaloisMonomial(this.table.convertPowerToInt(pi), pi, pv.next()));
            }
        }
        Collections.sort(this.polynomial);
    }

    /**
     * Constructor that initializes the code given as {@link BinaryString} as a polynomial.
     *
     * @param code Assume integer representation
     */
    public GaloisPolynomial(BinaryString code, PowerTable table) {
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

    /** Assignment operator */
    public final GaloisPolynomial operatorLet(GaloisPolynomial right) {
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

    /** Additive assignment operator */
    public final GaloisPolynomial operatorPlusLet(GaloisPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right is null");
        }
        List<GaloisMonomial> v = plus(polynomial, right.polynomial);
        polynomial.clear();
        polynomial.addAll(v);
        return this;
    }

    /** Multiplicative assignment operator */
    public final GaloisPolynomial operatorMultiplyLet(GaloisPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right is null");
        }
        List<GaloisMonomial> v = multiply(polynomial, right.polynomial);
        polynomial.clear();
        polynomial.addAll(v);
        return this;
    }

    /** Modulo assignment operator */
    public final GaloisPolynomial operatorModuloLet(GaloisPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right is null");
        }
        GaloisPolynomial v = remainder(this, right);
        polynomial.clear();
        polynomial.addAll(v.polynomial);
        return this;
    }

    /** Addition operator */
    public GaloisPolynomial operatorPlus(GaloisPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right is null");
        }

        return new GaloisPolynomial(plus(polynomial, right.polynomial), table);
    }

    /** Multiplication operator */
    public GaloisPolynomial operatorMultiply(GaloisPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right is null");
        }

        return new GaloisPolynomial(multiply(polynomial, right.polynomial), table);
    }

    /** Modulo operator */
    public GaloisPolynomial operatorModulo(GaloisPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right is null");
        }

        return remainder(this, right);
    }

    /** Returns the term at the position represented by index */
    public GaloisMonomial get(int index) {
        if (index > polynomial.size()) {
            throw new IllegalArgumentException("invalid index");
        }
        return polynomial.get(index);
    }

    /** Returns the stored polynomial as a string of integer-represented polynomial */
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

    /** Returns the stored polynomial as a string of power-represented polynomial */
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

    /** Returns the stored polynomial as an array of unsigned int of coefficient representation. */
    public List<Integer> getDataByIntArray() {
        List<Integer> v = new ArrayList<>();
        for (GaloisMonomial monomial : polynomial) {
            v.add(monomial.getInteger());
        }
        return v;
    }

    /** Returns the stored polynomial as a BinaryString of coefficient representation. */
    public BinaryString getDataByBinaryString() {
        BinaryString bs = new BinaryString();
        for (GaloisMonomial monomial : polynomial) {
            bs.append(new BinaryString(monomial.getInteger(), 8));
        }
        return bs;
    }

    private GaloisMonomial findMonomial(List<GaloisMonomial> list, int valPower) {
        for (GaloisMonomial m : list) {
            if (m.getValPower() == valPower) {
                return m;
            }
        }
        return null;
    }

    /**
     * Method that performs internal operations
     * Function that actually performs addition
     */
    private List<GaloisMonomial> plus(List<GaloisMonomial> monomialA, List<GaloisMonomial> monomialB) {
        if (monomialA.isEmpty()) {
            return new ArrayList<>(monomialB);
        }
        if (monomialB.isEmpty()) {
            return new ArrayList<>(monomialA);
        }

        List<GaloisMonomial> result = new ArrayList<>();

        int max = Math.max(monomialA.get(0).getValPower(), monomialB.get(0).getValPower());
        // Get the highest degree of x among the two polynomials

        // For terms that exist in monomial1 but not in monomial2, and vice versa, add them to the answer.
        // For terms that exist in both, add them and then add to the answer.
        for (int i = 0; i <= max; i++) {
            int n = max - i;
            GaloisMonomial monomialA2 = findMonomial(monomialA, n);
            GaloisMonomial monomialB2 = findMonomial(monomialB, n);

            if (monomialA2 != null && monomialB2 == null) {
                if (monomialA2.getInteger() != 0) {
                    result.add(monomialA2);
                }
            } else if (monomialB2 != null && monomialA2 == null) {
                if (monomialB2.getInteger() != 0) {
                    result.add(monomialB2);
                }
            } else if (monomialA2 != null && monomialB2 != null) {
                GaloisMonomial sum = monomialA2.plus(monomialB2, table);
                if (sum != null) {
                    result.add(sum);
                }
            }
        }
        Collections.sort(result);
        return result;
    }

    /** Function that actually performs multiplication */
    private List<GaloisMonomial> multiply(List<GaloisMonomial> a, List<GaloisMonomial> b) {
        if (a.isEmpty() || b.isEmpty()) {
            throw new IllegalArgumentException("Invalid Paramiter");
        }
        List<GaloisMonomial> ret = new ArrayList<>();
        List<GaloisMonomial> temp = new ArrayList<>();

        // Multiply each term of A by the terms of B
        for (GaloisMonomial pb : b) {
            if (pb.getInteger() == 0) continue;
            for (GaloisMonomial pa : a) {
                if (pa.getInteger() == 0) continue;
                temp.add(pa.multiply(pb, table));
            }
        }
        Collections.sort(temp);

        // Group terms of the same degree and add them in order to get the answer.
        int idx = 0;
        while (idx < temp.size()) {
            int currentValPower = temp.get(idx).getValPower();
            int xorSum = 0;
            while (idx < temp.size() && temp.get(idx).getValPower() == currentValPower) {
                xorSum ^= temp.get(idx).getInteger();
                idx++;
            }
            if (xorSum != 0) {
                ret.add(new GaloisMonomial(xorSum, table.convertIntToPower(xorSum), currentValPower));
            }
        }
        Collections.sort(ret);
        return ret;
    }

    /**
     * Actually finds the remainder.
     *
     * @param i Dividend polynomial
     * @param g Generator polynomial (Note: will not work if reversed)
     */
    private GaloisPolynomial remainder(GaloisPolynomial i, GaloisPolynomial g) {
        if (i == null || g == null) {
            throw new IllegalArgumentException("Invalid Paramiter");
        }

        int max = g.get(0).getValPower();
        int num = i.get(0).getValPower() - max;

        GaloisPolynomial f = new GaloisPolynomial(i);

        for (int n = num; n >= 0; n--) {
            int targetDegree = n + max;
            GaloisMonomial a = findMonomial(f.polynomial, targetDegree);
            if (a == null || a.getInteger() == 0) {
                continue;
            }
            GaloisPolynomial r = new GaloisPolynomial(new GaloisMonomial(a.getInteger(), a.getPower(), n), table);
            f = f.operatorPlus(g.operatorMultiply(r));
        }

        for (int ii = max - 1; ii >= 0; ii--) {
            if (findMonomial(f.polynomial, ii) == null) {
                f.polynomial.add(new GaloisMonomial(0, 0, ii));
            }
        }
        Collections.sort(f.polynomial);
        return f;
    }

    /** Data */
    private List<GaloisMonomial> polynomial = new ArrayList<>();

    /** */
    private PowerTable table;
}
