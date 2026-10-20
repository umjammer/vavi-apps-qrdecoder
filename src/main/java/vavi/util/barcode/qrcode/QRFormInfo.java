/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode;


/**
 * This class generates format information from the received symbol and mask pattern.
 *
 * @version Created 2003/02/24(Mon) by Kentaro Ishitoya
 *          Modified 2003/02/27(Tue) by Kentaro Ishitoya
 */
class QRFormInfo {

    final static String FORM_BCHG = "10100110111";
    final static String FORM_MASK = "101010000010010";

    /** Constructor */
    public QRFormInfo() {
        errorCollectionLevel = VersionTable.ErrorCollectionLevel.L;
        maskCode = MaskDecorator.Mask.PATTERN0;
    }

    // Encoder

    /** Function to create and return format information. */
    public void initialize(Symbol sym, MaskDecorator.Mask pattern) {
        if (sym == null) {
            throw new IllegalArgumentException("invalid ErrorCollectionLevel");
        }
        formInfo = new BinaryString();

        errorCollectionLevel = sym.getErrorCollectionLevel();
        maskCode = pattern;

        String codeStr = switch (errorCollectionLevel) {
            case L -> "01";
            case M -> "00";
            case Q -> "11";
            case H -> "10";
        };

        String maskStr = switch (maskCode) {
            case PATTERN0 -> "000";
            case PATTERN1 -> "001";
            case PATTERN2 -> "010";
            case PATTERN3 -> "011";
            case PATTERN4 -> "100";
            case PATTERN5 -> "101";
            case PATTERN6 -> "110";
            case PATTERN7 -> "111";
            default -> "000";
        };

        String format5 = codeStr + maskStr;
        BinaryString code = new BinaryString(format5);

        if (format5.equals("00000")) {
            formInfo = new BinaryString(FORM_MASK);
            return;
        }

        // Generate error correction codeword (adding 10 powers to code)
        BCHECCodeGenerator gen = new BCHECCodeGenerator();
        BinaryString ec = gen.execute(new BinaryString(format5 + "0000000000"), new BinaryString(FORM_BCHG));

        // If the returned code is less than 10 bits, pad with 0 at the front.
        BinaryString pad = new BinaryString();
        if (ec.GetLength() < 10) {
            for (int i = 0; i < 10 - ec.GetLength(); i++) {
                pad.append(false);
            }
            ec = pad.append(ec);
        }

        BinaryString full15 = new BinaryString(code).append(ec);
        BinaryString mask = new BinaryString(FORM_MASK);
        formInfo = full15.xor(mask);
    }

    /** Get format information */
    public BinaryString getFormInfo() {
        return formInfo;
    }

    // Decoder

    /** Read and decode the BinaryString containing format information. */
    public void initialize(BinaryString str) {
        // TODO : Add specific processing at this position.
        formInfo = str;
        BinaryString mask = new BinaryString(FORM_MASK);
        BinaryString unmasked = str.xor(mask);

        // ECL (bits 0..1)
        boolean bit0 = unmasked.at(0);
        boolean bit1 = unmasked.at(1);
        if (!bit0 && bit1) {
            errorCollectionLevel = VersionTable.ErrorCollectionLevel.L; // "01"
        } else if (!bit0 && !bit1) {
            errorCollectionLevel = VersionTable.ErrorCollectionLevel.M; // "00"
        } else if (bit0 && bit1) {
            errorCollectionLevel = VersionTable.ErrorCollectionLevel.Q; // "11"
        } else {
            errorCollectionLevel = VersionTable.ErrorCollectionLevel.H; // "10"
        }

        // Mask (bits 2..4)
        int patternIndex = (unmasked.at(2) ? 4 : 0) | (unmasked.at(3) ? 2 : 0) | (unmasked.at(4) ? 1 : 0);
        maskCode = MaskDecorator.Mask.values()[patternIndex];
    }

    /** Get error correction level */
    public VersionTable.ErrorCollectionLevel getECL() {
        return errorCollectionLevel;
    }

    /** Get mask code */
    public MaskDecorator.Mask getMaskCode() {
        return maskCode;
    }

    /** */
    private VersionTable.ErrorCollectionLevel errorCollectionLevel;
    /** */
    private MaskDecorator.Mask maskCode;
    /** */
    private BinaryString formInfo = new BinaryString();
}
