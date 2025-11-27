package projeto.salf.dao;

import projeto.salf.dto.DoacaoDTO;
import projeto.salf.dto.ItemDoacaoDTO;
import projeto.salf.model.Doacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DoacaoDAO {

    ProdutoDoacaoDAO produtoDAO = new ProdutoDoacaoDAO();

    public Integer inserirDoacao(Doacao doacao, Connection conn) {
        final String SQL = """
        INSERT INTO doacao (pessoa_carente_pc_cpf, data_doacao, observacao)
        VALUES (NULL, ?, ?)
        RETURNING doa_cod
        """;

        try (PreparedStatement stmt = conn.prepareStatement(SQL)) {

            if (doacao.getDataDoacao() != null) {
                stmt.setDate(1, java.sql.Date.valueOf(doacao.getDataDoacao()));
            } else {
                stmt.setNull(1, java.sql.Types.DATE);
            }

            stmt.setString(2, doacao.getObservacao());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
                throw new RuntimeException("Falha ao gerar doa_cod");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean excluirDoacao(int doaCod, Connection conn) {

        try {
            // -----------------------------
            // 1) BUSCAR TODOS OS ITENS DA DOAÇÃO
            // -----------------------------
            String SQL_ITENS = """
            SELECT produto_prod_cod, doa_prod_qtd
            FROM doacao_produto
            WHERE doacao_doa_cod = ?
        """;

            PreparedStatement stmtItens = conn.prepareStatement(SQL_ITENS);
            stmtItens.setInt(1, doaCod);
            ResultSet rs = stmtItens.executeQuery();

            EstoqueDAO estoqueDAO = new EstoqueDAO();

            // -----------------------------
            // 2) PARA CADA ITEM → REMOVER DO ESTOQUE
            // -----------------------------
            while (rs.next()) {
                int prodCod = rs.getInt("produto_prod_cod");
                int qtd = rs.getInt("doa_prod_qtd");

                removerDoEstoque(prodCod, qtd, conn);
            }

            // -----------------------------
            // 3) EXCLUIR TODOS OS ITENS DA DOAÇÃO
            // -----------------------------
            String SQL_DEL_ITENS = "DELETE FROM doacao_produto WHERE doacao_doa_cod = ?";
            PreparedStatement stmtDelItens = conn.prepareStatement(SQL_DEL_ITENS);
            stmtDelItens.setInt(1, doaCod);
            stmtDelItens.executeUpdate();

            // -----------------------------
            // 4) EXCLUIR A DOAÇÃO PRINCIPAL
            // -----------------------------
            String SQL_DEL_DOACAO = "DELETE FROM doacao WHERE doa_cod = ?";
            PreparedStatement stmtDelDoa = conn.prepareStatement(SQL_DEL_DOACAO);
            stmtDelDoa.setInt(1, doaCod);

            return stmtDelDoa.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private void removerDoEstoque(int prodCod, int qtd, Connection conn) {

        try {

            String SQL_SELECT = """
            SELECT est_prod_quantidade, data_validade
            FROM estoque
            WHERE produto_prod_cod = ?
            ORDER BY data_validade NULLS LAST
        """;

            PreparedStatement stmt = conn.prepareStatement(SQL_SELECT);
            stmt.setInt(1, prodCod);
            ResultSet rs = stmt.executeQuery();

            while (rs.next() && qtd > 0) {

                int qtdEstoque = rs.getInt("est_prod_quantidade");
                LocalDate validade = rs.getDate("data_validade") != null
                        ? rs.getDate("data_validade").toLocalDate()
                        : null;

                int remover = Math.min(qtd, qtdEstoque);
                int novoValor = qtdEstoque - remover;

                PreparedStatement upd;

                if (validade != null) {
                    // UPDATE com validade definida
                    String SQL_UPDATE = """
                    UPDATE estoque
                    SET est_prod_quantidade = ?
                    WHERE produto_prod_cod = ?
                    AND data_validade = ?
                """;

                    upd = conn.prepareStatement(SQL_UPDATE);
                    upd.setInt(1, novoValor);
                    upd.setInt(2, prodCod);
                    upd.setDate(3, java.sql.Date.valueOf(validade));

                } else {
                    // UPDATE quando data_validade é NULL
                    String SQL_UPDATE = """
                    UPDATE estoque
                    SET est_prod_quantidade = ?
                    WHERE produto_prod_cod = ?
                    AND data_validade IS NULL
                """;

                    upd = conn.prepareStatement(SQL_UPDATE);
                    upd.setInt(1, novoValor);
                    upd.setInt(2, prodCod);
                }

                upd.executeUpdate();
                qtd -= remover;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<DoacaoDTO> getAllDoacoes(Connection conn) {

        List<DoacaoDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM doacao";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                DoacaoDTO dto = new DoacaoDTO();
                dto.setDoaCod(rs.getInt("doa_cod"));
                dto.setDataDoacao(rs.getDate("data_doacao").toLocalDate().toString());
                dto.setObservacao(rs.getString("observacao"));

                List<Integer> produtos = produtoDAO.getListaProdutosDoacao(
                        rs.getInt("doa_cod"),
                        conn
                );
                dto.setProdutos(produtos);

                lista.add(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public List<ItemDoacaoDTO> getItensPorDoacao(int idDoacao, Connection conn) {

        List<ItemDoacaoDTO> lista = new ArrayList<>();

        String SQL = """
                SELECT p.prod_descr, dp.doa_prod_qtd
                FROM doacao_produto dp
                JOIN produto p ON p.prod_cod = dp.produto_prod_cod
                WHERE dp.doacao_doa_cod = ?
                """;

        try {
            PreparedStatement stmt = conn.prepareStatement(SQL);
            stmt.setInt(1, idDoacao);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ItemDoacaoDTO dto = new ItemDoacaoDTO();
                dto.setProduto(rs.getString("prod_descr"));
                dto.setQuantidade(rs.getInt("doa_prod_qtd"));
                lista.add(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return lista;
    }
}
