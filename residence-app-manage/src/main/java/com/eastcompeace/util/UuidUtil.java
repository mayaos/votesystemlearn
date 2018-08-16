package com.eastcompeace.util;

import java.util.UUID;

public class UuidUtil {
	public static void main(String[] args) {
		System.out.println(UuidUtil.getUuid());;
	}
	public static String getUuid() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}
}
