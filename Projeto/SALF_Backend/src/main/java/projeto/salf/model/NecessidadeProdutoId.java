package projeto.salf.model;


import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class NecessidadeProdutoId implements Serializable {

    @Column(name = "pessoacarente_pc_cpf", length = 14)
    private String pessoaCarentePcCpf;

    @Column(name = "produto_prod_cod")
    private Integer produtoProdCod;

    public NecessidadeProdutoId() {}

    public NecessidadeProdutoId(String pessoaCarentePcCpf, Integer produtoProdCod) {
        this.pessoaCarentePcCpf = pessoaCarentePcCpf;
        this.produtoProdCod = produtoProdCod;
    }

    public String getPessoaCarentePcCpf() { return pessoaCarentePcCpf; }
    public void setPessoaCarentePcCpf(String pessoaCarentePcCpf) { this.pessoaCarentePcCpf = pessoaCarentePcCpf; }

    public Integer getProdutoProdCod() { return produtoProdCod; }
    public void setProdutoProdCod(Integer produtoProdCod) { this.produtoProdCod = produtoProdCod; }
}
