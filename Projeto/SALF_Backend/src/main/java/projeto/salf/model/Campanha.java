//package projeto.salf.model;
//
//import jakarta.persistence.*;
//import java.io.Serializable;
//import java.time.LocalDate;
//
//@Entity
//@Table(name = "campanha")
//public class Campanha implements Serializable {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id_campanha")
//    private Integer idCampanha;
//
//    @Column(name = "campanha_descr", nullable = false, length = 100)
//    private String campanhaDescr;
//
//    @Column(name = "campanha_dtini", nullable = false)
//    private LocalDate campanhaDtIni;
//
//    @Column(name = "campanha_dtfim", nullable = false)
//    private LocalDate campanhaDtFim;
//
//    @Column(name = "campanha_total_arrecadado", nullable = false)
//    private Double campanhaTotalArrecadado;
//
//    @Column(name = "observacao", length = 100)
//    private String observacao;
//
//    @Column(name = "funcionario_func_cpf", length = 14)
//    private String funcionarioFuncCpf;
//
//    public Integer getIdCampanha() { return idCampanha; }
//    public void setIdCampanha(Integer idCampanha) { this.idCampanha = idCampanha; }
//
//    public String getCampanhaDescr() { return campanhaDescr; }
//    public void setCampanhaDescr(String campanhaDescr) { this.campanhaDescr = campanhaDescr; }
//
//    public LocalDate getCampanhaDtIni() { return campanhaDtIni; }
//    public void setCampanhaDtIni(LocalDate campanhaDtIni) { this.campanhaDtIni = campanhaDtIni; }
//
//    public LocalDate getCampanhaDtFim() { return campanhaDtFim; }
//    public void setCampanhaDtFim(LocalDate campanhaDtFim) { this.campanhaDtFim = campanhaDtFim; }
//
//    public Double getCampanhaTotalArrecadado() { return campanhaTotalArrecadado; }
//    public void setCampanhaTotalArrecadado(Double campanhaTotalArrecadado) { this.campanhaTotalArrecadado = campanhaTotalArrecadado; }
//
//    public String getObservacao() { return observacao; }
//    public void setObservacao(String observacao) { this.observacao = observacao; }
//
//    public String getFuncionarioFuncCpf() { return funcionarioFuncCpf; }
//    public void setFuncionarioFuncCpf(String funcionarioFuncCpf) { this.funcionarioFuncCpf = funcionarioFuncCpf; }
//}
