/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode.decoder;


/**
 * ���Υ��饹��CQRCodeImage�ˤ�����줿�ޥ����ˤĤ��Ƽ�����׻�����
 * ���ּ����ξ��ʤ��ޥ�������Ҥ�unsigned int���֤���
 *
 * @version 2002/02/14(Fri)	�и�ë������ϯ
 */
class MaskSelector {
    final static int WEIGHT_Adjacent = 3;
    final static int WEIGHT_Block = 3;
    final static int WEIGHT_Pattern = 40;
    final static int WEIGHT_Dark = 10;

    /** ���󥹥ȥ饯�� */
    public MaskSelector() {}

    /** �Ϥ��줿 QRCodeImage �˰���Ŭ�ڤʥޥ�������Ҥ��֤� */
    public MaskDecorator.Mask rateMask(QRCodeImage target) {
        int rate;
        int least = 0;
        MaskDecorator.Mask index = null;
        BinaryImage temp;

        // ���줾��Υѥ�����ˤĤ��ƺ�����Ԥ���
        for (MaskDecorator.Mask i : MaskDecorator.Mask.values()) {
            rate = 0;
            temp = target.getImage(i);
            rate += rateMaskAdjacent(temp);
            rate += rateMaskBlock(temp);
            rate += rateMaskPattern(temp);
            rate += rateMaskDark(temp);
            if (rate < least || i == MaskDecorator.Mask.PATTERN0) {
                least = rate;
                index = i;
            }
            System.out.println(i +  "        :");
            System.out.println("Adjacent : " + rateMaskAdjacent(temp));
            System.out.println("Block    : " + rateMaskBlock(temp));
            System.out.println("Pattern  : " + rateMaskPattern(temp));
            System.out.println("Dark     : " + rateMaskDark(temp));
            System.out.println("Rate     : " + rate);
            System.out.println();
        }
        return index;
    }

    /** Ʊ�������ܤ���⥸�塼��ˤĤ��Ƽ�����׻�����ؿ� */
    private int rateMaskAdjacent(BinaryImage image) {
        final int row = image.getMaxRow();
        final int col = image.getMaxCol();
        boolean module;
        int adjacent;
        int rate = 0;
        for (int y = 0; y < row; y++) {
            adjacent = 0;
            for (int x = 0; x < row; x++) {
                module = image.getPixel(x, y);

                if (x + 1 >= row) {
                    continue;
                }

                if (image.getPixel(x + 1, y) != module) {
                    if (adjacent > 5) {
                        rate += adjacent - 5;
                    }
                    adjacent = 0;
                }
                adjacent++;
            }
        }
        for (int x = 0; x < row; x++) {
            adjacent = 0;
            for (int y = 0; y < col; y++) {
                module = image.getPixel(x, y);

                if (y + 1 >= col) {
                    continue;
                }

                if (image.getPixel(x, y + 1) != module) {
                    if (adjacent > 5) {
                        rate += adjacent - 5;
                    }
                    adjacent = 0;
                }
                adjacent++;
            }
        }
        return rate;
    }

    /** Ʊ���Υ⥸�塼��֥�å��ˤĤ��Ƽ�����׻����롣 */
    private int rateMaskBlock(BinaryImage image) {
        final int row = image.getMaxRow();
        final int col = image.getMaxCol();
        BinaryImage temp = new BinaryImage();
        temp.initialize(row, col, false);
        BinaryImage bimg = new BinaryImage();
        bimg.initialize(2, 2, true);
        boolean module;
        int block = 0;
        for (int x = 0; x < row; x++) {
            for (int y = 0; y < col; y++) {
                if (temp.getPixel(x, y)) {
                    continue;
                } //�⤦���Ǥ�ɾ������Ƥ����顣

                module = image.getPixel(x, y);
                if (!module) {
                    continue;
                }

                if (x + 1 >= col || y + 1 >= row) {
                    continue;
                }		// ���ֱ����Υԥ����뤫��
                if (image.getPixel(x + 1, y) != module) {
                    continue;
                }		// ����
                if (image.getPixel(x, y + 1) != module) {
                    continue;
                }		// ��
                if (image.getPixel(x + 1, y + 1) != module) {
                    continue;
                }		// ����
                // ����Ʊ���ʤ鼺����û���
                block++;
                temp.or(x, y, bimg);
            }
        }
        return WEIGHT_Block * block;
    }

