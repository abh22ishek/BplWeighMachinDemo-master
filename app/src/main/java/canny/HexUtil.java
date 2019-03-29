package canny;

import logger.*;

public class HexUtil {

    private static final char[] DIGITS_LOWER =
            new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] DIGITS_UPPER =
            new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public HexUtil() {
    }

    public static char[] encodeHex(byte[] var0) {
        return encodeHex(var0, true);
    }

    public static char[] encodeHex(byte[] var0, boolean var1) {
        return encodeHex(var0, var1 ? DIGITS_LOWER : DIGITS_UPPER);
    }

    protected static char[] encodeHex(byte[] var0, char[] var1) {
        if (var0 == null) {
            return null;
        } else {
            int var2;
            char[] var3 = new char[(var2 = var0.length) << 1];
            int var4 = 0;

            for(int var5 = 0; var4 < var2; ++var4) {
                var3[var5++] = var1[(240 & var0[var4]) >>> 4];
                var3[var5++] = var1[15 & var0[var4]];
            }

            return var3;
        }
    }

    public static String encodeHexStr(byte[] var0) {
        return encodeHexStr(var0, true);
    }

    public static String encodeHexStr(byte[] var0, boolean var1) {
        return encodeHexStr(var0, var1 ? DIGITS_LOWER : DIGITS_UPPER);
    }

    protected static String encodeHexStr(byte[] var0, char[] var1) {
        return new String(encodeHex(var0, var1));
    }

    public static byte[] decodeHex(char[] var0) {
        int var1;
        if (((var1 = var0.length) & 1) != 0) {
            throw new RuntimeException("Odd number of characters.");
        } else {
            byte[] var2 = new byte[var1 >> 1];
            int var3 = 0;

            for(int var4 = 0; var4 < var1; ++var3) {
                int var5 = toDigit(var0[var4], var4) << 4;
                ++var4;
                var5 |= toDigit(var0[var4], var4);
                ++var4;
                var2[var3] = (byte)var5;
            }

            return var2;
        }
    }

    protected static int toDigit(char var0, int var1) {
        int var2;
        if ((var2 = Character.digit(var0, 16)) == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + var0 + " at index " + var1);
        } else {
            return var2;
        }
    }

    public static byte[] hexStringToBytes(String var0) {
        if (var0 != null && !var0.equals("")) {
            int var1 = (var0 = var0.toUpperCase()).length() / 2;
            char[] var5 = var0.toCharArray();
            byte[] var2 = new byte[var1];

            for(int var3 = 0; var3 < var1; ++var3) {
                int var4 = var3 << 1;
                var2[var3] = (byte)(charToByte(var5[var4]) << 4 | charToByte(var5[var4 + 1]));
            }

            return var2;
        } else {
            return null;
        }
    }

    public static byte charToByte(char var0) {
        return (byte)"0123456789ABCDEF".indexOf(var0);
    }

    public static String extractData(byte[] var0, int var1) {
        return encodeHexStr(new byte[]{var0[var1]});
    }

    public static int byteToInt(byte b) {
        return b & 0XFF;
    }




    public static String byteToHexVal(byte [] bytes)
    {

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        Logger.log(Level.DEBUG,"Hex Dec  Val from Byte Array[] ",sb.toString());

        return sb.toString();
    }


    public static String byteToHex(byte val)
    {

        StringBuilder sb = new StringBuilder();

            sb.append(String.format("%02X", val));

        Logger.log(Level.DEBUG,"Hex Dec from Byte Val ",sb.toString());

        return sb.toString();
    }
}
