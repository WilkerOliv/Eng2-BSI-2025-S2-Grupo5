package projeto.salf.dao;

import projeto.salf.dto.ProdutoDoacaoEstoqueDTO;
import projeto.salf.model.DoacaoProduto;
import projeto.salf.model.Estoque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDoacaoDAO {

    EstoqueDAO estoqueDAO = new EstoqueDAO();

    public int inserirDoacaoProdutos(ProdutoDoacaoEstoqueDTO dto, Connection conn) {

        String sql = "INSERT INTO doacao_produto (doacao_doa_cod, produto_prod_cod, doa_prod_qtd) VALUES (?, ?, ?)";
        int contInsercao = 0;

        DoacaoProduto doaProd = dto.getDoacaoProduto();
        List<Estoque> estoques = dto.getEstoques();

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (Estoque e : estoques) {

                if (e == null || e.getProdutoProdCod() == null) continue;

                stmt.setInt(1, doaProd.getDoacaoDoaCod());
                stmt.setInt(2, e.getProdutoProdCod());
                stmt.setInt(3, e.getEstProdQuantidade());

                if (stmt.executeUpdate() > 0) {
                    contInsercao++;
                }

                int quantidade = e.getEstProdQuantidade();
                LocalDate validade = e.getDataValidade();
                int produtoCod = e.getProdutoProdCod();

                estoqueDAO.insereItensEstoque(quantidade, validade, produtoCod, conn);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return contInsercao;
    }

    public List<Integer> getListaProdutosDoacao(int idDoacao, Connection conn){

        String SQl = "SELECT * FROM doacao_produto WHERE doacao_doa_cod = ?";
        List<Integer> lista = new ArrayList<>();

        try {
            PreparedStatement stmt = conn.prepareStatement(SQl);
            stmt.setInt(1, idDoacao);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(rs.getInt("produto_prod_cod"));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return lista;
    }
}
