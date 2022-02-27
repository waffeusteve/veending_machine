package org.pkfrc.core.services.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class BcryptEncryption {

	public static String encode(String text) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(text);
	}

	public static boolean matches(String rawText, String encryptedText) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(rawText, encryptedText);
	}

}
