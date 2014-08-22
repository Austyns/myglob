/*
 * MyGlob Android Application
 * 
 * Copyright (C) 2010 Petar Petrov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
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
