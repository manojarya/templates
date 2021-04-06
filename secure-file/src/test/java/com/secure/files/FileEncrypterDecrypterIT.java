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

		final SecretKeyRepository secretKeyRepository = new SecretKeyRepository("/tmp/secure_files/keys");
		final SecureFileService service = new SecureFileService("/tmp/secure_files/files", secretKeyRepository);

		final String userId = "u1";
		final String fileId = "f1";
		final String content = "my content bla bla blah";
		service.add(userId, fileId, content);

		final File downloadedFile = service.get(userId, fileId);

		logger.info("file {}", downloadedFile.getName());
	}

}
