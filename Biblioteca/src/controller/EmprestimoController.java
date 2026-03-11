package controller;

import model.*;
import view.EmprestimoView;

import javax.swing.*;

public class EmprestimoController {

    private EmprestimoView view;
    private Dados          dados;

    public EmprestimoController(EmprestimoView view, Dados dados) {
        this.view  = view;
        this.dados = dados;

        recarregarCombos();

        view.btnEmprestar.addActionListener(e -> emprestar());
        view.btnDevolver.addActionListener(e  -> devolver());
    }

    // Chamado pelo Main sempre que a aba de Emprestimos for aberta
    public void recarregarCombos() {
        view.cbUsuario.removeAllItems();
        view.cbLivro.removeAllItems();
        for (Usuario u : dados.usuarios) view.cbUsuario.addItem(u);
        for (Livro l   : dados.livros)   view.cbLivro.addItem(l);
    }

    void emprestar() {
        Usuario u = (Usuario) view.cbUsuario.getSelectedItem();
        Livro   l = (Livro)   view.cbLivro.getSelectedItem();

        if (!l.verificarDisponibilidade()) {
            JOptionPane.showMessageDialog(null, "Livro indisponivel!");
            return;
        }

        Emprestimo emp = new Emprestimo(u, l);
        l.diminuirQuantidade();
        dados.emprestimos.add(emp);
        view.modelo.addRow(new Object[]{
                u.getNome(), l.getTitulo(),
                emp.getDataRetirada(), emp.getDataPrevista(), emp.getStatus()
        });
        JOptionPane.showMessageDialog(null, "Emprestimo realizado!");
    }

    void devolver() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um emprestimo na tabela."); return; }

        Emprestimo emp = dados.emprestimos.get(linha);
        if (!emp.getStatus().equals("ATIVO")) {
            JOptionPane.showMessageDialog(null, "Este emprestimo ja foi finalizado.");
            return;
        }

        emp.finalizarEmprestimo();
        emp.getLivro().aumentarQuantidade();

        Multa multa = emp.gerarMulta();
        if (multa != null) JOptionPane.showMessageDialog(null, "Devolvido com atraso!\n" + multa);
        else               JOptionPane.showMessageDialog(null, "Devolvido com sucesso!");

        view.modelo.setValueAt(emp.getStatus(), linha, 4);
    }
}