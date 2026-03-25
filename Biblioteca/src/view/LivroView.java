package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LivroView extends JPanel {

    public DefaultTableModel modelo;
    public JTable  tabela;

    // Campos do formulario (todos os campos do diagrama)
    public JTextField campoTitulo    = new JTextField(10);
    public JTextField campoAutor     = new JTextField(10);
    public JTextField campoAno       = new JTextField(5);
    public JTextField campoCategoria = new JTextField(8);
    public JTextField campoQtd       = new JTextField(4);

    public JButton btnAdicionar = new JButton("Adicionar");
    public JButton btnEditar    = new JButton("Editar");
    public JButton btnRemover   = new JButton("Remover");
    public JButton btnLimpar    = new JButton("Limpar");

    public LivroView() {
        setLayout(new BorderLayout());

        String[] colunas = {"ID", "Titulo", "Autor", "Ano", "Categoria", "Qtd"};
        modelo = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modelo);

        // Formulario
        JPanel form = new JPanel(new GridLayout(3, 4, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Dados do Livro"));
        form.add(new JLabel("Titulo:"));    form.add(campoTitulo);
        form.add(new JLabel("Autor:"));     form.add(campoAutor);
        form.add(new JLabel("Ano:"));       form.add(campoAno);
        form.add(new JLabel("Categoria:")); form.add(campoCategoria);
        form.add(new JLabel("Qtd:"));       form.add(campoQtd);

        // Botoes
        JPanel botoes = new JPanel();
        botoes.add(btnAdicionar);
        botoes.add(btnEditar);
        botoes.add(btnRemover);
        botoes.add(btnLimpar);

        JPanel sul = new JPanel(new BorderLayout());
        sul.add(form,   BorderLayout.CENTER);
        sul.add(botoes, BorderLayout.SOUTH);

        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(sul, BorderLayout.SOUTH);
    }
}
