package controller;

import model.Dados;
import model.Professor;
import model.Usuario;
import view.ProfessorView;

import javax.swing.*;
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
        Professor p = new Professor(
            view.campoNome.getText(),
            view.campoMatricula.getText(),
            view.campoEndereco.getText()
        );
        dados.usuarios.add(p);
        carregarTabela();
        limparCampos();
    }

    void editar() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um professor para editar."); return; }

        Professor p = getProfessores().get(linha);
        p.setNome(view.campoNome.getText());
        p.setMatricula(view.campoMatricula.getText());
        p.setEndereco(view.campoEndereco.getText());
        carregarTabela();
        limparCampos();
    }

    void remover() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um professor para remover."); return; }

        int confirm = JOptionPane.showConfirmDialog(null, "Remover este professor?");
        if (confirm == JOptionPane.YES_OPTION) {
            dados.usuarios.remove(getProfessores().get(linha));
            carregarTabela();
            limparCampos();
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
