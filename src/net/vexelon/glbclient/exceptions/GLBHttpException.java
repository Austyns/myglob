package net.vexelon.glbclient.exceptions;

public class GLBHttpException extends Exception {

	private int statusCode;

	public GLBHttpException(String message, int statusCode) {
		super(message);
		
		this.statusCode = statusCode;
	}
	
	public int getStatusCode() {
		return this.statusCode;
	}
}
