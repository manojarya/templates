package com.secure.files;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class FileUploadExceptionAdvice {

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ModelAndView handleMaxSizeException(final MaxUploadSizeExceededException exc,
			final HttpServletRequest request, final HttpServletResponse response) {
		final ModelAndView modelAndView = new ModelAndView("file");
		modelAndView.getModel().put("message", "File too large!");
		return modelAndView;
	}
}
