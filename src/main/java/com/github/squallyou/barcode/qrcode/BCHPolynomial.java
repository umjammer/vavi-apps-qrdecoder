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
 * @version Created 2003/02/20(Thu) by Kentaro Ishitoya
 */
class BCHPolynomial {

    /** */
    protected BCHPolynomial() {
    }

    /** Copy constructor */
    public BCHPolynomial(BCHPolynomial source) {
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

    /** Constructor that takes a monomial as argument */
    public BCHPolynomial(BCHMonomial temp) {
        if (temp == null) {
            throw new IllegalArgumentException("source is null");
        }
        if (!this.polynomial.isEmpty()) {
            this.polynomial.clear();
        }
        this.polynomial.add(temp);
    }

    /** Constructor that takes a BinaryString as argument and initializes. */
    public BCHPolynomial(BinaryString str) {
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

    /** Constructor that takes a list as argument */
    private BCHPolynomial(List<BCHMonomial> temp) {
        // @@@
    }

    /** Constructor that takes a list of terms as argument */
    private void initBCHMonomialList(List<BCHMonomial> source) {
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

    /** Constructor that takes a list of unsigned int as argument and initializes */
    private void initIntegerList(List<Integer> source) {
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

    /** Overload of assignment operator */
    public final BCHPolynomial operatorLet(BCHPolynomial right) {
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

    /** Overload of additive assignment operator */
    public final BCHPolynomial operatorPlusLet(BCHPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        this.polynomial = plus(this, right).polynomial;
        return this;
    }

    /** Multiplicative assignment operator */
    public final BCHPolynomial operatorMultiplyLet(BCHPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        this.polynomial = multiply(this, right).polynomial;
        return this;
    }

    /** */
    public final BCHPolynomial operatorDivideLet(BCHPolynomial right) {
        throw new UnsupportedOperationException();
    }

    /** Overload of modulo assignment operator */
    public final BCHPolynomial operatorModuloLet(BCHPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        this.polynomial = remainder(this, right).polynomial;
        return this;
    }

    /** Overload of addition operator */
    public BCHPolynomial operatorPlus(BCHPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        return plus(this, right);
    }

    /** Overload of multiplication operator */
    public BCHPolynomial operatorMultiply(BCHPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        return multiply(this, right);
    }

    /** Overload of modulo operator */
    public BCHPolynomial operatorModulo(BCHPolynomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        return remainder(right, this);
    }

    /** Returns the term at the position specified by index */
    public BCHMonomial get(int index) {
        return polynomial.get(index);
    }

    /** Get data as string */
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

    /** Get data as unsigned int array */
    public List<Integer> getDataByUINTArray() {
        List<Integer> ret = new ArrayList<>();
        for (BCHMonomial p : polynomial) {
            ret.add(p.getPower());
        }
        return ret;
    }

    /** Get data as BinaryString */
    public BinaryString getDataByBinaryString() {
        if (polynomial.isEmpty()) {
            return new BinaryString("0");
        }
        int maxPower = polynomial.get(0).getPower();
        boolean[] bits = new boolean[maxPower + 1];
        for (BCHMonomial p : polynomial) {
            bits[p.getPower()] = true;
        }
        BinaryString bs = new BinaryString();
        for (int i = maxPower; i >= 0; i--) {
            bs.append(bits[i]);
        }
        return bs;
    }

    /**
     * Function that actually performs addition.
     * Eliminates terms of the same degree.
     */
    private BCHPolynomial plus(BCHPolynomial a, BCHPolynomial b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("parameters must not be null");
        }
        List<BCHMonomial> ret = new ArrayList<>(a.polynomial);

        for (BCHMonomial pb : b.polynomial) {
            int pret = ret.indexOf(pb);
            if (pret == -1) {
                ret.add(pb);
            } else {
                ret.remove(pret);
            }
        }
        Collections.sort(ret);
        return new BCHPolynomial(ret);
    }

    /**
     * Function that actually performs multiplication.
     * Unless the other party is a monomial, it throws an exception without calculating. It just adds the degrees of the terms.
     */
    private BCHPolynomial multiply(BCHPolynomial a, BCHPolynomial b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("parameters must not be null");
        }
        if (!(a.polynomial.size() == 1 || b.polynomial.size() == 1)) {
            throw new IllegalArgumentException("cannot multiply polynomial and polynomial");
        }
        List<BCHMonomial> ret = new ArrayList<>();
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
     * Function that actually finds the remainder.
     * Unlike Galois, it performs regular division and finds the remainder.
     */
    private BCHPolynomial remainder(BCHPolynomial g, BCHPolynomial i) {
        if (g == null || i == null) {
            throw new IllegalArgumentException("parameters must not be null");
        }
        if (i.polynomial.isEmpty() || g.polynomial.isEmpty()) {
            return i;
        }
        if (g.get(0).getPower() > i.get(0).getPower()) {
            return i;
        }

//      BCHMonomial m;
        BCHPolynomial ret = i;
//      List<BCHMonomial> pret;
        BCHMonomial pi = i.polynomial.get(0);
        BCHMonomial pg = g.polynomial.get(0);
        int n = pi.getPower() - pg.getPower();

        while (true) {
            ret = ret.operatorPlus(g.operatorMultiply(new BCHPolynomial(new BCHMonomial(n))));
            if (ret.polynomial.isEmpty()) {
                break;
            }
            pi = ret.polynomial.get(0);
            if (pi.getPower() < pg.getPower()) {
                break;
            }
            n = pi.getPower() - pg.getPower();
        }
        return ret;
    }

    /** */
    private List<BCHMonomial> polynomial = new ArrayList<>();
}
