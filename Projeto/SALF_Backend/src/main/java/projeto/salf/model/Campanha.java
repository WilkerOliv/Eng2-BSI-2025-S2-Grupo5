package projeto.salf.model;


import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "campanha")
public class Campanha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcampanha")
    private Integer idCampanha;

    @Column(name = "campanha_descr", length = 100, nullable = false)
    private String campanhaDescr;

    @Column(name = "campanha_dtini", nullable = false)
    private LocalDate campanhaDtIni;

    @Column(name = "campanha_dtfim", nullable = false)
    private LocalDate campanhaDtFim;

    @Column(name = "campanha_totalarrecado", nullable = false)
    private Double campanhaTotalArrecado;

    @Column(name = "observacao", length = 100)
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "funcionario_func_cpf", nullable = false)
    private Funcionario funcionario;

    public Campanha() {}

    public Campanha(Integer idCampanha, String campanhaDescr, LocalDate campanhaDtIni, LocalDate campanhaDtFim,
                    Double campanhaTotalArrecado, String observacao, Funcionario funcionario) {
        this.idCampanha = idCampanha;
        this.campanhaDescr = campanhaDescr;
        this.campanhaDtIni = campanhaDtIni;
        this.campanhaDtFim = campanhaDtFim;
        this.campanhaTotalArrecado = campanhaTotalArrecado;
        this.observacao = observacao;
        this.funcionario = funcionario;
    }

    public Integer getIdCampanha() { return idCampanha; }
    public void setIdCampanha(Integer idCampanha) { this.idCampanha = idCampanha; }

    public String getCampanhaDescr() { return campanhaDescr; }
    public void setCampanhaDescr(String campanhaDescr) { this.campanhaDescr = campanhaDescr; }

    public LocalDate getCampanhaDtIni() { return campanhaDtIni; }
    public void setCampanhaDtIni(LocalDate campanhaDtIni) { this.campanhaDtIni = campanhaDtIni; }

    public LocalDate getCampanhaDtFim() { return campanhaDtFim; }
    public void setCampanhaDtFim(LocalDate campanhaDtFim) { this.campanhaDtFim = campanhaDtFim; }

    public Double getCampanhaTotalArrecado() { return campanhaTotalArrecado; }
    public void setCampanhaTotalArrecado(Double campanhaTotalArrecado) { this.campanhaTotalArrecado = campanhaTotalArrecado; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }
}
