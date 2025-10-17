package projeto.salf.model;


import jakarta.persistence.*;

@Entity
@Table(name = "estoque_saida_refeicao")
public class EstoqueSaidaRefeicao {

    @EmbeddedId
    private EstoqueSaidaRefeicaoId id;

    @ManyToOne
    @MapsId("estoqueEstCod")
    @JoinColumn(name = "estoque_est_cod")
    private Estoque estoque;

    @ManyToOne
    @MapsId("saidaRefeicaoId")
    @JoinColumn(name = "saida_refeicao_idsaida_refeicao")
    private SaidaRefeicao saidaRefeicao;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    public EstoqueSaidaRefeicao() {}

    public EstoqueSaidaRefeicao(EstoqueSaidaRefeicaoId id, Estoque estoque, SaidaRefeicao saidaRefeicao, Integer quantidade) {
        this.id = id;
        this.estoque = estoque;
        this.saidaRefeicao = saidaRefeicao;
        this.quantidade = quantidade;
    }

    public EstoqueSaidaRefeicaoId getId() { return id; }
    public void setId(EstoqueSaidaRefeicaoId id) { this.id = id; }

    public Estoque getEstoque() { return estoque; }
    public void setEstoque(Estoque estoque) { this.estoque = estoque; }

    public SaidaRefeicao getSaidaRefeicao() { return saidaRefeicao; }
    public void setSaidaRefeicao(SaidaRefeicao saidaRefeicao) { this.saidaRefeicao = saidaRefeicao; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
