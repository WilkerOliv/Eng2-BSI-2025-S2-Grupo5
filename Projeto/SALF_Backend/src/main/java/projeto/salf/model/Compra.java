package projeto.salf.model;

import jakarta.persistence.*;
import projeto.salf.dao.CompraDAO;

import java.io.Serializable;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "compra")
public class Compra implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compra_cod")
    private Integer compraCod;

    @Column(name = "compra_valor_tt", nullable = false)
    private Double compraValorTt;

    @Column(name = "data_compra", nullable = false)
    private LocalDate dataCompra;

    @Column(name = "fornec_cotacao_fornecedor_id")
    private Integer fornecCotacaoFornecedorId;

    @Column(name = "fornec_cotacao_cotacao_id")
    private Integer fornecCotacaoCotacaoId;

    @Column(name = "fornecedor_id")
    private Integer fornecedorId;

    @Column(name = "funcionario_func_cpf", length = 14)
    private String funcionarioFuncCpf;

    // GETTERS & SETTERS -------------------------------------

    public Integer getCompraCod() { return compraCod; }
    public void setCompraCod(Integer compraCod) { this.compraCod = compraCod; }

    public Double getCompraValorTt() { return compraValorTt; }
    public void setCompraValorTt(Double compraValorTt) { this.compraValorTt = compraValorTt; }

    public LocalDate getDataCompra() { return dataCompra; }
    public void setDataCompra(LocalDate dataCompra) { this.dataCompra = dataCompra; }

    public Integer getFornecCotacaoFornecedorId() { return fornecCotacaoFornecedorId; }
    public void setFornecCotacaoFornecedorId(Integer fornecCotacaoFornecedorId) { this.fornecCotacaoFornecedorId = fornecCotacaoFornecedorId; }

    public Integer getFornecCotacaoCotacaoId() { return fornecCotacaoCotacaoId; }
    public void setFornecCotacaoCotacaoId(Integer fornecCotacaoCotacaoId) { this.fornecCotacaoCotacaoId = fornecCotacaoCotacaoId; }

    public Integer getFornecedorId() { return fornecedorId; }
    public void setFornecedorId(Integer fornecedorId) { this.fornecedorId = fornecedorId; }

    public String getFuncionarioFuncCpf() { return funcionarioFuncCpf; }
    public void setFuncionarioFuncCpf(String funcionarioFuncCpf) { this.funcionarioFuncCpf = funcionarioFuncCpf; }


    // --------------------------------------------------------
    //     MÉTODOS DE NEGÓCIO (CHAMAM DAO)
    // --------------------------------------------------------

    public Integer inserirCompra(Connection conn) {
        CompraDAO dao = new CompraDAO();
        return dao.insereCompra(this, conn);
    }

    public boolean inserirItens(ItensCompra itensCompra, LocalDate validade, Connection conn) {
        CompraDAO dao = new CompraDAO();
        return dao.insereItens(itensCompra, validade, conn);
    }

    public List<Map<String, Object>> listarCompras(Connection conn) {
        CompraDAO dao = new CompraDAO();
        return dao.listarCompras(conn);
    }

    public List<Map<String, Object>> listarItens(int compraCod, Connection conn) {
        CompraDAO dao = new CompraDAO();
        return dao.listarItensCompra(compraCod, conn);
    }

    public boolean excluirCompra(int compraCod, Connection conn) {
        CompraDAO dao = new CompraDAO();
        return dao.excluirCompra(compraCod, conn);
    }

}
