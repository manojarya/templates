package com.secure.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.secure.files.AppConstants.URL;

/**
 * Secure files controller.
 *
 * @author neha_pandey
 *
 */
@Controller
public class SecureFileController implements HandlerExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(SecureFileController.class);

	private final SecureFileService secureFileService;

	@Autowired
	public SecureFileController(final SecureFileService fileRepository) {
		this.secureFileService = fileRepository;
	}

	@GetMapping(value = URL.UPLOAD)
	public String getUpload() {
		logger.info("returning file upload view");
		return "file";
	}

	/**
	 * Stores a file in encrypted format.
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@PostMapping(value = URL.UPLOAD)
	@ResponseBody
	public String upload(@RequestParam("file") final MultipartFile file, @RequestParam final String user,
			@RequestParam final String secret) throws IOException {
		logger.info("upload post");
		final String content = readFromInputStream(file.getInputStream());
		logger.info("uploading user: {}, file: {}, content: {}", user, file.getOriginalFilename(), content);
		secureFileService.add(user, file.getOriginalFilename(), secret, content);
		return "success";
	}

	@GetMapping(value = URL.DOWNLOAD)
	public String download(final Model model) {
		return "download";
	}

	/**
	 * Downloads the file of a user in decrypted format.
	 *
	 * @param id
	 * @return encrypted file
	 */
	@PostMapping(value = URL.DOWNLOAD, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public FileSystemResource download(@RequestParam("user") final String user, @RequestParam("file") final String file,
			@RequestParam("secret") final String secret, final HttpServletResponse response) throws IOException {
		logger.info("downloading  user {}, file {} secret {}", user, file, secret);
		final File decFile = secureFileService.get(user, file, secret);
		logger.info("decrypted file {}", decFile.getName());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + file + "\"");
		return new FileSystemResource(decFile);
	}

	@Override
	public ModelAndView resolveException(final HttpServletRequest request, final HttpServletResponse response,
			final Object object, final Exception exc) {
		final ModelAndView modelAndView = new ModelAndView("file");
		if (exc instanceof MaxUploadSizeExceededException) {
			modelAndView.getModel().put("message", "File size exceeds limit!");
		}
		return modelAndView;
	}

	private static String readFromInputStream(final InputStream inputStream) throws IOException {
		final StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line).append(System.lineSeparator());
			}
		}
		return resultStringBuilder.toString();
	}

}