package projeto.salf.model;

import java.time.LocalDate;

public class ResultadoCampanha {

    private Long id;
    private Long idCampanha;
    private Double valorArrecadado;
    private Integer familiasAtendidas;
    private Integer produtosArrecadados;
    private String observacao;
    private LocalDate dataRegistro;

    public ResultadoCampanha() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCampanha() {
        return idCampanha;
    }

    public void setIdCampanha(Long idCampanha) {
        this.idCampanha = idCampanha;
    }

    public Double getValorArrecadado() {
        return valorArrecadado;
    }

    public void setValorArrecadado(Double valorArrecadado) {
        this.valorArrecadado = valorArrecadado;
    }

    public Integer getFamiliasAtendidas() {
        return familiasAtendidas;
    }

    public void setFamiliasAtendidas(Integer familiasAtendidas) {
        this.familiasAtendidas = familiasAtendidas;
    }

    public Integer getProdutosArrecadados() {
        return produtosArrecadados;
    }

    public void setProdutosArrecadados(Integer produtosArrecadados) {
        this.produtosArrecadados = produtosArrecadados;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    @Override
    public String toString() {
        return "ResultadoCampanha{" +
                "id=" + id +
                ", idCampanha=" + idCampanha +
                ", valorArrecadado=" + valorArrecadado +
                ", familiasAtendidas=" + familiasAtendidas +
                ", produtosArrecadados=" + produtosArrecadados +
                ", observacao='" + observacao + '\'' +
                ", dataRegistro=" + dataRegistro +
                '}';
    }
}
