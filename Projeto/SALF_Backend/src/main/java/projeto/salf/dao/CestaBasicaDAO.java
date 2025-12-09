package projeto.salf.dao;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CestaBasicaDAO {

    public boolean registrarCesta(String descricao, List<Map<String, Object>> itens, Connection conn) {

        try {
            int cbCod = 1;
            String SQL_MAX = "SELECT COALESCE(MAX(cb_cod), 0) FROM cesta_basica";

            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(SQL_MAX)) {

                if (rs.next()) {
                    cbCod = rs.getInt(1) + 1;
                }
            }

            String SQL_CESTA =
                    "INSERT INTO cesta_basica (cb_cod, descricao) VALUES (?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(SQL_CESTA)) {
                ps.setInt(1, cbCod);
                ps.setString(2, descricao);
                ps.executeUpdate();
            }

            String SQL_ITEM =
                    "INSERT INTO cesta_basica_produto (cesta_basica_cb_cod, produto_prod_cod, quantidade) " +
                            "VALUES (?, ?, ?)";

            for (Map<String, Object> item : itens) {

                int prodCod = toInt(item.get("produtoProdCod"));
                int qtd = toInt(item.get("quantidade"));

                if (prodCod <= 0 || qtd <= 0) return false;

                try (PreparedStatement ps = conn.prepareStatement(SQL_ITEM)) {
                    ps.setInt(1, cbCod);
                    ps.setInt(2, prodCod);
                    ps.setInt(3, qtd);
                    ps.executeUpdate();
                }
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean substituirItensCesta(int cbCod, List<Map<String, Object>> itens, Connection conn) {

        try {
            String SQL_DEL =
                    "DELETE FROM cesta_basica_produto WHERE cesta_basica_cb_cod = ?";

            try (PreparedStatement ps = conn.prepareStatement(SQL_DEL)) {
                ps.setInt(1, cbCod);
                ps.executeUpdate();
            }

            String SQL_INS =
                    "INSERT INTO cesta_basica_produto (cesta_basica_cb_cod, produto_prod_cod, quantidade) " +
                            "VALUES (?, ?, ?)";

            for (Map<String, Object> item : itens) {

                int prodCod = toInt(item.get("produtoProdCod"));
                int qtd = toInt(item.get("quantidade"));

                if (prodCod <= 0 || qtd <= 0) return false;

                try (PreparedStatement ps = conn.prepareStatement(SQL_INS)) {
                    ps.setInt(1, cbCod);
                    ps.setInt(2, prodCod);
                    ps.setInt(3, qtd);
                    ps.executeUpdate();
                }
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, Object>> listarCestas(Connection conn) {

        List<Map<String, Object>> lista = new ArrayList<>();

        String SQL =
                "SELECT cb.cb_cod, cb.descricao, " +
                        "(SELECT COALESCE(SUM(quantidade), 0) FROM cesta_basica_produto cp " +
                        " WHERE cp.cesta_basica_cb_cod = cb.cb_cod) AS total_itens " +
                        "FROM cesta_basica cb ORDER BY cb.cb_cod DESC";

        try (PreparedStatement st = conn.prepareStatement(SQL);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("cb_cod", rs.getInt("cb_cod"));
                map.put("descricao", rs.getString("descricao"));
                map.put("total_itens", rs.getInt("total_itens"));
                lista.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Map<String, Object>> listarItensDaCesta(int cbCod, Connection conn) {

        List<Map<String, Object>> lista = new ArrayList<>();

        String SQL =
                "SELECT p.prod_cod AS produto_cod, " +
                        "       p.prod_descr AS produto, " +
                        "       cp.quantidade " +
                        "  FROM cesta_basica_produto cp " +
                        "  JOIN produto p ON p.prod_cod = cp.produto_prod_cod " +
                        " WHERE cp.cesta_basica_cb_cod = ?";

        try (PreparedStatement st = conn.prepareStatement(SQL)) {

            st.setInt(1, cbCod);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("produto_cod", rs.getInt("produto_cod"));
                map.put("produto", rs.getString("produto"));
                map.put("quantidade", rs.getInt("quantidade"));
                lista.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean excluirCesta(int cbCod, Connection conn) {

        try {
            String SQL_DEL_ITEMS =
                    "DELETE FROM cesta_basica_produto WHERE cesta_basica_cb_cod = ?";

            try (PreparedStatement ps = conn.prepareStatement(SQL_DEL_ITEMS)) {
                ps.setInt(1, cbCod);
                ps.executeUpdate();
            }

            String SQL_DEL_CESTA =
                    "DELETE FROM cesta_basica WHERE cb_cod = ?";

            try (PreparedStatement ps = conn.prepareStatement(SQL_DEL_CESTA)) {
                ps.setInt(1, cbCod);
                int linhas = ps.executeUpdate();
                return linhas > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizarDescricao(int cbCod, String nova, Connection conn) {
        String SQL =
                "UPDATE cesta_basica SET descricao = ? WHERE cb_cod = ?";

        try (PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setString(1, nova);
            ps.setInt(2, cbCod);
            int linhas = ps.executeUpdate();
            return linhas > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private int toInt(Object o) {
        if (o instanceof Number n) return n.intValue();
        try { return Integer.parseInt(o.toString()); }
        catch (Exception e) { return -1; }
    }
}
