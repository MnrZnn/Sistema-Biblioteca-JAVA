package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UsuarioView extends JPanel {

    public DefaultTableModel modelo;
    public JTextField campoNome      = new JTextField(10);
    public JTextField campoMatricula = new JTextField(6);
    public JComboBox<String> cbTipo  = new JComboBox<>(new String[]{"Aluno", "Professor", "Funcionario"});
    public JButton btnAdicionar      = new JButton("Adicionar");

    public UsuarioView() {
        setLayout(new BorderLayout());

        String[] colunas = {"Nome", "Matricula", "Tipo"};
        modelo = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(modelo);

        JPanel rodape = new JPanel();
        rodape.add(new JLabel("Nome:"));      rodape.add(campoNome);
        rodape.add(new JLabel("Matricula:")); rodape.add(campoMatricula);
        rodape.add(new JLabel("Tipo:"));      rodape.add(cbTipo);
        rodape.add(btnAdicionar);

        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);
    }
}
