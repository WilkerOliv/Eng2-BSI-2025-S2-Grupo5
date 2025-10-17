package projeto.salf.model;

import jakarta.persistence.*;
import java.io.Serializable;

/* CestaBasica_has_Produto -> CestaBasicaProdutoId & CestaBasicaProduto */
@Embeddable
public class CestaBasicaProdutoId implements Serializable {

    @Column(name = "cestabasica_cb_cod")
    private Integer cestaBasicaCbCod;

    @Column(name = "produto_prod_cod")
    private Integer produtoProdCod;

    public CestaBasicaProdutoId() {}

    public CestaBasicaProdutoId(Integer cestaBasicaCbCod, Integer produtoProdCod) {
        this.cestaBasicaCbCod = cestaBasicaCbCod;
        this.produtoProdCod = produtoProdCod;
    }

    public Integer getCestaBasicaCbCod() { return cestaBasicaCbCod; }
    public void setCestaBasicaCbCod(Integer cestaBasicaCbCod) { this.cestaBasicaCbCod = cestaBasicaCbCod; }

    public Integer getProdutoProdCod() { return produtoProdCod; }
    public void setProdutoProdCod(Integer produtoProdCod) { this.produtoProdCod = produtoProdCod; }
}