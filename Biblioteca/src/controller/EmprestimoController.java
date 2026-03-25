package controller;

import model.*;
import view.EmprestimoView;

import javax.swing.*;
import java.sql.*;

public class EmprestimoController {

    private EmprestimoView view;
    private Dados          dados;

    public EmprestimoController(EmprestimoView view, Dados dados) {
        this.view  = view;
        this.dados = dados;

        recarregarCombos();
        carregarTabela();

        view.btnEmprestar.addActionListener(e -> emprestar());
        view.btnDevolver.addActionListener(e  -> devolver());
    }

    public void recarregarCombos() {
        dados.carregarUsuarios();
        dados.carregarLivros();
        view.cbUsuario.removeAllItems();
        view.cbLivro.removeAllItems();
        for (Usuario u : dados.usuarios) view.cbUsuario.addItem(u);
        for (Livro l   : dados.livros)   view.cbLivro.addItem(l);
    }

    void emprestar() {
        Usuario u = (Usuario) view.cbUsuario.getSelectedItem();
        Livro   l = (Livro)   view.cbLivro.getSelectedItem();
        if (u == null || l == null) return;

        if (!l.verificarDisponibilidade()) {
            JOptionPane.showMessageDialog(null, "Livro indisponivel!");
            return;
        }

        try {
            PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(
                    "INSERT INTO emprestimo (matricula_usuario, id_livro, data_retirada, data_prevista, status) VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 14 DAY), 'ATIVO')",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, u.getMatricula());
            ps.setInt(2,    l.getId());
            ps.executeUpdate();

            // Captura o id gerado e seta no objeto
            ResultSet rsKeys = ps.getGeneratedKeys();
            Emprestimo emp = new Emprestimo(u, l);
            if (rsKeys.next()) emp.setId(rsKeys.getInt(1));

            // Atualiza quantidade no banco
            PreparedStatement psLivro = ConexaoBD.getConexao().prepareStatement(
                    "UPDATE livro SET quantidade_disponivel = quantidade_disponivel - 1 WHERE id = ?"
            );
            psLivro.setInt(1, l.getId());
            psLivro.executeUpdate();

            l.diminuirQuantidade();

            // Fix 5: indexar no mapa além da lista
            dados.emprestimosMap.put(emp.getId(), emp);
            dados.emprestimos.add(emp);

            view.modelo.addRow(new Object[]{
                    emp.getId(),                                          // coluna oculta usada como chave
                    u.getNome(), l.getTitulo(),
                    emp.getDataRetirada(), emp.getDataPrevista(), emp.getStatus()
            });
            JOptionPane.showMessageDialog(null, "Emprestimo realizado!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao emprestar: " + e.getMessage());
        }
    }

    void devolver() {
        int linha = view.tabela.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(null, "Selecione um emprestimo na tabela."); return; }

        // Fix 5: recuperar pelo id guardado na coluna 0 do modelo, não pelo índice de linha
        int empId = (int) view.modelo.getValueAt(linha, 0);
        Emprestimo emp = dados.emprestimosMap.get(empId);
        if (emp == null) { JOptionPane.showMessageDialog(null, "Emprestimo nao encontrado."); return; }

        if (!emp.getStatus().equals("ATIVO")) {
            JOptionPane.showMessageDialog(null, "Este emprestimo ja foi finalizado.");
            return;
        }

        emp.finalizarEmprestimo();

        try {
            PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(
                    "UPDATE emprestimo SET data_devolucao_real = NOW(), status = ? WHERE id = ?"
            );
            ps.setString(1, emp.getStatus());
            ps.setInt(2,    emp.getId());
            ps.executeUpdate();

            // Atualiza quantidade no banco
            PreparedStatement psLivro = ConexaoBD.getConexao().prepareStatement(
                    "UPDATE livro SET quantidade_disponivel = quantidade_disponivel + 1 WHERE id = ?"
            );
            psLivro.setInt(1, emp.getLivro().getId());
            psLivro.executeUpdate();

            emp.getLivro().aumentarQuantidade();

            Multa multa = emp.gerarMulta();
            if (multa != null) JOptionPane.showMessageDialog(null, "Devolvido com atraso!\n" + multa);
            else               JOptionPane.showMessageDialog(null, "Devolvido com sucesso!");

            // coluna de status agora é a 5 (índice 5), pois adicionamos a coluna id na posição 0
            view.modelo.setValueAt(emp.getStatus(), linha, 5);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao devolver: " + e.getMessage());
        }
    }

    void carregarTabela() {
        view.modelo.setRowCount(0);
        dados.emprestimosMap.clear();
        for (Emprestimo emp : dados.emprestimos) {
            dados.emprestimosMap.put(emp.getId(), emp);
            view.modelo.addRow(new Object[]{
                    emp.getId(),                          // coluna 0: chave de lookup (Fix 5)
                    emp.getUsuario().getNome(), emp.getLivro().getTitulo(),
                    emp.getDataRetirada(), emp.getDataPrevista(), emp.getStatus()
            });
        }
    }
}