package projeto.salf.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "necessidade_produto")
public class NecessidadeProduto implements Serializable {

    @EmbeddedId
    private NecessidadeProdutoId id;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "observacao", length = 100)
    private String observacao;

    public NecessidadeProdutoId getId() { return id; }
    public void setId(NecessidadeProdutoId id) { this.id = id; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
