package projeto.salf.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "necessidade_cesta")
public class NecessidadeCesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_necessidade_cesta")
    private Integer id;

    @Column(name = "pessoacarente_pc_cpf", length = 14, nullable = false)
    private String pessoaCarentePcCpf;

    @ManyToOne
    @JoinColumn(name = "cestabasica_cb_cod", nullable = false)
    private CestaBasica cestaBasica;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "observacao", length = 100)
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "funcionario_func_cpf", nullable = false)
    private Funcionario funcionario;

    public NecessidadeCesta() {}

    public NecessidadeCesta(Integer id, String pessoaCarentePcCpf, CestaBasica cestaBasica, LocalDate data,
                            Integer quantidade, String observacao, Funcionario funcionario) {
        this.id = id;
        this.pessoaCarentePcCpf = pessoaCarentePcCpf;
        this.cestaBasica = cestaBasica;
        this.data = data;
        this.quantidade = quantidade;
        this.observacao = observacao;
        this.funcionario = funcionario;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPessoaCarentePcCpf() { return pessoaCarentePcCpf; }
    public void setPessoaCarentePcCpf(String pessoaCarentePcCpf) { this.pessoaCarentePcCpf = pessoaCarentePcCpf; }

    public CestaBasica getCestaBasica() { return cestaBasica; }
    public void setCestaBasica(CestaBasica cestaBasica) { this.cestaBasica = cestaBasica; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }
}
