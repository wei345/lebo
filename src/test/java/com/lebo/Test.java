package com.lebo;

import org.springside.modules.utils.Encodes;

import java.math.BigInteger;

import static com.mongodb.util.MyAsserts.assertTrue;

/**
 * @author: Wei Liu
 * Date: 13-7-4
 * Time: AM8:37
 */
public class Test {

    @org.junit.Test
    public void test() {
        String mongoDbId = "51d415ce1a88bbf2750b0daa";
        byte[] bytes = Encodes.decodeHex(mongoDbId);
        String base62Str = Encodes.encodeBase62(bytes);

        BigInteger bigi = Base62.decode(base62Str);
        byte[] bytes2 = bigi.toByteArray();

        assertTrue(bytes.length == bytes2.length);
        for (int i = 0; i < bytes.length; i++) {
            assertTrue(bytes[i] == bytes2[i]);
        }

        System.out.println(base62Str);
    }
}

class Base62 {
    public static final BigInteger BASE = BigInteger.valueOf(62);
    public static final String DIGITS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static final String REGEXP = "^[0-9A-Za-z]+$";

    /**
     * Encodes a number using Base62 encoding.
     *
     * @param number a positive integer
     * @return a Base62 string
     * @throws IllegalArgumentException if <code>number</code> is a negative integer
     */
    public static String encode(BigInteger number) {
        if (number.compareTo(BigInteger.ZERO) == -1) { // number < 0
            throw new IllegalArgumentException("number must not be negative");
        }
        StringBuilder result = new StringBuilder();
        while (number.compareTo(BigInteger.ZERO) == 1) { // number > 0
            BigInteger[] divmod = number.divideAndRemainder(BASE);
            number = divmod[0];
            int digit = divmod[1].intValue();
            result.insert(0, DIGITS.charAt(digit));
        }
        return (result.length() == 0) ? DIGITS.substring(0, 1) : result.toString();
    }

    public static String encode(long number) {
        return encode(BigInteger.valueOf(number));
    }

    /**
     * Decodes a string using Base62 encoding.
     *
     * @param string a Base62 string
     * @return a positive integer
     * @throws IllegalArgumentException if <code>string</code> is empty
     */
    public static BigInteger decode(final String string) {
        if (string.length() == 0) {
            throw new IllegalArgumentException("string must not be empty");
        }
        BigInteger result = BigInteger.ZERO;
        int digits = string.length();
        for (int index = 0; index < digits; index++) {
            int digit = DIGITS.indexOf(string.charAt(digits - index - 1));
            result = result.add(BigInteger.valueOf(digit).multiply(BASE.pow(index)));
        }
        return result;
    }

    @org.junit.Test
    public void urlEncode(){
        System.out.println(Encodes.urlEncode("626070255@qq.com"));
    }
}
