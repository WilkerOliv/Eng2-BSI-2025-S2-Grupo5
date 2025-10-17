package projeto.salf.model;


import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class ItensDaListaFornecCotacaoId implements Serializable {

    @Column(name = "itensdalista_produto_prod_cod")
    private Integer itensDaListaProdutoProdCod;

    @Column(name = "itensdalista_lista_compra_lc_cod")
    private Integer itensDaListaListaCompraLcCod;

    @Column(name = "itensdalista_lista_compra_funcionario_func_cpf", length = 14)
    private String itensDaListaListaCompraFuncionarioFuncCpf;

    @Column(name = "forneccotacao_fornecedor_idfornecedor")
    private Integer fornecCotFornecedorId;

    @Column(name = "forneccotacao_cotacao_idcotacao")
    private Integer fornecCotCotacaoId;

    public ItensDaListaFornecCotacaoId() {}

    public ItensDaListaFornecCotacaoId(Integer itensDaListaProdutoProdCod, Integer itensDaListaListaCompraLcCod,
                                       String itensDaListaListaCompraFuncionarioFuncCpf, Integer fornecCotFornecedorId,
                                       Integer fornecCotCotacaoId) {
        this.itensDaListaProdutoProdCod = itensDaListaProdutoProdCod;
        this.itensDaListaListaCompraLcCod = itensDaListaListaCompraLcCod;
        this.itensDaListaListaCompraFuncionarioFuncCpf = itensDaListaListaCompraFuncionarioFuncCpf;
        this.fornecCotFornecedorId = fornecCotFornecedorId;
        this.fornecCotCotacaoId = fornecCotCotacaoId;
    }

    public Integer getItensDaListaProdutoProdCod() { return itensDaListaProdutoProdCod; }
    public void setItensDaListaProdutoProdCod(Integer v) { this.itensDaListaProdutoProdCod = v; }

    public Integer getItensDaListaListaCompraLcCod() { return itensDaListaListaCompraLcCod; }
    public void setItensDaListaListaCompraLcCod(Integer v) { this.itensDaListaListaCompraLcCod = v; }

    public String getItensDaListaListaCompraFuncionarioFuncCpf() { return itensDaListaListaCompraFuncionarioFuncCpf; }
    public void setItensDaListaListaCompraFuncionarioFuncCpf(String v) { this.itensDaListaListaCompraFuncionarioFuncCpf = v; }

    public Integer getFornecCotFornecedorId() { return fornecCotFornecedorId; }
    public void setFornecCotFornecedorId(Integer v) { this.fornecCotFornecedorId = v; }

    public Integer getFornecCotCotacaoId() { return fornecCotCotacaoId; }
    public void setFornecCotCotacaoId(Integer v) { this.fornecCotCotacaoId = v; }
}
