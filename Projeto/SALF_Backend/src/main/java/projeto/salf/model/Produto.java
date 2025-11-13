package projeto.salf.model;

import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.dao.ProdutoDAO;

import java.util.List;


public class Produto {

    private Integer prodCod;
    private String prodDescr;
    private Integer categoriaProdCod; // FK para categoria_produto.cat_cod

    public Integer getProdCod() {
        return prodCod;
    }

    public void setProdCod(Integer prodCod) {
        this.prodCod = prodCod;
    }

    public String getProdDescr() {
        return prodDescr;
    }

    public void setProdDescr(String prodDescr) {
        this.prodDescr = prodDescr;
    }

    public Integer getCategoriaProdCod() {
        return categoriaProdCod;
    }

    public void setCategoriaProdCod(Integer categoriaProdCod) {
        this.categoriaProdCod = categoriaProdCod;
    }

    // =============== DAO ===============

    private static ProdutoDAO getDAO() {
        Conexao c = SingletonDB.getConexao();
        return new ProdutoDAO(c);
    }

    // =============== Buscas ===============

    public static Produto buscarPorId(Integer id) {
        return getDAO().findById(id);
    }

    public static List<Produto> listarTodos() {
        return getDAO().findAll();
    }

    public static List<Produto> buscarPorDescricao(String termo) {
        return getDAO().searchByDescricao(termo);
    }

    public static List<Produto> buscarPorCategoriaEDescricao(Integer catCod, String termo) {
        return getDAO().searchByCategoriaAndDescricao(catCod, termo);
    }
}
