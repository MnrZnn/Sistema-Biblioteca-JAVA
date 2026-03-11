package controller;

import model.Dados;
import model.Funcionario;
import model.Usuario;
import view.FuncionarioView;

import javax.swing.*;
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
        Funcionario f = new Funcionario(
            view.campoNome.getText(),
            view.campoMatricula.getText(),
            view.campoEndereco.getText()
        );
        dados.usuarios.add(f);
        carregarTabela();
        limparCampos();
    }

    void editar() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um funcionario para editar."); return; }

        Funcionario f = getFuncionarios().get(linha);
        f.setNome(view.campoNome.getText());
        f.setMatricula(view.campoMatricula.getText());
        f.setEndereco(view.campoEndereco.getText());
        carregarTabela();
        limparCampos();
    }

    void remover() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um funcionario para remover."); return; }

        int confirm = JOptionPane.showConfirmDialog(null, "Remover este funcionario?");
        if (confirm == JOptionPane.YES_OPTION) {
            dados.usuarios.remove(getFuncionarios().get(linha));
            carregarTabela();
            limparCampos();
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
