/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode;

import java.io.IOException;


/**
 * このクラスはファイルから読み込んだデータにアクセスする為のインタ
 * フェイスを導出クラスに提供する為の抽象基底クラスです。
 *
 * @version 新規作成 2002/11/13(Wed) 石戸谷　顕太朗
 */
abstract class Table {

    public Table() {
    }

    abstract boolean initialize(String fileName, FileReader file) throws IOException;

    protected FileReader file;
}
