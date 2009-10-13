/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode.decoder;

import java.util.List;


/**
 * ���Υ��饹�ϡ�
 * Message �� BinaryImage, BinaryImage -> Message ������Ѵ���Ԥʤ�
 * ���饹�Ǥ���
 * Message -> BinaryImage �Ѵ��ϰʲ��Τ褦�˹Ԥʤ��ޤ���
 * Message��BinaryImage �����֢��ޥ���������
 * BinaryImage->Message �Ѵ��ϰʲ��Τ褦�˹Ԥʤ��ޤ���
 * BinaryImage ���ɤ߹���->�ޥ������������
 * Mode �ΰ㤦�ؿ���ƤӽФ����㳰�����Ф��ޤ���
 *
 * @version �������� 2002/12/16(Mon) �и�ë����ϯ
 */
class QRCodeImage {
    /** */
    enum Mode {
        ENCODER,
        DECODER
    }

    /** */
    enum FormInfo {
        FORMINFO1,
        FORMINFO2
    }

    /** */
    public QRCodeImage() {
    }
    
    // Encoder�ǻȤ��ؿ��� (Encoder�⡼�ɤǤ����Ȥ��ʤ�)

    /** ��å������Υ��å� (Encoder) */
    public void setMessage(final Message msg, final Symbol sym) {
        if (mode != Mode.ENCODER) {
            throw new IllegalArgumentException("Wrong Mode");
        }
        if (msg == null) {
            throw new IllegalArgumentException("Message is not initialized");
        }
        if (sym == null) {
            throw new IllegalArgumentException("Symbol is not initialized");
        }
        modulesPerSide = sym.getModulesPerSide();
        image.initialize(modulesPerSide, modulesPerSide, false);
        data.initialize(modulesPerSide, modulesPerSide, false);
        function.initialize(modulesPerSide, modulesPerSide, false);
        system.initialize(modulesPerSide, modulesPerSide, false);
        positioning.initialize(modulesPerSide, modulesPerSide, false);
        mask.Initialize(data);
        masked = false;

        formatFunctionLayer(sym);
        Arrange(msg.getData(), sym);
    }
    
    /** ���־�������� (Encoder) */
    public void setVersionInfo(final BinaryString info) {
        if (mode != Mode.ENCODER) {
            throw new IllegalArgumentException("Wrong Mode");
        }
        if (info == null) {
            throw new IllegalArgumentException("Info is empty");
        }
        if (info.GetLength() != 18) {
            throw new IllegalArgumentException("invalid info length");
        }
        if (modulesPerSide <= 41) {
            throw new IllegalArgumentException("This version does not need VerInfo");
        }
        // �����η��־��������
        final int sx = modulesPerSide - 11;
        for (int x = sx; x < sx + 3; x++) {
            for (int y = 0; y < 6; y++) {
                system.putPixel(x, y, info.at(y * 3 + x - sx));
            }
        }
        // ����η��־��������
        final int sy = modulesPerSide - 11;
        for (int x = 0; x < 6; x++) {
            for (int y = sy; y < sy + 3; y++) {
                system.putPixel(x, y, info.at(x * 3 + y - sy));
            }
        }
    }

    /** ������������� (Encoder) */
    public void setFormInfo(final BinaryString info) {
        if (mode != Mode.ENCODER) {
            throw new IllegalArgumentException("Wrong Mode");
        }
        if (info == null) {
            throw new IllegalArgumentException("Info is empty");
        }
        if (info.GetLength() != 15) {
            throw new IllegalArgumentException("invalid info length");
        }

        // ����η������������
        system.putPixel(8, 0, info.at(14));
        system.putPixel(8, 1, info.at(13));
        system.putPixel(8, 2, info.at(12));
        system.putPixel(8, 3, info.at(11));
        system.putPixel(8, 4, info.at(10));
        system.putPixel(8, 5, info.at(9));
        system.putPixel(8, 7, info.at(8));
        system.putPixel(8, 8, info.at(7));
        system.putPixel(7, 8, info.at(6));
        system.putPixel(5, 8, info.at(5));
        system.putPixel(4, 8, info.at(4));
        system.putPixel(3, 8, info.at(3));
        system.putPixel(2, 8, info.at(2));
        system.putPixel(1, 8, info.at(1));
        system.putPixel(0, 8, info.at(0));

        int sx = modulesPerSide - 1;
        int sy = modulesPerSide - 1 - 6;
        // ���夫�麸���ˤ����Ƥη������������
        system.putPixel(sx , 8, info.at(14));
        system.putPixel(sx - 1, 8, info.at(13));
        system.putPixel(sx - 2, 8, info.at(12));
        system.putPixel(sx - 3, 8, info.at(11));
        system.putPixel(sx - 4, 8, info.at(10));
        system.putPixel(sx - 5, 8, info.at(9));
        system.putPixel(sx - 6, 8, info.at(8));
        system.putPixel(sx - 7, 8, info.at(7));
        system.putPixel(8, sy , info.at(6));
        system.putPixel(8, sy + 1, info.at(5));
        system.putPixel(8, sy + 2, info.at(4));
        system.putPixel(8, sy + 3, info.at(3));
        system.putPixel(8, sy + 4, info.at(2));
        system.putPixel(8, sy + 5, info.at(1));
        system.putPixel(8, sy + 6, info.at(0));
    }
    
