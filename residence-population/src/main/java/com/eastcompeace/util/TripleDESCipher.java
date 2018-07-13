package com.eastcompeace.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 2017-06-26
 * @author eastcompeace
 *
 */
public class TripleDESCipher {
	
	/**
	 * Encrypt byte array by <code>Triple DES ECB</code> arithmetic
	 * 
	 * @param data The string to encrypt, the length must be multiple of 16
	 * @param key The key, the length must be 32
	 */
	public static String encrypt(String data, String key) {
		byte[] buffer = TripleDESCipher.encrypt(ByteUtils.toBytes(data), ByteUtils.toBytes(key));
				
		return ByteUtils.toHexString(buffer);
	}
	
	/**
	 * Encrypt byte array by <code>Triple DES ECB</code> arithmetic
	 * 
	 * @param data The byte array to encrypt, the length must be multiple of 8 bytes
	 * @param key The key, the length must be 16 bytes
	 */
	public static byte[] encrypt(byte[] data, byte[] key) throws ArithmeticException {
		return TripleDESCipher.doCipher(data, key, Cipher.ENCRYPT_MODE);
	}
	
	/**
	 * Decrypt byte array by <code>Triple DES ECB</code> arithmetic
	 * 
	 * @param data The string to decrypt, the length must be multiple of 16
	 * @param key The key, the length must be 32
	 */
	public static String decrypt(String data, String key) {
		byte[] buffer = TripleDESCipher.decrypt(ByteUtils.toBytes(data), ByteUtils.toBytes(key));
		
		return ByteUtils.toHexString(buffer);
	}
	
	/**
	 * Decrypt byte array by <code>Triple DES ECB</code> arithmetic
	 * 
	 * @param data The byte array to decrypt, the length must be multiple of 8 bytes
	 * @param key The key, the length must be 16 bytes
	 */
	public static byte[] decrypt(byte[] data, byte[] key) throws ArithmeticException {
		return TripleDESCipher.doCipher(data, key, Cipher.DECRYPT_MODE);
	}
	
	
	/**
	 * Encrypt byte array by <code>Triple DES CBC</code> arithmetic
	 * 
	 * @param IV The initial vector
	 * @param data The string to encrypt, the length must be multiple of 16
	 * @param key The key, the length must be 32
	 */
	public static String encrypt(String IV, String data, String key) {
		byte[] buffer = TripleDESCipher.encrypt(ByteUtils.toBytes(IV), ByteUtils.toBytes(data), ByteUtils.toBytes(key));
				
		return ByteUtils.toHexString(buffer);
	}
	
	/**
	 * Encrypt byte array by <code>Triple DES CBC</code> arithmetic
	 * 
	 * @param IV The initial vector, null to be set 0x0000000000000000
	 * @param data The byte array to encrypt, the length must be multiple of 8 bytes
	 * @param key The key, the length must be 16 bytes
	 */
	public static byte[] encrypt(byte[] IV, byte[] data, byte[] key) throws ArithmeticException {
		return TripleDESCipher.doCipher(IV, data, key, Cipher.ENCRYPT_MODE);
	}
	
	/**
	 * Decrypt byte array by <code>Triple DES CBC</code> arithmetic
	 * 
	 * @param IV The initial vector
	 * @param data The string to encrypt, the length must be multiple of 16
	 * @param key The key, the length must be 32
	 */
	public static String decrypt(String IV, String data, String key) {
		byte[] buffer = TripleDESCipher.decrypt(ByteUtils.toBytes(IV), ByteUtils.toBytes(data), ByteUtils.toBytes(key));
				
		return ByteUtils.toHexString(buffer);
	}
	
	/**
	 * Decrypt byte array by <code>Triple DES CBC</code> arithmetic
	 * 
	 * @param IV The initial vector, null to be set 0x0000000000000000
	 * @param data The byte array to encrypt, the length must be multiple of 8 bytes
	 * @param key The key, the length must be 16 bytes
	 */
	public static byte[] decrypt(byte[] IV, byte[] data, byte[] key) throws ArithmeticException {
		return TripleDESCipher.doCipher(IV, data, key, Cipher.DECRYPT_MODE);
	}
	
	
	
	/**
	 * Execute encrypt/decrypt byte array by <code>Triple DES ECB</code> arithmetic
	 * 
	 * @param data The byte array to decrypt, the length must be multiple of 8 bytes
	 * @param key The key, the length must be 16 bytes
	 * @param mode The cipher mode, can be set to Cipher.DECRYPT_MODE, Cipher.ENCRYPT_MODE
	 */
	private static byte[] doCipher(byte[] data, byte[] key, int mode) throws ArithmeticException {
		if (data == null || key == null) throw new ArithmeticException("The data or key can not be null.");
		if (data.length % 8 != 0 || key.length != 16) throw new ArithmeticException("The length of data or key is incorrect.");
		
		try {
			byte[] newKey = new byte[24];
			System.arraycopy(key, 0, newKey, 0, 16);
			System.arraycopy(key, 0, newKey, 16, 8);
			
			DESedeKeySpec spec = new DESedeKeySpec(newKey);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
			SecretKey secureKey = factory.generateSecret(spec);
			
			Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
			cipher.init(mode, secureKey);
			
			byte[] buffer = cipher.doFinal(data);
			
			return buffer;
		} catch (Exception ex) {
			throw new ArithmeticException(ex.getMessage());
		}
	}
	
	/**
	 * Execute encrypt/decrypt byte array by <code>Triple DES CBC</code> arithmetic
	 * 
	 * @param IV The initial vector, null to be set 0x0000000000000000
	 * @param data The byte array to encrypt, the length must be multiple of 8 bytes
	 * @param key The key, the length must be 16 bytes
	 * @param mode The cipher mode, can be set to Cipher.DECRYPT_MODE, Cipher.ENCRYPT_MODE
	 */
	private static byte[] doCipher(byte[] IV, byte[] data, byte[] key, int mode) throws ArithmeticException {
		if (IV == null) {
			byte[] DEFAULT_IV = { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00 };
			IV = DEFAULT_IV;
		} else if (IV.length % 8 != 0) {
			throw new ArithmeticException("The length of initial vector is incorrect!");
		}
		
		if (data == null || key == null) throw new ArithmeticException("The data or key can not be null.");
		if (data.length % 8 != 0 || key.length != 16) throw new ArithmeticException("The length of data or key is incorrect.");
		
		try {
			byte[] newKey = new byte[24];
			System.arraycopy(key, 0, newKey, 0, 16);
			System.arraycopy(key, 0, newKey, 16, 8);
			
			DESedeKeySpec spec = new DESedeKeySpec(newKey);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
			SecretKey secureKey = factory.generateSecret(spec);
			
			SecureRandom sr = new SecureRandom();
			IvParameterSpec params = new IvParameterSpec(IV);
			
			Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
			cipher.init(mode, secureKey, params, sr);
			
			byte[] buffer = cipher.doFinal(data);
			
			return buffer;
		} catch (Exception ex) {
			throw new ArithmeticException(ex.getMessage());
		}
	}
	
}
