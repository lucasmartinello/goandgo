package org.goandgo.cliente;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("cliente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource {
    private final ClienteRepository repository = new ClienteRepository();

    @GET
    @Path("listaClientes")
    public Response listaClientes() {
        return Response.ok(repository.listaClientes()).build();
    }

    @POST
    public Response novo(Cliente cliente) { return repository.salvarCliente(cliente); }

    @PUT
    public Response alterar(Cliente cliente) {
        return repository.atualizarCliente(cliente);
    }

    @DELETE
    @Path("{id}")
    public Response deletar(@PathParam("id") Long id) throws Exception {
        return Response.ok(repository.deletarCliente(id)).build();
    }
}