    /** �ޥ��������ɤ����� (Encoder) */
    public void setMaskCode(final MaskDecorator.Mask mc) {
        if (mode != Mode.ENCODER) {
            throw new IllegalArgumentException("Wrong Mode");
        }
        maskCode = mc;
    }
    
    /** �����μ��� (Encoder) */
    public BinaryImage getImage(MaskDecorator.Mask mc) {
        if (mode != Mode.ENCODER) {
            throw new IllegalArgumentException("Wrong Mode");
        }
        BinaryImage temp = null; 
        temp.initialize(modulesPerSide, modulesPerSide, false);
        for (int x = 0; x < modulesPerSide; x++) {
            for (int y = 0; y < modulesPerSide; y++) {
                if (positioning.getPixel(x, y)) {
                    temp.putPixel(x, y, mask.GetMaskedPixel(x, y, mc));
                }
            }
        }
        return temp;
    }
    
    /**
     * Decoder�ǻȤ��ؿ��� (Decoder�⡼�ɤǤ����Ȥ��ʤ�)
     * ���������� (Decoder)
     * @param img
     * @param sym
     */
    public void setImage(final BinaryImage img, final Symbol sym) {
        if (mode != Mode.DECODER) {
            throw new IllegalArgumentException("Wrong Mode");
        }
        if (img == null) {
            throw new IllegalArgumentException("Image is empty");
        }
        if (sym == null) {
            throw new IllegalArgumentException("Symbol is empty");
        }
        modulesPerSide = sym.getModulesPerSide();
        image = img;
        formatFunctionLayer(sym);

        masked = true;
        
    }

    /** ��������μ��� (Decoder) */
    public BinaryString getFormInfo(final FormInfo which) {
        if (mode != Mode.DECODER) {
            throw new IllegalArgumentException("Wrong Mode");
        }

        BinaryString info = null;
        if (which == FormInfo.FORMINFO1) {
            //����η�������μ���
            info.add(image.getPixel(8, 0));
            info.add(image.getPixel(8, 1));
            info.add(image.getPixel(8, 2));
            info.add(image.getPixel(8, 3));
            info.add(image.getPixel(8, 4));
            info.add(image.getPixel(8, 5));
            info.add(image.getPixel(8, 7));
            info.add(image.getPixel(8, 8));
            info.add(image.getPixel(7, 8));
            info.add(image.getPixel(5, 8));
            info.add(image.getPixel(4, 8));
            info.add(image.getPixel(3, 8));
            info.add(image.getPixel(2, 8));
            info.add(image.getPixel(1, 8));
            info.add(image.getPixel(0, 8));

        } else if (which == FormInfo.FORMINFO2) {
            //���夫�麸���ˤ����Ƥη�������μ���
             int sx = modulesPerSide ;
             int sy = modulesPerSide - 7;
            info.add(image.getPixel(sx , 8));
            info.add(image.getPixel(sx - 1, 8));
            info.add(image.getPixel(sx - 2, 8));
            info.add(image.getPixel(sx - 3, 8));
            info.add(image.getPixel(sx - 4, 8));
            info.add(image.getPixel(sx - 5, 8));
            info.add(image.getPixel(sx - 6, 8));
            info.add(image.getPixel(sx - 7, 8));
            info.add(image.getPixel(8, sy));
            info.add(image.getPixel(8, sy + 1));
            info.add(image.getPixel(8, sy + 2));
            info.add(image.getPixel(8, sy + 3));
            info.add(image.getPixel(8, sy + 4));
            info.add(image.getPixel(8, sy + 5));
            info.add(image.getPixel(8, sy + 6));
        }
        return info;
        
    }

