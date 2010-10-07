package net.vexelon.myglob;

public class CryptAESImpl implements Crypto {
	
	private static CryptAESImpl _instance = null; 
	
	protected CryptAESImpl() {
		
	}
	
	public static CryptAESImpl _INSTANCE() {
		if (_instance == null) {
			_instance = new CryptAESImpl();
			_instance.init();
		}
		return _instance;
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
