package projeto.salf.model;

import java.io.Serializable;
import java.util.Objects;

public class DoacaoProdutoId implements Serializable {
    private Integer doacaoDoaCod;
    private Integer produtoProdCod;

    public DoacaoProdutoId() {}

    public DoacaoProdutoId(Integer doacaoDoaCod, Integer produtoProdCod) {
        this.doacaoDoaCod = doacaoDoaCod;
        this.produtoProdCod = produtoProdCod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoacaoProdutoId)) return false;
        DoacaoProdutoId that = (DoacaoProdutoId) o;
        return Objects.equals(doacaoDoaCod, that.doacaoDoaCod) &&
                Objects.equals(produtoProdCod, that.produtoProdCod);
    }

    @Override
    public int hashCode() { return Objects.hash(doacaoDoaCod, produtoProdCod); }
}
