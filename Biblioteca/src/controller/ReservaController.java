package controller;

import model.*;
import view.ReservaView;

import javax.swing.*;

public class ReservaController {

    private ReservaView view;
    private Dados       dados;

    public ReservaController(ReservaView view, Dados dados) {
        this.view  = view;
        this.dados = dados;

        recarregarCombos();

        view.btnReservar.addActionListener(e -> reservar());
        view.btnCancelar.addActionListener(e -> cancelar());
    }

    // Chamar pelo Main sempre que a aba de Reservas for aberta
    public void recarregarCombos() {
        view.cbUsuario.removeAllItems();
        view.cbLivro.removeAllItems();
        for (Usuario u : dados.usuarios) view.cbUsuario.addItem(u);
        for (Livro l   : dados.livros)   view.cbLivro.addItem(l);
    }

    void reservar() {
        Usuario u = (Usuario) view.cbUsuario.getSelectedItem();
        Livro   l = (Livro)   view.cbLivro.getSelectedItem();

        Reserva r = new Reserva(u, l);
        dados.reservas.add(r);
        view.modelo.addRow(new Object[]{
                u.getNome(), l.getTitulo(),
                r.getDataSolicitacao(), r.getDataExpiracao(), r.getStatus()
        });
        JOptionPane.showMessageDialog(null, "Reserva feita! Valida por 7 dias.");
    }

    void cancelar() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione uma reserva para cancelar."); return; }

        Reserva r = dados.reservas.get(linha);
        if (!r.getStatus().equals("ATIVA")) {
            JOptionPane.showMessageDialog(null, "Esta reserva ja foi cancelada.");
            return;
        }

        r.setStatus("CANCELADA");
        view.modelo.setValueAt("CANCELADA", linha, 4);
        JOptionPane.showMessageDialog(null, "Reserva cancelada.");
    }
}