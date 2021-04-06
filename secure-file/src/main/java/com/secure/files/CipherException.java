package com.secure.files;

public class CipherException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CipherException(final String msg, final Throwable ex) {
		super(msg, ex);
	}

}
