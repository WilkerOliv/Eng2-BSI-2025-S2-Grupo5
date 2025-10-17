package projeto.salf.model;


import jakarta.persistence.*;

@Entity
@Table(name = "itensdalista_fornec_cotacao")
public class ItensDaListaFornecCotacao {

    @EmbeddedId
    private ItensDaListaFornecCotacaoId id;

    @ManyToOne
    @MapsId("itensDaListaProdutoProdCod")
    @JoinColumn(name = "itensdalista_produto_prod_cod")
    private Produto produto;

    @Column(name = "valorcotacao", nullable = false)
    private Double valorCotacao;

    public ItensDaListaFornecCotacao() {}

    public ItensDaListaFornecCotacao(ItensDaListaFornecCotacaoId id, Produto produto, Double valorCotacao) {
        this.id = id;
        this.produto = produto;
        this.valorCotacao = valorCotacao;
    }

    public ItensDaListaFornecCotacaoId getId() { return id; }
    public void setId(ItensDaListaFornecCotacaoId id) { this.id = id; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public Double getValorCotacao() { return valorCotacao; }
    public void setValorCotacao(Double valorCotacao) { this.valorCotacao = valorCotacao; }
}
