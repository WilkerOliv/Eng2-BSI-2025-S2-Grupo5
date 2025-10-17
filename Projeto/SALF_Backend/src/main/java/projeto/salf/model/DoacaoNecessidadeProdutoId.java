package projeto.salf.model;


import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class DoacaoNecessidadeProdutoId implements Serializable {

    @Column(name = "doacao_doa_cod")
    private Integer doacaoDoaCod;

    @Column(name = "necessidadeproduto_pessoacarente_pc_cpf", length = 18)
    private String necessidadePessoaCpf;

    @Column(name = "necessidadeproduto_produto_prod_cod")
    private Integer necessidadeProdutoCod;

    public DoacaoNecessidadeProdutoId() {}

    public DoacaoNecessidadeProdutoId(Integer doacaoDoaCod, String necessidadePessoaCpf, Integer necessidadeProdutoCod) {
        this.doacaoDoaCod = doacaoDoaCod;
        this.necessidadePessoaCpf = necessidadePessoaCpf;
        this.necessidadeProdutoCod = necessidadeProdutoCod;
    }

    public Integer getDoacaoDoaCod() { return doacaoDoaCod; }
    public void setDoacaoDoaCod(Integer doacaoDoaCod) { this.doacaoDoaCod = doacaoDoaCod; }

    public String getNecessidadePessoaCpf() { return necessidadePessoaCpf; }
    public void setNecessidadePessoaCpf(String necessidadePessoaCpf) { this.necessidadePessoaCpf = necessidadePessoaCpf; }

    public Integer getNecessidadeProdutoCod() { return necessidadeProdutoCod; }
    public void setNecessidadeProdutoCod(Integer necessidadeProdutoCod) { this.necessidadeProdutoCod = necessidadeProdutoCod; }
}
