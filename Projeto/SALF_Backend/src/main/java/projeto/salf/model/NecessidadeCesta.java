package projeto.salf.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "necessidade_cesta")
public class NecessidadeCesta implements Serializable {
    @EmbeddedId
    private NecessidadeCestaId id;

    private Date data;
    private Integer quantidade;
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "func_cpf")
    private Funcionario funcionario;

    // Getters e Setters
    public NecessidadeCestaId getId() { return id; }
    public void setId(NecessidadeCestaId id) { this.id = id; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }
}
