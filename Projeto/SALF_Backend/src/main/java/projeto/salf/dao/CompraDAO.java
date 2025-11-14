package projeto.salf.dao;

import projeto.salf.model.Compra;
import projeto.salf.model.Estoque;
import projeto.salf.model.ItensCompra;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

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
}
