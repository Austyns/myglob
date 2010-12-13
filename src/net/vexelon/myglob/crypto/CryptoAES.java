/*
 * The MIT License
 * 
 * Copyright (c) 2010 Petar Petrov
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.vexelon.myglob.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoAES implements Crypto {
	
	private static CryptoAES INSTANCE = null; 
	
	private final static String ALGORITHM = "AES";
	private final static String TRANSFORM = "AES/CBC/PKCS5Padding";
	//private final static int KEY_SIZE = 256;
	
	private CryptoAES() {
	}
	
	public static Crypto getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CryptoAES();
			
			//INSTANCE.init(); // setup
		}
		return INSTANCE;
	}
	
	/**
	 * Creates a secret key
	 * @return encoded data of the created key
	 */
	public byte[] createSecretKey(int keysize) 
		throws NoSuchAlgorithmException {
		
		KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
		kg.init(keysize, new SecureRandom(
				String.valueOf(System.currentTimeMillis()).getBytes())
				);
		SecretKey key = kg.generateKey();
		
		return key.getEncoded();
	}

	@Override
	public byte[] decrypt(byte[] input, byte[] secretKey, byte[] iv)
		throws Exception {
		
		SecretKey key = new SecretKeySpec(secretKey, ALGORITHM);
		Cipher cipher = Cipher.getInstance(TRANSFORM);
		IvParameterSpec ips = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, key, ips);
		return cipher.doFinal(input);
	}

	@Override
	public byte[] encrypt(byte[] input, byte[] secretKey, byte[] iv) 
		throws Exception {
		
		SecretKey key = new SecretKeySpec(secretKey, ALGORITHM);
		Cipher cipher = Cipher.getInstance(TRANSFORM);
		IvParameterSpec ips = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, key, ips);
		return cipher.doFinal(input);		
	}

}
