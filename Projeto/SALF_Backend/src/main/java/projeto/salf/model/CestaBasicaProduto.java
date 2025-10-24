//package projeto.salf.model;
//
//import jakarta.persistence.*;
//import java.io.Serializable;
//
//@Entity
//@Table(name = "cesta_basica_produto")
//@IdClass(CestaBasicaProdutoId.class)
//public class CestaBasicaProduto implements Serializable {
//
//    @Id
//    @Column(name = "cesta_basica_cb_cod")
//    private Integer cestaBasicaCbCod;
//
//    @Id
//    @Column(name = "produto_prod_cod")
//    private Integer produtoProdCod;
//
//    @Column(name = "quantidade")
//    private Integer quantidade;
//
//    public Integer getCestaBasicaCbCod() {
//        return cestaBasicaCbCod;
//    }
//
//    public void setCestaBasicaCbCod(Integer cestaBasicaCbCod) {
//        this.cestaBasicaCbCod = cestaBasicaCbCod;
//    }
//
//    public Integer getProdutoProdCod() {
//        return produtoProdCod;
//    }
//
//    public void setProdutoProdCod(Integer produtoProdCod) {
//        this.produtoProdCod = produtoProdCod;
//    }
//
//    public Integer getQuantidade() {
//        return quantidade;
//    }
//
//    public void setQuantidade(Integer quantidade) {
//        this.quantidade = quantidade;
//    }
//}
