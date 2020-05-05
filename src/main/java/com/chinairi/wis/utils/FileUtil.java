package com.chinairi.wis.utils;

public class FileUtil {
	public static boolean hasWis(String path) {
		if (path != null && path != "") {
			String suffix = path.substring(path.lastIndexOf(".") + 1);
			if (suffix != null)
				return "WIS".equals(suffix.toUpperCase());
		}
		return false;
	}
}
