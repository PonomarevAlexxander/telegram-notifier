package edu.java.scrapper;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ConnectionIntegrationTest extends IntegrationTest {
    @Test
    public void checkConnection() throws SQLException {
        try (Connection connection = getConnection(POSTGRES)) {
            assertThat(connection.isValid(2))
                .isTrue();
            assertThat(connection.isClosed())
                .isFalse();
        }
    }
}
