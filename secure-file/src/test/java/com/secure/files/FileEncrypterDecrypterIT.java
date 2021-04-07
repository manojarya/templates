package com.secure.files;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class FileEncrypterDecrypterIT {

	private static final Logger logger = LoggerFactory.getLogger(FileEncrypterDecrypterIT.class);

	@Test
	public void encrypt_decrypt_file() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {

		final SecureFileService service = new SecureFileService("/tmp/secure_files/files");

		final String userId = "manojarya";
		final String fileId = "f1";
		final String secret = "pass@123";
		final String content = "my content bla bla blah";
		service.add(userId, fileId, secret, content);

		final File downloadedFile = service.get(userId, fileId, "p");

		logger.info("file {}", downloadedFile.getName());
	}

}
