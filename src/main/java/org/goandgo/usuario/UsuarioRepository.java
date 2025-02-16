package org.goandgo.usuario;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import org.goandgo.connection.DatabaseConnection;
import org.goandgo.utils.CriptografiaController;
import org.goandgo.utils.JWTController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class UsuarioRepository {
    private final JWTController jwtController = new JWTController();

    public List<Usuario> listaUsuarios() {
        try {
            List<Usuario> usuarios = new ArrayList<>();
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT * FROM usuario";
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapUsuario(rs));
                }
            } catch (SQLException e) {
                return null;
            }
            return usuarios;
        } catch (Exception e) {
            return null;
        }
    }

    private Usuario mapUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getString("login"),
                null
        );
    }

    public Long getNovoId() {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT COALESCE(MAX(id), 0) FROM usuario";
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

    public Boolean usuarioExiste(Long id, String login) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String sql = "";
            PreparedStatement stmt;
            if (id == null) {
                sql = "SELECT * from usuario where login = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, login);
            } else {
                sql = "SELECT * from usuario where login = ? and id <> ?";
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, login);
                stmt.setLong(2, id);
            }
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            return false;
        }
    }

    private String validarUsuario(Usuario u) {
        if (u.getNome().length() > 100)
            return "Nome pode ter, no máximo, 100 caracteres!";
        if (u.getLogin().length() > 20)
            return "Login pode ter, no máximo, 20 caracteres!";
        if (u.getSenha().length() > 100)
            return "Senha pode ter, no máximo, 100 caracteres!";
        if (usuarioExiste(u.getId(), u.getLogin()))
            return "Usuário com login já cadastrado no sistema!";
        return "";
    }

    public Response salvarUsuario(Usuario u) {
        String strValidacao = validarUsuario(u);
        if (!strValidacao.isEmpty())
            return Response.status(400).entity(strValidacao).build();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO usuario (id, nome, login, senha) VALUES (?, ?, ?, ?)")) {
            stmt.setLong(1, getNovoId());
            stmt.setString(2, u.getNome());
            stmt.setString(3, u.getLogin());
            stmt.setString(4, CriptografiaController.criptografar(u.getSenha()));
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return Response.ok(true).build();
            } else {
                return Response.status(400).entity("Não foi possível salvar o usuário!").build();
            }
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    public Response atualizarUsuario(Usuario u) {
        String strValidacao = validarUsuario(u);
        if (!strValidacao.isEmpty())
            return Response.status(400).entity(strValidacao).build();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement("UPDATE usuario set nome = ?, login = ?, senha = ? where id = ?")) {
            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getLogin());
            stmt.setString(3, CriptografiaController.criptografar(u.getSenha()));
            stmt.setLong(4, u.getId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return Response.ok(true).build();
            } else {
                return Response.status(400).entity("Não foi possível atualizar o usuário!").build();
            }
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    public boolean deletarUsuario(Long id) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM usuario WHERE ID = ?")) {
            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public String login(String xIdentificador, String usuario, String senha) {
        try {
            if (xIdentificador.equals(jwtController.X_IDENTIFICADOR)) {
                Connection connection = DatabaseConnection.getInstance().getConnection();
                String sql = "SELECT * FROM usuario where login =? and senha = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, usuario);
                stmt.setString(2, CriptografiaController.criptografar(senha));
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    return jwtController.gerarToken(mapUsuario(rs));
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}