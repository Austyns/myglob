package net.vexelon.myglob;

public interface Crypto {
	
	public byte[] encrypt(byte[] input);
	
	public byte[] decrypt(byte[] input);

}
