package projeto.salf.dao;

import projeto.salf.model.Compra;
import projeto.salf.model.Estoque;
import projeto.salf.model.ItensCompra;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompraDAO {

    EstoqueDAO estoqueDAO = new EstoqueDAO();

    public Integer insereCompra(Compra compra, Connection conn) {

        final String SQL =
                "INSERT INTO compra (" +
                        "compra_valor_tt," +
                        "data_compra," +
                        "fornec_cotacao_fornecedor_id," +
                        "fornec_cotacao_cotacao_id," +
                        "fornecedor_id," +
                        "funcionario_func_cpf" +
                        ") VALUES (?,?,?,?,?,?) RETURNING compra_cod";

        try (PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setDouble(1, compra.getCompraValorTt());
            stmt.setDate(2, Date.valueOf(compra.getDataCompra()));

            if (compra.getFornecCotacaoFornecedorId() != null)
                stmt.setInt(3, compra.getFornecCotacaoFornecedorId());
            else
                stmt.setNull(3, java.sql.Types.INTEGER);

            if (compra.getFornecCotacaoCotacaoId() != null)
                stmt.setInt(4, compra.getFornecCotacaoCotacaoId());
            else
                stmt.setNull(4, java.sql.Types.INTEGER);

            if (compra.getFornecedorId() != null)
                stmt.setInt(5, compra.getFornecedorId());
            else
                stmt.setNull(5, java.sql.Types.INTEGER);

            stmt.setString(6, compra.getFuncionarioFuncCpf());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
                throw new RuntimeException("INSERT de compra não retornou compra_cod");
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao inserir compra", e);
        }
    }

    public boolean insereItens(ItensCompra itensCompra, LocalDate validadeProd, Connection conn) {

        final String SQL = "INSERT INTO itens_compra (" +
                "produto_prod_cod, compra_compra_cod, valor, quantidade" +
                ") VALUES (?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setInt(1, itensCompra.getProdutoProdCod());
            stmt.setInt(2, itensCompra.getCompraCompraCod());
            stmt.setDouble(3, itensCompra.getValor());
            stmt.setInt(4, itensCompra.getQuantidade());

            int upd = stmt.executeUpdate();

            if (upd > 0) {
                estoqueDAO.insereItensEstoque(
                        itensCompra.getQuantidade(),
                        validadeProd,
                        itensCompra.getProdutoProdCod(),
                        conn
                );
            }

            return upd > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public List<Map<String, Object>> listarCompras(Connection conn) {

        List<Map<String, Object>> lista = new ArrayList<>();

        final String SQL =
                "SELECT compra_cod, data_compra, funcionario_func_cpf " +
                        "FROM compra ORDER BY compra_cod DESC";

        try (PreparedStatement st = conn.prepareStatement(SQL);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("compra_cod", rs.getInt("compra_cod"));
                map.put("data_compra", rs.getDate("data_compra").toLocalDate());
                map.put("funcionario", rs.getString("funcionario_func_cpf"));
                lista.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Map<String, Object>> listarItensCompra(int compraCod, Connection conn) {

        List<Map<String, Object>> lista = new ArrayList<>();

        final String SQL =
                "SELECT p.prod_descr, ic.quantidade, ic.valor, " +
                        "(ic.quantidade * ic.valor) AS subtotal, " +
                        "e.est_prod_quantidade, e.data_validade " +
                        "FROM itens_compra ic " +
                        "JOIN produto p ON p.prod_cod = ic.produto_prod_cod " +
                        "LEFT JOIN estoque e ON e.produto_prod_cod = ic.produto_prod_cod " +
                        "WHERE ic.compra_compra_cod = ?";

        try (PreparedStatement st = conn.prepareStatement(SQL)) {

            st.setInt(1, compraCod);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("produto", rs.getString("prod_descr"));
                map.put("quantidade", rs.getInt("quantidade"));
                map.put("valor_unit", rs.getDouble("valor"));
                map.put("subtotal", rs.getDouble("subtotal"));
                map.put("estoque", rs.getInt("est_prod_quantidade"));
                map.put("validade", rs.getDate("data_validade"));
                lista.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }



    public boolean excluirCompra(int compraCod, Connection conn) {

        try {
            // ===============================
            // 1. Buscar itens da compra
            // ===============================
            final String SQL_ITENS =
                    "SELECT produto_prod_cod, quantidade, " +
                            "       (SELECT data_validade FROM estoque " +
                            "           WHERE produto_prod_cod = ic.produto_prod_cod " +
                            "           ORDER BY data_validade ASC LIMIT 1) AS validade " +
                            "FROM itens_compra ic " +
                            "WHERE compra_compra_cod = ?";

            PreparedStatement stItens = conn.prepareStatement(SQL_ITENS);
            stItens.setInt(1, compraCod);

            ResultSet rs = stItens.executeQuery();

            // ===============================
            // 2. Para cada item → remover do estoque
            // ===============================
            EstoqueDAO estoqueDAO = new EstoqueDAO();

            while (rs.next()) {
                int prodCod = rs.getInt("produto_prod_cod");
                int qtd = rs.getInt("quantidade");
                LocalDate validade = rs.getDate("validade") != null
                        ? rs.getDate("validade").toLocalDate()
                        : null;

                boolean ok = estoqueDAO.removerItensEstoque(qtd, validade, prodCod, conn);
                if (!ok) return false;
            }

            // ===============================
            // 3. Excluir itens_compra
            // ===============================
            final String SQL_DEL_ITENS =
                    "DELETE FROM itens_compra WHERE compra_compra_cod = ?";

            PreparedStatement stDelItens = conn.prepareStatement(SQL_DEL_ITENS);
            stDelItens.setInt(1, compraCod);
            stDelItens.executeUpdate();

            // ===============================
            // 4. Excluir compra
            // ===============================
            final String SQL_DEL_COMPRA =
                    "DELETE FROM compra WHERE compra_cod = ?";

            PreparedStatement stDelCompra = conn.prepareStatement(SQL_DEL_COMPRA);
            stDelCompra.setInt(1, compraCod);

            return stDelCompra.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
