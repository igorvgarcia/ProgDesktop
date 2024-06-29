package dao;

import model.Pedido;
import util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {
    private Connection connection;

    public PedidoDAO() throws SQLException {
        connection = JDBCUtil.getConnection();
    }

    public void inserir(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (cliente_id, produto_id, quantidade) VALUES (?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, pedido.getClienteId());
        stmt.setInt(2, pedido.getProdutoId());
        stmt.setInt(3, pedido.getQuantidade());
        stmt.executeUpdate();
    }

    public void atualizar(Pedido pedido) throws SQLException {
        String sql = "UPDATE pedido SET cliente_id = ?, produto_id = ?, quantidade = ? WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, pedido.getClienteId());
        stmt.setInt(2, pedido.getProdutoId());
        stmt.setInt(3, pedido.getQuantidade());
        stmt.setInt(4, pedido.getId());
        stmt.executeUpdate();
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM pedido WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public Pedido buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM pedido WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Pedido pedido = new Pedido();
            pedido.setId(rs.getInt("id"));
            pedido.setClienteId(rs.getInt("cliente_id"));
            pedido.setProdutoId(rs.getInt("produto_id"));
            pedido.setQuantidade(rs.getInt("quantidade"));
            return pedido;
        }
        return null;
    }

    public List<Pedido> listarTodos() throws SQLException {
        String sql = "SELECT * FROM pedido";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        List<Pedido> pedidos = new ArrayList<>();
        while (rs.next()) {
            Pedido pedido = new Pedido();
            pedido.setId(rs.getInt("id"));
            pedido.setClienteId(rs.getInt("cliente_id"));
            pedido.setProdutoId(rs.getInt("produto_id"));
            pedido.setQuantidade(rs.getInt("quantidade"));
            pedidos.add(pedido);
        }
        return pedidos;
    }
}
