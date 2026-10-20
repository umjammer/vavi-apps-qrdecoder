/*
 *  Copyright (c) 2002 Kentaro Ishitoya & Manabu Shibata. All rights reserved.
 */

package vavi.util.barcode.qrcode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * This class was created for convenient handling of binary data represented by '0' and '1'.
 * It supports decimal-to-binary, binary-to-decimal conversion, and concatenation.
 * Notes: This class was coded with Borland C++Builder in mind. When using with Microsoft Visual C++,
 * please rewrite according to the following notes.
 * - Remove pragma.
 * - Remove vcl.h include statement.
 * - Remove GetDataByAnsiString method.
 * - Exception-related items
 * This will make it compilable with VC.
 *
 * @version Created 2002/11/08(Fri) by Kentaro Ishitoya
 *          Implementation completed 2002/11/09(Sat) by Kentaro Ishitoya
 *          Modified 2002/11/10(Sun) by Kentaro Ishitoya
 *          Modified 2002/11/13(Wed) by Kentaro Ishitoya
 *          Modified 2002/12/12(Thu) by Kentaro Ishitoya
 *          Modified 2003/02/24(Mon) by Kentaro Ishitoya
 *          Modified 2003/02/25(Tue) by Kentaro Ishitoya
 */
class BinaryString {

    /** Default constructor */
    public BinaryString() {
        this.length = 0;
        maxLength = 0;
    }

    /** Copy constructor */
    public BinaryString(BinaryString temp) {
        if (!data.isEmpty()) {
            data.clear();
        }
        if (temp.GetDataByBoolArray() == null) {
            return;
        }
        this.length = temp.GetLength();
        maxLength = temp.GetMaxLength();
        List<Boolean> v = temp.GetDataByBoolArray();
        data.addAll(v);
    }

    /** Constructor that takes a List as argument and initializes */
    @SuppressWarnings("unchecked")
    public BinaryString(List<?> temp) {
        // @@@ Dispatch by List<Type>
        if (temp == null || temp.isEmpty()) {
            return;
        }
        Object first = temp.get(0);
        if (first instanceof Boolean) {
            initBooleanList((List<Boolean>) temp);
        } else if (first instanceof Byte) {
            initByteList((List<Byte>) temp);
        } else if (first instanceof Short) {
            initShortList((List<Short>) temp);
        } else if (first instanceof Integer) {
            initIntegerList((List<Integer>) temp);
        }
    }

    /** Constructor that takes a Boolean array as argument and initializes */
    private void initBooleanList(List<Boolean> temp) {
        if (!data.isEmpty()) {
            data.clear();
        }
        if (temp.isEmpty()) {
            return;
        }
        this.length = temp.size();
        maxLength = temp.size();
        data.addAll(temp);
    }

    /**
     * Constructor that initializes with char*.
     * Converts a string composed only of '0' and '1' like "0101110" to BinaryString.
     * If a character other than '0' or '1' appears during conversion, it stops and throws an exception.
     */
    public BinaryString(byte[] tch) {
        if (!data.isEmpty()) {
            data.clear();
        }
        String temp = new String(tch);
        if (temp.isEmpty()) {
            return;
        }
        this.length = temp.length();
        maxLength = temp.length();

        for (int i = 0; i < temp.length(); i++) {
            char p = temp.charAt(i);
            if (p == '0') {
                data.add(Boolean.FALSE);
            } else if (p == '1') {
                data.add(Boolean.TRUE);
            } else {
                release();
                throw new IllegalArgumentException("Can't convert from char*");
            }
            p++;
        }
    }

    /**
     * Constructor that initializes with string.
     * Converts a string composed only of '0' and '1' like "0101110" to BinaryString.
     * If a character other than '0' or '1' appears during conversion, it stops and throws an exception.
     */
    public BinaryString(String temp) {
        if (!data.isEmpty()) {
            data.clear();
        }
        if (temp == null) {
            return;
        }
        this.length = temp.length();
        maxLength = temp.length();

        for (int p = 0; p < length; p++) {
            if (temp.charAt(p) == '0') {
                data.add(Boolean.FALSE);
            } else if (temp.charAt(p) == '1') {
                data.add(Boolean.TRUE);
            } else {
                release();
                throw new IllegalArgumentException("Can't convert from string");
            }
        }
    }

