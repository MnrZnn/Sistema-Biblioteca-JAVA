package view;

import model.Livro;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReservaView extends JPanel {

    public DefaultTableModel modelo;
    public JTable tabela;

    public JComboBox<Usuario> cbUsuario = new JComboBox<>();
    public JComboBox<Livro>   cbLivro   = new JComboBox<>();

    public JButton btnReservar = new JButton("Reservar");
    public JButton btnCancelar = new JButton("Cancelar selecionada");

    public ReservaView() {
        setLayout(new BorderLayout());

        String[] colunas = {"Usuario", "Livro", "Solicitacao", "Expiracao", "Status"};
        modelo = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modelo);

        JPanel rodape = new JPanel();
        rodape.add(new JLabel("Usuario:")); rodape.add(cbUsuario);
        rodape.add(new JLabel("Livro:"));   rodape.add(cbLivro);
        rodape.add(btnReservar);
        rodape.add(btnCancelar);

        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);
    }
}
