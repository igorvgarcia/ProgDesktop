package view;

import dao.ClienteDAO;
import dao.ProdutoDAO;
import dao.PedidoDAO;
import util.JDBCUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TelaPrincipal extends JFrame {

    private JTextArea txtAreaDados;
    private JLabel lblStatusBanco;

    public TelaPrincipal() {
        setTitle("Sistema de Gerenciamento");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnCliente = new JButton("Gerenciar Clientes");
        JButton btnProduto = new JButton("Gerenciar Produtos");
        JButton btnPedido = new JButton("Gerenciar Pedidos");

        panel.add(btnCliente);
        panel.add(btnProduto);
        panel.add(btnPedido);

        add(panel, BorderLayout.NORTH);

        txtAreaDados = new JTextArea(10, 40);
        JScrollPane scrollPane = new JScrollPane(txtAreaDados);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        lblStatusBanco = new JLabel("Conectado ao Banco de Dados: N/A");
        bottomPanel.add(lblStatusBanco, BorderLayout.NORTH);

        JButton btnConectarBanco = new JButton("Conectar ao Banco");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(btnConectarBanco);
        bottomPanel.add(btnPanel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        btnCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JanelaCliente().setVisible(true);
            }
        });

        btnProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JanelaProduto().setVisible(true);
            }
        });

        btnPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JanelaPedido().setVisible(true);
            }
        });

        btnConectarBanco.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conectarBanco();
            }
        });
    }

    private void conectarBanco() {
        try {
            Connection connection = JDBCUtil.getConnection();
            lblStatusBanco.setText("Conectado ao Banco de Dados: " + JDBCUtil.getDatabaseName(connection));
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM cliente");

            StringBuilder stringBuilder = new StringBuilder();
            while (resultSet.next()) {
                stringBuilder.append("ID: ").append(resultSet.getInt("id"))
                        .append(", Nome: ").append(resultSet.getString("nome"))
                        .append(", Email: ").append(resultSet.getString("email"))
                        .append(", Telefone: ").append(resultSet.getString("telefone"))
                        .append("\n");
            }

            txtAreaDados.setText(stringBuilder.toString());

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            lblStatusBanco.setText("Erro ao conectar ao Banco de Dados");
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaPrincipal().setVisible(true);
            }
        });
    }
}
