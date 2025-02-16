package org.goandgo.login;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.goandgo.usuario.UsuarioRepository;

@Path("login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginResource {
    private final UsuarioRepository repository = new UsuarioRepository();

    @POST
    public Response login(Login login) {
        return Response.ok(repository.login(login.getXidentificador(), login.getLogin(), login.getSenha())).build();
    }
}