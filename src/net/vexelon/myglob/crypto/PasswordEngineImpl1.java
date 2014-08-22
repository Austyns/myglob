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
