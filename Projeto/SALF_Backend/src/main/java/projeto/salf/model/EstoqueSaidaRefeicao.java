//package projeto.salf.model;
//
//import jakarta.persistence.*;
//import java.io.Serializable;
//
//@Entity
//@Table(name = "estoque_saida_refeicao")
//@IdClass(EstoqueSaidaRefeicaoId.class)
//public class EstoqueSaidaRefeicao implements Serializable {
//
//    @Id
//    @Column(name = "estoque_est_cod")
//    private Integer estoqueEstCod;
//
//    @Id
//    @Column(name = "saida_refeicao_id_saida_refeicao")
//    private Integer saidaRefeicaoIdSaidaRefeicao;
//
//    @Column(name = "quantidade", nullable = false)
//    private Integer quantidade;
//
//    public Integer getEstoqueEstCod() { return estoqueEstCod; }
//    public void setEstoqueEstCod(Integer estoqueEstCod) { this.estoqueEstCod = estoqueEstCod; }
//
//    public Integer getSaidaRefeicaoIdSaidaRefeicao() { return saidaRefeicaoIdSaidaRefeicao; }
//    public void setSaidaRefeicaoIdSaidaRefeicao(Integer saidaRefeicaoIdSaidaRefeicao) { this.saidaRefeicaoIdSaidaRefeicao = saidaRefeicaoIdSaidaRefeicao; }
//
//    public Integer getQuantidade() { return quantidade; }
//    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
//}
