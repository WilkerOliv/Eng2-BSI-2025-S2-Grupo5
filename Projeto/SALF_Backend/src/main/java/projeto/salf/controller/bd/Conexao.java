package projeto.salf.controller.bd;

import java.sql.*;
import java.util.*;

public class Conexao {
    private Connection connect;
    private String erro;

    public Conexao() {
        this.erro = "";
        this.connect = null;
    }

    public Connection getConnect() {
        return connect;
    }

    public boolean conectar(String local, String banco, String usuario, String senha) {
        boolean conectado = false;
        try {
            String url = local + banco;
            connect = DriverManager.getConnection(url, usuario, senha);
            connect.setAutoCommit(true);
            conectado = true;
        } catch (SQLException sqlex) {
            erro = "Impossível conectar com a base de dados: " + sqlex;
        } catch (Exception ex) {
            erro = "Outro erro: " + ex;
        }
        return conectado;
    }

    public String getMensagemErro() {
        return erro;
    }

    public boolean getEstadoConexao() {
        try {
            return connect != null && !connect.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /** Execuções DML (INSERT/UPDATE/DELETE) */
    public boolean manipular(String sql) {
        try (Statement st = connect.createStatement()) {
            int result = st.executeUpdate(sql);
            return result >= 1;
        } catch (SQLException e) {
            erro = "Erro: " + e;
            return false;
        }
    }


    /** Execuções DML parametrizadas */
    public boolean manipular(String sql, Object... params) {
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            erro = "Erro ao executar DML: " + e;
            return false;
        }
    }

    public List<Map<String, Object>> consultar(String sql) {
        List<Map<String, Object>> lista = new ArrayList<>();
        try (Statement st = connect.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            ResultSetMetaData md = rs.getMetaData();
            int cols = md.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>(cols);
                for (int i = 1; i <= cols; i++) {
                    String col = md.getColumnLabel(i);
                    row.put(col, rs.getObject(i));
                }
                lista.add(row);
            }
        } catch (SQLException e) {
            erro = "Erro: " + e;
        }
        return lista;
    }

    public List<Map<String, Object>> consultar(String sql, Object... params) {
        List<Map<String, Object>> lista = new ArrayList<>();
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                int cols = md.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>(cols);
                    for (int i = 1; i <= cols; i++) {
                        String col = md.getColumnLabel(i);
                        row.put(col, rs.getObject(i));
                    }
                    lista.add(row);
                }
            }
        } catch (SQLException e) {
            erro = "Erro: " + e;
        }
        return lista;
    }

    /** executa uma query que retorna apenas 1 valor (ex: count, max) */
    public Object consultarValorUnico(String sql, Object... params) {
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getObject(1);
            }
        } catch (SQLException e) {
            erro = "Erro: " + e;
        }
        return null;
    }

    /** retorna true se existir pelo menos 1 resultado */
    public boolean existe(String sql, Object... params) {
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            erro = "Erro: " + e;
            return false;
        }
    }

    public int getMaxPK(String tabela, String chave) {
        String sql = "select max(" + chave + ") as max_pk from " + tabela;
        try (Statement st = connect.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("max_pk");
            }
            return 0;
        } catch (SQLException e) {
            erro = "Erro: " + e;
            return -1;
        }
    }

    /** inicia transação manual */
    public void iniciarTransacao() {
        try {
            connect.setAutoCommit(false);
        } catch (SQLException e) {
            erro = "Erro ao iniciar transação: " + e;
        }
    }

    /** confirma transação */
    public void commit() {
        try {
            connect.commit();
        } catch (SQLException e) {
            erro = "Erro ao confirmar transação: " + e;
        }
    }

    /** desfaz transação */
    public void rollback() {
        try {
            connect.rollback();
        } catch (SQLException e) {
            erro = "Erro ao reverter transação: " + e;
        }
    }

    public void close() {
        try {
            if (connect != null && !connect.isClosed()) {
                connect.close();
            }
        } catch (SQLException e) {
            // ignora
        } finally {
            connect = null;
        }
    }

    public int manipularComRetorno(String sql, Object... params) {
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            int linhas = ps.executeUpdate();
            System.out.println("🧱 SQL executado: " + linhas + " linha(s) afetada(s)");
            return linhas;
        } catch (SQLException e) {
            erro = "Erro ao executar manipularComRetorno: " + e;
            System.err.println(erro);
            return 0;
        }
    }
}
