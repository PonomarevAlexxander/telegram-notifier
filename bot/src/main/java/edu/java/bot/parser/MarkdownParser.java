package edu.java.bot.parser;


public class MarkdownParser {
    private MarkdownParser() {

    }

    public static String bold(String text) {
        return "*" + text + "*";
    }

    public static String monospace(String text) {
        return "`" + text + "`";
    }

    public static String code(String text) {
        return "```\n" + text + "\n```";
    }

    public static String italic(String text) {
        return "_" + text + "_";
    }

    public static String link(String text, String url) {
        return String.format("[%s](%s)", text, url);
    }
}

