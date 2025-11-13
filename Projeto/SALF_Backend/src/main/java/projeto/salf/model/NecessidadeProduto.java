package projeto.salf.model;

import java.time.LocalDate;

public class NecessidadeProduto {
    private String pessoaCpf;
    private Integer produtoId;
    private LocalDate data;
    private Integer quantidade;
    private String observacao;

    public String getPessoaCpf() { return pessoaCpf; }
    public void setPessoaCpf(String pessoaCpf) { this.pessoaCpf = pessoaCpf; }

    public Integer getProdutoId() { return produtoId; }
    public void setProdutoId(Integer produtoId) { this.produtoId = produtoId; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
