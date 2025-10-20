package projeto.salf.model;

import java.io.Serializable;
import java.util.Objects;

public class FornecCotacaoId implements Serializable {
    private Integer fornecedorId;
    private Integer cotacaoId;

    public FornecCotacaoId() {}

    public FornecCotacaoId(Integer fornecedorId, Integer cotacaoId) {
        this.fornecedorId = fornecedorId;
        this.cotacaoId = cotacaoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FornecCotacaoId)) return false;
        FornecCotacaoId that = (FornecCotacaoId) o;
        return Objects.equals(fornecedorId, that.fornecedorId) &&
                Objects.equals(cotacaoId, that.cotacaoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fornecedorId, cotacaoId);
    }
}
