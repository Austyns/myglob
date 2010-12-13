package net.vexelon.myglob.crypto;

import net.vexelon.myglob.utils.Base64;

public class PasswordEngineImpl1 implements PasswordEngine {

	//128 bit key
	private final static byte[] iv = { 0x28, 0x1A, 0x03, 0x78, 0x4F, 0x12, 0x62, 0x04, 0x55, 0x07, 0x3A, 0x6F, 0x1B, 0x12, 0x0B, 0x01 };
	 
	private Crypto _crypto;
	private byte[] _key;
	
	public PasswordEngineImpl1(Crypto crypto, String encodedKey) throws Exception {
		_crypto = crypto;
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
