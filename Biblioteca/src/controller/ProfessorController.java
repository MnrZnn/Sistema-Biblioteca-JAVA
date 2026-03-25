package controller;

import model.*;
import view.ProfessorView;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class ProfessorController {

    private ProfessorView view;
    private Dados         dados;

    public ProfessorController(ProfessorView view, Dados dados) {
        this.view  = view;
        this.dados = dados;

        carregarTabela();

        view.btnAdicionar.addActionListener(e -> adicionar());
        view.btnEditar.addActionListener(e    -> editar());
        view.btnRemover.addActionListener(e   -> remover());
        view.btnLimpar.addActionListener(e    -> limparCampos());

        view.tabela.getSelectionModel().addListSelectionListener(e -> preencherCampos());
    }

    ArrayList<Professor> getProfessores() {
        ArrayList<Professor> lista = new ArrayList<>();
        for (Usuario u : dados.usuarios) {
            if (u instanceof Professor) lista.add((Professor) u);
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
                "INSERT INTO usuario (matricula, nome, endereco, tipo) VALUES (?, ?, ?, 'Professor')"
            );
            ps.setString(1, mat);
            ps.setString(2, nome);
            ps.setString(3, end);
            ps.executeUpdate();

            dados.usuarios.add(new Professor(nome, mat, end));
            carregarTabela();
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar: " + e.getMessage());
        }
    }

    void editar() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um professor para editar."); return; }

        Professor p = getProfessores().get(linha);
        try {
            PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(
                "UPDATE usuario SET nome = ?, endereco = ? WHERE matricula = ?"
            );
            ps.setString(1, view.campoNome.getText());
            ps.setString(2, view.campoEndereco.getText());
            ps.setString(3, p.getMatricula());
            ps.executeUpdate();

            p.setNome(view.campoNome.getText());
            p.setEndereco(view.campoEndereco.getText());
            carregarTabela();
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar: " + e.getMessage());
        }
    }

    void remover() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um professor para remover."); return; }

        int confirm = JOptionPane.showConfirmDialog(null, "Remover este professor?");
        if (confirm != JOptionPane.YES_OPTION) return;

        Professor p = getProfessores().get(linha);
        try {
            PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(
                "DELETE FROM usuario WHERE matricula = ?"
            );
            ps.setString(1, p.getMatricula());
            ps.executeUpdate();

            dados.usuarios.remove(p);
            carregarTabela();
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao remover: " + e.getMessage());
        }
    }

    void preencherCampos() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) return;
        Professor p = getProfessores().get(linha);
        view.campoNome.setText(p.getNome());
        view.campoMatricula.setText(p.getMatricula());
        view.campoEndereco.setText(p.getEndereco());
    }

    void limparCampos() {
        view.campoNome.setText("");
        view.campoMatricula.setText("");
        view.campoEndereco.setText("");
        view.tabela.clearSelection();
    }

    void carregarTabela() {
        view.modelo.setRowCount(0);
        for (Professor p : getProfessores()) {
            view.modelo.addRow(new Object[]{p.getNome(), p.getMatricula(), p.getEndereco()});
        }
    }
}
