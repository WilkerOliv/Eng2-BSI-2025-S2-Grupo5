package projeto.salf.model;

import jakarta.persistence.*;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prod_cod")
    private Integer prodCod;

    @Column(name = "prod_descr", length = 100, nullable = false)
    private String prodDescr;

    @ManyToOne
    @JoinColumn(name = "categoria_produto_cat_cod", nullable = false)
    private CategoriaProduto categoriaProduto;

    public Produto() {}

    public Produto(Integer prodCod, String prodDescr, CategoriaProduto categoriaProduto) {
        this.prodCod = prodCod;
        this.prodDescr = prodDescr;
        this.categoriaProduto = categoriaProduto;
    }

    public Integer getProdCod() { return prodCod; }
    public void setProdCod(Integer prodCod) { this.prodCod = prodCod; }

    public String getProdDescr() { return prodDescr; }
    public void setProdDescr(String prodDescr) { this.prodDescr = prodDescr; }

    public CategoriaProduto getCategoriaProduto() { return categoriaProduto; }
    public void setCategoriaProduto(CategoriaProduto categoriaProduto) { this.categoriaProduto = categoriaProduto; }
}
