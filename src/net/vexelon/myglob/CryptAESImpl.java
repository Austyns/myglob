package net.vexelon.myglob;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptAESImpl implements Crypto {
	
	private static CryptAESImpl INSTANCE = null; 
	
	private final static byte[] IV = { 0x28, 0x1A, 0x03, 0x78, 0x4F, 0x12, 0x62, 0x04, 0x55, 0x07, 0x3A, 0x6F, 0x1B, 0x12, 0x0B, 0x01 };
	private final static String ALGORITHM = "AES";
	private final static String TRANSFORM = "AES/CBC/PKCS5Padding";
	private final static int KEY_SIZE = 128;
	
	private CryptAESImpl() {
	}
	
	public static Crypto getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CryptAESImpl();
			
			//INSTANCE.init(); // setup
		}
		return INSTANCE;
	}
	
	/**
	 * Creates a secret key
	 * @return encoded data of the created key
	 */
	public byte[] createSecretKey() 
		throws NoSuchAlgorithmException {
		
		KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
		kg.init(KEY_SIZE, new SecureRandom(
				String.valueOf(System.currentTimeMillis()).getBytes())
				);
		SecretKey key = kg.generateKey();
		
		return key.getEncoded();
	}

	@Override
	public byte[] decrypt(byte[] input, byte[] secretKey)
		throws Exception {
		
		SecretKey key = new SecretKeySpec(secretKey, ALGORITHM);
		Cipher cipher = Cipher.getInstance(TRANSFORM);
		IvParameterSpec ips = new IvParameterSpec(IV);
		cipher.init(Cipher.DECRYPT_MODE, key, ips);
		return cipher.doFinal(input);
	}

	@Override
	public byte[] encrypt(byte[] input, byte[] secretKey) 
		throws Exception {
		
		SecretKey key = new SecretKeySpec(secretKey, ALGORITHM);
		Cipher cipher = Cipher.getInstance(TRANSFORM);
		IvParameterSpec ips = new IvParameterSpec(IV);
		cipher.init(Cipher.ENCRYPT_MODE, key, ips);
		return cipher.doFinal(input);		
	}

}