    /** Constructor when 8-bit signed data is passed */
    public BinaryString(byte temp) {
        if (!data.isEmpty()) {
            data.clear();
        }
        this.length = 1 * 8;
        maxLength = 1 * 8;
        int offset = (int) Math.pow(2, length - 1);
        for (int i = 0; i < length; i++) {
            // Extract the leftmost bit.
            if (((temp << i) & offset) != 0) {
                data.add(Boolean.TRUE);
            } else {
                data.add(Boolean.FALSE);
            }
        }
    }

    /** Constructor when 16-bit signed integer is passed */
    public BinaryString(short temp) {
        if (!data.isEmpty()) {
            data.clear();
        }
        this.length = 2 * 8;
        maxLength = 2 * 8;
        int offset = (int) Math.pow(2, length - 1);
        for (int i = 0; i < length; i++) {
            // Extract the leftmost bit.
            if (((temp << i) & offset) != 0) {
                data.add(Boolean.TRUE);
            } else {
                data.add(Boolean.FALSE);
            }
        }
    }

    /** Constructor when 32-bit signed integer is passed */
    public BinaryString(int temp) {
        if (!data.isEmpty()) {
            data.clear();
        }
        this.length = 4 * 8;
        maxLength = 4 * 8;
        int offset = (int) Math.pow(2, length - 1);
        for (int i = 0; i < length; i++) {
            // Extract the leftmost bit.
            if (((temp << i) & offset) != 0) {
                data.add(Boolean.TRUE);
            } else {
                data.add(Boolean.FALSE);
            }
        }
    }

    /** Constructor when 32-bit signed integer is passed */
    public BinaryString(long temp) {
        if (!data.isEmpty()) {
            data.clear();
        }
        this.length = 4 * 8;
        maxLength = 4 * 8;
        int offset = (int) Math.pow(2, length - 1);
        for (int i = 0; i < length; i++) {
            // Extract the leftmost bit.
            if (((temp << i) & offset) != 0) {
                data.add(Boolean.TRUE);
            } else {
                data.add(Boolean.FALSE);
            }
        }
    }

//    /** Constructor when 8-bit unsigned integer is passed */
//    public BinaryString(unsigned char data) {
//        if (!Data.empty()) {
//            Data.clear();
//        }
//        Length = sizeof(unsigned char) * 8;
//        MaxLength = sizeof(unsigned char) * 8;
//        int offset = pow(2, Length - 1);
//        for (int i = 0; i < Length; i++) {
//            // Extract the leftmost bit.
//            if ((temp << i) & offset) {
//                Data.add(true);
//            } else {
//                Data.add(false);
//            }
//        }
//    }

//    /** Constructor when 16-bit unsigned integer is passed */
//    public BinaryString(unsigned short data) {
//        if (!Data.empty()) {
//            Data.clear();
//        }
//        Length = sizeof(unsigned short) * 8;
//        MaxLength = sizeof(unsigned short) * 8;
//        unsigned int offset = pow(2, Length - 1);
//        for (unsigned int i = 0; i < Length; i++) {
//            // Extract the leftmost bit.
//            if ((temp << i) & offset) {
//                Data.push_back(true);
//            } else {
//                Data.push_back(false);
//            }
//        }
//    }

//    /** Constructor when 32-bit unsigned integer is passed */
//    public BinaryString(unsigned int data) {
//        if (!Data.empty()) {
//            Data.clear();
//        }
//        Length = sizeof(unsigned int) * 8;
//        MaxLength = sizeof(unsigned int) * 8;
//        unsigned int offset = pow(2, Length - 1);
//        for (unsigned int i = 0; i < Length; i++) {
//            //  Extract the leftmost bit.
//            if ((temp << i) & offset) {
//                Data.push_back(true);
//            } else {
//                Data.push_back(false);
//            }
//        }
//    }

//    /** Constructor when 32-bit unsigned integer is passed */
//    public BinaryString(unsigned long data) {
//        if (!Data.empty()) {
//            Data.clear();
//        }
//        Length = sizeof(unsigned long) * 8;
//        MaxLength = sizeof(unsigned long) * 8;
//        int offset = pow(2, Length - 1);
//        for (int i = 0; i < Length; i++) {
//            // Extract the leftmost bit.
//            if ((temp << i) & offset) {
//                Data.add(true);
//            } else {
//                Data.add(false);
//            }
//        }
//    }

