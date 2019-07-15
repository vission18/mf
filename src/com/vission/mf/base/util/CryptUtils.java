package com.vission.mf.base.util;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public final class CryptUtils {

	private final static BASE64Encoder base64encoder = new BASE64Encoder();
	private final static BASE64Decoder base64decoder = new BASE64Decoder();
	private final static String encoding = "UTF-8";
	public static final String key = "MjAxMDAxMDE=";

	/**
	 * 加密字符串
	 */
	public static String getEncString(String str, String key) {
		String result = str;
		if (str != null && str.length() > 0) {
			try {
				byte[] encodeByte = symmetricEncrypto(str.getBytes(encoding), key);
				result = base64encoder.encode(encodeByte);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 解密字符串
	 */
	public static String getDesString(String str, String key) {
		String result = str;
		if (str != null && str.length() > 0) {
			try {
				byte[] encodeByte = base64decoder.decodeBuffer(str);
				byte[] decoder = CryptUtils.symmetricDecrypto(encodeByte, key);
				result = new String(decoder, encoding);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 对称加密方法
	 * 
	 * @param byteSource
	 *            需要加密的数据
	 * @return 经过加密的数据
	 * @throws Exception
	 */
	public static byte[] symmetricEncrypto(byte[] byteSource, String strKey) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			int mode = Cipher.ENCRYPT_MODE;
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			DESKeySpec keySpec = new DESKeySpec(strKey.getBytes());
			Key key = keyFactory.generateSecret(keySpec);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(mode, key);
			byte[] result = cipher.doFinal(byteSource);
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			baos.close();
		}
	}

	/**
	 * 对称解密方法
	 * 
	 * @param byteSource
	 *            需要解密的数据
	 * @return 经过解密的数据
	 * @throws Exception
	 */
	public static byte[] symmetricDecrypto(byte[] byteSource, String strKey) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			int mode = Cipher.DECRYPT_MODE;
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			DESKeySpec keySpec = new DESKeySpec(strKey.getBytes());
			Key key = keyFactory.generateSecret(keySpec);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(mode, key);
			byte[] result = cipher.doFinal(byteSource);
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			baos.close();
		}
	}

	/**
	 * 取随机KEY
	 */
	public static String getRandomKey(){
		String key = String.valueOf(Math.round(Math.random()*100000000));
		return base64encoder.encode(key.getBytes());
	}
	
	
	public static void main(String[] args) throws Exception {
		String strEnc = CryptUtils.getEncString("app1",key);// 加密字符串,
		// 返回String的密文
		System.out.println(strEnc);
		String strDes = CryptUtils.getDesString(strEnc,key);// 把String
		// 类型的密文解密
		System.out.println(strDes);
	}
}