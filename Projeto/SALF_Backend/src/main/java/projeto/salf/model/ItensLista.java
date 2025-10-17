package projeto.salf.model;


import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "itens_lista")
public class ItensLista implements Serializable {
    @EmbeddedId
    private ItensListaId id;

    private Integer quantidade;

    // Getters e Setters
    public ItensListaId getId() { return id; }
    public void setId(ItensListaId id) { this.id = id; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}