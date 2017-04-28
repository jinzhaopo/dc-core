package com.dc.common.util;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.Assert;
/**
 * 
 * @ClassName: RSAUtils
 * @Description: 前端加密工具类
 * @author: zhaopo
 * @date: 2016年11月4日 下午2:04:58
 */
public final class RSAUtils {

	private static final Provider provider = new BouncyCastleProvider();
	private static final int keysize = 1024;

	public static KeyPair generateKeyPair() {
		KeyPairGenerator keypairgenerator = null;
		try {
			keypairgenerator = KeyPairGenerator.getInstance("RSA", provider);
			keypairgenerator.initialize(keysize, new SecureRandom());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return keypairgenerator.generateKeyPair();
	}

	public static byte[] encrypt(PublicKey publicKey, byte data[]) {
		Assert.notNull(publicKey);
		Assert.notNull(data);
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA", provider);
			cipher.init(1, publicKey);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String encrypt(PublicKey publicKey, String text) {
		Assert.notNull(publicKey);
		Assert.notNull(text);
		byte abyte0[] = encrypt(publicKey, text.getBytes());
		return abyte0 == null ? null : Base64.encodeBase64String(abyte0);
	}

	public static byte[] decrypt(PrivateKey privateKey, byte data[]) {
		Assert.notNull(privateKey);
		Assert.notNull(data);
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", provider);
			cipher.init(2, privateKey);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decrypt(PrivateKey privateKey, String text) {
		Assert.notNull(privateKey);
		Assert.notNull(text);
		byte abyte0[] = decrypt(privateKey, Base64.decodeBase64(text));
		return abyte0 == null ? null : new String(abyte0);
	}

}
