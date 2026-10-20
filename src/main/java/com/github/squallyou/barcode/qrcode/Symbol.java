/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode;

import java.util.ArrayList;
import java.util.List;


/**
 * This class holds all QR code symbol information.
 * This class is initialized by {@link VersionTable}.
 * The {@link Symbol} class cannot be put into a usable state
 * except through {@link VersionTable}.
 * Always initialize it using {@link VersionTable#formatSymbol()}.
 *
 * @version Created 2002/12/08(Sun) by Kentaro Ishitoya
 *          Modified 2002/12/11(Wed) by Kentaro Ishitoya
 */
class Symbol {

    /** */
    Symbol() {
        this.id = 0;
        this.version = 0;
        this.errorCollectionLevel = VersionTable.ErrorCollectionLevel.L;
        this.modulesPerSide = 0;
        this.functionModules = 0;
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

    /** Assignment operator */
    public final Symbol operatorLet(Symbol right) {
        if (right == this) {
            return this;
        }
        if (right == null) {
            throw new IllegalArgumentException("Data is not Initialized");
        }

        id = right.id;
        version = right.version;
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
            dataCodeWords = right.dataCodeWords;
            dataBits = right.dataBits;
            numeric = right.numeric;
            alphabet = right.alphabet;
            bytes = right.bytes;
            kanji = right.kanji;
            ecCodeWords = right.ecCodeWords;
            blocks = right.blocks;
            rsBlock1 = right.rsBlock1;
            rsBlock1WholeCodes = right.rsBlock1WholeCodes;
            rsBlock1DataCodeWords = right.rsBlock1DataCodeWords;
            rsBlock1EC = right.rsBlock1EC;
            rsBlock2 = right.rsBlock2;
            rsBlock2WholeCodes = right.rsBlock2WholeCodes;
            rsBlock2DataCodeWords = right.rsBlock2DataCodeWords;
            rsBlock2EC = right.rsBlock2EC;
        }
        partial = right.partial;
        return this;
    }

    /** Getter for id */
    public final int getId() {
        return id;
    }

    /** Getter for version */
    public final int getVersion() {
        return version;
    }

