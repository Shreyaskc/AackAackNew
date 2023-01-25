package com.aackaacknew.utils;

import java.io.File;

public class Deletedirectory {

	// To delete particular directory(folder) using path
	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
		return (path.delete());
	}

}
