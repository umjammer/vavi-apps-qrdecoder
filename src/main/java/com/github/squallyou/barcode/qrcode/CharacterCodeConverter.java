/*
 * Copyright (c) 2002-2003 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package com.github.squallyou.barcode.qrcode;

import java.util.List;


/**
 * Abstract base class for classes that convert the given string into a bit sequence
 *
 * @version Created 2002/11/25(Sun) by Manabu Shibata
 *          Modified 2003/02/25(Tue) by Kentaro Ishitoya
 */
interface CharacterCodeConverter {

    /** Convert to BinaryString */
    BinaryString convert(String data);

    /** Convert to BinaryString */
    BinaryString convert(byte[] data);

    /** Convert to BinaryString */
    BinaryString convert(List<Byte> data);
}
