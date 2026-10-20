/*
 * Copyright (c) 2000-2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode.decoder;


/**
 * このクラスはX、Yの二次元の座標を格納するクラスです。
 * X, Y は公開されていますが、変更しないで下さい・・・。
 *
 * @version 新規作成 2000/06/26 石戸谷顕太朗
 * 追加変更 2002/12/08 石戸谷顕太朗
 * CObjectからの継承を取った。
 * WindowsのRECT構造体との互換性の排除
 */
class Point {

    /**  */
    public Point(int tempX /* = 0 */, int tempY /* = 0 */) {
        setPoint(tempX, tempY);
    }

    /**  */
    public Point(Point temp) {
        setPoint(temp);
    }

    /** + 演算子の多重定義 */
    public Point operatorPlus(Point right) {
        return new Point(x + right.x, y + right.y);
    }

    /** -演算子の多重定義 */
    public Point operatorMinus(Point right) {
        return new Point(x - right.x, y - right.y);
    }

    /** ＝ 演算子の多重定義 */
    public final Point operatorLet(Point right) {
        setPoint(right.x, right.y);
        return this;
    }

    /** += 演算子の多重定義 */
    public final Point operatorPlusLet(Point right) {
        addPoint(right);
        return this;
    }

    /** -= 演算子の多重定義 */
    public final Point operatorMinusLet(Point right) {
        subPoint(right);
        return this;
    }

    /** () 演算子の多重定義 */
    public final Point operatorFunction(int TempX, int TempY) {
        setPoint(TempX, TempY);
        return this;
    }

    /** < 演算子の多重定義 */
    public boolean operatorLessThan(Point right) {
        if (x < right.x && y < right.y) {
            return true;
        }
        return false;
    }

    /** > 演算子の多重定義 */
    public boolean operatorGreaterThan(Point right) {
        if (x > right.x && y > right.y) {
            return true;
        }
        return false;
    }

    /** ==演算子の多重定義 */
    public boolean operatorEqual(Point right) {
        if (x == right.x && y == right.y) {
            return true;
        }
        return false;
    }

    /** != 演算子の多重定義 */
    public boolean operatorNotEqual(Point right) {
        if (x != right.x || y != right.y) {
            return true;
        }
        return false;
    }

    /** <= 演算子の多重定義 */
    public boolean operatorLessEqual(Point right) {
        if (x <= right.x && y <= right.y) {
            return true;
        }
        return false;
    }

    /** >= 演算子の多重定義 */
    public boolean operatorGreaterEqual(Point right) {
        if (x >= right.x && y >= right.y) {
            return true;
        }
        return false;
    }

    /**  */
    public int x;
    /**  */
    public int y;

    /** ポイントのセット関数 */
    private void setPoint(Point temp) {
        x = temp.x;
        y = temp.y;
    }

    /** ポイントのセット関数 */
    private void setPoint(int tempX, int tempY) {
        x = tempX;
        y = tempY;
    }

    /** ポイントをたす関数 */
    private void addPoint(Point temp) {
        x += temp.x;
        y += temp.y;
    }

    /** ポイントを引く関数 */
    private void subPoint(Point temp) {
        x -= temp.x;
        y -= temp.y;
    }
}
