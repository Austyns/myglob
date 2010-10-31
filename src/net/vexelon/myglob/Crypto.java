package net.vexelon.myglob;

import java.security.NoSuchAlgorithmException;

public interface Crypto {
	
	public byte[] createSecretKey() throws NoSuchAlgorithmException;	
	
	public byte[] encrypt(byte[] input, byte[] secretKey)
		throws Exception;
	
	public byte[] decrypt(byte[] input, byte[] secretKey)
		throws Exception;

}
