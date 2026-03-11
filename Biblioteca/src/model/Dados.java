package model;

import java.util.ArrayList;

public class Dados {

    public ArrayList<Usuario>    usuarios    = new ArrayList<>();
    public ArrayList<Livro>      livros      = new ArrayList<>();
    public ArrayList<Emprestimo> emprestimos = new ArrayList<>();
    public ArrayList<Reserva>    reservas    = new ArrayList<>();

    public Dados() {
        usuarios.add(new Aluno("Joao Silva",   "A001", "Rua A, 10"));
        usuarios.add(new Aluno("Ana Souza",    "A002", "Rua B, 22"));
        usuarios.add(new Professor("Prof. Maria",  "P001", "Av. C, 5"));
        usuarios.add(new Funcionario("Carlos",     "F001", "Rua D, 3"));

        livros.add(new Livro(1, "Clean Code",     "Robert Martin",    2008, "Tecnologia", 3));
        livros.add(new Livro(2, "Dom Casmurro",   "Machado de Assis", 1899, "Literatura", 5));
        livros.add(new Livro(3, "O Hobbit",       "Tolkien",          1937, "Fantasia",   2));
    }

    public int proximoIdLivro() {
        return livros.stream().mapToInt(Livro::getId).max().orElse(0) + 1;
    }
}