    /** Constructor that takes a vector of 8-bit unsigned integer type unsigned char */
    private void initByteList(List<Byte> temp) {
        if (!data.isEmpty()) {
            data.clear();
        }

        int size = 1 * 8;
        int offset = (int) Math.pow(2, size - 1);
        this.length = temp.size() * size;
        maxLength = temp.size() * size;

        for (Byte p : temp) {
            for (int i = 0; i < size; i++) {
                // Extract the leftmost bit.
                if (((p << i) & offset) != 0) {
                    data.add(Boolean.TRUE);
                } else {
                    data.add(Boolean.FALSE);
                }
            }
        }
    }

    /** Constructor that takes a vector of 16-bit unsigned integer type unsigned short */
    private void initShortList(List<Short> temp) {
        if (!data.isEmpty()) {
            data.clear();
        }

        int size = 2 * 8;
        int offset = (int) Math.pow(2, size - 1);
        this.length = temp.size() * size;
        maxLength = temp.size() * size;

        for (Short p : temp) {
            for (int i = 0; i < size; i++) {
                // Extract the leftmost bit.
                if (((p << i) & offset) != 0) {
                    data.add(Boolean.TRUE);
                } else {
                    data.add(Boolean.FALSE);
                }
            }
        }
    }

    /** Constructor that takes a vector of 32-bit unsigned integer type unsigned int */
    private void initIntegerList(List<Integer> temp) {
        if (!data.isEmpty()) {
            data.clear();
        }

        int size = 4 * 8;
        int offset = (int) Math.pow(2, size - 1);
        this.length = temp.size() * size;
        maxLength = temp.size() * size;

        for (Integer p : temp) {
            for (int i = 0; i < size; i++) {
                // Extract the leftmost bit.
                if (((p << i) & offset) != 0) {
                    data.add(Boolean.TRUE);
                } else {
                    data.add(Boolean.FALSE);
                }
            }
        }
    }

    /** Constructor that takes a vector of 32-bit unsigned integer type unsigned long */
    private void initLongList(List<Long> temp) {
        if (!data.isEmpty()) {
            data.clear();
        }

        int size = 8 * 8;
        int offset = (int) Math.pow(2, size - 1);
        length = temp.size() * size;
        maxLength = temp.size() * size;

        for (Long p : temp) {
            for (int i = 0; i < size; i++) {
                // Extract the leftmost bit.
                if (((p << i) & offset) != 0) {
                    data.add(Boolean.TRUE);
                } else {
                    data.add(Boolean.FALSE);
                }
            }
        }
    }

    /** Store Data with specified length */
    public BinaryString(int temp, int length) {
        if (!data.isEmpty()) {
            data.clear();
        }
        if (length == 0) {
            length = 0;
            maxLength = 0;
            return;
        }
        this.length = length;
        maxLength = length;
        int offset = (int) Math.pow(2, length - 1);
        for (int i = 0; i < length; i++) {
            // Extract the leftmost bit.
            if (((temp << i) & offset) != 0) {
                data.add(Boolean.TRUE);
            } else {
                data.add(Boolean.FALSE);
            }
        }
    }

