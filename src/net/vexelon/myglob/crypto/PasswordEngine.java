package net.vexelon.myglob.crypto;

public interface PasswordEngine {
	
	public String encryptAndEncode(String rawPassword)
		throws Exception;
	
	public String decodeAndDecrypt(String encodedPassword)
		throws Exception;

}
