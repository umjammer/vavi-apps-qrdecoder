/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode;


/**
 * This class generates version information from the received symbol.
 *
 * @version Created 2003/02/24(Mon) by Kentaro Ishitoya
 *          Modified 2003/02/27(Tue) by Kentaro Ishitoya
 */
class QRVersionInfo {

    /** */
    final String VERSION_BCHG = "1111100100101";

    /** Constructor */
    public QRVersionInfo() {
        version = 0;
    }

    /** Method to create and return version information */
    public void initialize(Symbol symbol) {
        if (symbol == null) {
            throw new IllegalArgumentException("invalid ECL");
        }

        version = symbol.getVersion();

        if (version < 7) {
            versionInfo = new BinaryString("000000000000000000");
            return;
        }

        if (versionInfo != null) {
            versionInfo.clear();
        }

        BinaryString code = new BinaryString(version, 6);
        BinaryString ec;
        BinaryString pad = new BinaryString();
        BCHECCodeGenerator gen = new BCHECCodeGenerator();

        // Generate error correction codeword (adding 12 powers to code)
        ec = gen.execute(code.add(new BinaryString("000000000000")), new BinaryString(VERSION_BCHG));

        // If the returned code is less than 12 bits, pad with 0 at the front.
        if (ec.GetLength() < 12) {
            for (int i = 0; i < 12 - ec.GetLength(); i++) {
                pad.add(false);
            }
            ec = pad.operatorPlus(ec);
        }

        versionInfo = code.operatorPlus(ec);
    }

    /** Get version information */
    public BinaryString getVersionInfo() {
        return versionInfo;
    }

    // For decoder

    /** Decode the String with version information set */
    public void initialize(BinaryString String) {
        // TODO : Add specific processing at this position.
    }

    /** Get version */
    public int getVersion() {
        return version;
    }

    /** */
    private int version;

    /** */
    private BinaryString versionInfo;
}
