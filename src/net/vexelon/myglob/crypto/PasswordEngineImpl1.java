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

import net.vexelon.myglob.utils.Base64;

public class PasswordEngineImpl1 implements PasswordEngine {
	
	private static PasswordEngineImpl1 _INSTANCE = null; 

	//128 bit key
	private final static byte[] iv = { 0x28, 0x1A, 0x03, 0x78, 0x4F, 0x12, 0x62, 0x04, 0x55, 0x07, 0x3A, 0x6F, 0x1B, 0x12, 0x0B, 0x01 };
	 
	private Crypto _crypto;
	private byte[] _key;
	
	public static PasswordEngineImpl1 getInstance(String encodedKey) throws Exception {
		if (_INSTANCE == null)
			_INSTANCE = new PasswordEngineImpl1(encodedKey);
		
		return _INSTANCE;
	}	
	
	private PasswordEngineImpl1(String encodedKey) throws Exception {
		_crypto = CryptoAES.getInstance();
		_key = Base64.decode(encodedKey);
	}
	
	@Override
	public String encryptAndEncode(String rawPassword) throws Exception {
		byte[] encrypted = _crypto.encrypt(rawPassword.getBytes(), _key, iv);
		return Base64.encodeBytes(encrypted);
	}
	
	@Override
	public String decodeAndDecrypt(String encodedPassword) throws Exception {
		byte[] decrypted = _crypto.decrypt(Base64.decode(encodedPassword), _key, iv);
		return new String(decrypted);
	}

}
