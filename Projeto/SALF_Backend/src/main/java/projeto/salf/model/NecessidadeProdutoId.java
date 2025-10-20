package projeto.salf.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class NecessidadeProdutoId implements Serializable {

    @Column(name = "pessoa_carente_pc_cpf", length = 14)
    private String pessoaCarentePcCpf;

    @Column(name = "produto_prod_cod")
    private Integer produtoProdCod;

    public String getPessoaCarentePcCpf() { return pessoaCarentePcCpf; }
    public void setPessoaCarentePcCpf(String pessoaCarentePcCpf) { this.pessoaCarentePcCpf = pessoaCarentePcCpf; }

    public Integer getProdutoProdCod() { return produtoProdCod; }
    public void setProdutoProdCod(Integer produtoProdCod) { this.produtoProdCod = produtoProdCod; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NecessidadeProdutoId)) return false;
        NecessidadeProdutoId that = (NecessidadeProdutoId) o;
        return Objects.equals(pessoaCarentePcCpf, that.pessoaCarentePcCpf) &&
                Objects.equals(produtoProdCod, that.produtoProdCod);
    }

    @Override
    public int hashCode() { return Objects.hash(pessoaCarentePcCpf, produtoProdCod); }
}
