/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode.decoder;

import java.util.ArrayList;
import java.util.List;


/**
 * ���Υ��饹�ϡ�QR �����ɤΥ���ܥ��������ƻ��ĥ��饹�Ǥ���
 * ���Υ��饹�� {@link vavi.util.barcode.qrcode.decoder.VersionTable} �ˤ�äƽ��������ޤ���
 * {@link vavi.util.barcode.qrcode.decoder.VersionTable} ��𤹤��Ȱʳ��� {@link Symbol}
 * ���饹����Ѳ�ǽ�ʾ��֤ˤ��뤳�ȤϤǤ��ޤ���
 * ɬ�� {@link vavi.util.barcode.qrcode.decoder.VersionTable#formatSymbol()}
 * ��Ȥäƽ�������Ƥ���������
 *
 * @version	�������� 2002/12/08(Sun) �и�ë������ϯ
 *          �ɲ��ѹ� 2002/12/11(Wed) �и�ë������ϯ
 */
class Symbol {
    /** */
    Symbol() {
        this.id = 0;
        this.version = 0;
        this.errorCollectionLevel = VersionTable.ErrorCollectionLevel.L;
        this.modulesPerSide = 0;
        this.functionModules	= 0;
        this.versionModules = 0;
        this.otherModules = 0;
        this.wholeCodeWords = 0;
        this.remainderBits = 0;
        this.dataCodeWords = 0;
        this.dataBits = 0;
        this.numeric = 0;
        this.alphabet = 0;
        this.bytes = 0;
        this.kanji = 0;
        this.ecCodeWords = 0;
        this.blocks = 0;
        this.rsBlock1 = 0;
        this.rsBlock1WholeCodes = 0;
        this.rsBlock1DataCodeWords = 0;
        this.rsBlock1EC = 0;
        this.rsBlock2 = 0;
        this.rsBlock2WholeCodes = 0;
        this.rsBlock2DataCodeWords = 0;
        this.rsBlock2EC = 0;
        this.alignmentPatterns = 0;
        this.apPos1 = 0;
        this.apPos2 = 0;
        this.apPos3 = 0;
        this.apPos4 = 0;
        this.apPos5 = 0;
        this.apPos6 = 0;
        this.apPos7 = 0;
        this.charCountNumeric = 0;
        this.charCountAlpha = 0;
        this.charCountAscii = 0;
        this.charCountKanji = 0;
        this.partial = false;
        apPositions.clear();
    }
    
    /** �����黻�� */
    public final Symbol operatorLet(final Symbol right) {
        if (right == this) {
            return this;
        }
        if (right == null) {
            throw new IllegalArgumentException("Data is not Initialized");
        }

        id = right.id;
        version	= right.version;
        modulesPerSide = right.modulesPerSide;
        functionModules = right.functionModules;
        versionModules = right.versionModules;
        otherModules = right.otherModules;
        wholeCodeWords = right.wholeCodeWords;
        remainderBits = right.remainderBits;
        alignmentPatterns = right.alignmentPatterns;
        apPos1 = right.apPos1;
        apPos2 = right.apPos2;
        apPos3 = right.apPos3;
        apPos4 = right.apPos4;
        apPos5 = right.apPos5;
        apPos6 = right.apPos6;
        apPos7 = right.apPos7;
        charCountNumeric = right.charCountNumeric;
        charCountAlpha = right.charCountAlpha;
        charCountAscii = right.charCountAscii;
        charCountKanji = right.charCountKanji;
        apPositions.addAll(right.apPositions);
        if (!right.isPartial()) {
            errorCollectionLevel = right.errorCollectionLevel;
            dataCodeWords	= right.dataCodeWords;
            dataBits = right.dataBits;
            numeric = right.numeric;
            alphabet = right.alphabet;
            bytes = right.bytes;
            kanji = right.kanji;
            ecCodeWords	= right.ecCodeWords;
            blocks = right.blocks;
            rsBlock1 = right.rsBlock1;
            rsBlock1WholeCodes = right.rsBlock1WholeCodes;
            rsBlock1DataCodeWords = right.rsBlock1DataCodeWords;
            rsBlock1EC = right.rsBlock1EC;
            rsBlock2 = right.rsBlock2;
            rsBlock2WholeCodes	= right.rsBlock2WholeCodes;
            rsBlock2DataCodeWords = right.rsBlock2DataCodeWords;
            rsBlock2EC = right.rsBlock2EC;
        }
        partial = right.partial;
        return this;
    }

