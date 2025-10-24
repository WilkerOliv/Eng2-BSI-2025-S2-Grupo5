//package projeto.salf.model;
//
//import jakarta.persistence.*;
//import java.io.Serializable;
//import java.time.LocalDate;
//
//@Entity
//@Table(name = "estoque")
//public class Estoque implements Serializable {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "est_cod")
//    private Integer estCod;
//
//    @Column(name = "est_prod_quantidade", nullable = false)
//    private Integer estProdQuantidade;
//
//    @Column(name = "data_validade", nullable = false)
//    private LocalDate dataValidade;
//
//    @Column(name = "produto_prod_cod", nullable = false)
//    private Integer produtoProdCod;
//
//    public Integer getEstCod() { return estCod; }
//    public void setEstCod(Integer estCod) { this.estCod = estCod; }
//
//    public Integer getEstProdQuantidade() { return estProdQuantidade; }
//    public void setEstProdQuantidade(Integer estProdQuantidade) { this.estProdQuantidade = estProdQuantidade; }
//
//    public LocalDate getDataValidade() { return dataValidade; }
//    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }
//
//    public Integer getProdutoProdCod() { return produtoProdCod; }
//    public void setProdutoProdCod(Integer produtoProdCod) { this.produtoProdCod = produtoProdCod; }
//}
