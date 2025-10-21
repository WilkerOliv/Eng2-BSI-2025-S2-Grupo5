package projeto.salf.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            String url = local + banco; // ex: jdbc:postgresql://localhost:5432/salf_db
            connect = DriverManager.getConnection(url, usuario, senha);
            connect.setAutoCommit(true); // mantenho autocommit; ajuste se precisar de transação
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

    /**
     * CONSULTA SEGURA que já consome o ResultSet e fecha recursos.
     * Retorna uma lista de maps (coluna->valor). Evita leak de Statement/ResultSet.
     */
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
            return List.of();
        }
        return lista;
    }

    /** Versão que permite parâmetros com PreparedStatement */
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
            return List.of();
        }
        return lista;
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

    public void close() {
        try {
            if (connect != null && !connect.isClosed()) {
                connect.close();
            }
        } catch (SQLException e) {
            // loga e segue
        } finally {
            connect = null;
        }
    }
}
