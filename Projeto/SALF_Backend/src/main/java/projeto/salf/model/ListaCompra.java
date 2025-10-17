package projeto.salf.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/* ListaCompra */
@Entity
@Table(name = "lista_compra")
public class ListaCompra implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lcCod;

    @ManyToOne
    @JoinColumn(name = "func_cpf", nullable = false)
    private Funcionario funcionario;

    @Temporal(TemporalType.DATE)
    private Date dataCriacao;

    private String descricao;
    private Integer statusAtendimento;

    public Integer getLcCod() { return lcCod; }
    public void setLcCod(Integer lcCod) { this.lcCod = lcCod; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }

    public Date getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(Date dataCriacao) { this.dataCriacao = dataCriacao; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Integer getStatusAtendimento() { return statusAtendimento; }
    public void setStatusAtendimento(Integer statusAtendimento) { this.statusAtendimento = statusAtendimento; }
}