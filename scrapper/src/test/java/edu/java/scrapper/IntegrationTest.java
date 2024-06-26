package edu.java.scrapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class IntegrationTest {
    public static PostgreSQLContainer<?> POSTGRES;

    static {
        POSTGRES = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("scrapper")
            .withUsername("postgres")
            .withPassword("postgres")
            .waitingFor(Wait.forListeningPort());
        POSTGRES.   start();
        runMigrations(POSTGRES);
    }

    public static Connection getConnection(JdbcDatabaseContainer<?> c) {
        try {
            return DriverManager.getConnection(c.getJdbcUrl(), c.getUsername(), c.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void runMigrations(JdbcDatabaseContainer<?> c) {
        try (Connection connection = getConnection(c);
             Database database = DatabaseFactory.getInstance()
                 .findCorrectDatabaseImplementation(new JdbcConnection(connection))) {
            Path changelogDir = new File(".").toPath()
                .toAbsolutePath()
                .getParent()
                .getParent()
                .resolve("migrations");
            DirectoryResourceAccessor directoryResourceAccessor = new DirectoryResourceAccessor(changelogDir);
            Liquibase liquibase = new liquibase.Liquibase(
                "master.xml",
                directoryResourceAccessor,
                database
            );
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (SQLException | FileNotFoundException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }
}
