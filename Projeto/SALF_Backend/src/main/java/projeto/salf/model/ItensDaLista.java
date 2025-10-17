package projeto.salf.model;


import jakarta.persistence.*;

@Entity
@Table(name = "itens_da_lista")
public class ItensDaLista {

    @EmbeddedId
    private ItensDaListaId id;

    @ManyToOne
    @MapsId("produtoProdCod")
    @JoinColumn(name = "produto_prod_cod")
    private Produto produto;

    @ManyToOne
    @MapsId("listaCompraLcCod")
    @JoinColumn(name = "lista_compra_lc_cod")
    private ListaCompra listaCompra;

    @Column(name = "quantidade")
    private Integer quantidade;

    public ItensDaLista() {}

    public ItensDaLista(ItensDaListaId id, Produto produto, ListaCompra listaCompra, Integer quantidade) {
        this.id = id;
        this.produto = produto;
        this.listaCompra = listaCompra;
        this.quantidade = quantidade;
    }

    public ItensDaListaId getId() { return id; }
    public void setId(ItensDaListaId id) { this.id = id; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public ListaCompra getListaCompra() { return listaCompra; }
    public void setListaCompra(ListaCompra listaCompra) { this.listaCompra = listaCompra; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
