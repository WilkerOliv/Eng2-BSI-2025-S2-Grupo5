package projeto.salf.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "itens_da_lista")
@IdClass(ItensDaListaId.class)
public class ItensDaLista implements Serializable {

    @Id
    @Column(name = "produto_prod_cod")
    private Integer produtoProdCod;

    @Id
    @Column(name = "lista_compra_lc_cod")
    private Integer listaCompraLcCod;

    @Id
    @Column(name = "lista_compra_funcionario_func_cpf", length = 14)
    private String listaCompraFuncionarioFuncCpf;

    @Column(name = "quantidade")
    private Integer quantidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_prod_cod", referencedColumnName = "prod_cod", insertable = false, updatable = false)
    private Produto produto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "lista_compra_lc_cod", referencedColumnName = "lc_cod", insertable = false, updatable = false),
            @JoinColumn(name = "lista_compra_funcionario_func_cpf", referencedColumnName = "funcionario_func_cpf", insertable = false, updatable = false)
    })
    private ListaCompra listaCompra;

    public ItensDaLista() {}

    public Integer getProdutoProdCod() { return produtoProdCod; }
    public void setProdutoProdCod(Integer produtoProdCod) { this.produtoProdCod = produtoProdCod; }

    public Integer getListaCompraLcCod() { return listaCompraLcCod; }
    public void setListaCompraLcCod(Integer listaCompraLcCod) { this.listaCompraLcCod = listaCompraLcCod; }

    public String getListaCompraFuncionarioFuncCpf() { return listaCompraFuncionarioFuncCpf; }
    public void setListaCompraFuncionarioFuncCpf(String listaCompraFuncionarioFuncCpf) { this.listaCompraFuncionarioFuncCpf = listaCompraFuncionarioFuncCpf; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public ListaCompra getListaCompra() { return listaCompra; }
    public void setListaCompra(ListaCompra listaCompra) { this.listaCompra = listaCompra; }
}
