package org.goandgo;
import org.goandgo.connection.FlywayConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import java.net.URI;

public class Main {
    public static void main(String[] args) {
        try {
            URI baseUri = URI.create("http://localhost:8080/");
            // Configuração do Jersey e recursos
            ResourceConfig config = new ResourceConfig()
                    .packages("org.goandgo")
                    .register(JacksonFeature.class)
                    .register(org.goandgo.security.controller.RequestFilter.class);

            // Cria e inicializa o servidor Grizzly
            GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
            FlywayConfig.migrateDatabase();
            System.out.println("Servidor rodando em http://localhost:8080/");
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}