    /** id �μ����᥽�å� */
    public final int getId() {
        return id;
    }

    /** version�μ����᥽�å� */
    public final int getVersion() {
        return version;
    }

    /** ���顼������٥�μ����᥽�å� */
    public final VersionTable.ErrorCollectionLevel getErrorCollectionLevel() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return errorCollectionLevel;
    }

    /** ���դΥ⥸�塼����μ����᥽�å� */
    public final int getModulesPerSide() {
        return modulesPerSide;
    }

    /** ��ǽ�⥸�塼����μ����᥽�å� */
    public final int getFunctionModules() {
        return functionModules;
    }

    /** ���֥⥸�塼����μ����᥽�å� */
    public final int getVersionModules() {
        return versionModules;
    }

    /** ����¾�Υ⥸�塼����μ����᥽�å� */
    public final int getOtherModules() {
        return otherModules;
    }

    /** �����ɸ�����������᥽�å� */
    public final int getWholeCodeWords() {
        return wholeCodeWords;
    }

    /** ��;�����ɥӥåȿ��μ����᥽�å� */
    public final int getRemainderBits() {
        return remainderBits;
    }

    /** �ǡ��������ɸ�μ����᥽�å� */
    public final int getDataCodeWords() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return dataCodeWords;
    }

    /** �ǡ����ӥåȿ��μ����᥽�å� */
    public final int getDataBits() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return dataBits;
    }

    /** �����γ�Ǽ��ǽ���μ����᥽�å� */
    public final int getNumeric() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return numeric;
    }

    /** �Ѹ�γ�Ǽ��ǽ���μ����᥽�å� */
    public final int getAlphabet() {
        return alphabet;
    }

    /** 8 �ӥåȥХ��Ȥγ�Ǽ��ǽ�������᥽�å� */
    public final int getByte() {
        return bytes;
    }

    /** �����γ�Ǽ��ǽ�������᥽�å� */
    public final int getKanji() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return kanji;
    }

    /** ���顼������ο����֤��ؿ� */
    public final int getECCodeWords() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return ecCodeWords;
    }

    /** RS�֥�å��μ���ο����֤��ؿ� */
    public final int getBlocks() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return blocks;
    }

    /** RS�֥�å����ο����֤��ؿ� */
    public final int getRsBlock1() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return rsBlock1;
        
    }

    /** RS�֥�å����������ɿ����֤��ؿ� */
    public final int getRSBlock1WholeCodes() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return rsBlock1WholeCodes;
    }

    /** RS�֥�å��Υǡ���������������ؿ� */
    public final int getRSBlock1DataCodeWords() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return rsBlock1DataCodeWords;
    }

    /** RS1�֥�å��θ�����������������ؿ� */
    public final int getRsBlock1EC() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return rsBlock1EC;
    }

    /** RS�֥�å�2�ο����֤��ؿ� */
    public final int getRsBlock2() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return rsBlock2;
    }

    /** RS�֥�å�2�������ɿ����֤��ؿ� */
    public final int getRsBlock2WholeCodes() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return rsBlock2WholeCodes;
    }

    /** RS�֥�å�2�Υǡ���������������ؿ� */
    public final int getRsBlock2DataCodeWords() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return rsBlock2DataCodeWords;
    }

    /** RS�֥�å�2�θ�����������������ؿ� */
    public final int getRsBlock2EC() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return rsBlock2EC;
    }

    /** �����⡼�ɤ�ʸ�����ؼ��ҤΥӥå�Ĺ���֤��ؿ��� */
    public final int getCharCountNumeric() {
        return charCountNumeric;
    }

    /** �ѿ����⡼�ɤ�ʸ�����ؼ��ҤΥӥå�Ĺ���֤��ؿ��� */
    public final int getCharCountAlpha() {
        return charCountAlpha;
    }

    /** 8�ӥåȥХ��ȥ⡼�ɤ�ʸ�����ؼ��ҤΥӥå�Ĺ���֤��ؿ��� */
    public final int getCharCountAscii() {
        return charCountAscii;
    }

    /** �����⡼�ɤ�ʸ�����ؼ��ҤΥӥå�Ĺ���֤��ؿ� */
    public final int getCharCountKanji() {
        return charCountKanji;
    }

    /** ���֤��碌�ѥ�������󼡸���ɸ�ꥹ�Ȥ��֤��᥽�å� */
    public final List<Point> getApPositions() {
        return apPositions;
    }

    /** ����ܥ뤬��ʬ�����Ǥ��뤫�ɤ������֤� */
    public final boolean isPartial() {
        return partial;
    }

    /**
     * @param partial The partial to set.
     */
    public void setPartial(boolean partial) {
        this.partial = partial;
    }

    /** */
    private boolean partial;

    /** VersionTable�����ľ�ܥ��Ф򿨤�롣 */
    private VersionTable versionTable;

    /** APPositions����������ؿ��� */
    void CalcAPPositions() {
        if (version == 0) {
            throw new IllegalArgumentException("Symbol wasnt initialized");
        }
        List<Integer> posx = new ArrayList<Integer>();
        List<Integer> posy = new ArrayList<Integer>();
        posx.add(new Integer(apPos1));
        posx.add(new Integer(apPos2));
        posx.add(new Integer(apPos3));
        posx.add(new Integer(apPos4));
        posx.add(new Integer(apPos5));
        posx.add(new Integer(apPos6));
        posx.add(new Integer(apPos7));
        posy.addAll(posx);
        for (Integer y : posy) {
            for (Integer x : posx) {
                // ���塢���塢�����κ�ɸ�ϥ����åס�
                if ((x != posx.get(0) && y != posy.get(0)) &&
                        x != posx.get(posx.size() - 1) && y != posy.get(0) &&
                        x != posx.get(0) && y != posy.get(posy.size() - 1) && y.intValue() !=  0 && x.intValue() != 0) {
                    apPositions.add(new Point(x.intValue(), y.intValue()));
                }
            }
        }
    }

    /**
     * ����ܥ����ꤹ��Τ�ɬ�פʥǡ�����
     * VersionTable ����ǡ���������٤ΰ�դ� ID
     */
    private int id;

    /** ����ܥ�η��� */
    private int version;

    /** ����ܥ�Υ��顼������٥� */
    private VersionTable.ErrorCollectionLevel errorCollectionLevel;
    
    /**
     * ���֤ˤ��ä�����ܥ����
     * ���դΥ⥸�塼���
     */
    private int modulesPerSide;
    /** ��ǽ�⥸�塼��� */
    private int functionModules;
    /** ���֥⥸�塼��� */
    private int versionModules;
    /** ����¾�Υ⥸�塼��� */
    private int otherModules;
    /** �����ɸ�� */
    private int wholeCodeWords;
    /** ��;�ӥåȿ� */
    private int remainderBits;
    /** �ǡ��������ɸ�ο� */
    private int dataCodeWords;
    /** �ǡ��������ɸ�Υӥåȿ� */
    private int dataBits;
    /** �����γ�Ǽ��ǽ�� */
    private int numeric;
    /** ����ե��٥åȤγ�Ǽ��ǽ�� */
    private int alphabet;
    /** 8 �ӥåȥХ��Ȥγ�Ǽ��ǽ�� */
    private int bytes;
    /** �����γ�Ǽ��ǽ�� */
    private int kanji;
    /** ���顼������ο� */
    private int ecCodeWords;
    /** RS�֥�å��μ���ο� */
    private int blocks;
    /** RS�֥�å�1���Υ֥�å��ο� */
    private int rsBlock1;
    /** RS�֥�å�1���˳�Ǽ����������ɸ�� */
    private int rsBlock1WholeCodes;
    /** RS�֥�å�1���˳�Ǽ�����ǡ��������ɸ�� */
    private int rsBlock1DataCodeWords;
    /** RS�֥�å�1���˸�������� */
    private int rsBlock1EC;
    /** RS�֥�å�2���Υ֥�å��� */
    private int rsBlock2;
    /** RS�֥�å�2���˳�Ǽ����������ɸ�� */
    private int rsBlock2WholeCodes;
    /** RS�֥�å�2���˳�Ǽ�����ǡ��������� */
    private int rsBlock2DataCodeWords;
    /** RS�֥�å�2���θ�������� */
    private int rsBlock2EC;
    /** ���ֹ�碌�ѥ�����ο� */
    private int alignmentPatterns;
    /** ���ֹ�碌�ѥ�����ι����ɸ1 */
    private int apPos1;
    /** ���ֹ�碌�ѥ�����ι����ɸ2 */
    private int apPos2;
    /** ���ֹ�碌�ѥ�����ι����ɸ3 */
    private int apPos3;
    /** ���ֹ�碌�ѥ�����ι����ɸ4 */
    private int apPos4;
    /** ���ֹ�碌�ѥ�����ι����ɸ5 */
    private int apPos5;
    /** ���ֹ�碌�ѥ�����ι����ɸ6 */
    private int apPos6;
    /** ���ֹ�碌�ѥ�����ι����ɸ7 */
    private int apPos7;
    /** �����⡼�ɤΤȤ���ʸ��������ҤΥӥåȿ� */
    private int charCountNumeric;
    /** �ѿ��⡼�ɤΤȤ���ʸ��������ҤΥӥåȿ� */
    private int charCountAlpha;
    /** �Х��ȥ⡼�ɤΤȤ���ʸ��������ҤΥӥåȿ� */
    private int charCountAscii;
    /** �����⡼�ɤΤȤ���ʸ��������ҤΥӥåȿ� */
    private int charCountKanji;
    /** ���ֹ�碌�ѥ�������󼡸���ɸ */
    private List<Point> apPositions;

    /**
     * @param alignmentPatterns The alignmentPatterns to set.
     */
    public void setAlignmentPatterns(int alignmentPatterns) {
        this.alignmentPatterns = alignmentPatterns;
    }

    /**
     * @param alphabet The alphabet to set.
     */
    public void setAlphabet(int alphabet) {
        this.alphabet = alphabet;
    }

    /**
     * @param pos1 The aPPos1 to set.
     */
    public void setApPos1(int pos1) {
        this.apPos1 = pos1;
    }

    /**
     * @param pos2 The aPPos2 to set.
     */
    public void setApPos2(int pos2) {
        this.apPos2 = pos2;
    }

    /**
     * @param pos3 The aPPos3 to set.
     */
    public void setApPos3(int pos3) {
        this.apPos3 = pos3;
    }

    /**
     * @param pos4 The aPPos4 to set.
     */
    public void setApPos4(int pos4) {
        this.apPos4 = pos4;
    }

    /**
     * @param pos5 The aPPos5 to set.
     */
    public void setApPos5(int pos5) {
        this.apPos5 = pos5;
    }

    /**
     * @param pos6 The aPPos6 to set.
     */
    public void setApPos6(int pos6) {
        this.apPos6 = pos6;
    }

    /**
     * @param pos7 The aPPos7 to set.
     */
    public void setApPos7(int pos7) {
        this.apPos7 = pos7;
    }

    /**
     * @param apPositions The apPositions to set.
     */
    public void setApPositions(List<Point> apPositions) {
        this.apPositions = apPositions;
    }

    /**
     * @param bytes The bytes to set.
     */
    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    /**
     * @param alpha The cC_Alpha to set.
     */
    public void setCharCountAlpha(int alpha) {
        this.charCountAlpha = alpha;
    }

    /**
     * @param ascii The cC_Ascii to set.
     */
    public void setCharCountAscii(int ascii) {
        this.charCountAscii = ascii;
    }

    /**
     * @param numeric The cC_Numeric to set.
     */
    public void setCharCountNumeric(int numeric) {
        this.charCountNumeric = numeric;
    }

    /**
     * @param charCountKanji The charCountKanji to set.
     */
    public void setCharCountKanji(int charCountKanji) {
        this.charCountKanji = charCountKanji;
    }

    /**
     * @param dataBits The dataBits to set.
     */
    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    /**
     * @param dataCodeWords The dataCodeWords to set.
     */
    public void setDataCodeWords(int dataCodeWords) {
        this.dataCodeWords = dataCodeWords;
    }

    /**
     * @param ecCodeWords The ecCodeWords to set.
     */
    public void setEcCodeWords(int ecCodeWords) {
        this.ecCodeWords = ecCodeWords;
    }

    /**
     * @param errorCollectionLevel The errorCollectionLevel to set.
     */
    public void setErrorCollectionLevel(VersionTable.ErrorCollectionLevel errorCollectionLevel) {
        this.errorCollectionLevel = errorCollectionLevel;
    }

    /**
     * @param functionModules The functionModules to set.
     */
    public void setFunctionModules(int functionModules) {
        this.functionModules = functionModules;
    }

    /**
     * @param id The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param kanji The kanji to set.
     */
    public void setKanji(int kanji) {
        this.kanji = kanji;
    }

    /**
     * @param modulesPerSide The modulesPerSide to set.
     */
    public void setModulesPerSide(int modulesPerSide) {
        this.modulesPerSide = modulesPerSide;
    }

    /**
     * @param numeric The numeric to set.
     */
    public void setNumeric(int numeric) {
        this.numeric = numeric;
    }

    /**
     * @param otherModules The otherModules to set.
     */
    public void setOtherModules(int otherModules) {
        this.otherModules = otherModules;
    }

    /**
     * @param remainderBits The remainderBits to set.
     */
    public void setRemainderBits(int remainderBits) {
        this.remainderBits = remainderBits;
    }

    /**
     * @param blocks The blocks to set.
     */
    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }

    /**
     * @param block1_EC The rSBlock1_EC to set.
     */
    public void setRsBlock1EC(int block1_EC) {
        this.rsBlock1EC = block1_EC;
    }

    /**
     * @param rsBlock1DataCodeWords The rsBlock1DataCodeWords to set.
     */
    public void setRsBlock1DataCodeWords(int rsBlock1DataCodeWords) {
        this.rsBlock1DataCodeWords = rsBlock1DataCodeWords;
    }

    /**
     * @param rsBlock1WholeCodes The rsBlock1WholeCodes to set.
     */
    public void setRsBlock1WholeCodes(int rsBlock1WholeCodes) {
        this.rsBlock1WholeCodes = rsBlock1WholeCodes;
    }

    /**
     * @param block2 The rSBlock2 to set.
     */
    public void setRsBlock2(int block2) {
        this.rsBlock2 = block2;
    }

    /**
     * @param block2DataCodeWords The rSBlock2_DataCodeWords to set.
     */
    public void setRsBlock2DataCodeWords(int block2DataCodeWords) {
        this.rsBlock2DataCodeWords = block2DataCodeWords;
    }

    /**
     * @param block2EC The rSBlock2_EC to set.
     */
    public void setRsBlock2EC(int block2EC) {
        this.rsBlock2EC = block2EC;
    }

    /**
     * @param block2WholeCodes The rSBlock2_WholeCodes to set.
     */
    public void setRsBlock2WholeCodes(int block2WholeCodes) {
        this.rsBlock2WholeCodes = block2WholeCodes;
    }

    /**
     * @param version The version to set.
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * @param versionModules The versionModules to set.
     */
    public void setVersionModules(int versionModules) {
        this.versionModules = versionModules;
    }

    /**
     * @param versionTable The versionTable to set.
     */
    public void setVersionTable(VersionTable versionTable) {
        this.versionTable = versionTable;
    }

    /**
     * @param wholeCodeWords The wholeCodeWords to set.
     */
    public void setWholeCodeWords(int wholeCodeWords) {
        this.wholeCodeWords = wholeCodeWords;
    }
}

/* */
