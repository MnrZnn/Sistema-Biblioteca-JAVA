package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AlunoView extends JPanel {

    public DefaultTableModel modelo;
    public JTable  tabela;

    public JTextField campoNome      = new JTextField(12);
    public JTextField campoMatricula = new JTextField(8);
    public JTextField campoEndereco  = new JTextField(12);

    public JButton btnAdicionar = new JButton("Adicionar");
    public JButton btnEditar    = new JButton("Editar");
    public JButton btnRemover   = new JButton("Remover");
    public JButton btnLimpar    = new JButton("Limpar");

    public AlunoView() {
        setLayout(new BorderLayout());

        String[] colunas = {"Nome", "Matricula", "Endereco"};
        modelo = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modelo);

        JPanel form = new JPanel(new GridLayout(2, 4, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Dados do Aluno"));
        form.add(new JLabel("Nome:"));      form.add(campoNome);
        form.add(new JLabel("Matricula:")); form.add(campoMatricula);
        form.add(new JLabel("Endereco:"));  form.add(campoEndereco);

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
