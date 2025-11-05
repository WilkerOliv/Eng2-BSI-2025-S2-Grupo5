package projeto.salf.dao;

import org.springframework.stereotype.Repository;
import projeto.salf.model.Compra;
import projeto.salf.model.Estoque;
import projeto.salf.model.ItensCompra;
import projeto.salf.utils.SingletonDB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

@Repository
public class CompraDAO {

    EstoqueDAO estoqueDAO = new EstoqueDAO();
    public Integer insereCompra(Compra compra) {
        // Ordem e nomes CERTOS, como estão na sua tabela:
        final String SQL =
                "INSERT INTO compra (" +
                        "  compra_valor_tt," +                 // 1
                        "  data_compra," +                     // 2
                        "  fornec_cotacao_fornecedor_id," +    // 3 (nullable)
                        "  fornec_cotacao_cotacao_id," +       // 4 (nullable)
                        "  fornecedor_id," +                   // 5 (nullable)
                        "  funcionario_func_cpf" +             // 6
                        ") VALUES (?,?,?,?,?,?) RETURNING compra_cod";

        try (Connection conn = SingletonDB.getConexao().getConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            // 1) total da compra (double precision) – use BigDecimal se seu model tiver
            stmt.setDouble(1, compra.getCompraValorTt());

            // 2) data (java.sql.Date). Se no model for LocalDate, use valueOf:
                stmt.setDate(2, java.sql.Date.valueOf(compra.getDataCompra()));

            // 3) id do fornecedor da COTAÇÃO (nullable)
            if (compra.getFornecCotacaoFornecedorId() != null) {
                stmt.setInt(3, compra.getFornecCotacaoFornecedorId());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }

            // 4) id da COTAÇÃO (nullable)
            if (compra.getFornecCotacaoCotacaoId() != null) {
                stmt.setInt(4, compra.getFornecCotacaoCotacaoId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            // 5) id do FORNECEDOR DIRETO (nullable)
            if (compra.getFornecedorId() != null) {
                stmt.setInt(5, compra.getFornecedorId());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            stmt.setString(6, compra.getFuncionarioFuncCpf());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1); // compra_cod
                }
                throw new RuntimeException("INSERT de compra não retornou compra_cod.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inserir compra", e);
        }
    }

    public boolean insereItens(ItensCompra itensCompra, LocalDate validadeProd) {
        final String SQL = "INSERT INTO itens_compra (" +
                "produto_prod_cod," +
                "compra_compra_cod," +
                "valor," +
                "quantidade" +
                ") VALUES (?,?,?,?)";

        try (Connection conn = SingletonDB.getConexao().getConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setInt(1, itensCompra.getProdutoProdCod());
            stmt.setInt(2, itensCompra.getCompraCompraCod());
            stmt.setDouble(3, itensCompra.getValor());
            stmt.setInt(4, itensCompra.getQuantidade());

            int upd = stmt.executeUpdate(); // <-- INSERT
            if(upd > 0){
                boolean ok = estoqueDAO.insereItensEstoque(itensCompra.getQuantidade(),validadeProd ,itensCompra.getProdutoProdCod() );

            }

            return upd > 0;

        } catch (Exception e) {
            // logue o erro para entender se há FK, etc.
            e.printStackTrace();
            return false;
        }
    }
}
