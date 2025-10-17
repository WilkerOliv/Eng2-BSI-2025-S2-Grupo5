package projeto.salf.model;


import jakarta.persistence.*;

@Entity
@Table(name = "doacao_necessidade_produto")
public class DoacaoNecessidadeProduto {

    @EmbeddedId
    private DoacaoNecessidadeProdutoId id;

    @ManyToOne
    @MapsId("doacaoDoaCod")
    @JoinColumn(name = "doacao_doa_cod")
    private Doacao doacao;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    public DoacaoNecessidadeProduto() {}

    public DoacaoNecessidadeProduto(DoacaoNecessidadeProdutoId id, Doacao doacao, Integer quantidade) {
        this.id = id;
        this.doacao = doacao;
        this.quantidade = quantidade;
    }

    public DoacaoNecessidadeProdutoId getId() { return id; }
    public void setId(DoacaoNecessidadeProdutoId id) { this.id = id; }

    public Doacao getDoacao() { return doacao; }
    public void setDoacao(Doacao doacao) { this.doacao = doacao; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
