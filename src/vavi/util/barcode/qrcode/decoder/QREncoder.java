/*
 * Copyright (c) 2002-2003 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */ 

package vavi.util.barcode.qrcode.decoder;

import java.io.IOException;


/**
 * ���Υ��饹�ϼ�����ä�ʸ���󤫤�QR�����ɤ����������֤���
 * �ƥơ��֥��CFileReader�ϡ�������������뤬������¸���֤�ɬ��
 * CQREncoder����Ĺ����뤳�ȡ�
 *
 * @version	�������� 2003/02/25(Tue) �и�ë������ϯ
 */
class QREncoder {
    enum CharMode {
        NUMERIC(1),
        ALPHA(2),
        ASCII(4),
        KANJI(8);
        int value;
        CharMode(int value) {
            this.value = value;
        }
        int getValue() {
            return value;
        }
    }

    /** ���󥹥ȥ饯�� */
    public QREncoder() {
    }

    /** ���󥳡���������������ؿ� */
    public void initialize(final String pfr, FileReader pow, final String vfr, FileReader ver, final String efr, FileReader exp) throws IOException {
        pTable.initialize(pfr, pow);
        vTable.initialize(vfr, ver);
        eTable.initialize(efr, exp);
    }

    /** �ºݤ� QRCode ����������᥽�å� */
    public BinaryImage execute(final String str, final CharMode mode, final VersionTable.ErrorCollectionLevel ecl, final int ver, final boolean quiet) {

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
        // �⡼�ɻؼ��Ҥ��Ѵ����饹������
        switch (mode) {
        case NUMERIC:
            cclength = sym.getCharCountNumeric();
            // TODO ̤����
            break;
        case ALPHA:
            cclength = sym.getCharCountAlpha();
            // TODO ̤����
            break;
        case ASCII:
            cclength = sym.getCharCountAscii();
            unit = 8;
            con = new AsciiCodeConverter();
            break;
        case KANJI:
            cclength = sym.getCharCountKanji();
            // TODO ̤����
            break;
        }

        // ��å��������Ѵ�
        BinaryString code = con.convert(str);

        // ��å������ι���
        codeword.setDataCodeWord(new BinaryString(mode.getValue(), 4), new BinaryString(code.GetLength() / unit, cclength), code, sym);
        msg.setDataCodeWord(codeword, sym);

        // ���顼������γ�Ǽ
        for (int i = 0; i < msg.getNumberOfBlocks(); i++) {
            msg.addECBlock(ecgen.execute(msg.getECLength(i), msg.getRSLength(i), msg.getRSBlockAt(i), pTable, eTable));
        }

        // �Ƽ����Υ��å�
        image.setMode(QRCodeImage.Mode.ENCODER);
        image.setMessage(msg, sym);
        image.setMaskCode(sel.rateMask(image));
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
    private PowerTable pTable = new PowerTable();
    /** */
    private VersionTable vTable = new VersionTable();
    /** */
    private ExpressionTable eTable = new ExpressionTable();
    
    //----
    
    /** */
    public static void main (String[] args) throws Exception {
        QREncoder encoder = new QREncoder();
        CSVFileReader pFile = new CSVFileReader();
        CSVFileReader vFile = new CSVFileReader();
        CSVFileReader eFile = new CSVFileReader();
        
        encoder.initialize("power.csv", vFile, "version.csv", pFile, "expression.csv", eFile);
        
        BinaryImage image = encoder.execute("test", CharMode.ASCII, VersionTable.ErrorCollectionLevel.L, 1, true);
        
        PBMImage pbm = new PBMImage();
        pbm.initialize(image);
        pbm.saveToFile("test.pbm", 1);
    }
}

/* */