    /** �ޥ�������ѤΥ��������� (Decorder) */
    public void setUnMaskCode(final MaskDecorator.Mask mc) {
        if (mode != Mode.DECODER) {
            throw new IllegalArgumentException("Wrong Mode");
        }
        maskCode = mc;
        unMask(mc);
        
    }
    
    /** */
    public BinaryString getMessage(final Symbol sym) {
        
        if (mode != Mode.DECODER) {
            throw new IllegalArgumentException("Wrong Mode");
        }
        if (!masked) {
            throw new IllegalArgumentException("This Image was not UnMasked");
        }
        if (sym.isPartial()) {
            throw new IllegalArgumentException("This Method needs compleate symbol");
        }
        boolean isup = true;	// �岼�ե饰
        boolean isleft = true;	// �����ե饰
        int mleft = 0;		// ��Ϣ³��ư
        int x = modulesPerSide - 1;
        int y = modulesPerSide - 1;
        int tx;
        int ty;
        BinaryString str = new BinaryString();
        for ( int i = 0; i < str.GetLength(); i++) {
            str.add(image.getPixel(x, y) ? new BinaryString("1") : new BinaryString("0"));
            tx = x;
            ty = y;
            while (true) {
                //����ư
                if (isleft) {
                    if (tx > 0) {
                        tx -= 1;
                    }
                    //���־�ޤ��ϲ��ޤǹԤäƺ��˰�ư�������
                    if (mleft > 0 && mleft < 4) {
                        mleft++;
                        isleft = true;
                    } else {
                        mleft = 0;
                        isleft = false;
                    }
                    //����ޤ��ϱ�����ư
                } else {
                    if (isup) {
                        if (ty > 0) {
                            ty -= 1;
                            tx += 1;
                        } else {
                            isup = false;
                            mleft = 1;
                        }
                    } else {
                        if (ty < modulesPerSide - 1) {
                            ty += 1;
                            tx += 1;
                        } else {
                            isup = true;
                            mleft = 1;
                        }
                    }
                    isleft = true;
                }
                //��ư������˥ǡ����������Ǥ��뤫Ĵ�٤�
                if (positioning.getPixel(tx, ty)) {
                    x = tx;
                    y = ty;
                    break;
                }
            }
        }
        return str;        
    }
    
    // �桼�ƥ���ƥ�

    /** �⡼�ɤ����� */
    public void setMode(final Mode m) {
        mode = m;
    }

    /** QR�����ɥ��᡼���μ��� */
    public BinaryImage getQRCodeImage() {
        BinaryImage temp;
        if (mode == Mode.ENCODER) {
            temp = getImage(maskCode);
            temp = temp.or(function);
            temp = temp.or(system);
        } else {
            temp = image;
        }
        return temp;
    }

    /** �⡼�ɤ����� */
    public final MaskDecorator.Mask getMaskCode() {
        return maskCode;
    }
    
    /** ��å����������� (Encoder) */
    private void Arrange(final BinaryString str, final Symbol sym) {
        if (mode != Mode.ENCODER) {
            throw new IllegalArgumentException("Wrong Mode");
        }
       if (str.GetLength() != sym.getWholeCodeWords() * 8) {
            throw new IllegalArgumentException("Invalid Data");
        }
        boolean isup = true;	// �岼�ե饰
        boolean isleft = true;	// �����ե饰
        int x = modulesPerSide - 1;
        int y = modulesPerSide - 1;
        int tx;
        int ty;
        int m = 0;
        while (m < str.GetLength()) {
            data.putPixel(x, y, str.at(m));
            tx = x;
            ty = y;
            while (true) {
                // �����ߥ󥰥ѥ������������Ф���
                if (tx == 6) {
                    tx--;
                }
                // ����ư
                if (isleft) {
                    if (tx > 0) {
                        tx -= 1;
                    }
                    isleft = false;
                    // ����ޤ��ϱ�����ư
                } else {
                    if (isup) {
                        if (ty > 0) {
                            ty -= 1;
                            tx += 1;
                        } else {
                            isup = false;
                            if (tx > 0) {
                                tx -= 1;
                            }
                        }
                    } else {
                        if (ty < modulesPerSide - 1) {
                            ty += 1;
                            tx += 1;
                        } else {
                            isup = true;
                            if (tx > 0) {
                                tx -= 1;
                            }
                        }
                    }
                    isleft = true;
                }
                // ��ư������˥ǡ��������֤Ǥ��뤫Ĵ�٤�
                if (positioning.getPixel(tx, ty)) {
                    x = tx;
                    y = ty;
                    m++;
                    break;
                }
            }
        }
    }

