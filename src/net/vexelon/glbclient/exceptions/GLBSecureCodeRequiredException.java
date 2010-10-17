package net.vexelon.glbclient.exceptions;

public class GLBSecureCodeRequiredException extends Exception {

	private String secureCodeImageUrl;

	public GLBSecureCodeRequiredException(String secureCodeImageUrl) {
		
		this.secureCodeImageUrl = secureCodeImageUrl;
	}
	
	public String getSecureCodeImageUrl() {
		return this.secureCodeImageUrl;
	}
	
}
