//package projeto.salf.model;
//
//import jakarta.persistence.*;
//import java.io.Serializable;
//
//@Entity
//@Table(name = "itens_compra")
//@IdClass(ItensCompraId.class)
//public class ItensCompra implements Serializable {
//
//    @Id
//    @Column(name = "produto_prod_cod")
//    private Integer produtoProdCod;
//
//    @Id
//    @Column(name = "compra_compra_cod")
//    private Integer compraCompraCod;
//
//    @Column(name = "valor", nullable = false)
//    private Double valor;
//
//    @Column(name = "quantidade", nullable = false)
//    private Integer quantidade;
//
//    public Integer getProdutoProdCod() { return produtoProdCod; }
//    public void setProdutoProdCod(Integer produtoProdCod) { this.produtoProdCod = produtoProdCod; }
//
//    public Integer getCompraCompraCod() { return compraCompraCod; }
//    public void setCompraCompraCod(Integer compraCompraCod) { this.compraCompraCod = compraCompraCod; }
//
//    public Double getValor() { return valor; }
//    public void setValor(Double valor) { this.valor = valor; }
//
//    public Integer getQuantidade() { return quantidade; }
//    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
//}
