package model;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexaoBD {

    // Altere URL, usuario e senha conforme seu MySQL
    private static final String URL    = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USUARIO = "root";
    private static final String SENHA   = "";

    private static Connection conexao;

    public static Connection getConexao() {
        try {
            if (conexao == null || conexao.isClosed()) {
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            }
        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
        return conexao;
    }
}
