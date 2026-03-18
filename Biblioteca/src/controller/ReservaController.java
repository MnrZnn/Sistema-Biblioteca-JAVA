package controller;

import model.*;
import view.ReservaView;

import javax.swing.*;
import java.sql.*;

public class ReservaController {

    private ReservaView view;
    private Dados       dados;

    public ReservaController(ReservaView view, Dados dados) {
        this.view  = view;
        this.dados = dados;

        recarregarCombos();
        carregarTabela();

        view.btnReservar.addActionListener(e -> reservar());
        view.btnCancelar.addActionListener(e -> cancelar());
    }

    public void recarregarCombos() {
        dados.carregarUsuarios();
        dados.carregarLivros();
        view.cbUsuario.removeAllItems();
        view.cbLivro.removeAllItems();
        for (Usuario u : dados.usuarios) view.cbUsuario.addItem(u);
        for (Livro l   : dados.livros)   view.cbLivro.addItem(l);
    }

    void reservar() {
        Usuario u = (Usuario) view.cbUsuario.getSelectedItem();
        Livro   l = (Livro)   view.cbLivro.getSelectedItem();
        if (u == null || l == null) return;

        try {
            PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(
                "INSERT INTO reserva (matricula_usuario, id_livro, data_solicitacao, data_expiracao, status) VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 'ATIVA')"
            );
            ps.setString(1, u.getMatricula());
            ps.setInt(2,    l.getId());
            ps.executeUpdate();

            Reserva r = new Reserva(u, l);
            dados.reservas.add(r);
            view.modelo.addRow(new Object[]{
                u.getNome(), l.getTitulo(),
                r.getDataSolicitacao(), r.getDataExpiracao(), r.getStatus()
            });
            JOptionPane.showMessageDialog(null, "Reserva feita! Valida por 7 dias.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao reservar: " + e.getMessage());
        }
    }

    void cancelar() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione uma reserva para cancelar."); return; }

        Reserva r = dados.reservas.get(linha);
        if (!r.getStatus().equals("ATIVA")) {
            JOptionPane.showMessageDialog(null, "Esta reserva ja foi cancelada.");
            return;
        }

        try {
            PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(
                "UPDATE reserva SET status = 'CANCELADA' WHERE id = ?"
            );
            ps.setInt(1, r.getId());
            ps.executeUpdate();

            r.setStatus("CANCELADA");
            view.modelo.setValueAt("CANCELADA", linha, 4);
            JOptionPane.showMessageDialog(null, "Reserva cancelada.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao cancelar: " + e.getMessage());
        }
    }

    void carregarTabela() {
        view.modelo.setRowCount(0);
        for (Reserva r : dados.reservas) {
            view.modelo.addRow(new Object[]{
                r.getUsuario().getNome(), r.getLivro().getTitulo(),
                r.getDataSolicitacao(), r.getDataExpiracao(), r.getStatus()
            });
        }
    }
}
