package projeto.salf.dao;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoacaoPCDAO {

    public DoacaoPCDAO() { }

    @SuppressWarnings("unchecked")
    public Map<String, Object> registrarDoacao(Map<String, Object> dados, Connection conn) {

        String funcCpf = toStr(dados.get("funcCpf"));
        String pcCpf   = toStr(dados.get("pcCpf"));
        Integer cbCod  = toInt(dados.get("cbCod"));

        if (funcCpf != null) funcCpf = funcCpf.trim();
        if (pcCpf   != null) pcCpf   = pcCpf.trim();

        List<Map<String, Object>> itens =
                (List<Map<String, Object>>) dados.get("itens");

        try {
            if (funcCpf == null || funcCpf.isBlank())
                return fail("CPF do funcionário é obrigatório");

            if (pcCpf == null || pcCpf.isBlank())
                return fail("CPF da pessoa carente é obrigatório");

            if (itens == null || itens.isEmpty())
                return fail("Nenhum item informado");

            if (!existeFuncionarioPorCpf(conn, funcCpf))
                return fail("Funcionário não encontrado");

            if (!existePessoaCarente(conn, pcCpf))
                return fail("Pessoa carente não encontrada");

            Integer dpcCod = inserirDoacaoPC(conn, funcCpf, pcCpf);
            if (dpcCod == null)
                return fail("Erro ao salvar cabeçalho da doação");

            for (Map<String, Object> it : itens) {
                Integer produtoCod = toInt(it.get("produtoCod"));
                Integer quantidade = toInt(it.get("quantidade"));

                if (produtoCod == null || quantidade == null || quantidade <= 0)
                    return fail("Itens inválidos");

                if (!debitarProdutoFIFO(conn, produtoCod, quantidade))
                    return fail("Estoque insuficiente para produto " + produtoCod);

                if (!inserirItemDoacaoPCProduto(conn, dpcCod, produtoCod, quantidade))
                    return fail("Erro ao salvar item da doação");
            }

            removerNecessidadesDaPessoa(conn, pcCpf);

            if (cbCod != null && cbCod > 0)
                excluirCestaLigada(conn, cbCod);

            Map<String, Object> resp = new HashMap<>();
            resp.put("sucesso", true);
            resp.put("mensagem", "Doação registrada, estoque baixado e necessidades removidas com sucesso");
            resp.put("doacaoPcCod", dpcCod);
            resp.put("funcCpf", funcCpf);
            resp.put("pcCpf", pcCpf);
            resp.put("cbCod", cbCod);
            resp.put("itens", itens);
            return resp;

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of(
                    "sucesso", false,
                    "mensagem", "Erro: " + e.getMessage()
            );
        }
    }

    private void removerNecessidadesDaPessoa(Connection conn, String cpf) throws SQLException {

        String sql =
                "DELETE FROM necessidade_produto " +
                        "WHERE regexp_replace(trim(pessoa_carente_pc_cpf), '[^0-9]', '', 'g') = " +
                        "      regexp_replace(trim(?), '[^0-9]', '', 'g')";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            int linhas = ps.executeUpdate();
            System.out.println("Necessidades removidas: " + linhas);
        }
    }


    private boolean existeFuncionarioPorCpf(Connection conn, String cpf) throws SQLException {
        final String sql =
                "SELECT 1 FROM funcionario " +
                        "WHERE regexp_replace(trim(func_cpf), '[^0-9]', '', 'g') = " +
                        "      regexp_replace(trim(?),        '[^0-9]', '', 'g') LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean existePessoaCarente(Connection conn, String cpf) throws SQLException {
        final String sql =
                "SELECT 1 FROM pessoa_carente " +
                        "WHERE regexp_replace(trim(pc_cpf), '[^0-9]', '', 'g') = " +
                        "      regexp_replace(trim(?),      '[^0-9]', '', 'g') LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Integer inserirDoacaoPC(Connection conn, String funcCpf, String pcCpf)
            throws SQLException {

        final String SQL =
                "INSERT INTO doacao_pc (pessoa_carente_pc_cpf, funcionario_func_cpf) " +
                        "VALUES (?, ?) RETURNING dpc_cod";

        try (PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setString(1, pcCpf);
            ps.setString(2, funcCpf);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return null;
    }

    private boolean inserirItemDoacaoPCProduto(Connection conn, int dpcCod, int prodCod, int qtd)
            throws SQLException {

        final String SQL =
                "INSERT INTO doacao_pc_produto (doacao_pc_dpc_cod, produto_prod_cod, quantidade) " +
                        "VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setInt(1, dpcCod);
            ps.setInt(2, prodCod);
            ps.setInt(3, qtd);
            return ps.executeUpdate() > 0;
        }
    }

    private boolean debitarProdutoFIFO(Connection conn, int produtoCod, int qtdNecessaria)
            throws SQLException {

        final String SQL_SELECT =
                "SELECT est_cod, est_prod_quantidade, data_validade " +
                        "FROM estoque " +
                        "WHERE produto_prod_cod = ? " +
                        "ORDER BY " +
                        "  CASE WHEN data_validade IS NULL THEN 1 ELSE 0 END, " +
                        "  data_validade ASC " +
                        "FOR UPDATE";

        final String SQL_UPDATE =
                "UPDATE estoque SET est_prod_quantidade = ? WHERE est_cod = ?";

        int restante = qtdNecessaria;

        try (PreparedStatement ps = conn.prepareStatement(SQL_SELECT);
             PreparedStatement up = conn.prepareStatement(SQL_UPDATE)) {

            ps.setInt(1, produtoCod);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next() && restante > 0) {
                    int estCod = rs.getInt("est_cod");
                    int qtdEst = rs.getInt("est_prod_quantidade");
                    if (qtdEst <= 0) continue;

                    int usar = Math.min(qtdEst, restante);
                    int novo = qtdEst - usar;

                    up.setInt(1, novo);
                    up.setInt(2, estCod);
                    up.executeUpdate();

                    restante -= usar;
                }
            }
        }
        return restante == 0;
    }

    private boolean excluirCestaLigada(Connection conn, int cbCod) throws SQLException {

        final String SQL_DEL_ITENS =
                "DELETE FROM cesta_basica_produto WHERE cesta_basica_cb_cod = ?";

        final String SQL_DEL_CESTA =
                "DELETE FROM cesta_basica WHERE cb_cod = ?";

        try (PreparedStatement delItens = conn.prepareStatement(SQL_DEL_ITENS);
             PreparedStatement delCesta = conn.prepareStatement(SQL_DEL_CESTA)) {

            delItens.setInt(1, cbCod);
            delItens.executeUpdate();

            delCesta.setInt(1, cbCod);
            return delCesta.executeUpdate() > 0;
        }
    }

    private static Map<String, Object> fail(String msg) {
        return Map.of("sucesso", false, "mensagem", msg);
    }

    private static Integer toInt(Object o) {
        if (o instanceof Number n) return n.intValue();
        try { return (o != null) ? Integer.valueOf(o.toString()) : null; }
        catch (Exception e) { return null; }
    }

    private static String toStr(Object o) {
        return (o != null) ? o.toString() : null;
    }
}
