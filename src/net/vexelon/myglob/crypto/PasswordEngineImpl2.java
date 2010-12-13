package net.vexelon.myglob.crypto;

import net.vexelon.myglob.utils.Base64;

public class PasswordEngineImpl2 implements PasswordEngine {

	//256 bit key
	private static final byte[] key = {-33, 61, -77, -2, -124, -82, -12, -97, 84, -2, 4, 36, 26, 51, 71, -107, -69, 25, -45, -113, 73, 77, -25, -125, 92, 31, 81, 81, 8, 99, 74, 125};
	private static final byte[] iv = {-127, -108, -110, 68, -16, 7, -36, -79, -34, 16, -117, 50, 5, -97, -88, 11};
	 
	private Crypto _crypto;
	
	public PasswordEngineImpl2(Crypto crypto) {
		_crypto = crypto;
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
