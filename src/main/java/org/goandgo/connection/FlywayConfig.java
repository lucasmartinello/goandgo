package org.goandgo.connection;
import org.flywaydb.core.Flyway;

public class FlywayConfig {
    public static void migrateDatabase() {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://localhost:5432/goandgo", "postgres", "1")
                .load();
        flyway.migrate();
        System.out.println("Migrações do Flyway aplicadas com sucesso!");
    }
}