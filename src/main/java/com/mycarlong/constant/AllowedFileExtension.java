package com.mycarlong.constant;


public enum AllowedFileExtension {
	JPEG("image/jpeg"),
	JPG("image/jpg"),
	PNG("image/png"),
	GIF("image/gif"),
	BMP("image/bmp"),
	X_WINDOWS_BMP("image/x-windows-bmp");

	private final String extension;

	AllowedFileExtension(String extension) {
		this.extension = extension;
	}

	public String getExtension() {
		return this.extension;
	}
}
