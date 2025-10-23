package projeto.salf.model;

import java.io.Serializable;
import java.util.Objects;

public class ItensCompraId implements Serializable {
    private Integer produtoProdCod;
    private Integer compraCompraCod;

    public ItensCompraId() {}

    public ItensCompraId(Integer p1, Integer p2) { this.produtoProdCod = p1; this.compraCompraCod = p2; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItensCompraId)) return false;
        ItensCompraId that = (ItensCompraId) o;
        return Objects.equals(produtoProdCod, that.produtoProdCod) &&
                Objects.equals(compraCompraCod, that.compraCompraCod);
    }

    @Override
    public int hashCode() { return Objects.hash(produtoProdCod, compraCompraCod); }

    public Integer getProdutoProdCod() { return produtoProdCod; }
    public void setProdutoProdCod(Integer v) { this.produtoProdCod = v; }
    public Integer getCompraCompraCod() { return compraCompraCod; }
    public void setCompraCompraCod(Integer v) { this.compraCompraCod = v; }
}
