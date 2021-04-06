package com.secure.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class SecureFileService {

	private static final Logger logger = LoggerFactory.getLogger(SecureFileService.class);

	private static final String ALGO = "AES";
	private static final String TRANS = "AES/CBC/PKCS5Padding";
	private static final String ENC = ".encrypted";
	private static final String STAGING = "staging";

	private final SecretKeyRepository secretKeyRepository;
	private final String fileDrive;

	@Autowired
	public SecureFileService(@Value("${secure_files_directory}") final String fileDrive,
			final SecretKeyRepository secretKeyRepository) {
		this.fileDrive = fileDrive;
		this.secretKeyRepository = secretKeyRepository;
		new File(getStagingPath()).mkdirs();
	}

	public void add(final String userId, final String fileId, final String content) {
		try {
			this.createUserDirectory(userId);
			final SecretKey secretKey = KeyGenerator.getInstance(ALGO).generateKey();
			final FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter(secretKey, TRANS);
			fileEncrypterDecrypter.encrypt(content, getFilePath(userId, fileId));
			this.secretKeyRepository.saveKey(userId, fileId, secretKey);
		} catch (final NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException ex) {
			logger.error("file IO error ", ex);
			throw new CipherException("file IO error", ex);
		}
	}

	public File get(final String userId, final String fileId) {
		try {
			final SecretKey secretKey = this.secretKeyRepository.getKey(userId, fileId);
			final FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter(secretKey, TRANS);
			final String content = fileEncrypterDecrypter.decrypt(getFilePath(userId, fileId));
			return createFile(getStagingFilePath(UUID.randomUUID().toString()), content);
		} catch (final IOException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException ex) {
			logger.error("file IO error ", ex);
			throw new CipherException("file IO error", ex);
		}
	}

	private String getFilePath(final String userId, final String fileId) {
		return this.fileDrive + File.separator + userId + File.separator + fileId + ENC;
	}

	private void createUserDirectory(final String userId) {
		new File(this.fileDrive + File.separator + userId).mkdirs();
	}

	private String getStagingPath() {
		return this.fileDrive + File.separator + STAGING;
	}

	private String getStagingFilePath(final String name) {
		return this.fileDrive + File.separator + STAGING + File.separator + name;
	}

	private File createFile(final String fileName, final String content) throws IOException {
		final Path path = Paths.get(fileName);
		final byte[] strToBytes = content.getBytes();
		Files.write(path, strToBytes);
		return new File(fileName);
	}

}