    /** Getter for error correction level */
    public final VersionTable.ErrorCollectionLevel getErrorCollectionLevel() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return errorCollectionLevel;
    }

    /** Getter for number of modules per side */
    public final int getModulesPerSide() {
        return modulesPerSide;
    }

    /** Getter for number of function modules */
    public final int getFunctionModules() {
        return functionModules;
    }

    /** Getter for number of version modules */
    public final int getVersionModules() {
        return versionModules;
    }

    /** Getter for number of other modules */
    public final int getOtherModules() {
        return otherModules;
    }

    /** Method to get the total number of codewords */
    public final int getWholeCodeWords() {
        return wholeCodeWords;
    }

    /** Getter for number of remainder bits */
    public final int getRemainderBits() {
        return remainderBits;
    }

    /** Getter for data codewords */
    public final int getDataCodeWords() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return dataCodeWords;
    }

    /** Getter for number of data bits */
    public final int getDataBits() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return dataBits;
    }

    /** Getter for storable numeric characters count */
    public final int getNumeric() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return numeric;
    }

    /** Getter for storable alphanumeric characters count */
    public final int getAlphabet() {
        return alphabet;
    }

    /** Getter for storable 8-bit bytes count */
    public final int getByte() {
        return bytes;
    }

    /** Getter for storable kanji characters count */
    public final int getKanji() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return kanji;
    }

    /** Function to return the number of error correction codewords */
    public final int getECCodeWords() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return ecCodeWords;
    }

    /** Function to return the number of RS block types */
    public final int getBlocks() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return blocks;
    }

    /** Function to return the number of RS block 1 */
    public final int getRsBlock1() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return rsBlock1;

    }

    /** Function to return the total number of codewords in RS block 1 */
    public final int getRSBlock1WholeCodes() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return rsBlock1WholeCodes;
    }

    /** Function to get the data codeword count of RS block 1 */
    public final int getRSBlock1DataCodeWords() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return rsBlock1DataCodeWords;
    }

    /** Function to get the error correction count of RS block 1 */
    public final int getRsBlock1EC() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return rsBlock1EC;
    }

    /** Function to return the number of RS block 2 */
    public final int getRsBlock2() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return rsBlock2;
    }

    /** Function to return the total number of codewords in RS block 2 */
    public final int getRsBlock2WholeCodes() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return rsBlock2WholeCodes;
    }

    /** Function to get the data codeword count of RS block 2 */
    public final int getRsBlock2DataCodeWords() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return rsBlock2DataCodeWords;
    }

    /** Function to get the error correction count of RS block 2 */
    public final int getRsBlock2EC() {
        if (partial) {
            throw new IllegalArgumentException("partial mode");
        }
        return rsBlock2EC;
    }

    /** Function to return the bit length of the character count indicator for numeric mode. */
    public final int getCharCountNumeric() {
        return charCountNumeric;
    }

    /** Function to return the bit length of the character count indicator for alphanumeric mode. */
    public final int getCharCountAlpha() {
        return charCountAlpha;
    }

    /** Function to return the bit length of the character count indicator for 8-bit byte mode. */
    public final int getCharCountAscii() {
        return charCountAscii;
    }

    /** Function to return the bit length of the character count indicator for kanji mode */
    public final int getCharCountKanji() {
        return charCountKanji;
    }

    /** Method to return the 2D coordinate list of alignment patterns */
    public final List<Point> getApPositions() {
        return apPositions;
    }

    /** Returns whether the symbol is a partial configuration */
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

    /** VersionTable can directly access members. */
    private VersionTable versionTable;

    /** Function to initialize APPositions. */
    void CalcAPPositions() {
        if (version == 0) {
            throw new IllegalArgumentException("Symbol wasnt initialized");
        }
        List<Integer> posx = new ArrayList<>();
        List<Integer> posy = new ArrayList<>();
        posx.add(apPos1);
        posx.add(apPos2);
        posx.add(apPos3);
        posx.add(apPos4);
        posx.add(apPos5);
        posx.add(apPos6);
        posx.add(apPos7);
        posy.addAll(posx);
        for (Integer y : posy) {
            for (Integer x : posx) {
                // Skip coordinates of top-right, top-left, and bottom-left.
                if ((x != posx.get(0) && y != posy.get(0)) &&
                        x != posx.get(posx.size() - 1) && y != posy.get(0) &&
                        x != posx.get(0) && y != posy.get(posy.size() - 1) && y != 0 && x != 0) {
                    apPositions.add(new Point(x, y));
                }
            }
        }
    }

    /**
     * Data needed to determine the symbol.
     * Unique ID for looking up data from VersionTable
     */
    private int id;

    /** Symbol version number */
    private int version;

    /** Symbol error correction level */
    private VersionTable.ErrorCollectionLevel errorCollectionLevel;

    /**
     * Symbol information for the version.
     * Number of modules per side
     */
    private int modulesPerSide;
    /** Number of function modules */
    private int functionModules;
    /** Number of version modules */
    private int versionModules;
    /** Number of other modules */
    private int otherModules;
    /** Total number of codewords */
    private int wholeCodeWords;
    /** Number of remainder bits */
    private int remainderBits;
    /** Number of data codewords */
    private int dataCodeWords;
    /** Number of data codeword bits */
    private int dataBits;
    /** Storable numeric characters count */
    private int numeric;
    /** Storable alphanumeric characters count */
    private int alphabet;
    /** Storable 8-bit bytes count */
    private int bytes;
    /** Storable kanji characters count */
    private int kanji;
    /** Number of error correction codewords */
    private int ecCodeWords;
    /** Number of RS block types */
    private int blocks;
    /** Number of block 1 of RS block type 1 */
    private int rsBlock1;
    /** Total number of codewords stored in block 1 of RS block type 1 */
    private int rsBlock1WholeCodes;
    /** Number of data codewords stored in block 1 of RS block type 1 */
    private int rsBlock1DataCodeWords;
    /** Error correction count of block 1 of RS block type 1 */
    private int rsBlock1EC;
    /** Number of block 2 of RS block type 2 */
    private int rsBlock2;
    /** Total number of codewords stored in block 2 of RS block type 2 */
    private int rsBlock2WholeCodes;
    /** Number of data codewords stored in block 2 of RS block type 2 */
    private int rsBlock2DataCodeWords;
    /** Error correction count of block 2 of RS block type 2 */
    private int rsBlock2EC;
    /** Number of alignment patterns */
    private int alignmentPatterns;
    /** Matrix coordinates of alignment pattern 1 */
    private int apPos1;
    /** Matrix coordinates of alignment pattern 2 */
    private int apPos2;
    /** Matrix coordinates of alignment pattern 3 */
    private int apPos3;
    /** Matrix coordinates of alignment pattern 4 */
    private int apPos4;
    /** Matrix coordinates of alignment pattern 5 */
    private int apPos5;
    /** Matrix coordinates of alignment pattern 6 */
    private int apPos6;
    /** Matrix coordinates of alignment pattern 7 */
    private int apPos7;
    /** Bit length of character count indicator for numeric mode */
    private int charCountNumeric;
    /** Bit length of character count indicator for alphanumeric mode */
    private int charCountAlpha;
    /** Bit length of character count indicator for byte mode */
    private int charCountAscii;
    /** Bit length of character count indicator for kanji mode */
    private int charCountKanji;
    /** 2D coordinates of alignment patterns */
    private List<Point> apPositions = new ArrayList<>();

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
     * @param block1 The rsBlock1 to set.
     */
    public void setRsBlock1(int block1) {
        this.rsBlock1 = block1;
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
