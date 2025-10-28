package projeto.salf.model;

import java.io.Serializable;
import java.util.Objects;

public class ItensCompraId implements Serializable {
    private Integer lcCod;
    private Integer produtoCod;

    public ItensCompraId() {}

    public ItensCompraId(Integer lcCod, Integer produtoCod) {
        this.lcCod = lcCod;
        this.produtoCod = produtoCod;
    }

    public Integer getLcCod() {
        return lcCod;
    }

    public void setLcCod(Integer lcCod) {
        this.lcCod = lcCod;
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
        if (!(o instanceof ItensCompraId)) return false;
        ItensCompraId that = (ItensCompraId) o;
        return Objects.equals(lcCod, that.lcCod) &&
                Objects.equals(produtoCod, that.produtoCod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lcCod, produtoCod);
    }
}
