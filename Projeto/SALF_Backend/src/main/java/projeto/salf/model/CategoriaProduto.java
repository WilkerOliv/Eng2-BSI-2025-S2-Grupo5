package projeto.salf.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "categoria_produto")
public class CategoriaProduto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_cod")
    private Integer catCod;

    @Column(name = "cat_descr", nullable = false, length = 100)
    private String catDescr;

    public Integer getCatCod() { return catCod; }
    public void setCatCod(Integer catCod) { this.catCod = catCod; }

    public String getCatDescr() { return catDescr; }
    public void setCatDescr(String catDescr) { this.catDescr = catDescr; }
}
