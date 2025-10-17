package projeto.salf.model;

import jakarta.persistence.*;

/* CategoriaProduto */
@Entity
@Table(name = "categoria_produto")
public class CategoriaProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_cod")
    private Integer catCod;

    @Column(name = "cat_descr", length = 100, nullable = false)
    private String catDescr;

    public CategoriaProduto() {}

    public CategoriaProduto(Integer catCod, String catDescr) {
        this.catCod = catCod;
        this.catDescr = catDescr;
    }

    public Integer getCatCod() { return catCod; }
    public void setCatCod(Integer catCod) { this.catCod = catCod; }

    public String getCatDescr() { return catDescr; }
    public void setCatDescr(String catDescr) { this.catDescr = catDescr; }
}