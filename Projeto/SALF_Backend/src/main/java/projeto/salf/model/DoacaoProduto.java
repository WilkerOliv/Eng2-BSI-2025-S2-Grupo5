package projeto.salf.model;

import jakarta.persistence.*;
import projeto.salf.dao.ProdutoDoacaoDAO;
import projeto.salf.dto.ProdutoDoacaoEstoqueDTO;

import java.io.Serializable;
import java.sql.Connection;

@Entity
@Table(name = "doacao_produto")
@IdClass(DoacaoProdutoId.class)
public class DoacaoProduto implements Serializable {

    @Id
    @Column(name = "doacao_doa_cod")
    private Integer doacaoDoaCod;

    @Id
    @Column(name = "produto_prod_cod")
    private Integer produtoProdCod;

    public Integer getDoacaoDoaCod() { return doacaoDoaCod; }
    public void setDoacaoDoaCod(Integer doacaoDoaCod) { this.doacaoDoaCod = doacaoDoaCod; }

    public Integer getProdutoProdCod() { return produtoProdCod; }
    public void setProdutoProdCod(Integer produtoProdCod) { this.produtoProdCod = produtoProdCod; }

    // -------------------------------
    // MÉTODO DO MODEL QUE CHAMA O DAO
    // -------------------------------
    public int inserirProdDoacao(ProdutoDoacaoEstoqueDTO dto, Connection conn) {
        ProdutoDoacaoDAO dao = new ProdutoDoacaoDAO();
        return dao.inserirDoacaoProdutos(dto, conn);
    }
}
