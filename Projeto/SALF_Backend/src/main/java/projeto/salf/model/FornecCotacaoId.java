package projeto.salf.model;

import jakarta.persistence.*;
import java.io.Serializable;

/* FornecCotacao (join Fornecedor x Cotacao) */
@Embeddable
public class FornecCotacaoId implements Serializable {

    @Column(name = "fornecedor_idfornecedor")
    private Integer fornecedorIdFornecedor;

    @Column(name = "cotacao_idcotacao")
    private Integer cotacaoIdCotacao;

    public FornecCotacaoId() {}

    public FornecCotacaoId(Integer fornecedorIdFornecedor, Integer cotacaoIdCotacao) {
        this.fornecedorIdFornecedor = fornecedorIdFornecedor;
        this.cotacaoIdCotacao = cotacaoIdCotacao;
    }

    public Integer getFornecedorIdFornecedor() { return fornecedorIdFornecedor; }
    public void setFornecedorIdFornecedor(Integer fornecedorIdFornecedor) { this.fornecedorIdFornecedor = fornecedorIdFornecedor; }

    public Integer getCotacaoIdCotacao() { return cotacaoIdCotacao; }
    public void setCotacaoIdCotacao(Integer cotacaoIdCotacao) { this.cotacaoIdCotacao = cotacaoIdCotacao; }
}