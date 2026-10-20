/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package com.github.squallyou.barcode.qrcode;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.github.squallyou.barcode.qrcode.QREncoder.CharMode;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import static org.junit.jupiter.api.Assertions.*;


/**
 * QREncoderTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2026-10-20 nsano initial version <br>
 */
class QREncoderTest {

    @Test
    void test1() throws Exception {
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
