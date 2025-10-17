package projeto.salf.model;

import jakarta.persistence.*;

/* CestaBasica_has_Produto */
@Entity
@Table(name = "cestabasica_has_produto")
public class CestaBasicaProduto {

    @EmbeddedId
    private CestaBasicaProdutoId id;

    @ManyToOne
    @MapsId("cestaBasicaCbCod")
    @JoinColumn(name = "cestabasica_cb_cod")
    private CestaBasica cestaBasica;

    @ManyToOne
    @MapsId("produtoProdCod")
    @JoinColumn(name = "produto_prod_cod")
    private Produto produto;

    @Column(name = "quantidade")
    private Integer quantidade;

    public CestaBasicaProduto() {}

    public CestaBasicaProduto(CestaBasicaProdutoId id, CestaBasica cestaBasica, Produto produto, Integer quantidade) {
        this.id = id;
        this.cestaBasica = cestaBasica;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public CestaBasicaProdutoId getId() { return id; }
    public void setId(CestaBasicaProdutoId id) { this.id = id; }

    public CestaBasica getCestaBasica() { return cestaBasica; }
    public void setCestaBasica(CestaBasica cestaBasica) { this.cestaBasica = cestaBasica; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}