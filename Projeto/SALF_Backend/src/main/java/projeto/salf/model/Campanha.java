package projeto.salf.model;

import java.time.LocalDate;

public class Campanha {

    private Long id;
    private String nome; // Descrição da Campanha (campanhaDescr)
    private LocalDate dataInicio; // Data de Início (campanhaDtIni)
    private LocalDate dataFim; // Data de Fim (campanhaDtFim)
    private String observacao;
    private String status; // Ex: "Em Andamento", "Finalizada", "Cancelada"
    private boolean ativo; // Para exclusão lógica

    public Campanha() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Campanha{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", observacao='" + observacao + '\'' +
                ", status='" + status + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}