    /** Overload of equality operator */
    public boolean operatorEqual(BinaryString right) {
        if (right == null) {
            throw new IllegalArgumentException("right is NULL");
        }
        if (right.GetLength() != length) {
            return false;
        }
        Iterator<Boolean> p = data.iterator();
        Iterator<Boolean> rp = right.data.iterator();
        while (p.hasNext() && rp.hasNext()) {
            if (p.next() != rp.next()) {
                return false;
            }
        }
        return true;
    }

    /** Overload of inequality operator */
    public boolean operatorNotEqual(BinaryString right) {
        if (right == null) {
            throw new IllegalArgumentException("right is NULL");
        }
        if (right.GetLength() != length) {
            return true;
        }
        Iterator<Boolean> p = data.iterator();
        Iterator<Boolean> rp = right.data.iterator();
        while (p.hasNext() && rp.hasNext()) {
            if (p.next() != rp.next()) {
                return true;
            }
        }
        return false;
    }

    /** Overload of assignment operator. Create a method like Copy when implementing outside C++. */
    public final BinaryString operatorPlus(BinaryString right) {
        if (!data.isEmpty()) {
            data.clear();
        }
        if (right.GetDataByBoolArray().isEmpty()) {
            throw new IllegalArgumentException("Data is NULL");
        }

        List<Boolean> v = right.GetDataByBoolArray();
        data.addAll(v);
        length = v.size();
        maxLength = v.size();
        return this;
    }

    /**
     * Overload of addition operator (binary string concatenation operator)
     * Conceptually similar to Perl's "0001" . "0101" resulting in "00010101".
     */
    public final BinaryString operatorXor(BinaryString right) {
        if (right.GetDataByBoolArray().isEmpty()) {
            throw new IllegalArgumentException("Data is NULL");
        }

        List<Boolean> temp = data;
        List<Boolean> v = right.GetDataByBoolArray();
        temp.addAll(v);
        return new BinaryString(temp);
    }

    /** Overload of additive assignment operator. Meaning is almost the same as addition operator. */
    public final BinaryString operatorLet(BinaryString right) {
        if (right.GetDataByBoolArray() == null) {
            throw new IllegalArgumentException("Data is NULL");
        }

        List<Boolean> v = right.GetDataByBoolArray();
        data.addAll(v);
        this.length = data.size();
        return this;
    }

    /** Overload of OR operator. */
    public final BinaryString operatorPlusLet(BinaryString right) {
        if (right.GetDataByBoolArray() == null) {
            throw new IllegalArgumentException("Data is NULL");
        }
        if (right.GetLength() != length) {
            throw new IllegalArgumentException("Length is not match");
        }
        List<Boolean> v = new ArrayList<>();
        Iterator<Boolean> p = data.iterator();
        Iterator<Boolean> rp = right.data.iterator();
        while (p.hasNext() && rp.hasNext()) {
            v.add(p.next().booleanValue() ^ rp.next().booleanValue());
        }
        return new BinaryString(v);
    }

    /** Overload of XOR assignment operator */
    public final BinaryString operatorXorLet(BinaryString right) {
        if (right.GetDataByBoolArray() == null) {
            throw new IllegalArgumentException("Data is NULL");
        }
        if (right.GetLength() != length) {
            throw new IllegalArgumentException("Length is not match");
        }
        Iterator<Boolean> p = data.iterator();
        Iterator<Boolean> rp = right.data.iterator();
        while (p.hasNext() && rp.hasNext()) {
            Boolean b = p.next();
            b = b.booleanValue() ^ rp.next().booleanValue(); // @@@
        }
        return this;
    }

    /**
     * Overload of () operator.
     * Extracts and returns count bits from the offset represented by index (bit).
     * Throws an exception if it exceeds the range.
     * Same as GetSubBit
     */
    public final BinaryString operatorFunction(int index, int count) {
        if (data.isEmpty() || index > length || index + count > length) {
            throw new IllegalArgumentException("index overflow");
        }
        List<Boolean> temp = new ArrayList<>(count);
        temp.addAll(data.subList(index, index + count));
        return new BinaryString(temp);
    }

