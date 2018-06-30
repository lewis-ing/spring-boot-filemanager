package com.nvr.filemanager.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import com.nvr.filemanager.constants.CommonConstants;

public class PermissionUtils {

	public static String getPermissions(Path path) throws IOException {
		PosixFileAttributeView fileAttributeView = Files.getFileAttributeView(path, PosixFileAttributeView.class);
		if (fileAttributeView != null) {
			PosixFileAttributes readAttributes = fileAttributeView.readAttributes();
			Set<PosixFilePermission> permissions = readAttributes.permissions();
			return PosixFilePermissions.toString(permissions);
		}
		return CommonConstants.EMPTY;
	}

	public static String setPermissions(File file, String permsCode, boolean recursive) throws IOException {
		PosixFileAttributeView fileAttributeView = Files.getFileAttributeView(file.toPath(),PosixFileAttributeView.class);
		fileAttributeView.setPermissions(PosixFilePermissions.fromString(permsCode));
		if (file.isDirectory() && recursive && file.listFiles() != null) {
			for (File f : file.listFiles()) {
				setPermissions(f, permsCode, recursive);
			}
		}
		return permsCode;
	}
}
