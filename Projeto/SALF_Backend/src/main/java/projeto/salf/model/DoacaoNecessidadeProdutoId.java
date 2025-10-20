package projeto.salf.model;

import java.io.Serializable;
import java.util.Objects;

public class DoacaoNecessidadeProdutoId implements Serializable {
    private Integer doacaoDoaCod;
    private String necessidadeProdutoPessoaCarentePcCpf;
    private Integer necessidadeProdutoProdutoProdCod;

    public DoacaoNecessidadeProdutoId() {}
    public DoacaoNecessidadeProdutoId(Integer a, String b, Integer c) { this.doacaoDoaCod = a; this.necessidadeProdutoPessoaCarentePcCpf = b; this.necessidadeProdutoProdutoProdCod = c; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoacaoNecessidadeProdutoId)) return false;
        DoacaoNecessidadeProdutoId that = (DoacaoNecessidadeProdutoId) o;
        return Objects.equals(doacaoDoaCod, that.doacaoDoaCod) &&
                Objects.equals(necessidadeProdutoPessoaCarentePcCpf, that.necessidadeProdutoPessoaCarentePcCpf) &&
                Objects.equals(necessidadeProdutoProdutoProdCod, that.necessidadeProdutoProdutoProdCod);
    }

    @Override
    public int hashCode() { return Objects.hash(doacaoDoaCod, necessidadeProdutoPessoaCarentePcCpf, necessidadeProdutoProdutoProdCod); }

    public Integer getDoacaoDoaCod() { return doacaoDoaCod; }
    public void setDoacaoDoaCod(Integer v) { this.doacaoDoaCod = v; }
    public String getNecessidadeProdutoPessoaCarentePcCpf() { return necessidadeProdutoPessoaCarentePcCpf; }
    public void setNecessidadeProdutoPessoaCarentePcCpf(String v) { this.necessidadeProdutoPessoaCarentePcCpf = v; }
    public Integer getNecessidadeProdutoProdutoProdCod() { return necessidadeProdutoProdutoProdCod; }
    public void setNecessidadeProdutoProdutoProdCod(Integer v) { this.necessidadeProdutoProdutoProdCod = v; }
}
