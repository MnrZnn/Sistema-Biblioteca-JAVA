import model.Dados;
import view.*;
import controller.*;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Dados dados = new Dados();

        // View
        AlunoView       alunoView       = new AlunoView();
        ProfessorView   professorView   = new ProfessorView();
        FuncionarioView funcionarioView = new FuncionarioView();
        LivroView       livroView       = new LivroView();
        EmprestimoView  emprestimoView  = new EmprestimoView();
        ReservaView     reservaView     = new ReservaView();

        // Controller
        new AlunoController(alunoView, dados);
        new ProfessorController(professorView, dados);
        new FuncionarioController(funcionarioView, dados);
        new LivroController(livroView, dados);
        EmprestimoController empController = new EmprestimoController(emprestimoView, dados);
        ReservaController    resController = new ReservaController(reservaView, dados);

        // Panel
        JTabbedPane abas = new JTabbedPane();
        abas.add("Alunos",       alunoView);
        abas.add("Professores",  professorView);
        abas.add("Funcionarios", funcionarioView);
        abas.add("Livros",       livroView);
        abas.add("Emprestimos",  emprestimoView);
        abas.add("Reservas",     reservaView);

        // Sistema simplifiado de paginação
        abas.addChangeListener(e -> {
            int aba = abas.getSelectedIndex();
            if (aba == 4) empController.recarregarCombos();
            if (aba == 5) resController.recarregarCombos();
        });

        JFrame janela = new JFrame("Sistema de Biblioteca");
        janela.setSize(800, 550);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLocationRelativeTo(null);
        janela.add(abas);
        janela.setVisible(true);
    }
}