/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode;


/**
 * This class represents a monomial over the Galois field GF(2).
 * It supports addition and multiplication of monomials, and comparison operators for sorting.
 * A {@link GaloisMonomial} representing the monomial <code>a ^ A * x ^ X</code> has
 * {@link #integer}<code> = a ^ A</code> (integer representation of coefficient)
 * {@link #power}<code> = a ^ A</code> (power representation of coefficient)
 * {@link #valPower}<code> = X</code> (degree of x)
 * as data.
 *
 * @version Created 2002/11/14(Thu) by Kentaro Ishitoya
 */
class GaloisMonomial implements Comparable<GaloisMonomial> {

    /**
     * Default constructor.
     * Since it is private, instances cannot be created from outside the class
     * without a constructor with arguments.
     */
    private GaloisMonomial() {
        integer = 0;
        power = 0;
        valPower = 0;
    }

    /** Invocation is permitted from within the polynomial class. */
    public GaloisMonomial(GaloisMonomial source) {
        integer = source.getInteger();
        power = source.getPower();
        valPower = source.getValPower();
    }

    /** Constructor that takes all data as arguments and initializes. */
    public GaloisMonomial(int integer, int power, int valPower) {
        if (integer > 255) {
            throw new IllegalArgumentException("Integer > 255");
        }
        this.integer = integer;
        this.power = power % 255;
        this.valPower = valPower % 255;
    }

    /** Assignment operator */
    public final GaloisMonomial operatorLet(GaloisMonomial right) {
        integer = right.getInteger();
        power = right.getPower();
        valPower = right.getValPower();
        return this;
    }

    /** Compare by degree of term {@link #valPower} */
    public boolean operatorLessThan(GaloisMonomial right) {
        if (valPower < right.getValPower()) {
            return true;
        }
        return false;
    }

    /** Compare by degree of term {@link #valPower} */
    public boolean operatorGreaterThan(GaloisMonomial right) {
        if (valPower > right.getValPower()) {
            return true;
        }
        return false;
    }

    /** Compare by degree of term {@link #valPower} */
    public boolean operatorEqual(GaloisMonomial right) {
        if (valPower == right.getValPower()) {
            return true;
        }
        return false;
    }

    /** Performs addition over GF(2). */
    public GaloisMonomial plus(GaloisMonomial right, PowerTable table) {
        // If either term is 0, return the non-zero term.
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

    /** Performs multiplication over GF(2). */
    public GaloisMonomial multiply(GaloisMonomial right, PowerTable table) {
        // If either term is 0, return the 0 term.
        if (this == null || right == null) {
            return new GaloisMonomial();
        }
        if (integer == 0 || right.getInteger() == 0) {
            return new GaloisMonomial(0, 0, (valPower + right.getValPower()) % 255);
        }
        int resultPower = (power + right.getPower()) % 255;
        int resultInteger = table.convertPowerToInt(resultPower);
        int resultValPower = (valPower + right.getValPower()) % 255;
        return new GaloisMonomial(resultInteger, resultPower, resultValPower);
    }

    /** Returns the integer representation of the coefficient */
    public int getInteger() {
        return integer;
    }

    /** Returns the power representation of the coefficient */
    public int getPower() {
        return power;
    }

    /** Returns the degree of the term. */
    public int getValPower() {
        return valPower;
    }

    /** Integer representation of coefficient */
    private int integer;

    /** Power representation of coefficient */
    private int power;

    /** Degree of the term */
    private int valPower;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GaloisMonomial that = (GaloisMonomial) o;
        return valPower == that.valPower;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(valPower);
    }

    @Override
    public int compareTo(GaloisMonomial target) {
        return target.getValPower() - valPower;
    }
}
