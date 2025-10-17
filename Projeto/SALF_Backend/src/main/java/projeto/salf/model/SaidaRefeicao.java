package projeto.salf.model;


import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "saida_refeicao")
public class SaidaRefeicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsaidarefeicao")
    private Integer idSaidaRefeicao;

    @Column(name = "dataregistro", nullable = false)
    private LocalDate dataRegistro;

    @ManyToOne
    @JoinColumn(name = "funcionario_func_cpf", nullable = false)
    private Funcionario funcionario;

    @Column(name = "observacao", length = 100)
    private String observacao;

    public SaidaRefeicao() {}

    public SaidaRefeicao(Integer idSaidaRefeicao, LocalDate dataRegistro, Funcionario funcionario, String observacao) {
        this.idSaidaRefeicao = idSaidaRefeicao;
        this.dataRegistro = dataRegistro;
        this.funcionario = funcionario;
        this.observacao = observacao;
    }

    public Integer getIdSaidaRefeicao() { return idSaidaRefeicao; }
    public void setIdSaidaRefeicao(Integer idSaidaRefeicao) { this.idSaidaRefeicao = idSaidaRefeicao; }

    public LocalDate getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDate dataRegistro) { this.dataRegistro = dataRegistro; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
