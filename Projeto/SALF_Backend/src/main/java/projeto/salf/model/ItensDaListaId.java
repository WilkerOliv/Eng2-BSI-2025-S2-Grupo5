package projeto.salf.model;

import java.io.Serializable;
import java.util.Objects;

public class ItensDaListaId implements Serializable {
    private Integer produtoProdCod;
    private Integer listaCompraLcCod;
    private String listaCompraFuncionarioFuncCpf;

    public ItensDaListaId() {}

    public ItensDaListaId(Integer produtoProdCod, Integer listaCompraLcCod, String listaCompraFuncionarioFuncCpf) {
        this.produtoProdCod = produtoProdCod;
        this.listaCompraLcCod = listaCompraLcCod;
        this.listaCompraFuncionarioFuncCpf = listaCompraFuncionarioFuncCpf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItensDaListaId)) return false;
        ItensDaListaId that = (ItensDaListaId) o;
        return Objects.equals(produtoProdCod, that.produtoProdCod) &&
                Objects.equals(listaCompraLcCod, that.listaCompraLcCod) &&
                Objects.equals(listaCompraFuncionarioFuncCpf, that.listaCompraFuncionarioFuncCpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produtoProdCod, listaCompraLcCod, listaCompraFuncionarioFuncCpf);
    }

    public Integer getProdutoProdCod() { return produtoProdCod; }
    public void setProdutoProdCod(Integer produtoProdCod) { this.produtoProdCod = produtoProdCod; }
    public Integer getListaCompraLcCod() { return listaCompraLcCod; }
    public void setListaCompraLcCod(Integer listaCompraLcCod) { this.listaCompraLcCod = listaCompraLcCod; }
    public String getListaCompraFuncionarioFuncCpf() { return listaCompraFuncionarioFuncCpf; }
    public void setListaCompraFuncionarioFuncCpf(String listaCompraFuncionarioFuncCpf) { this.listaCompraFuncionarioFuncCpf = listaCompraFuncionarioFuncCpf; }
}
