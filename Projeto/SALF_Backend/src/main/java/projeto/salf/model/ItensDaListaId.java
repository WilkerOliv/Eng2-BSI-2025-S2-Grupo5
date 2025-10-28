package projeto.salf.model;

import java.io.Serializable;
import java.util.Objects;

public class ItensDaListaId implements Serializable {
    private Integer listaCod;
    private Integer produtoCod;

    public ItensDaListaId() {}

    public ItensDaListaId(Integer listaCod, Integer produtoCod) {
        this.listaCod = listaCod;
        this.produtoCod = produtoCod;
    }

    public Integer getListaCod() {
        return listaCod;
    }

    public void setListaCod(Integer listaCod) {
        this.listaCod = listaCod;
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
        if (!(o instanceof ItensDaListaId)) return false;
        ItensDaListaId that = (ItensDaListaId) o;
        return Objects.equals(listaCod, that.listaCod) &&
                Objects.equals(produtoCod, that.produtoCod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listaCod, produtoCod);
    }
}
