package controller;

import model.*;
import view.FuncionarioView;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class FuncionarioController {

    private FuncionarioView view;
    private Dados           dados;

    public FuncionarioController(FuncionarioView view, Dados dados) {
        this.view  = view;
        this.dados = dados;

        carregarTabela();

        view.btnAdicionar.addActionListener(e -> adicionar());
        view.btnEditar.addActionListener(e    -> editar());
        view.btnRemover.addActionListener(e   -> remover());
        view.btnLimpar.addActionListener(e    -> limparCampos());

        view.tabela.getSelectionModel().addListSelectionListener(e -> preencherCampos());
    }

    ArrayList<Funcionario> getFuncionarios() {
        ArrayList<Funcionario> lista = new ArrayList<>();
        for (Usuario u : dados.usuarios) {
            if (u instanceof Funcionario) lista.add((Funcionario) u);
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
                "INSERT INTO usuario (matricula, nome, endereco, tipo) VALUES (?, ?, ?, 'Funcionario')"
            );
            ps.setString(1, mat);
            ps.setString(2, nome);
            ps.setString(3, end);
            ps.executeUpdate();

            dados.usuarios.add(new Funcionario(nome, mat, end));
            carregarTabela();
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar: " + e.getMessage());
        }
    }

    void editar() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um funcionario para editar."); return; }

        Funcionario f = getFuncionarios().get(linha);
        try {
            PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(
                "UPDATE usuario SET nome = ?, endereco = ? WHERE matricula = ?"
            );
            ps.setString(1, view.campoNome.getText());
            ps.setString(2, view.campoEndereco.getText());
            ps.setString(3, f.getMatricula());
            ps.executeUpdate();

            f.setNome(view.campoNome.getText());
            f.setEndereco(view.campoEndereco.getText());
            carregarTabela();
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar: " + e.getMessage());
        }
    }

    void remover() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um funcionario para remover."); return; }

        int confirm = JOptionPane.showConfirmDialog(null, "Remover este funcionario?");
        if (confirm != JOptionPane.YES_OPTION) return;

        Funcionario f = getFuncionarios().get(linha);
        try {
            PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(
                "DELETE FROM usuario WHERE matricula = ?"
            );
            ps.setString(1, f.getMatricula());
            ps.executeUpdate();

            dados.usuarios.remove(f);
            carregarTabela();
            limparCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao remover: " + e.getMessage());
        }
    }

    void preencherCampos() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) return;
        Funcionario f = getFuncionarios().get(linha);
        view.campoNome.setText(f.getNome());
        view.campoMatricula.setText(f.getMatricula());
        view.campoEndereco.setText(f.getEndereco());
    }

    void limparCampos() {
        view.campoNome.setText("");
        view.campoMatricula.setText("");
        view.campoEndereco.setText("");
        view.tabela.clearSelection();
    }

    void carregarTabela() {
        view.modelo.setRowCount(0);
        for (Funcionario f : getFuncionarios()) {
            view.modelo.addRow(new Object[]{f.getNome(), f.getMatricula(), f.getEndereco()});
        }
    }
}
