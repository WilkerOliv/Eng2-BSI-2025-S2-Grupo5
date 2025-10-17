package projeto.salf.model;


import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class ItensListaId implements Serializable {
    private Integer produtoId;
    private Integer listaId;

    // Getters e Setters
    public Integer getProdutoId() { return produtoId; }
    public void setProdutoId(Integer produtoId) { this.produtoId = produtoId; }

    public Integer getListaId() { return listaId; }
    public void setListaId(Integer listaId) { this.listaId = listaId; }
}
