package projeto.salf.model;

import java.io.Serializable;
import java.util.Objects;

public class CestaBasicaProdutoId implements Serializable {
    private Integer cestaBasicaCbCod;
    private Integer produtoProdCod;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CestaBasicaProdutoId)) return false;
        CestaBasicaProdutoId that = (CestaBasicaProdutoId) o;
        return Objects.equals(cestaBasicaCbCod, that.cestaBasicaCbCod) &&
                Objects.equals(produtoProdCod, that.produtoProdCod);
    }

    @Override
    public int hashCode() { return Objects.hash(cestaBasicaCbCod, produtoProdCod); }
}
