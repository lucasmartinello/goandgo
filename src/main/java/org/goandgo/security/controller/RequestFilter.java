package org.goandgo.security.controller;
import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.Response;
import org.goandgo.utils.JWTController;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION) // O filtro será executado antes de processar a requisição
public class RequestFilter implements ContainerRequestFilter {
    private final JWTController jwtController = new JWTController();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Obtém o token do header Authorization
        if (!requestContext.getUriInfo().getPath().equals("login") &&
                !(requestContext.getUriInfo().getPath().equals("usuario") && (requestContext.getMethod().equals("POST")))) {
            String authorizationHeader = requestContext.getHeaderString("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Token JWT ausente ou inválido")
                        .build());
                return;
            }
            String token = authorizationHeader.substring("Bearer ".length());
            try {
                if (!jwtController.validateToken(token)) {
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Token JWT inválido ou expirado")
                            .build());
                }
            } catch (Exception e) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Token JWT inválido ou expirado")
                        .build());
            }
        }
    }
}