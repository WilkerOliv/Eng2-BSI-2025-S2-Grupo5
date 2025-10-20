package projeto.salf.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "produto")
public class Produto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prod_cod")
    private Integer prodCod;

    @Column(name = "prod_descr", nullable = false, length = 100)
    private String prodDescr;

    @ManyToOne
    @JoinColumn(name = "categoria_produto_cat_cod", nullable = false)
    private CategoriaProduto categoria;

    public Integer getProdCod() { return prodCod; }
    public void setProdCod(Integer prodCod) { this.prodCod = prodCod; }

    public String getProdDescr() { return prodDescr; }
    public void setProdDescr(String prodDescr) { this.prodDescr = prodDescr; }

    public CategoriaProduto getCategoria() { return categoria; }
    public void setCategoria(CategoriaProduto categoria) { this.categoria = categoria; }
}
