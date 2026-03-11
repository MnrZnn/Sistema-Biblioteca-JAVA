package controller;

import model.Aluno;
import model.Dados;
import model.Usuario;
import view.AlunoView;

import javax.swing.*;
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

    // Pega só os alunos da lista geral de usuarios
    ArrayList<Aluno> getAlunos() {
        ArrayList<Aluno> lista = new ArrayList<>();
        for (Usuario u : dados.usuarios) {
            if (u instanceof Aluno) lista.add((Aluno) u);
        }
        return lista;
    }

    void adicionar() {
        Aluno a = new Aluno(
            view.campoNome.getText(),
            view.campoMatricula.getText(),
            view.campoEndereco.getText()
        );
        dados.usuarios.add(a);
        carregarTabela();
        limparCampos();
    }

    void editar() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um aluno para editar."); return; }

        Aluno a = getAlunos().get(linha);
        a.setNome(view.campoNome.getText());
        a.setMatricula(view.campoMatricula.getText());
        a.setEndereco(view.campoEndereco.getText());
        carregarTabela();
        limparCampos();
    }

    void remover() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um aluno para remover."); return; }

        int confirm = JOptionPane.showConfirmDialog(null, "Remover este aluno?");
        if (confirm == JOptionPane.YES_OPTION) {
            dados.usuarios.remove(getAlunos().get(linha));
            carregarTabela();
            limparCampos();
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
