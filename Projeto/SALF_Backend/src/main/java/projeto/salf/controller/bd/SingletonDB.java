package projeto.salf.controller.bd;

import java.sql.Connection;
import java.sql.SQLException;

public class SingletonDB {
    private static Conexao conexao = null;

    private SingletonDB() {}

    public static synchronized boolean conectar() {
        // Fecha a antiga se tiver quebrada/abERTA
        close();
        conexao = new Conexao();
        boolean status = conexao.conectar(
                "jdbc:postgresql://localhost:5432/",
                "salf_db",
                "postgres",
                "postgres" // Banco do Wilker é essa senha

        );
        if (!status) {
            System.err.println("Falha ao conectar: " + conexao.getMensagemErro());
            conexao = null;
        } else {
            System.out.println("✅ Conectado ao banco com sucesso!");
        }
        return status;
    }

    public static synchronized Conexao getConexao() {
        try {
            if (conexao == null || !conexao.getEstadoConexao()) {
                System.out.println("Recriando conexão com o banco...");
                conectar();
            }
        } catch (Exception e) {
            System.err.println("Erro ao verificar/restabelecer conexão: " + e.getMessage());
            conectar();
        }
        return conexao;
    }

    /** Retorna a Connection pura do JDBC (para uso avançado, commits manuais, etc) */
    public static Connection getRawConnection() {
        Conexao c = getConexao();
        return c != null ? c.getConnect() : null;
    }

    /** Força commit manual (caso autocommit esteja desativado) */
    public static void commit() {
        try {
            Connection conn = getRawConnection();
            if (conn != null && !conn.getAutoCommit()) {
                conn.commit();
                System.out.println("✅ Transação confirmada (commit).");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao dar commit: " + e.getMessage());
        }
    }

    /** Força rollback manual */
    public static void rollback() {
        try {
            Connection conn = getRawConnection();
            if (conn != null && !conn.getAutoCommit()) {
                conn.rollback();
                System.out.println("⛔ Transação revertida (rollback).");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao dar rollback: " + e.getMessage());
        }
    }

    public static synchronized void close() {
        try {
            if (conexao != null) {
                conexao.close();
                System.out.println("🔒 Conexão fechada com sucesso.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        } finally {
            conexao = null;
        }
    }

    static {
        // Fecha limpo ao encerrar a JVM
        Runtime.getRuntime().addShutdownHook(new Thread(SingletonDB::close));
    }
}
