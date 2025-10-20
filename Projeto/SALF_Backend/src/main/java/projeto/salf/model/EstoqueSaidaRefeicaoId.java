package projeto.salf.model;

import java.io.Serializable;
import java.util.Objects;

public class EstoqueSaidaRefeicaoId implements Serializable {
    private Integer estoqueEstCod;
    private Integer saidaRefeicaoIdSaidaRefeicao;

    public EstoqueSaidaRefeicaoId() {}

    public EstoqueSaidaRefeicaoId(Integer e, Integer s) { this.estoqueEstCod = e; this.saidaRefeicaoIdSaidaRefeicao = s; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EstoqueSaidaRefeicaoId)) return false;
        EstoqueSaidaRefeicaoId that = (EstoqueSaidaRefeicaoId) o;
        return Objects.equals(estoqueEstCod, that.estoqueEstCod) &&
                Objects.equals(saidaRefeicaoIdSaidaRefeicao, that.saidaRefeicaoIdSaidaRefeicao);
    }

    @Override
    public int hashCode() { return Objects.hash(estoqueEstCod, saidaRefeicaoIdSaidaRefeicao); }

    public Integer getEstoqueEstCod() { return estoqueEstCod; }
    public void setEstoqueEstCod(Integer v) { this.estoqueEstCod = v; }
    public Integer getSaidaRefeicaoIdSaidaRefeicao() { return saidaRefeicaoIdSaidaRefeicao; }
    public void setSaidaRefeicaoIdSaidaRefeicao(Integer v) { this.saidaRefeicaoIdSaidaRefeicao = v; }
}
