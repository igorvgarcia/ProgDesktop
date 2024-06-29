package view;

import dao.ClienteDAO;
import model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JanelaCliente extends JFrame {

    private JTextField txtNome, txtEmail, txtTelefone;
    private JButton btnSalvar, btnAtualizar, btnExcluir, btnBuscar, btnVoltar;
    private JTable tableClientes;
    private DefaultTableModel tableModel;

    private ClienteDAO clienteDAO;

    public JanelaCliente() {
        setTitle("Cadastro de Cliente");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            clienteDAO = new ClienteDAO();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + ex.getMessage());
            System.exit(1);
        }

        initComponents();
    }

    private void initComponents() {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtNome = new JTextField();
        txtEmail = new JTextField();
        txtTelefone = new JTextField();

        inputPanel.add(new JLabel("Nome:"));
        inputPanel.add(txtNome);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(txtEmail);
        inputPanel.add(new JLabel("Telefone:"));
        inputPanel.add(txtTelefone);

        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Email", "Telefone"}, 0);
        tableClientes = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableClientes);

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
                salvarCliente();
                listarClientes();
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarCliente();
                listarClientes();
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirCliente();
                listarClientes();
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarCliente();
            }
        });

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                voltarTelaPrincipal();
            }
        });

        listarClientes();
    }

    private void salvarCliente() {
        Map<String, Object> valores = Map.of(
                "nome", txtNome.getText(),
                "email", txtEmail.getText(),
                "telefone", txtTelefone.getText()
        );

        try {
            clienteDAO.inserir(valores);
            JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar cliente: " + ex.getMessage());
        }
    }

    private void atualizarCliente() {
        Map<String, Object> valores = Map.of(
                "nome", txtNome.getText(),
                "email", txtEmail.getText(),
                "telefone", txtTelefone.getText()
        );

        int id = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do cliente a ser atualizado:"));

        try {
            clienteDAO.atualizar(valores, id);
            JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar cliente: " + ex.getMessage());
        }
    }

    private void excluirCliente() {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do cliente a ser excluído:"));

        try {
            clienteDAO.excluir(id);
            JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir cliente: " + ex.getMessage());
        }
    }

    private void buscarCliente() {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do cliente a ser buscado:"));

        try {
            Cliente cliente = clienteDAO.buscarPorId(id);
            if (cliente != null) {
                txtNome.setText(cliente.getNome());
                txtEmail.setText(cliente.getEmail());
                txtTelefone.setText(cliente.getTelefone());
                JOptionPane.showMessageDialog(this, "Cliente encontrado!");
            } else {
                JOptionPane.showMessageDialog(this, "Cliente não encontrado!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar cliente: " + ex.getMessage());
        }
    }

    private void listarClientes() {
        try {
            List<Map<String, Object>> clientes = clienteDAO.listarTodos();
            tableModel.setRowCount(0); // Limpar tabela
            for (Map<String, Object> cliente : clientes) {
                tableModel.addRow(new Object[]{cliente.get("id"), cliente.get("nome"), cliente.get("email"), cliente.get("telefone")});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar clientes: " + ex.getMessage());
        }
    }

    private void voltarTelaPrincipal() {
        setVisible(false); // Esconde a janela atual
        dispose(); // Libera os recursos associados à janela atual
        new TelaPrincipal().setVisible(true); // Cria e exibe a tela principal novamente
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JanelaCliente().setVisible(true);
            }
        });
    }
}
