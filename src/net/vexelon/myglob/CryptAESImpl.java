package net.vexelon.myglob;

public class CryptAESImpl implements Crypto {
	
	private static CryptAESImpl INSTANCE = null; 
	
	private CryptAESImpl() {
	}
	
	public static CryptAESImpl getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CryptAESImpl();
			
			INSTANCE.init(); // setup
		}
		return INSTANCE;
	}
	
	private void init() {
		//TODO:
	}

	@Override
	public byte[] decrypt(byte[] input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] encrypt(byte[] input) {
		// TODO Auto-generated method stub
		return null;
	}

}
