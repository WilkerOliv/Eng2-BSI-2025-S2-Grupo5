package projeto.salf.model;


import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class EstoqueSaidaRefeicaoId implements Serializable {

    @Column(name = "estoque_est_cod")
    private Integer estoqueEstCod;

    @Column(name = "saida_refeicao_idsaida_refeicao")
    private Integer saidaRefeicaoId;

    public EstoqueSaidaRefeicaoId() {}

    public EstoqueSaidaRefeicaoId(Integer estoqueEstCod, Integer saidaRefeicaoId) {
        this.estoqueEstCod = estoqueEstCod;
        this.saidaRefeicaoId = saidaRefeicaoId;
    }

    public Integer getEstoqueEstCod() { return estoqueEstCod; }
    public void setEstoqueEstCod(Integer estoqueEstCod) { this.estoqueEstCod = estoqueEstCod; }

    public Integer getSaidaRefeicaoId() { return saidaRefeicaoId; }
    public void setSaidaRefeicaoId(Integer saidaRefeicaoId) { this.saidaRefeicaoId = saidaRefeicaoId; }
}
