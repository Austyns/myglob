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

public class PasswordEngineImpl2 implements PasswordEngine {
	
	private static PasswordEngineImpl2 _INSTANCE = null; 

	//256 bit key
	private static final byte[] key = {-33, 61, -77, -2, -124, -82, -12, -97, 84, -2, 4, 36, 26, 51, 71, -107, -69, 25, -45, -113, 73, 77, -25, -125, 92, 31, 81, 81, 8, 99, 74, 125};
	private static final byte[] iv = {-127, -108, -110, 68, -16, 7, -36, -79, -34, 16, -117, 50, 5, -97, -88, 11};
	 
	private Crypto _crypto;
	
	public static PasswordEngineImpl2 getInstance() {
		if (_INSTANCE == null)
			_INSTANCE = new PasswordEngineImpl2();
		
		return _INSTANCE;
	}
	
	public PasswordEngineImpl2() {
		_crypto = CryptoAES.getInstance();
	}
	
	@Override
	public String encryptAndEncode(String rawPassword) throws Exception {
		byte[] encrypted = _crypto.encrypt(rawPassword.getBytes(), key, iv);
		return Base64.encodeBytes(encrypted);
	}
	
	@Override
	public String decodeAndDecrypt(String encodedPassword) throws Exception {
		byte[] decrypted = _crypto.decrypt(Base64.decode(encodedPassword), key, iv);
		return new String(decrypted);
	}

}
