package projeto.salf.dao;

import org.springframework.stereotype.Repository;
import projeto.salf.dto.DoacaoDTO;
import projeto.salf.dto.ItemDoacaoDTO;
import projeto.salf.model.Doacao;
import projeto.salf.model.DoacaoProduto;
import projeto.salf.model.Produto;
import projeto.salf.utils.SingletonDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DoacaoDAO {


    ProdutoDoacaoDAO produtoDAO = new ProdutoDoacaoDAO();

    public Integer inserirDoacao(Doacao doacao) {
        final String SQL = """
        INSERT INTO doacao (pessoa_carente_pc_cpf, data_doacao, observacao)
        VALUES (NULL, ?, ?)
        RETURNING doa_cod
    """;

        try (Connection conn = SingletonDB.getConexao().getConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

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


    public List<DoacaoDTO> getAllDoacoes() {

        List<DoacaoDTO> lista = new ArrayList<>();

        String sql = "SELECT * FROM doacao";

        try {
            Connection conn = SingletonDB.getConexao().getConnect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                DoacaoDTO dto = new DoacaoDTO();
                dto.setDoaCod(rs.getInt("doa_cod"));
                dto.setDataDoacao(rs.getDate("data_doacao").toLocalDate().toString());
                dto.setObservacao(rs.getString("observacao"));

                List<Integer> produtos = produtoDAO.getListaProdutosDoacao(rs.getInt("doa_cod"));
                dto.setProdutos(produtos);

                lista.add(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return lista;
    }


    public List<ItemDoacaoDTO> getItensPorDoacao(int idDoacao) {
        List<ItemDoacaoDTO> lista = new ArrayList<>();

        String SQL =
                "SELECT p.prod_descr, dp.doa_prod_qtd " +
                        "FROM doacao_produto dp " +
                        "JOIN produto p ON p.prod_cod = dp.produto_prod_cod " +
                        "WHERE dp.doacao_doa_cod = ?";

        try {
            Connection conn = SingletonDB.getConexao().getConnect();
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
