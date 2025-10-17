package projeto.salf.model;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "necessidade_produto")
public class NecessidadeProduto implements Serializable {
    @EmbeddedId
    private NecessidadeProdutoId id;

    private Date data;
    private Integer quantidade;
    private String observacao;

    public NecessidadeProdutoId getId() { return id; }
    public void setId(NecessidadeProdutoId id) { this.id = id; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}