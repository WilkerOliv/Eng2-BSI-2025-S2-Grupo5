package projeto.salf.model;


import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class ItensDaListaId implements Serializable {

    @Column(name = "produto_prod_cod")
    private Integer produtoProdCod;

    @Column(name = "lista_compra_lc_cod")
    private Integer listaCompraLcCod;

    @Column(name = "lista_compra_funcionario_func_cpf", length = 18)
    private String listaCompraFuncionarioFuncCpf;

    public ItensDaListaId() {}

    public ItensDaListaId(Integer produtoProdCod, Integer listaCompraLcCod, String listaCompraFuncionarioFuncCpf) {
        this.produtoProdCod = produtoProdCod;
        this.listaCompraLcCod = listaCompraLcCod;
        this.listaCompraFuncionarioFuncCpf = listaCompraFuncionarioFuncCpf;
    }

    public Integer getProdutoProdCod() { return produtoProdCod; }
    public void setProdutoProdCod(Integer produtoProdCod) { this.produtoProdCod = produtoProdCod; }

    public Integer getListaCompraLcCod() { return listaCompraLcCod; }
    public void setListaCompraLcCod(Integer listaCompraLcCod) { this.listaCompraLcCod = listaCompraLcCod; }

    public String getListaCompraFuncionarioFuncCpf() { return listaCompraFuncionarioFuncCpf; }
    public void setListaCompraFuncionarioFuncCpf(String listaCompraFuncionarioFuncCpf) { this.listaCompraFuncionarioFuncCpf = listaCompraFuncionarioFuncCpf; }
}
