package com.nvr.filemanager.constants;

public interface CommonConstants {

	String APPLICATION_ZIP = "application/zip";
	
	String CONTENT_DISPOSITION = "Content-Disposition";
	
	String EMPTY = "";
	
	static String downloadFileName(String toFilename) {
		return String.format("inline; filename=%s ",toFilename);
	}
}
