package projeto.salf.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "compra")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compra_cod")
    private Integer compraCod;

    @Column(name = "compra_valortt", nullable = false)
    private Double compraValorTT;

    @Column(name = "datacompra", nullable = false)
    private LocalDate dataCompra;

    @Column(name = "forneccotacao_fornecedor_idfornecedor", nullable = false)
    private Integer fornecCotFornecedorId;

    @Column(name = "forneccotacao_cotacao_idcotacao", nullable = false)
    private Integer fornecCotCotacaoId;

    @ManyToOne
    @JoinColumn(name = "fornecedor_idfornecedor", nullable = false)
    private Fornecedor fornecedor;

    @Column(name = "funcionario_func_cpf", length = 14, nullable = false)
    private String funcionarioFuncCpf;

    public Compra() {}

    public Compra(Integer compraCod, Double compraValorTT, LocalDate dataCompra, Integer fornecCotFornecedorId,
                  Integer fornecCotCotacaoId, Fornecedor fornecedor, String funcionarioFuncCpf) {
        this.compraCod = compraCod;
        this.compraValorTT = compraValorTT;
        this.dataCompra = dataCompra;
        this.fornecCotFornecedorId = fornecCotFornecedorId;
        this.fornecCotCotacaoId = fornecCotCotacaoId;
        this.fornecedor = fornecedor;
        this.funcionarioFuncCpf = funcionarioFuncCpf;
    }

    public Integer getCompraCod() { return compraCod; }
    public void setCompraCod(Integer compraCod) { this.compraCod = compraCod; }

    public Double getCompraValorTT() { return compraValorTT; }
    public void setCompraValorTT(Double compraValorTT) { this.compraValorTT = compraValorTT; }

    public LocalDate getDataCompra() { return dataCompra; }
    public void setDataCompra(LocalDate dataCompra) { this.dataCompra = dataCompra; }

    public Integer getFornecCotFornecedorId() { return fornecCotFornecedorId; }
    public void setFornecCotFornecedorId(Integer fornecCotFornecedorId) { this.fornecCotFornecedorId = fornecCotFornecedorId; }

    public Integer getFornecCotCotacaoId() { return fornecCotCotacaoId; }
    public void setFornecCotCotacaoId(Integer fornecCotCotacaoId) { this.fornecCotCotacaoId = fornecCotCotacaoId; }

    public Fornecedor getFornecedor() { return fornecedor; }
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }

    public String getFuncionarioFuncCpf() { return funcionarioFuncCpf; }
    public void setFuncionarioFuncCpf(String funcionarioFuncCpf) { this.funcionarioFuncCpf = funcionarioFuncCpf; }
}