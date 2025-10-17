package projeto.salf.model;

import jakarta.persistence.*;

@Entity
@Table(name = "fornec_cotacao")
public class FornecCotacao {

    @EmbeddedId
    private FornecCotacaoId id;

    @ManyToOne
    @MapsId("fornecedorIdFornecedor")
    @JoinColumn(name = "fornecedor_idfornecedor")
    private Fornecedor fornecedor;

    @ManyToOne
    @MapsId("cotacaoIdCotacao")
    @JoinColumn(name = "cotacao_idcotacao")
    private Cotacao cotacao;

    @Column(name = "statusrealizou", nullable = false)
    private Integer statusRealizou;

    public FornecCotacao() {}

    public FornecCotacao(FornecCotacaoId id, Fornecedor fornecedor, Cotacao cotacao, Integer statusRealizou) {
        this.id = id;
        this.fornecedor = fornecedor;
        this.cotacao = cotacao;
        this.statusRealizou = statusRealizou;
    }

    public FornecCotacaoId getId() { return id; }
    public void setId(FornecCotacaoId id) { this.id = id; }

    public Fornecedor getFornecedor() { return fornecedor; }
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }

    public Cotacao getCotacao() { return cotacao; }
    public void setCotacao(Cotacao cotacao) { this.cotacao = cotacao; }

    public Integer getStatusRealizou() { return statusRealizou; }
    public void setStatusRealizou(Integer statusRealizou) { this.statusRealizou = statusRealizou; }
}