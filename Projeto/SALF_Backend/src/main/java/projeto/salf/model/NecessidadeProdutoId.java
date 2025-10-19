package projeto.salf.model;


import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class NecessidadeProdutoId implements Serializable {
    private String pessoaCpf;
    private Integer produtoId;

    public String getPessoaCpf() { return pessoaCpf; }
    public void setPessoaCpf(String pessoaCpf) { this.pessoaCpf = pessoaCpf; }

    public Integer getProdutoId() { return produtoId; }
    public void setProdutoId(Integer produtoId) { this.produtoId = produtoId; }
}