    /**
     * Overload of [] operator
     * Returns only one bit at the index given by index.
     * Throws an exception if it exceeds the range.
     * Same as At
     */
    public final boolean operatorArray(int index) {
        if (data.isEmpty() || index > length) {
            throw new IllegalArgumentException("index overflow");
        }
        return data.get(index);
    }

    /**
     * Function to return data as a string
     * Returns a string composed of '0' and '1' like "01011111".
     */
    public String GetDataByString() {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Data NULL");
        }
        StringBuilder str = new StringBuilder();
        for (Boolean p : data) {
            if (p) {
                str.append("1");
            } else {
                str.append("0");
            }
        }
        return str.toString();
    }

    /** Function to return data as a bool array () operator */
    public List<Boolean> GetDataByBoolArray() {
        return data;
    }

    /**
     * Function to return data as an unsigned int array.
     * Converts each byte to an integer and stores it in an array to return.
     */
    public List<Integer> GetDataByUINTArray() {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Data NULL");
        }
        return GetSubByteByUINTArray(0, length / 8);
    }

    /**
     * Function to return data as a UCHAR array.
     * Converts each byte to an integer and stores it in an array to return.
     */
    public List<Byte> GetDataByUCHARArray() {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Data NULL");
        }
        return GetSubByteByUCHARArray(0, length / 8);
    }

    /** Returns Length. Length is in bits. */
    public int GetLength() {
        return length;
    }

    /** Returns MaxLength in bits. */
    public int GetMaxLength() {
        return maxLength;
    }

    /** Returns MaxLength in bytes. */
    public int GetMaxLengthByByte() {
        return maxLength / 8;
    }

    /** Sets MaxLength. In bits. */
    public void SetMaxLength(int len) {
        maxLength = len;
    }

    /** Sets maxLength in bytes. */
    public void SetMaxLengthByByte(int len) {
        maxLength = len * 8;
    }

    /** Extracts and returns count bits from the offset specified by index. */
    public BinaryString GetSubBit(int index, int count) {
        if (data.isEmpty() || index > length || index + count > length) {
            throw new IllegalArgumentException("index overflow");
        }
        List<Boolean> temp = new ArrayList<>(count);
        temp.addAll(data.subList(index, index + count));
        return new BinaryString(temp);
    }

    /** Function to convert the byte at the position specified by index to unsigned int and return. */
    public int getSubByte(int index) {
        if (data.isEmpty() || (index * 8) >= length) {
            throw new IllegalArgumentException("index overflow");
        }
        int temp = 0;
        int c = 0;
        for (int i = 7; index * 8 + c < data.size() && i >= 0; i--) {
            Boolean p = data.get(index * 8 + c);
            if (p) {
                temp += Math.pow(2, i);
            }
            c++;
        }
        return temp;
    }

    /** Function to convert the byte at the position specified by index to unsigned int and return. */
    public char GetSubByteByUCHAR(int index) {
        if (data.isEmpty() || (index * 8) >= length) {
            throw new IllegalArgumentException("index overflow");
        }
        char temp = 0;
        int c = 0;
        for (int i = 7; index * 8 + c < data.size() && i >= 0; i--) {
            Boolean p = data.get(index * 8 + c);
            if (p) {
                temp += Math.pow(2, i);
            }
            c++;
        }
        return temp;
    }

    /**
     * Function to convert Byte from the position specified by index to an unsigned int array and return.
     * 1 unsigned int per 8 bits
     */
    public List<Integer> GetSubByteByUINTArray(int index, int byte_) {
        if (data.isEmpty() || index * 8 >= length || index * 8 + byte_ * 8 > length) {
            throw new IllegalArgumentException("index overflow");
        }
        List<Integer> v = new ArrayList<>();
        for (int b = 0; b < byte_; b++) {
            int val = 0;
            for (int i = 0; i < 8; i++) {
                if (data.get((index + b) * 8 + i)) {
                    val |= (1 << (7 - i));
                }
            }
            v.add(val);
        }
        return v;
    }

    /**
     * Function to convert Byte from the position specified by index to a UCHAR array and return.
     * 1 block per 8 bits
     */
    public List<Byte> GetSubByteByUCHARArray(int index, int byte_) {
        if (data.isEmpty() || index * 8 >= length || index * 8 + byte_ * 8 > length) {
            throw new IllegalArgumentException("index overflow");
        }
        List<Byte> v = new ArrayList<>();
        for (int b = 0; b < byte_; b++) {
            int val = 0;
            for (int i = 0; i < 8; i++) {
                if (data.get((index + b) * 8 + i)) {
                    val |= (1 << (7 - i));
                }
            }
            v.add((byte) val);
        }
        return v;
    }

    /** Function to convert Byte from the position specified by index to a binary string and return. */
    public BinaryString GetSubByte(int index, int byte_) {
        if (data.isEmpty() || index > length || index + byte_ * 8 > length) {
            throw new IllegalArgumentException("Error : index overflow");
        }
        List<Boolean> v = new ArrayList<>();
        v.addAll(data.subList(index * 8, index * 8 + byte_ * 8));
        return new BinaryString(v);
    }

    /**
     * Utility
     * Function to clear data.
     * Only clears the data, does not clear maxLength.
     */
    public void clear() {
        if (!data.isEmpty()) {
            data.clear();
        }
        length = 0;
    }

    /** Same as [] function. Throws an exception if index exceeds the range. */
    public boolean at(int index) {
        if (data.isEmpty() || index > length) {
            throw new IllegalArgumentException("index overflow");
        }
        return data.get(index);
    }

    /** Same as + operator. */
    public final BinaryString add(BinaryString temp) {
        if (temp.GetDataByBoolArray() == null) {
            throw new IllegalArgumentException("index overflow");
        }
        if (length + temp.GetLength() > maxLength) {
            throw new IllegalArgumentException("Data Overflow");
        }

        data.addAll(temp.GetDataByBoolArray());
        length = data.size();
        return this;
    }

    /** Inverts the bit at the index. */
    void flip(int index) {
        if (data.isEmpty() || index >= length) {
            throw new IllegalArgumentException("Error : index overflow @CBinaryString::Flip");
        }
        data.set(index, !data.get(index).booleanValue());
    }

    /** Inserts 1 bit at the end of the array. */
    void add(boolean bit) {
        this.length += 1;
        data.add(bit);
        if (this.maxLength < this.length) {
            this.maxLength = this.length;
        }
    }

    /** Append data to the end */
    public BinaryString append(BinaryString other) {
        if (other != null && other.GetDataByBoolArray() != null) {
            data.addAll(other.GetDataByBoolArray());
            this.length = data.size();
            if (this.maxLength < this.length) {
                this.maxLength = this.length;
            }
        }
        return this;
    }

    /** Append 1 bit to the end */
    public void append(boolean bit) {
        add(bit);
    }

    /** XOR operation */
    public BinaryString xor(BinaryString other) {
        if (other == null || other.GetDataByBoolArray() == null) {
            throw new IllegalArgumentException("Data is NULL");
        }
        List<Boolean> result = new ArrayList<>();
        int len = Math.min(this.length, other.length);
        for (int i = 0; i < len; i++) {
            result.add(this.at(i) ^ other.at(i));
        }
        return new BinaryString(result);
    }

    /**
     * Function to free data.
     * Resets the values of all data members.
     */
    private void release() {
        if (!data.isEmpty()) {
            data.clear();
        }
        this.length = 0;
        maxLength = 0;
    }

    /** Data */
    private List<Boolean> data = new ArrayList<>();

    /** Current length (length up to where data is stored) */
    private int length;

    /** Maximum length */
    private int maxLength;
}
