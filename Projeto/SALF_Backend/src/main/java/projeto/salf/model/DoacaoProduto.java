package projeto.salf.model;


import jakarta.persistence.*;

@Entity
@Table(name = "doacao_has_produto")
public class DoacaoProduto {

    @EmbeddedId
    private DoacaoProdutoId id;

    @ManyToOne
    @MapsId("doacaoDoaCod")
    @JoinColumn(name = "doacao_doa_cod")
    private Doacao doacao;

    @ManyToOne
    @MapsId("produtoProdCod")
    @JoinColumn(name = "produto_prod_cod")
    private Produto produto;

    public DoacaoProduto() {}

    public DoacaoProduto(DoacaoProdutoId id, Doacao doacao, Produto produto) {
        this.id = id;
        this.doacao = doacao;
        this.produto = produto;
    }

    public DoacaoProdutoId getId() { return id; }
    public void setId(DoacaoProdutoId id) { this.id = id; }

    public Doacao getDoacao() { return doacao; }
    public void setDoacao(Doacao doacao) { this.doacao = doacao; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
}
