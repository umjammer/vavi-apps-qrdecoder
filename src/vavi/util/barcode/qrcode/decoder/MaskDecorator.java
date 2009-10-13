/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode.decoder;


/**
 * ���Υ��饹�ϡ�BinaryImage�Υǡ����ξ�ˤ��֤���ե��륿�Ȥ��Ƥ�
 * ��ǽ������ޤ������򤵤줿�ޥ��������ɤˤ�äơ�BinaryImage���
 * �ǡ�����ޥ������ڥ졼�����ˤ�ä��Ѳ������ޤ���
 *
 * @version	�������� 2002/12/16(Mon) �и�ë ����ϯ
 */
class MaskDecorator {
    /**
     * �ޥ����ѥ��������� (NOTMASKED �ʳ��ϻȤ�����ʤ�����ɤ�)
     */
    enum Mask {
        PATTERN0,
        PATTERN1,
        PATTERN2,
        PATTERN3,
        PATTERN4,
        PATTERN5,
        PATTERN6,
        PATTERN7,
        NOTMASKED
    }

    /** */
    public MaskDecorator() {
        image = null;
    }

    /**
     * ���˥���饤�������᡼���ؤλ��Ȥ򥻥åȤ��롣
     */
    void Initialize(BinaryImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image does not initialized");
        }
        this.image = image;
    }

    /**
     * x, y ��ɽ�������֤Υԥ������ {@link Mask} ��ɽ�����ѥ�����ǥե��륿��󥰤���
     * �֤��ؿ���{@link Mask#NOTMASKED} �����򤵤�Ƥ�������ǡ���������Ǥ��롣
     */
    boolean GetMaskedPixel(final int x, final int y, final Mask mc /* = NOTMASKED */) {
        if (x > image.getMaxCol() || y > image.getMaxRow()) {
            throw new IllegalArgumentException("Index over flow");
        }
        boolean pixel = image.getPixel(x, y);
        // ���������⥸�塼��˥ޥ��������򤷤��֤���
        switch (mc) {
            // ���ͤǤ� (x + y) % 2 = 0 �����ˤʤ�Ȥ�ȿž�Ȥ��뤬��! �黻�Ҥ�Ȥ������ʤ��Τ�
            // ��Ｐ�����ΤȤ� pixel �򤽤Τޤ��֤������λ� pixel ��ȿž�������֤���
        case PATTERN0:
            return ((x + y) % 2) != 0 ? pixel : !pixel;
        case PATTERN1:
            return (y % 2) != 0 ? pixel : !pixel;
        case PATTERN2:
            return (x % 3) != 0 ? pixel : !pixel;
        case PATTERN3:
            return ((x + y) % 3) != 0 ? pixel : !pixel;
        case PATTERN4:
            return (((x / 3) + (y / 2)) % 2) != 0 ? pixel : !pixel;
        case PATTERN5:
            return ((x * y) % 2 + (x * y) % 3) != 0 ? pixel : !pixel;
        case PATTERN6:
            return (((x * y) % 2 + (x * y) % 3) % 2) != 0 ? pixel : !pixel;
        case PATTERN7:
            return (((x + y) % 3 + (x + y) % 2) % 2) != 0 ? pixel : !pixel;
        default	:
            return pixel;
        }
    }

    /**
     * x, y ��ɽ�������֤Υԥ������ {@link Mask} ��ɽ�����ѥ�����ǥޥ����������
     * �֤��ؿ���{@link Mask#NOTMASKED} �����򤵤�Ƥ�������ǡ���������Ǥ��롣
     */
    boolean GetUnMaskedPixel(final int x, final int y, final Mask mc /* = NOTMASKED */) {
        if (x > image.getMaxCol() || y > image.getMaxRow()) {
            throw new IllegalArgumentException("Index over flow");
        }
        boolean pixel = image.getPixel(x, y);
        // �ޥ����������٤�����ȿž�����Ƥ�����
        pixel = !pixel;
        // ���������⥸�塼��˥ޥ��������򤷤��֤���
        switch (mc) {
            // ���ͤǤ�(x + y) % 2 = 0�����ˤʤ�Ȥ�ȿž�Ȥ��뤬��!�黻�Ҥ�Ȥ������ʤ��Τ�
            // ��Ｐ�����ΤȤ�pixel�򤽤Τޤ��֤������λ�pixel��ȿž�������֤���
        case PATTERN0:
            return ((x + y) % 2) != 0 ? pixel : pixel;
        case PATTERN1:
            return (y % 2) != 0 ? pixel : !pixel;
        case PATTERN2:
            return (x % 3) != 0 ? pixel : !pixel;
        case PATTERN3:
            return ((x + y) % 3) != 0 ? pixel : !pixel;
        case PATTERN4:
            return (((x / 3) + (y / 2)) % 2) != 0 ? pixel : !pixel;
        case PATTERN5:
            return ((x * y) % 2 + (x * y) % 3) != 0 ? pixel : !pixel;
        case PATTERN6:
            return (((x * y) % 2 + (x * y) % 3) % 2) != 0 ? pixel : !pixel;
        case PATTERN7:
            return (((x + y) % 3 + (x + y) % 2) % 2) != 0 ? pixel : !pixel;
        default	:
            return pixel;
        }
    }
    
    /** ��Ϣ�դ����줿���� */
    private BinaryImage image;
}

/* */
