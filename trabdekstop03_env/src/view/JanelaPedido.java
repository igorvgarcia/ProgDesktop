package view;

import dao.PedidoDAO;
import model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class JanelaPedido extends JFrame {
    private JTextField txtClienteId, txtProdutoId, txtQuantidade;
    private JButton btnSalvar, btnAtualizar, btnExcluir, btnBuscar, btnVoltar;
    private JTable tablePedidos;
    private DefaultTableModel tableModel;

    public JanelaPedido() {
        setTitle("Cadastro de Pedido");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        try {
            initComponents();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao inicializar a interface: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() throws SQLException {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtClienteId = new JTextField();
        txtProdutoId = new JTextField();
        txtQuantidade = new JTextField();

        inputPanel.add(new JLabel("Cliente ID:"));
        inputPanel.add(txtClienteId);
        inputPanel.add(new JLabel("Produto ID:"));
        inputPanel.add(txtProdutoId);
        inputPanel.add(new JLabel("Quantidade:"));
        inputPanel.add(txtQuantidade);

        tableModel = new DefaultTableModel(new String[]{"ID", "Cliente ID", "Produto ID", "Quantidade"}, 0);
        tablePedidos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tablePedidos);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnSalvar = new JButton("Salvar");
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        btnBuscar = new JButton("Buscar");
        btnVoltar = new JButton("Voltar");

        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnExcluir);
        buttonPanel.add(btnBuscar);
        buttonPanel.add(btnVoltar);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    salvarPedido();
                    listarPedidos();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(JanelaPedido.this, "Erro ao salvar pedido: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    atualizarPedido();
                    listarPedidos();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(JanelaPedido.this, "Erro ao atualizar pedido: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    excluirPedido();
                    listarPedidos();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(JanelaPedido.this, "Erro ao excluir pedido: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    buscarPedido();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(JanelaPedido.this, "Erro ao buscar pedido: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        listarPedidos();
    }

    private void salvarPedido() throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setClienteId(Integer.parseInt(txtClienteId.getText()));
        pedido.setProdutoId(Integer.parseInt(txtProdutoId.getText()));
        pedido.setQuantidade(Integer.parseInt(txtQuantidade.getText()));

        PedidoDAO dao = new PedidoDAO();
        dao.inserir(pedido);
        JOptionPane.showMessageDialog(this, "Pedido salvo com sucesso!");
    }

    private void atualizarPedido() throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setId(Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do pedido a ser atualizado:")));
        pedido.setClienteId(Integer.parseInt(txtClienteId.getText()));
        pedido.setProdutoId(Integer.parseInt(txtProdutoId.getText()));
        pedido.setQuantidade(Integer.parseInt(txtQuantidade.getText()));

        PedidoDAO dao = new PedidoDAO();
        dao.atualizar(pedido);
        JOptionPane.showMessageDialog(this, "Pedido atualizado com sucesso!");
    }

    private void excluirPedido() throws SQLException {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do pedido a ser excluído:"));

        PedidoDAO dao = new PedidoDAO();
        dao.excluir(id);
        JOptionPane.showMessageDialog(this, "Pedido excluído com sucesso!");
    }

    private void buscarPedido() throws SQLException {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do pedido a ser buscado:"));

        PedidoDAO dao = new PedidoDAO();
        Pedido pedido = dao.buscarPorId(id);
        if (pedido != null) {
            txtClienteId.setText(String.valueOf(pedido.getClienteId()));
            txtProdutoId.setText(String.valueOf(pedido.getProdutoId()));
            txtQuantidade.setText(String.valueOf(pedido.getQuantidade()));
            JOptionPane.showMessageDialog(this, "Pedido encontrado!");
        } else {
            JOptionPane.showMessageDialog(this, "Pedido não encontrado!");
        }
    }

    private void listarPedidos() throws SQLException {
        PedidoDAO dao = new PedidoDAO();
        List<Pedido> pedidos = dao.listarTodos();
        tableModel.setRowCount(0); // Limpar tabela
        for (Pedido pedido : pedidos) {
            tableModel.addRow(new Object[]{pedido.getId(), pedido.getClienteId(), pedido.getProdutoId(), pedido.getQuantidade()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JanelaPedido().setVisible(true);
            }
        });
    }
}
