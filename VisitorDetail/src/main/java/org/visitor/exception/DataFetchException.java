package org.visitor.exception;

public class DataFetchException extends Exception {
	private int errorCode;
	
	public DataFetchException() {
	}
	
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public DataFetchException(String message) {
		super(message);
	}

	public DataFetchException(Throwable cause) {
		super(cause);
	}

	public DataFetchException(String message, Throwable cause) {
		super(message, cause);
	}
}
