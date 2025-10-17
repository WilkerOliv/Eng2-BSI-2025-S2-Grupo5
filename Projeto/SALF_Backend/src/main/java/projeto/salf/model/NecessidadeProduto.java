package projeto.salf.model;


import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "necessidade_produto")
public class NecessidadeProduto {

    @EmbeddedId
    private NecessidadeProdutoId id;

    @ManyToOne
    @MapsId("pessoaCarentePcCpf")
    @JoinColumn(name = "pessoacarente_pc_cpf")
    private PessoaCarente pessoaCarente;

    @ManyToOne
    @MapsId("produtoProdCod")
    @JoinColumn(name = "produto_prod_cod")
    private Produto produto;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "observacao", length = 100)
    private String observacao;

    public NecessidadeProduto() {}

    public NecessidadeProduto(NecessidadeProdutoId id, PessoaCarente pessoaCarente, Produto produto,
                              LocalDate data, Integer quantidade, String observacao) {
        this.id = id;
        this.pessoaCarente = pessoaCarente;
        this.produto = produto;
        this.data = data;
        this.quantidade = quantidade;
        this.observacao = observacao;
    }

    public NecessidadeProdutoId getId() { return id; }
    public void setId(NecessidadeProdutoId id) { this.id = id; }

    public PessoaCarente getPessoaCarente() { return pessoaCarente; }
    public void setPessoaCarente(PessoaCarente pessoaCarente) { this.pessoaCarente = pessoaCarente; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
