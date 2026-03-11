package controller;

import model.Dados;
import model.Livro;
import view.LivroView;

import javax.swing.*;

public class LivroController {

    private LivroView view;
    private Dados     dados;

    public LivroController(LivroView view, Dados dados) {
        this.view  = view;
        this.dados = dados;

        carregarTabela();

        view.btnAdicionar.addActionListener(e -> adicionar());
        view.btnEditar.addActionListener(e    -> editar());
        view.btnRemover.addActionListener(e   -> remover());
        view.btnLimpar.addActionListener(e    -> limparCampos());

        // Ao clicar numa linha, preenche os campos para editar
        view.tabela.getSelectionModel().addListSelectionListener(e -> preencherCampos());
    }

    void adicionar() {
        try {
            Livro l = new Livro(
                dados.proximoIdLivro(),
                view.campoTitulo.getText(),
                view.campoAutor.getText(),
                Integer.parseInt(view.campoAno.getText()),
                view.campoCategoria.getText(),
                Integer.parseInt(view.campoQtd.getText())
            );
            dados.livros.add(l);
            carregarTabela();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos corretamente.");
        }
    }

    void editar() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um livro para editar."); return; }

        try {
            Livro l = dados.livros.get(linha);
            l.setTitulo(view.campoTitulo.getText());
            l.setAutor(view.campoAutor.getText());
            l.setAnoPublicacao(Integer.parseInt(view.campoAno.getText()));
            l.setCategoria(view.campoCategoria.getText());
            l.setQuantidade(Integer.parseInt(view.campoQtd.getText()));
            carregarTabela();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos corretamente.");
        }
    }

    void remover() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um livro para remover."); return; }

        int confirm = JOptionPane.showConfirmDialog(null, "Remover este livro?");
        if (confirm == JOptionPane.YES_OPTION) {
            dados.livros.remove(linha);
            carregarTabela();
            limparCampos();
        }
    }

    void preencherCampos() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) return;

        Livro l = dados.livros.get(linha);
        view.campoTitulo.setText(l.getTitulo());
        view.campoAutor.setText(l.getAutor());
        view.campoAno.setText(String.valueOf(l.getAnoPublicacao()));
        view.campoCategoria.setText(l.getCategoria());
        view.campoQtd.setText(String.valueOf(l.getQuantidadeDisponivel()));
    }

    void limparCampos() {
        view.campoTitulo.setText("");
        view.campoAutor.setText("");
        view.campoAno.setText("");
        view.campoCategoria.setText("");
        view.campoQtd.setText("");
        view.tabela.clearSelection();
    }

    void carregarTabela() {
        view.modelo.setRowCount(0);
        for (Livro l : dados.livros) {
            view.modelo.addRow(new Object[]{
                l.getId(), l.getTitulo(), l.getAutor(),
                l.getAnoPublicacao(), l.getCategoria(), l.getQuantidadeDisponivel()
            });
        }
    }
}
