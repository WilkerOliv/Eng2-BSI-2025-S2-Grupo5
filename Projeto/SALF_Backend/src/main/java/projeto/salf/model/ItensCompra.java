package projeto.salf.model;


import jakarta.persistence.*;

@Entity
@Table(name = "itens_compra")
public class ItensCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_itens_compra")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_prod_cod", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "compra_compra_cod", nullable = false)
    private Compra compra;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    public ItensCompra() {}

    public ItensCompra(Long id, Produto produto, Compra compra, Double valor, Integer quantidade) {
        this.id = id;
        this.produto = produto;
        this.compra = compra;
        this.valor = valor;
        this.quantidade = quantidade;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public Compra getCompra() { return compra; }
    public void setCompra(Compra compra) { this.compra = compra; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
