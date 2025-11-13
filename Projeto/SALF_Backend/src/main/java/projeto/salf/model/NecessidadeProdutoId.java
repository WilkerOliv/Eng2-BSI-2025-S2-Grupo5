package projeto.salf.model;

import java.io.Serializable;
import java.util.Objects;

public class NecessidadeProdutoId implements Serializable {
    private String pessoaCpf;
    private Integer produtoId;

    public NecessidadeProdutoId() {}
    public NecessidadeProdutoId(String pessoaCpf, Integer produtoId) {
        this.pessoaCpf = pessoaCpf;
        this.produtoId = produtoId;
    }

    public String getPessoaCpf() { return pessoaCpf; }
    public void setPessoaCpf(String pessoaCpf) { this.pessoaCpf = pessoaCpf; }

    public Integer getProdutoId() { return produtoId; }
    public void setProdutoId(Integer produtoId) { this.produtoId = produtoId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NecessidadeProdutoId)) return false;
        NecessidadeProdutoId that = (NecessidadeProdutoId) o;
        return Objects.equals(pessoaCpf, that.pessoaCpf) && Objects.equals(produtoId, that.produtoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pessoaCpf, produtoId);
    }
}
