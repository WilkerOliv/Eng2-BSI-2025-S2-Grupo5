package projeto.salf.model;


import jakarta.persistence.*;

@Entity
@Table(name = "doacao_necessidade_cesta")
public class DoacaoNecessidadeCesta {

    @EmbeddedId
    private DoacaoNecessidadeCestaId id;

    @ManyToOne
    @MapsId("doacaoDoaCod")
    @JoinColumn(name = "doacao_doa_cod")
    private Doacao doacao;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    public DoacaoNecessidadeCesta() {}

    public DoacaoNecessidadeCesta(DoacaoNecessidadeCestaId id, Doacao doacao, Integer quantidade) {
        this.id = id;
        this.doacao = doacao;
        this.quantidade = quantidade;
    }

    public DoacaoNecessidadeCestaId getId() { return id; }
    public void setId(DoacaoNecessidadeCestaId id) { this.id = id; }

    public Doacao getDoacao() { return doacao; }
    public void setDoacao(Doacao doacao) { this.doacao = doacao; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
