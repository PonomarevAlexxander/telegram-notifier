package edu.java.bot.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LinkValidatorTest {

    @ParameterizedTest
    @CsvSource({
        "https://link.com, true",
        "invalid, false",
        "http://goodone.ru, true",
        "https: //bad, false",
        "notok.com, false"
    })
    @DisplayName("Test isValid() with invalid links")
    void isValid(String link, boolean expected) {
        assertThat(LinkValidator.isValid(link))
            .isEqualTo(expected);
    }
}
