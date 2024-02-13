package edu.java.bot.util;

import java.net.URL;

public class LinkValidator {
    private LinkValidator() {

    }

    public static boolean isValid(String link) {
        try {
            new URL(link).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
