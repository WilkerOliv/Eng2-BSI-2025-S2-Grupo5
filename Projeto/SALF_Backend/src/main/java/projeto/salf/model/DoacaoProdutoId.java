package projeto.salf.model;

import java.io.Serializable;
import java.util.Objects;

public class DoacaoProdutoId implements Serializable {
    private Integer doacaoCod;
    private Integer produtoCod;

    public DoacaoProdutoId() {}

    public DoacaoProdutoId(Integer doacaoCod, Integer produtoCod) {
        this.doacaoCod = doacaoCod;
        this.produtoCod = produtoCod;
    }

    public Integer getDoacaoCod() {
        return doacaoCod;
    }

    public void setDoacaoCod(Integer doacaoCod) {
        this.doacaoCod = doacaoCod;
    }

    public Integer getProdutoCod() {
        return produtoCod;
    }

    public void setProdutoCod(Integer produtoCod) {
        this.produtoCod = produtoCod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoacaoProdutoId)) return false;
        DoacaoProdutoId that = (DoacaoProdutoId) o;
        return Objects.equals(doacaoCod, that.doacaoCod) &&
                Objects.equals(produtoCod, that.produtoCod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doacaoCod, produtoCod);
    }
}
