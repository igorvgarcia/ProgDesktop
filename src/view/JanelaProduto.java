package view;

import dao.ProdutoDAO;
import model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class JanelaProduto extends JFrame {
    private JTextField txtNome, txtPreco, txtQuantidade;
    private JButton btnSalvar, btnAtualizar, btnExcluir, btnBuscar, btnVoltar;
    private JTable tableProdutos;
    private DefaultTableModel tableModel;

    public JanelaProduto() {
        setTitle("Cadastro de Produto");
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

        txtNome = new JTextField();
        txtPreco = new JTextField();
        txtQuantidade = new JTextField();

        inputPanel.add(new JLabel("Nome:"));
        inputPanel.add(txtNome);
        inputPanel.add(new JLabel("Preço:"));
        inputPanel.add(txtPreco);
        inputPanel.add(new JLabel("Quantidade:"));
        inputPanel.add(txtQuantidade);

        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Preço", "Quantidade"}, 0);
        tableProdutos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableProdutos);

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
                    salvarProduto();
                    listarProdutos();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(JanelaProduto.this, "Erro ao salvar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    atualizarProduto();
                    listarProdutos();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(JanelaProduto.this, "Erro ao atualizar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    excluirProduto();
                    listarProdutos();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(JanelaProduto.this, "Erro ao excluir produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    buscarProduto();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(JanelaProduto.this, "Erro ao buscar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        listarProdutos();
    }

    private void salvarProduto() throws SQLException {
        Produto produto = new Produto();
        produto.setNome(txtNome.getText());
        produto.setPreco(Double.parseDouble(txtPreco.getText()));
        produto.setQuantidade(Integer.parseInt(txtQuantidade.getText()));

        ProdutoDAO dao = new ProdutoDAO();
        dao.inserir(produto);
        JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!");
    }

    private void atualizarProduto() throws SQLException {
        Produto produto = new Produto();
        produto.setId(Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do produto a ser atualizado:")));
        produto.setNome(txtNome.getText());
        produto.setPreco(Double.parseDouble(txtPreco.getText()));
        produto.setQuantidade(Integer.parseInt(txtQuantidade.getText()));

        ProdutoDAO dao = new ProdutoDAO();
        dao.atualizar(produto);
        JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
    }

    private void excluirProduto() throws SQLException {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do produto a ser excluído:"));

        ProdutoDAO dao = new ProdutoDAO();
        dao.excluir(id);
        JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
    }

    private void buscarProduto() throws SQLException {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do produto a ser buscado:"));

        ProdutoDAO dao = new ProdutoDAO();
        Produto produto = dao.buscarPorId(id);
        if (produto != null) {
            txtNome.setText(produto.getNome());
            txtPreco.setText(String.valueOf(produto.getPreco()));
            txtQuantidade.setText(String.valueOf(produto.getQuantidade()));
            JOptionPane.showMessageDialog(this, "Produto encontrado!");
        } else {
            JOptionPane.showMessageDialog(this, "Produto não encontrado!");
        }
    }

    private void listarProdutos() throws SQLException {
        ProdutoDAO dao = new ProdutoDAO();
        List<Produto> produtos = dao.listarTodos();
        tableModel.setRowCount(0); // Limpar tabela
        for (Produto produto : produtos) {
            tableModel.addRow(new Object[]{produto.getId(), produto.getNome(), produto.getPreco(), produto.getQuantidade()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JanelaProduto().setVisible(true);
            }
        });
    }
}
