package controller;

import model.*;
import view.AlunoView;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class AlunoController {

    private AlunoView view;
    private Dados     dados;

    public AlunoController(AlunoView view, Dados dados) {
        this.view  = view;
        this.dados = dados;

        carregarTabela();

        view.btnAdicionar.addActionListener(e -> adicionar());
        view.btnEditar.addActionListener(e    -> editar());
        view.btnRemover.addActionListener(e   -> remover());
        view.btnLimpar.addActionListener(e    -> limparCampos());

        view.tabela.getSelectionModel().addListSelectionListener(e -> preencherCampos());
    }

    ArrayList<Aluno> getAlunos() {
        ArrayList<Aluno> lista = new ArrayList<>();
        for (Usuario u : dados.usuarios) {
            if (u instanceof Aluno) lista.add((Aluno) u);
        }
        return lista;
    }

    void adicionar() {
        String nome = view.campoNome.getText().trim();
        String mat  = view.campoMatricula.getText().trim();
        String end  = view.campoEndereco.getText().trim();

        if (nome.isEmpty() || mat.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nome e matricula sao obrigatorios.");
            return;
        }

        try {
            PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(
                "INSERT INTO usuario (matricula, nome, endereco, tipo) VALUES (?, ?, ?, 'Aluno')"
            );
            ps.setString(1, mat);
            ps.setString(2, nome);
            ps.setString(3, end);
            ps.executeUpdate();

            dados.usuarios.add(new Aluno(nome, mat, end));
            carregarTabela();
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar: " + e.getMessage());
        }
    }

    void editar() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um aluno para editar."); return; }

        Aluno a    = getAlunos().get(linha);
        String mat = a.getMatricula(); // matricula é a PK, não muda

        try {
            PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(
                "UPDATE usuario SET nome = ?, endereco = ? WHERE matricula = ?"
            );
            ps.setString(1, view.campoNome.getText());
            ps.setString(2, view.campoEndereco.getText());
            ps.setString(3, mat);
            ps.executeUpdate();

            a.setNome(view.campoNome.getText());
            a.setEndereco(view.campoEndereco.getText());
            carregarTabela();
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar: " + e.getMessage());
        }
    }

    void remover() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um aluno para remover."); return; }

        int confirm = JOptionPane.showConfirmDialog(null, "Remover este aluno?");
        if (confirm != JOptionPane.YES_OPTION) return;

        Aluno a = getAlunos().get(linha);
        try {
            PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(
                "DELETE FROM usuario WHERE matricula = ?"
            );
            ps.setString(1, a.getMatricula());
            ps.executeUpdate();

            dados.usuarios.remove(a);
            carregarTabela();
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao remover: " + e.getMessage());
        }
    }

    void preencherCampos() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) return;
        Aluno a = getAlunos().get(linha);
        view.campoNome.setText(a.getNome());
        view.campoMatricula.setText(a.getMatricula());
        view.campoEndereco.setText(a.getEndereco());
    }

    void limparCampos() {
        view.campoNome.setText("");
        view.campoMatricula.setText("");
        view.campoEndereco.setText("");
        view.tabela.clearSelection();
    }

    void carregarTabela() {
        view.modelo.setRowCount(0);
        for (Aluno a : getAlunos()) {
            view.modelo.addRow(new Object[]{a.getNome(), a.getMatricula(), a.getEndereco()});
        }
    }
}
