
package projeto.salf.controller.bd;

import java.sql.*;
import java.util.*;


public class Conexao {

    private Connection connect;
    private String erro;

    public Conexao(String url, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            connect = DriverManager.getConnection(url, user, password);
            connect.setAutoCommit(true);
            erro = null;
            // System.out.println("Conexão aberta com sucesso.");
        } catch (Exception e) {
            erro = "Erro ao conectar: " + e.getMessage();
            connect = null;
            System.err.println(erro);
        }
    }

    public boolean getEstadoConexao() {
        try {
            return connect != null && !connect.isClosed();
        } catch (SQLException e) {
            erro = "Erro ao verificar conexão: " + e.getMessage();
            return false;
        }
    }

    public String getMensagemErro() {
        return erro;
    }

    public Connection getConnection() {
        return connect;
    }

    // INSERT/UPDATE/DELETE
    public boolean manipular(String sql, Object... params) {
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            erro = "Erro ao manipular: " + e.getMessage();
            System.err.println(erro);
            return false;
        }
    }

    // SELECT
    public List<Map<String, Object>> consultar(String sql, Object... params) {
        List<Map<String, Object>> lista = new ArrayList<>();
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                int colCount = md.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= colCount; i++) {
                        String col = md.getColumnLabel(i);
                        row.put(col, rs.getObject(i));
                    }
                    lista.add(row);
                }
            }
        } catch (SQLException e) {
            erro = "Erro ao consultar: " + e.getMessage();
            System.err.println(erro);
        }
        return lista;
    }

    // Retorna apenas um valor
    public Object consultarValorUnico(String sql, Object... params) {
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getObject(1);
            }
        } catch (SQLException e) {
            erro = "Erro ao consultar valor único: " + e.getMessage();
            System.err.println(erro);
        }
        return null;
    }

    // Verifica se existe registro
    public boolean existe(String sql, Object... params) {
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            erro = "Erro ao verificar existência: " + e.getMessage();
            System.err.println(erro);
            return false;
        }
    }

    public void close() {
        try {
            if (connect != null && !connect.isClosed()) {
                connect.close();
                System.out.println("Conexão fechada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        } finally {
            connect = null;
        }
    }
}
