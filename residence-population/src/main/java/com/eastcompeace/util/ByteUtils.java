package com.eastcompeace.util;

/**
 * 2017-06-26
 * @author eastcompeace
 *
 */
public final class ByteUtils {
	
	public final static int PAD_LEFT = 1;
	public final static int PAD_RIGHT = 2;
	
	
	/**
	 * Convert the byte to Hex string
	 * 
	 * @param data The short data
	 * @return
	 */
	public static String toHexString(short data) {
		return ByteUtils.toHexString(ByteUtils.toBytes(data));		
	}
	
	/**
	 * Convert the byte to Hex string
	 * 
	 * @param data The integer data
	 * @return
	 */
	public static String toHexString(int data) {
		return ByteUtils.toHexString(ByteUtils.toBytes(data));		
	}
	
	/**
	 * Convert the byte to Hex string
	 * 
	 * @param data The byte data
	 * @return
	 */
	public static String toHexString(byte data) {
		return ByteUtils.toHexString(new byte[]{ data });
	}
	
	/**
	 * Convert the byte array to Hex string
	 * 
	 * @param data The byte array
	 * @return
	 */
	public static String toHexString(byte[] data) {
		if (data == null) return null;
		
		StringBuffer sb = new StringBuffer(128);
		int len = data.length;
		for (int i = 0; i < len; i++) {
			sb.append(Character.forDigit((data[i] >> 4) & 0x0f, 16));
			sb.append(Character.forDigit(data[i] & 0x0f, 16));
		}
		return sb.toString().toUpperCase();
	}
	
	/**
	 * Convert integer to byte array
	 * 
	 * @param num the short
	 * @return
	 */
	public static byte[] toBytes(short n) {
		byte[] arrBytes = new byte[2];
		for (int i=0; i<2; i++) {
			arrBytes[i] = (byte)((n >>> (8-i*8)) & 0xff);
		}
		return arrBytes;
    }
	
	/**
	 * Convert integer to byte array
	 * 
	 * @param num the integer
	 * @return
	 */
	public static byte[] toBytes(int n) {
		byte[] arrBytes = new byte[4];
		for (int i=0; i<4; i++) {
			arrBytes[i] = (byte)(n >>> (24-i*8) & 0xff);
		}
		return arrBytes;
    }
	
	/**
	 * Convert integer to byte array
	 * 
	 * @param num the long
	 * @return
	 */
	public static byte[] toBytes(long n) {
		byte[] arrBytes = new byte[8];
		for (int i=0; i<8; i++) {
			arrBytes[8 - 1 - i] = (byte)((n >>> (i * 8)) & 0xff);
		}
		return arrBytes;
    }
	
	/**
	 * Convert char to byte array
	 * 
	 * @param ch the char
	 * @return
	 */
	public static byte[] toBytes(char c) {
		int tmp = c;
		byte[] result = new byte[2];
		result[0] = (byte) ((tmp >> 8) & 0xff);
		result[1] = (byte) (tmp & 0xff);
		return result;
	}
	
	/**
	 * Convert hext string to byte array
	 * 
	 * @param str The hex string
	 * @return
	 */
	public static byte[] toBytes(String strHex) {
		if (strHex.length() % 2 != 0) strHex = "0" + strHex;
		
		int length = strHex.length() / 2;
		byte[] result = new byte[length];
		char[] chars = strHex.toCharArray();
		
		for (int i = 0; i < length; i++) {
			int high = Character.digit(chars[i * 2], 16);
			int low = Character.digit(chars[i * 2 + 1], 16);
			
			high = (high << 4) & 0xf0;
			low = low & 0x0f;
			result[i] = (byte)(high | low);
		}
		return result;
	}
	
	/**
	 * Convert string to byte
	 * 
	 * @param str The hex string
	 * @return
	 */
	public static byte toByte(String strHex) {
		return ByteUtils.toBytes(strHex)[0];
	}
	
	
	/**
	 * Padding the specified byte array
	 * 
	 * @param src the source byte array
	 * @param length the length after padding
	 * @param data the byte to be padded
	 * @param type the padding mode, ByteUtils.PAD_LEFT - left padding, ByteUtils.PAD_RIGHT - right padding
	 * @return
	 */
	public static byte[] padding(byte[] src, int length, byte data, int type) {
		if (src == null) return src;
		if (length < 1 || length <= src.length) return src;
		
		int n = length - src.length;
		byte[] buffer = new byte[n];
		for (int i=0; i<n; i++) {
			buffer[i] = data;
		}
		
		return ByteUtils.padding(src, length, buffer, type);
	}
	
	/**
	 * Padding the specified byte array
	 * 
	 * @param src the source byte array
	 * @param length the length after padding
	 * @param data the byte array to be padded
	 * @param type the padding mode, ByteUtils.PAD_LEFT - left padding, ByteUtils.PAD_RIGHT - right padding
	 * @return
	 */
	public static byte[] padding(byte[] src, int length, byte[] data, int type) {
		if (src == null || data == null) return src;
		if (length < 1 || length <= src.length) return src;
		if (length > src.length + data.length) length = src.length + data.length;
		
		int n = length - src.length;
		byte[] buffer = null;
		if (n == data.length) {
			buffer = data;
		} else {
			buffer = new byte[n];
			System.arraycopy(data, 0, buffer, 0, n);
		}
		
		byte[] arrBytes = new byte[length];
		if (type == ByteUtils.PAD_LEFT) {
			System.arraycopy(buffer, 0, arrBytes, 0, buffer.length);
			System.arraycopy(src, 0, arrBytes, buffer.length, src.length);
		} else if (type == ByteUtils.PAD_RIGHT) {
			System.arraycopy(src, 0, arrBytes, 0, src.length);
			System.arraycopy(buffer, 0, arrBytes, src.length, buffer.length);
		}
		
		return arrBytes;
	}
	
	public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

        }
        return d;
    }
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
	
}