    /** �ޥ������ (Decoder) */
    private void unMask(final MaskDecorator.Mask mc) {
        if (mode != Mode.DECODER) {
            throw new IllegalArgumentException("Wrong Mode");
        }
        for (int x = 0; x < modulesPerSide; x++) {
            for (int y = 0; y < modulesPerSide; y++) {
                image.putPixel(x, y, mask.GetUnMaskedPixel(x, y, mc));
            }
        }
        
    }

    /**
     * ��å������μ�����Decoder��
     * ���ֹ�碌�����ָ��С������ߥ󥰥ѥ����������ڤӡ��ݥ�����˥󥰥쥤�䡼��
     * ����
     */
    private void formatFunctionLayer(final Symbol sym) {
        // ���ָ��Хѥ�����κ���
        BinaryImage f = new BinaryImage();
        f.initialize(7, 7, true);
        f.fill(1, 1, 6, 6, false);
        f.fill(2, 2, 5, 5, true);
        // ���ֹ�碌�ѥ�����κ���
        BinaryImage p = new BinaryImage();
        p.initialize(5, 5, true);
        p.fill(1, 1, 4, 4, false);
        p.putPixel(2, 2, true);

        // ���ָ��Хѥ����������
        function = function.or(0, 0, f);
        function = function.or(modulesPerSide - 7, 0, f);
        function = function.or(0, modulesPerSide - 7, f);

        // ���ֹ�碌�ѥ����������
        List<Point> vp = sym.getApPositions();
        for (Point vpi : vp) {
            function = function.or((vpi).x - 2, (vpi).y - 2, p);
            positioning.fill((vpi).x - 2, (vpi).y - 2, (vpi).x + 3, (vpi).y + 3, true);
        }

        // �����ߥ󥰥ѥ����������
        // �ĤΥ����ߥ󥰥ѥ�����
        boolean t = true;
        for (int y = 8; y < modulesPerSide - 8; y++) {
            function.putPixel(6, y, t);
            positioning.putPixel(6, y, true);
            t = !t;
        }
        // ���Υ����ߥ󥰥ѥ�����
        t = true;
        for (int x = 8; x < modulesPerSide - 8; x++) {
            function.putPixel(x, 6, t);
            positioning.putPixel(x, 6, true);
            t = !t;
        }

        // Positioning�ΰ��ָ��Хѥ�����ʬΥ�ѥ�����ڤӷ������������
        // �ޤ�����ΰ��ָ��Хѥ�����μ���
        positioning.fill(0, 0, 9, 9, true);
        positioning.fill(modulesPerSide - 8, 0, modulesPerSide, 9, true);
        positioning.fill(0, modulesPerSide - 8, 9, modulesPerSide, true);

        // Positioning�η��־�������ꡣVersion7�ʾ�
        if (sym.getVersion() > 6) {
            positioning.fill(modulesPerSide - 10, 0, modulesPerSide - 8, 2, true);
            positioning.fill(0, modulesPerSide - 10, 2, modulesPerSide - 8, true);
        }

        // 4V+9, 8�ΰ��֤ˤ����˰ŤΥ⥸�塼�롣
        positioning.putPixel(8, sym.getVersion() * 4 + 9, true);
        function.putPixel(8, sym.getVersion() * 4 + 9, true);

        // �Ǹ��positioning��ȿž
        positioning = positioning.not();        
    }
    
    /** ���ƤΥ쥤�䡼���礷�ƻ��ĥ쥤�䡼 */
    private BinaryImage image;
    /** �ǡ�����ʬ�Τߤ���ĥ쥤�䡼 */
    private BinaryImage data;
    /** ��ǽ�⥸�塼��Τߤ���ĥ쥤�䡼 */
    private BinaryImage function;
    /** �������󡢷��־�����ĥ쥤�䡼 */
    private BinaryImage system;
    /** ���֤ΰ٤����˻��Ѥ����쥤�䡼 */
    private BinaryImage positioning;
    /** �ե��륿��󥰤��뤤�ϥ�����ե��륿��󥰤��롣 */
    private MaskDecorator mask;
    
    /** �ޥ������� */
    private MaskDecorator.Mask maskCode;
    /** ���դΥ⥸�塼��� */
    private int modulesPerSide;
    /** ��ǽ���ڤ��ؤ� */
    private Mode mode;
    /** �ޥ�������Ƥ��뤫�� */
    private boolean masked;
}

/* */
