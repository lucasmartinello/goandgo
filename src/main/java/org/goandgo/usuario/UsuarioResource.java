package org.goandgo.usuario;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {
    private final UsuarioRepository repository = new UsuarioRepository();

    @GET
    @Path("listaUsuarios")
    public Response listaUsuarios() {
        return Response.ok(repository.listaUsuarios()).build();
    }

    @POST
    public Response novo(Usuario usuario) {
        return repository.salvarUsuario(usuario);
    }

    @PUT
    public Response alterar(Usuario usuario) {
        return repository.atualizarUsuario(usuario);
    }

    @DELETE
    @Path("{id}")
    public Response deletar(@PathParam("id") Long id) throws Exception {
        return Response.ok(repository.deletarUsuario(id)).build();
    }
}