package projeto.salf.model;

public class Produto {
    private Integer prodCod;
    private String prodDescr;
    private Integer categoriaProdCod; // FK -> categoria_produto(cat_cod)

    public Integer getProdCod() { return prodCod; }
    public void setProdCod(Integer prodCod) { this.prodCod = prodCod; }

    public String getProdDescr() { return prodDescr; }
    public void setProdDescr(String prodDescr) { this.prodDescr = prodDescr; }

    public Integer getCategoriaProdCod() { return categoriaProdCod; }
    public void setCategoriaProdCod(Integer categoriaProdCod) { this.categoriaProdCod = categoriaProdCod; }
}
