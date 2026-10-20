/*
 * Copyright (c) 2000-2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode;


/**
 * This class stores 2D coordinates of X and Y.
 * X and Y are public, but please do not modify them.
 *
 * @version Created 2000/06/26 by Kentaro Ishitoya
 *          Modified 2002/12/08 by Kentaro Ishitoya
 *          Removed inheritance from CObject.
 *          Excluded compatibility with Windows RECT structure
 */
class Point {

    /** */
    public Point(int tempX /* = 0 */, int tempY /* = 0 */) {
        setPoint(tempX, tempY);
    }

    /** */
    public Point(Point temp) {
        setPoint(temp);
    }

    /** Overload of + operator */
    public Point operatorPlus(Point right) {
        return new Point(x + right.x, y + right.y);
    }

    /** Overload of - operator */
    public Point operatorMinus(Point right) {
        return new Point(x - right.x, y - right.y);
    }

    /** Overload of = operator */
    public final Point operatorLet(Point right) {
        setPoint(right.x, right.y);
        return this;
    }

    /** Overload of += operator */
    public final Point operatorPlusLet(Point right) {
        addPoint(right);
        return this;
    }

    /** Overload of -= operator */
    public final Point operatorMinusLet(Point right) {
        subPoint(right);
        return this;
    }

    /** Overload of () operator */
    public final Point operatorFunction(int TempX, int TempY) {
        setPoint(TempX, TempY);
        return this;
    }

    /** Overload of < operator */
    public boolean operatorLessThan(Point right) {
        if (x < right.x && y < right.y) {
            return true;
        }
        return false;
    }

    /** Overload of > operator */
    public boolean operatorGreaterThan(Point right) {
        if (x > right.x && y > right.y) {
            return true;
        }
        return false;
    }

    /** Overload of == operator */
    public boolean operatorEqual(Point right) {
        if (x == right.x && y == right.y) {
            return true;
        }
        return false;
    }

    /** Overload of != operator */
    public boolean operatorNotEqual(Point right) {
        if (x != right.x || y != right.y) {
            return true;
        }
        return false;
    }

    /** Overload of <= operator */
    public boolean operatorLessEqual(Point right) {
        if (x <= right.x && y <= right.y) {
            return true;
        }
        return false;
    }

    /** Overload of >= operator */
    public boolean operatorGreaterEqual(Point right) {
        if (x >= right.x && y >= right.y) {
            return true;
        }
        return false;
    }

    /** */
    public int x;
    /** */
    public int y;

    /** Function to set point */
    private void setPoint(Point temp) {
        x = temp.x;
        y = temp.y;
    }

    /** Function to set point */
    private void setPoint(int tempX, int tempY) {
        x = tempX;
        y = tempY;
    }

    /** Function to add point */
    private void addPoint(Point temp) {
        x += temp.x;
        y += temp.y;
    }

    /** Function to subtract point */
    private void subPoint(Point temp) {
        x -= temp.x;
        y -= temp.y;
    }
}
