package projeto.salf.model;

import java.io.Serializable;
import java.util.Objects;

public class ItensDaListaFornecCotacaoId implements Serializable {
    private Integer itensDaListaProdutoProdCod;
    private Integer itensDaListaListaCompraLcCod;
    private String itensDaListaListaCompraFuncionarioFuncCpf;
    private Integer fornecCotacaoFornecedorId;
    private Integer fornecCotacaoCotacaoId;

    public ItensDaListaFornecCotacaoId() {}

    public ItensDaListaFornecCotacaoId(Integer p1, Integer p2, String p3, Integer p4, Integer p5) {
        this.itensDaListaProdutoProdCod = p1;
        this.itensDaListaListaCompraLcCod = p2;
        this.itensDaListaListaCompraFuncionarioFuncCpf = p3;
        this.fornecCotacaoFornecedorId = p4;
        this.fornecCotacaoCotacaoId = p5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItensDaListaFornecCotacaoId)) return false;
        ItensDaListaFornecCotacaoId that = (ItensDaListaFornecCotacaoId) o;
        return Objects.equals(itensDaListaProdutoProdCod, that.itensDaListaProdutoProdCod) &&
                Objects.equals(itensDaListaListaCompraLcCod, that.itensDaListaListaCompraLcCod) &&
                Objects.equals(itensDaListaListaCompraFuncionarioFuncCpf, that.itensDaListaListaCompraFuncionarioFuncCpf) &&
                Objects.equals(fornecCotacaoFornecedorId, that.fornecCotacaoFornecedorId) &&
                Objects.equals(fornecCotacaoCotacaoId, that.fornecCotacaoCotacaoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itensDaListaProdutoProdCod, itensDaListaListaCompraLcCod, itensDaListaListaCompraFuncionarioFuncCpf, fornecCotacaoFornecedorId, fornecCotacaoCotacaoId);
    }

    public Integer getItensDaListaProdutoProdCod() { return itensDaListaProdutoProdCod; }
    public void setItensDaListaProdutoProdCod(Integer v) { this.itensDaListaProdutoProdCod = v; }
    public Integer getItensDaListaListaCompraLcCod() { return itensDaListaListaCompraLcCod; }
    public void setItensDaListaListaCompraLcCod(Integer v) { this.itensDaListaListaCompraLcCod = v; }
    public String getItensDaListaListaCompraFuncionarioFuncCpf() { return itensDaListaListaCompraFuncionarioFuncCpf; }
    public void setItensDaListaListaCompraFuncionarioFuncCpf(String v) { this.itensDaListaListaCompraFuncionarioFuncCpf = v; }
    public Integer getFornecCotacaoFornecedorId() { return fornecCotacaoFornecedorId; }
    public void setFornecCotacaoFornecedorId(Integer v) { this.fornecCotacaoFornecedorId = v; }
    public Integer getFornecCotacaoCotacaoId() { return fornecCotacaoCotacaoId; }
    public void setFornecCotacaoCotacaoId(Integer v) { this.fornecCotacaoCotacaoId = v; }
}
