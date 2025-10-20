package projeto.salf.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "itens_da_lista_fornec_cotacao")
@IdClass(ItensDaListaFornecCotacaoId.class)
public class ItensDaListaFornecCotacao implements Serializable {

    @Id
    @Column(name = "itens_da_lista_produto_prod_cod")
    private Integer itensDaListaProdutoProdCod;

    @Id
    @Column(name = "itens_da_lista_lista_compra_lc_cod")
    private Integer itensDaListaListaCompraLcCod;

    @Id
    @Column(name = "itens_da_lista_lista_compra_funcionario_func_cpf", length = 14)
    private String itensDaListaListaCompraFuncionarioFuncCpf;

    @Id
    @Column(name = "fornec_cotacao_fornecedor_id")
    private Integer fornecCotacaoFornecedorId;

    @Id
    @Column(name = "fornec_cotacao_cotacao_id")
    private Integer fornecCotacaoCotacaoId;

    @Column(name = "valor_cotacao", nullable = false)
    private Double valorCotacao;

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

    public Double getValorCotacao() { return valorCotacao; }
    public void setValorCotacao(Double valorCotacao) { this.valorCotacao = valorCotacao; }
}
