package projeto.salf.model;


import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class DoacaoProdutoId implements Serializable {

    @Column(name = "doacao_doa_cod")
    private Integer doacaoDoaCod;

    @Column(name = "produto_prod_cod")
    private Integer produtoProdCod;

    public DoacaoProdutoId() {}

    public DoacaoProdutoId(Integer doacaoDoaCod, Integer produtoProdCod) {
        this.doacaoDoaCod = doacaoDoaCod;
        this.produtoProdCod = produtoProdCod;
    }

    public Integer getDoacaoDoaCod() { return doacaoDoaCod; }
    public void setDoacaoDoaCod(Integer doacaoDoaCod) { this.doacaoDoaCod = doacaoDoaCod; }

    public Integer getProdutoProdCod() { return produtoProdCod; }
    public void setProdutoProdCod(Integer produtoProdCod) { this.produtoProdCod = produtoProdCod; }
}
