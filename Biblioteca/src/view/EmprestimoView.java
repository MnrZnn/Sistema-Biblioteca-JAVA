package view;

import model.Livro;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmprestimoView extends JPanel {

    public DefaultTableModel modelo;
    public JTable  tabela;

    public JComboBox<Usuario> cbUsuario = new JComboBox<>();
    public JComboBox<Livro>   cbLivro   = new JComboBox<>();

    public JButton btnEmprestar = new JButton("Emprestar");
    public JButton btnDevolver  = new JButton("Devolver selecionado");

    public EmprestimoView() {
        setLayout(new BorderLayout());

        String[] colunas = {"Usuario", "Livro", "Retirada", "Prev. Devolucao", "Status"};
        modelo = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modelo);

        JPanel rodape = new JPanel();
        rodape.add(new JLabel("Usuario:")); rodape.add(cbUsuario);
        rodape.add(new JLabel("Livro:"));   rodape.add(cbLivro);
        rodape.add(btnEmprestar);
        rodape.add(btnDevolver);

        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);
    }
}
