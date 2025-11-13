package projeto.salf.model;

import jakarta.persistence.*;
import java.io.Serializable;

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
}
