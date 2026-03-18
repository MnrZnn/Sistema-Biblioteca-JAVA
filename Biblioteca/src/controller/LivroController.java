package controller;

import model.*;
import view.LivroView;

import javax.swing.*;
import java.sql.*;

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

        view.tabela.getSelectionModel().addListSelectionListener(e -> preencherCampos());
    }

    void adicionar() {
        try {
            // AUTO_INCREMENT no banco, não precisamos passar o id
            PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(
                "INSERT INTO livro (titulo, autor, ano_publicacao, categoria, quantidade_disponivel) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, view.campoTitulo.getText());
            ps.setString(2, view.campoAutor.getText());
            ps.setInt(3,    Integer.parseInt(view.campoAno.getText()));
            ps.setString(4, view.campoCategoria.getText());
            ps.setInt(5,    Integer.parseInt(view.campoQtd.getText()));
            ps.executeUpdate();

            // Pega o id gerado pelo banco
            ResultSet rs = ps.getGeneratedKeys();
            int novoId = rs.next() ? rs.getInt(1) : dados.proximoIdLivro();

            dados.livros.add(new Livro(novoId,
                view.campoTitulo.getText(), view.campoAutor.getText(),
                Integer.parseInt(view.campoAno.getText()),
                view.campoCategoria.getText(),
                Integer.parseInt(view.campoQtd.getText())
            ));
            carregarTabela();
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos corretamente.\n" + e.getMessage());
        }
    }

    void editar() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um livro para editar."); return; }

        Livro l = dados.livros.get(linha);
        try {
            PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(
                "UPDATE livro SET titulo = ?, autor = ?, ano_publicacao = ?, categoria = ?, quantidade_disponivel = ? WHERE id = ?"
            );
            ps.setString(1, view.campoTitulo.getText());
            ps.setString(2, view.campoAutor.getText());
            ps.setInt(3,    Integer.parseInt(view.campoAno.getText()));
            ps.setString(4, view.campoCategoria.getText());
            ps.setInt(5,    Integer.parseInt(view.campoQtd.getText()));
            ps.setInt(6,    l.getId());
            ps.executeUpdate();

            l.setTitulo(view.campoTitulo.getText());
            l.setAutor(view.campoAutor.getText());
            l.setAnoPublicacao(Integer.parseInt(view.campoAno.getText()));
            l.setCategoria(view.campoCategoria.getText());
            l.setQuantidade(Integer.parseInt(view.campoQtd.getText()));
            carregarTabela();
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar: " + e.getMessage());
        }
    }

    void remover() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um livro para remover."); return; }

        int confirm = JOptionPane.showConfirmDialog(null, "Remover este livro?");
        if (confirm != JOptionPane.YES_OPTION) return;

        Livro l = dados.livros.get(linha);
        try {
            PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(
                "DELETE FROM livro WHERE id = ?"
            );
            ps.setInt(1, l.getId());
            ps.executeUpdate();

            dados.livros.remove(linha);
            carregarTabela();
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao remover: " + e.getMessage());
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
