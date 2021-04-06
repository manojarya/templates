package com.secure.files;

public class SecureFileOpException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SecureFileOpException(final String msg, final Throwable ex) {
		super(msg, ex);
	}

}
