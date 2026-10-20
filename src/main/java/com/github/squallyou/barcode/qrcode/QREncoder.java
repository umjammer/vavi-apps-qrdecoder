/*
 * Copyright (c) 2002-2003 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode;

import java.io.IOException;


/**
 * This class generates and returns a QR code from the received string.
 * The CFileReader for each table is declared externally, but its lifetime must
 * be longer than that of CQREncoder.
 *
 * @version Created 2003/02/25(Tue) by Kentaro Ishitoya
 */
class QREncoder {

    enum CharMode {
        NUMERIC(1),
        ALPHA(2),
        ASCII(4),
        KANJI(8);
        final int value;

        CharMode(int value) {
            this.value = value;
        }

        int getValue() {
            return value;
        }
    }

    /** Function to initialize the encoder */
    public void initialize(String pfr, FileReader pow, String vfr, FileReader ver, String efr, FileReader exp) throws IOException {
        pTable.initialize(pfr, pow);
        vTable.initialize(vfr, ver);
        eTable.initialize(efr, exp);
    }

    /** Method that actually generates the QRCode */
    public BinaryImage execute(String str, CharMode mode, VersionTable.ErrorCollectionLevel ecl, int ver, boolean quiet) {

        CharacterCodeConverter con = new AsciiCodeConverter();
        Symbol sym = vTable.getSymbol(ver, ecl);
        DataCodeWord codeword = new DataCodeWord();
        Message msg = new Message();
        QRCodeImage image = new QRCodeImage();
        MaskSelector sel = new MaskSelector();
        ECCodeGenerator ecgen = new ECCodeGenerator();
        QRFormInfo fi = new QRFormInfo();
        QRVersionInfo vi = new QRVersionInfo();
        int cclength = 0;
        int unit = 0;

        fi.initialize(sym, MaskDecorator.Mask.NOTMASKED);
        vi.initialize(sym);
        // Select mode indicator and converter class
        switch (mode) {
            case NUMERIC:
                cclength = sym.getCharCountNumeric();
                // TODO Not yet implemented
                break;
            case ALPHA:
                cclength = sym.getCharCountAlpha();
                // TODO Not yet implemented
                break;
            case ASCII:
                cclength = sym.getCharCountAscii();
                unit = 8;
                con = new AsciiCodeConverter();
                break;
            case KANJI:
                cclength = sym.getCharCountKanji();
                // TODO Not yet implemented
                break;
        }

        // Convert message
        BinaryString code = con.convert(str);

        // Build message
        codeword.setDataCodeWord(new BinaryString(mode.getValue(), 4), new BinaryString(code.GetLength() / unit, cclength), code, sym);
        msg.setDataCodeWord(codeword, sym);

        // Store error correction codewords
        for (int i = 0; i < msg.getNumberOfBlocks(); i++) {
            msg.addECBlock(ecgen.execute(msg.getECLength(i), msg.getRSLength(i), msg.getRSBlockAt(i), pTable, eTable));
        }

        // Set various information
        image.setMode(QRCodeImage.Mode.ENCODER);
        image.setMessage(msg, sym);
        MaskDecorator.Mask mask = sel.rateMask(image);
        image.setMaskCode(mask);
        fi.initialize(sym, mask);
        image.setFormInfo(fi.getFormInfo());
        if (ver >= 7) {
            image.setVersionInfo(vi.getVersionInfo());
        }
        if (quiet) {
            BinaryImage ret = new BinaryImage();
            BinaryImage temp = image.getQRCodeImage();
            ret.initialize(temp.getMaxRow() + 8, temp.getMaxCol() + 8, false);
            return ret.or(4, 4, temp);
        } else {
            return image.getQRCodeImage();
        }
    }

    /** */
    private final PowerTable pTable = new PowerTable();
    /** */
    private final VersionTable vTable = new VersionTable();
    /** */
    private final ExpressionTable eTable = new ExpressionTable();
}
