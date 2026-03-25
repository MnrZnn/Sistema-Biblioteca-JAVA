package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Dados {

    public ArrayList<Usuario>              usuarios    = new ArrayList<>();
    public ArrayList<Livro>               livros      = new ArrayList<>();
    /** Fix 5: mapa id→Emprestimo para que o índice de linha não quebre com ordenação/filtro */
    public LinkedHashMap<Integer, Emprestimo> emprestimosMap = new LinkedHashMap<>();
    public ArrayList<Emprestimo>          emprestimos = new ArrayList<>();   // view conveniente sobre o mapa
    public ArrayList<Reserva>             reservas    = new ArrayList<>();

    public Dados() {
        carregarUsuarios();
        carregarLivros();
        carregarEmprestimos();
        carregarReservas();
    }

    public void carregarUsuarios() {
        usuarios.clear();
        try {
            Statement st = ConexaoBD.getConexao().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM usuario");
            while (rs.next()) {
                String mat  = rs.getString("matricula");
                String nome = rs.getString("nome");
                String end  = rs.getString("endereco");
                String tipo = rs.getString("tipo");

                if (tipo.equals("Aluno"))           usuarios.add(new Aluno(nome, mat, end));
                else if (tipo.equals("Professor"))   usuarios.add(new Professor(nome, mat, end));
                else if (tipo.equals("Funcionario")) usuarios.add(new Funcionario(nome, mat, end));
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar usuarios: " + e.getMessage());
        }
    }

    public void carregarLivros() {
        livros.clear();
        try {
            Statement st = ConexaoBD.getConexao().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM livro");
            while (rs.next()) {
                livros.add(new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("ano_publicacao"),
                        rs.getString("categoria"),
                        rs.getInt("quantidade_disponivel")
                ));
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar livros: " + e.getMessage());
        }
    }

    void carregarEmprestimos() {
        emprestimosMap.clear();
        emprestimos.clear();
        try {
            Statement st = ConexaoBD.getConexao().createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT e.*, u.nome, u.endereco, u.tipo, l.titulo, l.autor, " +
                            "l.ano_publicacao, l.categoria, l.quantidade_disponivel " +
                            "FROM emprestimo e " +
                            "JOIN usuario u ON e.matricula_usuario = u.matricula " +
                            "JOIN livro   l ON e.id_livro = l.id"
            );
            while (rs.next()) {
                String tipo = rs.getString("tipo");
                String mat  = rs.getString("matricula_usuario");
                String nome = rs.getString("nome");
                String end  = rs.getString("endereco");

                Usuario u;
                if (tipo.equals("Professor"))        u = new Professor(nome, mat, end);
                else if (tipo.equals("Funcionario")) u = new Funcionario(nome, mat, end);
                else                                 u = new Aluno(nome, mat, end);

                Livro l = new Livro(
                        rs.getInt("id_livro"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("ano_publicacao"),
                        rs.getString("categoria"),
                        rs.getInt("quantidade_disponivel")
                );

                Emprestimo emp = new Emprestimo(u, l);
                emp.setId(rs.getInt("id"));
                emp.setStatus(rs.getString("status"));
                // Fix 2: restaurar as datas reais do banco em vez de recalcular no construtor
                if (rs.getTimestamp("data_retirada") != null)
                    emp.setDataRetirada(new java.util.Date(rs.getTimestamp("data_retirada").getTime()));
                if (rs.getTimestamp("data_prevista") != null)
                    emp.setDataPrevista(new java.util.Date(rs.getTimestamp("data_prevista").getTime()));

                // Fix 5: indexar pelo id para acesso seguro a partir do controller
                emprestimosMap.put(emp.getId(), emp);
                emprestimos.add(emp);
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar emprestimos: " + e.getMessage());
        }
    }

    void carregarReservas() {
        reservas.clear();
        try {
            Statement st = ConexaoBD.getConexao().createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT r.*, u.nome, u.endereco, u.tipo, l.titulo, l.autor, " +
                            "l.ano_publicacao, l.categoria, l.quantidade_disponivel " +
                            "FROM reserva r " +
                            "JOIN usuario u ON r.matricula_usuario = u.matricula " +
                            "JOIN livro   l ON r.id_livro = l.id"
            );
            while (rs.next()) {
                String tipo = rs.getString("tipo");
                String mat  = rs.getString("matricula_usuario");
                String nome = rs.getString("nome");
                String end  = rs.getString("endereco");

                Usuario u;
                if (tipo.equals("Professor"))        u = new Professor(nome, mat, end);
                else if (tipo.equals("Funcionario")) u = new Funcionario(nome, mat, end);
                else                                 u = new Aluno(nome, mat, end);

                Livro l = new Livro(
                        rs.getInt("id_livro"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("ano_publicacao"),
                        rs.getString("categoria"),
                        rs.getInt("quantidade_disponivel")
                );

                Reserva res = new Reserva(u, l);
                res.setId(rs.getInt("id"));
                res.setStatus(rs.getString("status"));
                reservas.add(res);
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar reservas: " + e.getMessage());
        }
    }

    public int proximoIdLivro() {
        // O banco usa AUTO_INCREMENT, mas mantemos esse método para compatibilidade
        return livros.stream().mapToInt(Livro::getId).max().orElse(0) + 1;
    }
}