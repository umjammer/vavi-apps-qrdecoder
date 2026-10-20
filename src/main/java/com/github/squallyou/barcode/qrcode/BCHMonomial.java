/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode;


/**
 * This class represents a monomial over the Galois field GF(2).
 * It supports addition and multiplication of monomials, and comparison operators for sorting.
 * A {@link BCHMonomial} representing the monomial * x ^ X has
 * {@link #power} = X (degree of x)
 * as its only data.
 *
 * @version Created 2003/02/20(Thu) by Kentaro Ishitoya
 */
class BCHMonomial implements Comparable<BCHMonomial> {

    /** Constructor */
    private BCHMonomial() {
        power = 0;
    }

    /** */
    public BCHMonomial(BCHMonomial source) {
        if (source == null) {
            throw new IllegalArgumentException("source is null");
        }
        power = source.power;
    }

    /** */
    public BCHMonomial(int power) {
        this.power = power;
    }

    /** Overload of = operator */
    public final BCHMonomial operatorLet(BCHMonomial right) {
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
     * Overload of multiplicative assignment operator
     * Just adds the degrees
     */
    public final BCHMonomial operatorMultiplyLet(BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        power += right.power;
        return this;
    }

    /**
     * Overload of additive assignment operator
     * If the degrees are the same, it becomes 0 (meaning XOR)
     */
    public final BCHMonomial operatorPlusLet(BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        power ^= right.power;
        return this;
    }

    /*
     * Overload of division assignment operator
     * Subtracts the degrees
     */
    public final BCHMonomial operatorDivideLet(BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        power -= right.power;
        return this;
    }

    /**
     * Overload of multiplication operator
     * Just adds the degrees
     */
    public BCHMonomial operatorMultiply(BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        power += right.power;
        return this;
    }

    /**
     * Overload of addition operator
     * If the degrees are the same, it becomes 0 (meaning XOR)
     */
    public BCHMonomial operatorPlus(BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        power ^= right.power;
        return this;
    }

    /**
     * Overload of division operator
     * Subtracts the degrees
     */
    public BCHMonomial operatorDivide(BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        power -= right.power;
        return this;
    }

    /** Overload of comparison operator */
    public boolean operatorLessThan(BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        if (power < right.power) {
            return true;
        }
        return false;
    }

    /** Overload of comparison operator */
    public boolean operatorGreaterThan(BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        if (power > right.power) {
            return true;
        }
        return false;
    }

    /** Overload of equality operator */
    public boolean operatorEqual(BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        if (power == right.power) {
            return true;
        }
        return false;
    }

    /** Overload of inequality operator */
    public boolean operatorNotEqual(BCHMonomial right) {
        if (right == null) {
            throw new IllegalArgumentException("right operand is null");
        }
        if (power != right.power) {
            return true;
        }
        return false;
    }

    /** Returns the degree */
    public final int getPower() {
        return power;
    }

    /** */
    private int power;

    @Override
    public int compareTo(BCHMonomial target) {
        return power - target.power;
    }
}
