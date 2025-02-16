package org.goandgo.cliente;
import jakarta.ws.rs.core.Response;
import org.goandgo.connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class ClienteRepository {

    public List<Cliente> listaClientes() {
        try {
            List<Cliente> clientes = new ArrayList<>();
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT * FROM cliente";
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(mapCliente(rs));
                }
            } catch (SQLException e) {
                return null;
            }
            return clientes;
        } catch (Exception e) {
            return null;
        }
    }

    private Cliente mapCliente(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("telefone"),
                rs.getString("cpf")
        );
    }

    public Long getNovoId() {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT COALESCE(MAX(id), 0) FROM cliente";
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1) + 1;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 1L;
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean clienteExiste(Long id, String nome) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String sql = "";
            PreparedStatement stmt;
            if (id == null) {
                sql = "SELECT * from cliente where nome = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, nome);
            } else {
                sql = "SELECT * from cliente where nome = ? and id <> ?";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, nome);
                stmt.setLong(2, id);
            }
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            return false;
        }
    }

    private String validarCliente(Cliente c) {
        if (c.getNome().length() > 100)
            return "Nome pode ter, no máximo, 100 caracteres!";
        if (c.getTelefone().length() > 20)
            return "Telefone pode ter, no máximo, 20 caracteres!";
        if (c.getEmail().length() > 100)
            return "E-mail pode ter, no máximo, 100 caracteres!";
        if (c.getCpf().length() > 11)
            return "CPF pode ter, no máximo, 11 caracteres!";
        if (clienteExiste(c.getId(), c.getNome()))
            return "Cliente com nome já cadastrado no sistema!";
        return "";
    }

    public Response salvarCliente(Cliente c) {
        String strValidacao = validarCliente(c);
        if (!strValidacao.isEmpty())
            return Response.status(400).entity(strValidacao).build();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO cliente (id, nome, email, telefone, cpf) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setLong(1, getNovoId());
            stmt.setString(2, c.getNome());
            stmt.setString(3, c.getEmail());
            stmt.setString(4, c.getTelefone());
            stmt.setString(5, c.getCpf());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return Response.ok(true).build();
            } else {
                return Response.status(400).entity("Não foi possível salvar o cliente!").build();
            }
        } catch (SQLException e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    public Response atualizarCliente(Cliente c) {
        String strValidacao = validarCliente(c);
        if (!strValidacao.isEmpty())
            return Response.status(400).entity(strValidacao).build();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement("UPDATE cliente set nome = ?, email = ?, telefone = ?, cpf = ? where id = ?")) {
            stmt.setString(1, c.getNome());
            stmt.setString(2, c.getEmail());
            stmt.setString(3, c.getTelefone());
            stmt.setString(4, c.getCpf());
            stmt.setLong(5, c.getId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return Response.ok(true).build();
            } else {
                return Response.status(400).entity("Não foi possível atualizar o cliente!").build();
            }
        } catch (SQLException e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    public boolean deletarCliente(Long id) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM CLIENTE WHERE ID = ?")) {
            stmt.setLong(1, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}