/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package com.github.squallyou.barcode.qrcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import javax.imageio.ImageIO;


/**
 * QRDecoder.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2026-10-20 nsano initial version <br>
 */
public class QRDecoder {

    /** */
    private final PowerTable pTable = new PowerTable();
    /** */
    private final VersionTable vTable = new VersionTable();
    /** */
    private final ExpressionTable eTable = new ExpressionTable();
    /** */
    private boolean initialized = false;

    /** Function to initialize the decoder */
    public void initialize() throws IOException {
        initialize("power.csv", new CSVFileReader(), "version.csv", new CSVFileReader(), "expression.csv", new CSVFileReader());
    }

    /** Function to initialize the decoder */
    public void initialize(String pfr, FileReader pow, String vfr, FileReader ver, String efr, FileReader exp) throws IOException {
        pTable.initialize(pfr, pow);
        vTable.initialize(vfr, ver);
        eTable.initialize(efr, exp);
        initialized = true;
    }

    private void ensureInitialized() {
        if (!initialized) {
            try {
                initialize();
            } catch (IOException e) {
                throw new IllegalStateException("Failed to initialize QRDecoder tables", e);
            }
        }
    }

    /** Decode QRCode from a Path */
    public String decode(Path path) throws IOException {
        return decode(ImageIO.read(path.toFile()));
    }

    /** Decode QRCode from a File */
    public String decode(File file) throws IOException {
        return decode(ImageIO.read(file));
    }

    /** Decode QRCode from an InputStream */
    public String decode(InputStream is) throws IOException {
        return decode(ImageIO.read(is));
    }

    /** Decode QRCode from a BufferedImage */
    public String decode(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image is null");
        }
        BinaryImage binImg = convertToBinaryImage(image);
        return decode(binImg);
    }

    /** Decode QRCode from a BinaryImage */
    public String decode(BinaryImage image) {
        ensureInitialized();
        if (image == null) {
            throw new IllegalArgumentException("BinaryImage is null");
        }

        int modulesPerSide = image.getMaxCol();
        int version = (modulesPerSide - 17) / 4;
        if (version < 1 || version > 40) {
            throw new IllegalArgumentException("Invalid QR code size: " + modulesPerSide);
        }

        Symbol partialSym = vTable.getPartialSymbol(version);

        QRCodeImage qrImage = new QRCodeImage();
        qrImage.setMode(QRCodeImage.Mode.DECODER);
        qrImage.setImage(image, partialSym);

        BinaryString formInfoBits = qrImage.getFormInfo(QRCodeImage.FormInfo.FORMINFO1);
        QRFormInfo formInfo = new QRFormInfo();
        formInfo.initialize(formInfoBits);

        VersionTable.ErrorCollectionLevel ecl = formInfo.getECL();
        MaskDecorator.Mask mask = formInfo.getMaskCode();

        Symbol fullSym = vTable.getSymbol(version, ecl);

        qrImage.setUnMaskCode(mask);

        BinaryString rawData = qrImage.getMessage(fullSym);

        Message message = new Message();
        message.setData(rawData, fullSym);

        BinaryString data = message.getPlainData();

        return decodeData(data, fullSym);
    }

    /** Extract string from data codeword sequence */
    private String decodeData(BinaryString data, Symbol sym) {
        int bitOffset = 0;

        // 4-bit mode indicator
        int modeVal = 0;
        for (int i = 0; i < 4; i++) {
            if (data.at(bitOffset + i)) {
                modeVal |= (1 << (3 - i));
            }
        }
        bitOffset += 4;

        // Bit length of character count indicator
        int countBits = switch (modeVal) {
            case 1 -> sym.getCharCountNumeric();
            case 2 -> sym.getCharCountAlpha();
            case 4 -> sym.getCharCountAscii();
            case 8 -> sym.getCharCountKanji();
            default -> 8;
        };

        int charCount = 0;
        for (int i = 0; i < countBits; i++) {
            if (data.at(bitOffset + i)) {
                charCount |= (1 << (countBits - 1 - i));
            }
        }
        bitOffset += countBits;

        // Decode by mode
        if (modeVal == 4) { // 8-bit byte / ASCII
            AsciiCodeConverter converter = new AsciiCodeConverter();
            return converter.convertToString(data, bitOffset, charCount);
        } else if (modeVal == 1) { // Numeric mode
            StringBuilder sb = new StringBuilder();
            int remaining = charCount;
            while (remaining >= 3) {
                int val = 0;
                for (int i = 0; i < 10; i++) {
                    if (data.at(bitOffset + i)) val |= (1 << (9 - i));
                }
                bitOffset += 10;
                sb.append(String.format("%03d", val));
                remaining -= 3;
            }
            if (remaining == 2) {
                int val = 0;
                for (int i = 0; i < 7; i++) {
                    if (data.at(bitOffset + i)) val |= (1 << (6 - i));
                }
                bitOffset += 7;
                sb.append(String.format("%02d", val));
            } else if (remaining == 1) {
                int val = 0;
                for (int i = 0; i < 4; i++) {
                    if (data.at(bitOffset + i)) val |= (1 << (3 - i));
                }
                bitOffset += 4;
                sb.append(String.format("%01d", val));
            }
            return sb.toString();
        }

        throw new UnsupportedOperationException("Unsupported QR mode: " + modeVal);
    }

    /** Convert BufferedImage to binary image (in module units) */
    public static BinaryImage convertToBinaryImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int minX = width, maxX = -1;
        int minY = height, maxY = -1;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (isDarkPixel(image, x, y)) {
                    if (x < minX) minX = x;
                    if (x > maxX) maxX = x;
                    if (y < minY) minY = y;
                    if (y > maxY) maxY = y;
                }
            }
        }

        if (maxX < minX || maxY < minY) {
            throw new IllegalArgumentException("No QR code found in image");
        }

        int qrWidth = maxX - minX + 1;
        int qrHeight = maxY - minY + 1;

        // Estimate module size from the width of the top-left finder pattern (7 modules)
        int finderWidth = 0;
        while (minX + finderWidth <= maxX && isDarkPixel(image, minX + finderWidth, minY)) {
            finderWidth++;
        }
        if (finderWidth == 0) {
            finderWidth = 1;
        }

        double moduleSize = finderWidth / 7.0;
        int numModules = (int) Math.round(qrWidth / moduleSize);

        // N = 4 * version + 17
        int version = (numModules - 17 + 2) / 4;
        if (version < 1) version = 1;
        if (version > 40) version = 40;
        numModules = 4 * version + 17;

        BinaryImage binImg = new BinaryImage();
        binImg.initialize(numModules, numModules, false);

        double modW = (double) qrWidth / numModules;
        double modH = (double) qrHeight / numModules;

        for (int r = 0; r < numModules; r++) {
            for (int c = 0; c < numModules; c++) {
                int cx = (int) Math.round(minX + (c + 0.5) * modW);
                int cy = (int) Math.round(minY + (r + 0.5) * modH);
                if (cx >= 0 && cx < width && cy >= 0 && cy < height) {
                    if (isDarkPixel(image, cx, cy)) {
                        binImg.putPixel(c, r, true);
                    }
                }
            }
        }

        return binImg;
    }

    private static boolean isDarkPixel(BufferedImage image, int x, int y) {
        int argb = image.getRGB(x, y);
        int alpha = (argb >> 24) & 0xff;
        if (alpha < 128) {
            return false;
        }
        int r = (argb >> 16) & 0xff;
        int g = (argb >> 8) & 0xff;
        int b = argb & 0xff;
        double lum = 0.299 * r + 0.587 * g + 0.114 * b;
        return lum < 128;
    }
}