    /** 1��1��3��1��1��Ψ�Υ⥸�塼��ѥ�����ˤĤ��Ƽ�����׻����롣 */
    private int rateMaskPattern(BinaryImage image) {
        final int row = image.getMaxRow();
        final int col = image.getMaxCol();

//      boolean module;
        int rate = 0;
        // �������ؤ�����
        for (int y = 0; y < col; y++) {
            for (int x = 0; x < row; x++) {
                if (image.getPixel(x, y) && (x + 6) < row) {
                    // 1:1:3:1:1�ʰ�:��:��:��:�šˤΥԥ��������õ��
                    // image.GetPixel() == true ���� ==false ����

                    if (x != 0) {
                        if (image.getPixel(x - 1, y)) {
                            continue;
                        } // ��������ť⥸�塼�뤫
                    }
                    if ( image.getPixel(x + 1, y)) {
                        continue;
                    } // ���⥸�塼�뤫
                    if (!image.getPixel(x + 2, y)) {
                        continue;
                    } // �ť⥸�塼�뤫
                    if (!image.getPixel(x + 3, y)) {
                        continue;
                    } // �ť⥸�塼�뤫
                    if (!image.getPixel(x + 4, y)) {
                        continue;
                    } // �ť⥸�塼�뤫
                    if ( image.getPixel(x + 5, y)) {
                        continue;
                    } // ���⥸�塼�뤫
                    if (!image.getPixel(x + 6, y)) {
                        continue;
                    } // �ť⥸�塼�뤫
                    if (x + 7 != row) {
                        if (image.getPixel(x + 7, y)) {
                            continue;
                        } // ��ĸ夬�ť⥸�塼�뤫
                    }
                    rate += WEIGHT_Pattern;
                }
            }
        }
        // �������ؤ�����
        for (int x = 0; x < row; x++) {
            for (int y = 0; y < col; y++) {
                if (image.getPixel(x, y) && (y + 6) < col) {
                    // 1:1:3:1:1�ʰ�:��:��:��:�šˤΥԥ��������õ��
                    // image.getPixel() == true ���� ==false ����

                    if (y != 0) {
                        if (image.getPixel(x, y - 1)) {
                            continue;
                        }	// ����������⥸�塼��
                    }
                    if ( image.getPixel(x, y + 1)) {
                        continue;
                    }	// ���⥸�塼�뤫
                    if (!image.getPixel(x, y + 2)) {
                        continue;
                    }	// �ť⥸�塼�뤫
                    if (!image.getPixel(x, y + 3)) {
                        continue;
                    }	// �ť⥸�塼�뤫
                    if (!image.getPixel(x, y + 4)) {
                        continue;
                    }	// �ť⥸�塼�뤫
                    if ( image.getPixel(x, y + 5)) {
                        continue;
                    }	// ���⥸�塼�뤫
                    if (!image.getPixel(x, y + 6)) {
                        continue;
                    }	// �ť⥸�塼�뤫
                    if (y + 7 != col) {
                        if (image.getPixel(x, y + 7)) {
                            continue;
                        }	// ��ĸ夬�ť⥸�塼��
                    }
                    rate += WEIGHT_Pattern;
                }
            }
        }
        return rate;
    }

    /** ���Τ��Ф���ť⥸�塼�����Ψ�ˤĤ��Ƽ�����׻����롣 */
    private int rateMaskDark(BinaryImage image) {
        final int row = image.getMaxRow();
        final int col = image.getMaxCol();

        int modules = 0;
        // �������ؤ�����
        for (int y = 0; y < col; y++) {
            for (int x = 0; x < row; x++) {
                if (!image.getPixel(x, y)) {
                    modules++;
                }
            }
        }
        return (int) (Math.abs(((double) modules / (double) (row * col) * 100.0 - 50.0) / 5.0) * WEIGHT_Dark);
    }
}

/* */
