package com.autowhouse.loginservice.util;


import java.security.SecureRandom;

public class SecurePasswordGenerator {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIALS = "!@#$%^&*()-_+=<>?";
    private static final String ALL = UPPER + LOWER + DIGITS + SPECIALS;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("Password length should be at least 8 characters");
        }

        StringBuilder password = new StringBuilder(length);
        password.append(getRandomChar(UPPER));
        password.append(getRandomChar(LOWER));
        password.append(getRandomChar(DIGITS));
        password.append(getRandomChar(SPECIALS));

        for (int i = 4; i < length; i++) {
            password.append(getRandomChar(ALL));
        }
        return shuffleString(password.toString());
    }

    private static char getRandomChar(String chars) {
        return chars.charAt(RANDOM.nextInt(chars.length()));
    }

    private static String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            char temp = characters[i];
            characters[i] = characters[j];
            characters[j] = temp;
        }
        return new String(characters);
    }

    public static String GenerateNums() {
        int x = RANDOM.nextInt(1000, 10000);
        return Integer.toString(x);
    }

}

