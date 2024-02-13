package edu.java.bot.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MarkdownParserTest {

    @Test
    @DisplayName("Test bold()")
    void bold() {
        String styled = MarkdownParser.bold("text");
        assertThat(styled)
            .asString()
            .containsPattern("^\\*.+\\*$");
    }

    @Test
    @DisplayName("Test monospace()")
    void monospace() {
        String styled = MarkdownParser.monospace("text");
        assertThat(styled)
            .asString()
            .containsPattern("^`.+`$");
    }

    @Test
    @DisplayName("Test code()")
    void code() {
        String styled = MarkdownParser.code("text");
        assertThat(styled)
            .asString()
            .containsPattern("^```.*\\n.+\\n```$");
    }

    @Test
    @DisplayName("Test italic()")
    void italic() {
        String styled = MarkdownParser.italic("text");
        assertThat(styled)
            .asString()
            .containsPattern("^_.+_$");
    }
}
