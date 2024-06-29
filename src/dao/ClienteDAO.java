package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.Cliente;
import util.JDBCUtil;

public class ClienteDAO {

    private Connection connection;

    public ClienteDAO() throws SQLException {
        // Obter a conexão do JDBCUtil
        this.connection = JDBCUtil.getConnection();
    }

    public void inserir(Map<String, Object> valores) throws SQLException {
        String sql = "INSERT INTO Cliente (nome, email, telefone) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, (String) valores.get("nome"));
            stmt.setString(2, (String) valores.get("email"));
            stmt.setString(3, (String) valores.get("telefone"));
            stmt.executeUpdate();
        }
    }

    public void atualizar(Map<String, Object> valores, int id) throws SQLException {
        String sql = "UPDATE Cliente SET nome = ?, email = ?, telefone = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, (String) valores.get("nome"));
            stmt.setString(2, (String) valores.get("email"));
            stmt.setString(3, (String) valores.get("telefone"));
            stmt.setInt(4, id);
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM Cliente WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getInt("id"));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setTelefone(rs.getString("telefone"));
                    return cliente;
                }
            }
        }
        return null;
    }

    public List<Map<String, Object>> listarTodos() throws SQLException {
        List<Map<String, Object>> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> cliente = Map.of(
                    "id", rs.getInt("id"),
                    "nome", rs.getString("nome"),
                    "email", rs.getString("email"),
                    "telefone", rs.getString("telefone")
                );
                clientes.add(cliente);
            }
        }
        return clientes;
    }
}
