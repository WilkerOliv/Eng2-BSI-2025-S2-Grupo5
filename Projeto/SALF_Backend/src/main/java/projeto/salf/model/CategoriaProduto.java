package projeto.salf.model;

import jakarta.persistence.*;

import java.io.Serializable;

/* CategoriaProduto */
@Entity
@Table(name = "categoria_produto")
public class CategoriaProduto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer catCod;

    @Column(nullable = false, length = 100)
    private String catDescr;

    public Integer getCatCod() { return catCod; }
    public void setCatCod(Integer catCod) { this.catCod = catCod; }

    public String getCatDescr() { return catDescr; }
    public void setCatDescr(String catDescr) { this.catDescr = catDescr; }
}