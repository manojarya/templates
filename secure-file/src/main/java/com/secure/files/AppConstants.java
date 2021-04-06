package com.secure.files;

/**
 * Constants.
 *
 * @author neha_pandey
 *
 */
public final class AppConstants {

	private AppConstants() {
		// marking private
	}

	/** Spring profiles. */
	public static class URL {

		public static final String SECURE_FILES = "/secure_files";
		public static final String UPLOAD = "/upload";
		public static final String DOWNLOAD = "users/{user_id}/download/{file_id}";

		private URL() {
		}

	}

}
