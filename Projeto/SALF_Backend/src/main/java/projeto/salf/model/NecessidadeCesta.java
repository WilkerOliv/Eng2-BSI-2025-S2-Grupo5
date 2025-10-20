package projeto.salf.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "necessidade_cesta")
public class NecessidadeCesta implements Serializable {

    @EmbeddedId
    private NecessidadeCestaId id;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "observacao", length = 100)
    private String observacao;

    @Column(name = "funcionario_func_cpf", length = 14, nullable = false)
    private String funcionarioFuncCpf;

    public NecessidadeCestaId getId() { return id; }
    public void setId(NecessidadeCestaId id) { this.id = id; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public String getFuncionarioFuncCpf() { return funcionarioFuncCpf; }
    public void setFuncionarioFuncCpf(String funcionarioFuncCpf) { this.funcionarioFuncCpf = funcionarioFuncCpf; }
}
