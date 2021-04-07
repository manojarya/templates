package com.secure.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.crypto.BadPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class SecureFileService {

	private static final Logger logger = LoggerFactory.getLogger(SecureFileService.class);

	private static final String ENC = ".encrypted";
	private static final String SOURCE = "source";
	private static final String DOWNLOAD = "staging";

	private final String fileDrive;

	@Autowired
	public SecureFileService(@Value("${secure_files_directory}") final String fileDrive) {
		this.fileDrive = fileDrive;
		this.mkDirs();
	}

	public void add(final String userId, final String fileId, final String secret, final String content) {
		try {
			new File(this.fileDrive + File.separator + userId).mkdirs();
			final File in = createFile(getSourceFilePath(fileId), content);
			final File out = new File(getEncryptFilePath(userId, fileId));
			final FileLineEncrypterDecrypter fileEncrypterDecrypter = new FileLineEncrypterDecrypter(secret);
			fileEncrypterDecrypter.encryptLineByLine(in, out);
			logger.info("encrypted file is upload for user {} and file {}", userId, fileId);
		} catch (final Exception ex) {
			logger.error("error while adding file", ex);
			throw new SecureFileOpException("error while adding file", ex);
		}
	}

	public File get(final String userId, final String fileId, final String secret) {
		try {
			logger.info("fetching file is user {} and file {}", userId, fileId);
			final File in = new File(getEncryptFilePath(userId, fileId));
			final File out = new File(getDownloadFilePath(UUID.randomUUID().toString()));
			final FileLineEncrypterDecrypter fileEncrypterDecrypter = new FileLineEncrypterDecrypter(secret);
			fileEncrypterDecrypter.decryptLineByLine(in, out);
			return out;
		} catch (final IOException ex) {
			if (ex.getCause() instanceof BadPaddingException) {
				logger.error("bad secret key", ex);
				throw new SecureFileOpException("Invalid password for file", ex);
			}
			throw new SecureFileOpException("Error while fetching file", ex);
		} catch (final Exception ex) {
			logger.error("error while fetching file ", ex);
			throw new SecureFileOpException("error while fetching file", ex);
		}
	}

	private File createFile(final String fileName, final String content) throws IOException {
		final Path path = Paths.get(fileName);
		final byte[] strToBytes = content.getBytes();
		Files.write(path, strToBytes);
		return new File(fileName);
	}

	private String getSourceFilePath(final String fileId) {
		return this.fileDrive + File.separator + SOURCE + File.separator + fileId;
	}

	private String getEncryptFilePath(final String userId, final String fileId) {
		return this.fileDrive + File.separator + userId + File.separator + fileId + ENC;
	}

	private String getDownloadFilePath(final String fileId) {
		return this.fileDrive + File.separator + DOWNLOAD + File.separator + fileId;
	}

	private void mkDirs() {
		new File(this.fileDrive).mkdirs();
		new File(this.fileDrive + File.separator + SOURCE).mkdirs();
		new File(this.fileDrive + File.separator + DOWNLOAD).mkdirs();
	}

}
