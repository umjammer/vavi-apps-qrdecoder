/*
 * Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode;

import java.util.List;


/**
 * This class performs mutual conversion between
 * Message -> BinaryImage and BinaryImage -> Message.
 * Message -> BinaryImage conversion is done as follows:
 * Place Message in BinaryImage -> Mask -> Output
 * BinaryImage -> Message conversion is done as follows:
 * Read BinaryImage -> Unmask -> Output
 * Calling functions with a different Mode will throw an exception.
 *
 * @version Created 2002/12/16(Mon) by Kentaro Ishitoya
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

    // Functions used by Encoder (cannot be used in Decoder mode)

    /** Set message (Encoder) */
    public void setMessage(Message msg, Symbol sym) {
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

    /** Set version information (Encoder) */
    public void setVersionInfo(BinaryString info) {
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
        // Set version information at bottom-left
        int sx = modulesPerSide - 11;
        for (int x = sx; x < sx + 3; x++) {
            for (int y = 0; y < 6; y++) {
                system.putPixel(x, y, info.at(y * 3 + x - sx));
            }
        }
        // Set version information at top-right
        int sy = modulesPerSide - 11;
        for (int x = 0; x < 6; x++) {
            for (int y = sy; y < sy + 3; y++) {
                system.putPixel(x, y, info.at(x * 3 + y - sy));
            }
        }
    }

    /** Set format information (Encoder) */
    public void setFormInfo(BinaryString info) {
        if (mode != Mode.ENCODER) {
            throw new IllegalArgumentException("Wrong Mode");
        }
        if (info == null) {
            throw new IllegalArgumentException("Info is empty");
        }
        if (info.GetLength() != 15) {
            throw new IllegalArgumentException("invalid info length");
        }

        // Set format information at top-left
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
        // Set format information from top-right to bottom-left
        system.putPixel(sx, 8, info.at(14));
        system.putPixel(sx - 1, 8, info.at(13));
        system.putPixel(sx - 2, 8, info.at(12));
        system.putPixel(sx - 3, 8, info.at(11));
        system.putPixel(sx - 4, 8, info.at(10));
        system.putPixel(sx - 5, 8, info.at(9));
        system.putPixel(sx - 6, 8, info.at(8));
        system.putPixel(sx - 7, 8, info.at(7));
        system.putPixel(8, sy, info.at(6));
        system.putPixel(8, sy + 1, info.at(5));
        system.putPixel(8, sy + 2, info.at(4));
        system.putPixel(8, sy + 3, info.at(3));
        system.putPixel(8, sy + 4, info.at(2));
        system.putPixel(8, sy + 5, info.at(1));
        system.putPixel(8, sy + 6, info.at(0));
    }

    /** Set mask code (Encoder) */
    public void setMaskCode(MaskDecorator.Mask mc) {
        if (mode != Mode.ENCODER) {
            throw new IllegalArgumentException("Wrong Mode");
        }
        maskCode = mc;
    }

    /** Get image (Encoder) */
    public BinaryImage getImage(MaskDecorator.Mask mc) {
        if (mode != Mode.ENCODER) {
            throw new IllegalArgumentException("Wrong Mode");
        }
        BinaryImage temp = new BinaryImage();
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
     * Functions used by Decoder (cannot be used in Encoder mode)
     * Set image (Decoder)
     *
     * @param img
     * @param sym
     */
    public void setImage(BinaryImage img, Symbol sym) {
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
        data = new BinaryImage();
        data.initialize(modulesPerSide, modulesPerSide, false);
        function = new BinaryImage();
        function.initialize(modulesPerSide, modulesPerSide, false);
        system = new BinaryImage();
        system.initialize(modulesPerSide, modulesPerSide, false);
        positioning = new BinaryImage();
        positioning.initialize(modulesPerSide, modulesPerSide, false);
        mask = new MaskDecorator();
        mask.Initialize(image);
        formatFunctionLayer(sym);

        masked = true;

    }

    /** Get format information (Decoder) */
    public BinaryString getFormInfo(FormInfo which) {
        if (mode != Mode.DECODER) {
            throw new IllegalArgumentException("Wrong Mode");
        }

        BinaryString info = new BinaryString();
        if (which == FormInfo.FORMINFO1) {
            // Get format information at top-left
            info.add(image.getPixel(0, 8));
            info.add(image.getPixel(1, 8));
            info.add(image.getPixel(2, 8));
            info.add(image.getPixel(3, 8));
            info.add(image.getPixel(4, 8));
            info.add(image.getPixel(5, 8));
            info.add(image.getPixel(7, 8));
            info.add(image.getPixel(8, 8));
            info.add(image.getPixel(8, 7));
            info.add(image.getPixel(8, 5));
            info.add(image.getPixel(8, 4));
            info.add(image.getPixel(8, 3));
            info.add(image.getPixel(8, 2));
            info.add(image.getPixel(8, 1));
            info.add(image.getPixel(8, 0));

        } else if (which == FormInfo.FORMINFO2) {
            // Get format information from top-right to bottom-left
            int sx = modulesPerSide - 1;
            int sy = modulesPerSide - 1 - 6;
            info.add(image.getPixel(8, sy + 6));
            info.add(image.getPixel(8, sy + 5));
            info.add(image.getPixel(8, sy + 4));
            info.add(image.getPixel(8, sy + 3));
            info.add(image.getPixel(8, sy + 2));
            info.add(image.getPixel(8, sy + 1));
            info.add(image.getPixel(8, sy));
            info.add(image.getPixel(sx - 7, 8));
            info.add(image.getPixel(sx - 6, 8));
            info.add(image.getPixel(sx - 5, 8));
            info.add(image.getPixel(sx - 4, 8));
            info.add(image.getPixel(sx - 3, 8));
            info.add(image.getPixel(sx - 2, 8));
            info.add(image.getPixel(sx - 1, 8));
            info.add(image.getPixel(sx, 8));
        }
        return info;

    }

    /** Set unmask code (Decoder) */
    public void setUnMaskCode(MaskDecorator.Mask mc) {
        if (mode != Mode.DECODER) {
            throw new IllegalArgumentException("Wrong Mode");
        }
        maskCode = mc;
        unMask(mc);

    }

    /** */
    public BinaryString getMessage(Symbol sym) {

        if (mode != Mode.DECODER) {
            throw new IllegalArgumentException("Wrong Mode");
        }
        if (masked) {
            throw new IllegalArgumentException("This Image was not UnMasked");
        }
        if (sym.isPartial()) {
            throw new IllegalArgumentException("This Method needs compleate symbol");
        }
        boolean isup = true;    // vertical direction flag
        boolean isleft = true;    // left/right flag
        int x = modulesPerSide - 1;
        int y = modulesPerSide - 1;
        int tx;
        int ty;
        int totalBits = sym.getWholeCodeWords() * 8 + sym.getRemainderBits();
        BinaryString str = new BinaryString();
        int m = 0;
        while (m < totalBits) {
            str.add(image.getPixel(x, y));
            tx = x;
            ty = y;
            while (true) {
                // Skip the timing pattern column.
                if (tx == 6) {
                    tx--;
                }
                // Move left
                if (isleft) {
                    if (tx > 0) {
                        tx -= 1;
                    }
                    isleft = false;
                    // Move to upper-right or lower-right
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
                // Check if data can be obtained at the moved position
                if (positioning.getPixel(tx, ty)) {
                    x = tx;
                    y = ty;
                    m++;
                    break;
                }
            }
        }
        return str;
    }

    // Utilities

    /** Set mode */
    public void setMode(Mode m) {
        mode = m;
    }

    /** Get QR code image */
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

    /** Get mode */
    public final MaskDecorator.Mask getMaskCode() {
        return maskCode;
    }

    /** Arrange message (Encoder) */
    private void Arrange(BinaryString str, Symbol sym) {
        if (mode != Mode.ENCODER) {
            throw new IllegalArgumentException("Wrong Mode");
        }
        if (str.GetLength() != sym.getWholeCodeWords() * 8) {
            throw new IllegalArgumentException("Invalid Data");
        }
        boolean isup = true;    // vertical direction flag
        boolean isleft = true;    // left/right flag
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
                // Skip the timing pattern column.
                if (tx == 6) {
                    tx--;
                }
                // Move left
                if (isleft) {
                    if (tx > 0) {
                        tx -= 1;
                    }
                    isleft = false;
                    // Move to upper-right or lower-right
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
                // Check if data can be placed at the moved position
                if (positioning.getPixel(tx, ty)) {
                    x = tx;
                    y = ty;
                    m++;
                    break;
                }
            }
        }
    }

    /** Unmask (Decoder) */
    private void unMask(MaskDecorator.Mask mc) {
        if (mode != Mode.DECODER) {
            throw new IllegalArgumentException("Wrong Mode");
        }
        for (int x = 0; x < modulesPerSide; x++) {
            for (int y = 0; y < modulesPerSide; y++) {
                if (positioning.getPixel(x, y)) {
                    image.putPixel(x, y, mask.GetUnMaskedPixel(x, y, mc));
                }
            }
        }
        masked = false;
    }

    /**
     * Get message (Decoder)
     * Set alignment, finder patterns, timing patterns, and positioning layer
     */
    private void formatFunctionLayer(Symbol sym) {
        // Create finder pattern
        BinaryImage f = new BinaryImage();
        f.initialize(7, 7, true);
        f.fill(1, 1, 6, 6, false);
        f.fill(2, 2, 5, 5, true);
        // Create alignment pattern
        BinaryImage p = new BinaryImage();
        p.initialize(5, 5, true);
        p.fill(1, 1, 4, 4, false);
        p.putPixel(2, 2, true);

        // Set finder patterns
        function = function.or(0, 0, f);
        function = function.or(modulesPerSide - 7, 0, f);
        function = function.or(0, modulesPerSide - 7, f);

        // Set alignment patterns
        List<Point> vp = sym.getApPositions();
        for (Point vpi : vp) {
            function = function.or((vpi).x - 2, (vpi).y - 2, p);
            positioning.fill((vpi).x - 2, (vpi).y - 2, (vpi).x + 3, (vpi).y + 3, true);
        }

        // Set timing patterns
        // Vertical timing pattern
        boolean t = true;
        for (int y = 8; y < modulesPerSide - 8; y++) {
            function.putPixel(6, y, t);
            positioning.putPixel(6, y, true);
            t = !t;
        }
        // Horizontal timing pattern
        t = true;
        for (int x = 8; x < modulesPerSide - 8; x++) {
            function.putPixel(x, 6, t);
            positioning.putPixel(x, 6, true);
            t = !t;
        }

        // Set positioning finder patterns, separator patterns, and format information
        // First, around the top-left finder pattern
        positioning.fill(0, 0, 9, 9, true);
        positioning.fill(modulesPerSide - 8, 0, modulesPerSide, 9, true);
        positioning.fill(0, modulesPerSide - 8, 9, modulesPerSide, true);

        // Set positioning version information. Version 7 and above
        if (sym.getVersion() > 6) {
            positioning.fill(modulesPerSide - 10, 0, modulesPerSide - 8, 2, true);
            positioning.fill(0, modulesPerSide - 10, 2, modulesPerSide - 8, true);
        }

        // Always-dark module at position 4V+9, 8.
        positioning.putPixel(8, sym.getVersion() * 4 + 9, true);
        function.putPixel(8, sym.getVersion() * 4 + 9, true);

        // Finally, invert positioning
        positioning = positioning.not();
    }

    /** Layer that holds all combined layers */
    private BinaryImage image = new BinaryImage();
    /** Layer that holds only the data portion */
    private BinaryImage data = new BinaryImage();
    /** Layer that holds only function modules */
    private BinaryImage function = new BinaryImage();
    /** Layer that holds format information and version information */
    private BinaryImage system = new BinaryImage();
    /** Layer used only for positioning */
    private BinaryImage positioning = new BinaryImage();
    /** Filters or unfilters */
    private MaskDecorator mask = new MaskDecorator();

    /** Mask information */
    private MaskDecorator.Mask maskCode;
    /** Number of modules per side */
    private int modulesPerSide;
    /** Function switching */
    private Mode mode;
    /** Whether it is masked. */
    private boolean masked;
}
