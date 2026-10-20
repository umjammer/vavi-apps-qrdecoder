/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.github.squallyou.barcode.qrcode.QRDecoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * TestCase.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2026-10-20 nsano initial version <br>
 */
public class TestCase {

    @Test
    @DisplayName("read sample.png from resource stream")
    void testSamplePngStream() throws Exception {
        QRDecoder decoder = new QRDecoder();
        try (InputStream is = TestCase.class.getResourceAsStream("/sample.png")) {
            assertNotNull(is, "sample.png resource not found");
            String result = decoder.decode(is);
            assertEquals("http://en.m.wikipedia.org", result);
        }
    }

    @Test
    @DisplayName("read sample.png from path")
    void testSamplePngPath() throws Exception {
        QRDecoder decoder = new QRDecoder();
        Path path = Paths.get("src/test/resources/sample.png");
        String result = decoder.decode(path);
        assertEquals("http://en.m.wikipedia.org", result);
    }
}
