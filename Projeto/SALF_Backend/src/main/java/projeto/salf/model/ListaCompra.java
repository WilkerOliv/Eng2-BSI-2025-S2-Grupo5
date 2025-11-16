package projeto.salf.model;

import java.time.LocalDate;

public class ListaCompra {
    private Integer lcCod;
    private String funcionarioCpf; // apenas o CPF do funcionário (FK)
    private LocalDate dataCriacao;
    private String descricao;
    private Integer statusAtendimento;

    public Integer getLcCod() { return lcCod; }
    public void setLcCod(Integer lcCod) { this.lcCod = lcCod; }

    public String getFuncionarioCpf() { return funcionarioCpf; }
    public void setFuncionarioCpf(String funcionarioCpf) { this.funcionarioCpf = funcionarioCpf; }

    public LocalDate getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao = dataCriacao; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Integer getStatusAtendimento() { return statusAtendimento; }
    public void setStatusAtendimento(Integer statusAtendimento) { this.statusAtendimento = statusAtendimento; }
}
