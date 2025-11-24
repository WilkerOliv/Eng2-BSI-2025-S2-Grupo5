package projeto.salf.model;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.dao.ProdutoDAO;

import java.util.List;

public class Produto {

    private Integer prodCod;
    private String prodDescr;
    private Integer categoriaCod;
    private String categoriaDescr;

    // GETTERS & SETTERS
    public Integer getProdCod() { return prodCod; }
    public void setProdCod(Integer prodCod) { this.prodCod = prodCod; }

    public String getProdDescr() { return prodDescr; }
    public void setProdDescr(String prodDescr) { this.prodDescr = prodDescr; }

    public Integer getCategoriaCod() { return categoriaCod; }
    public void setCategoriaCod(Integer categoriaCod) { this.categoriaCod = categoriaCod; }

    public String getCategoriaDescr() { return categoriaDescr; }
    public void setCategoriaDescr(String categoriaDescr) { this.categoriaDescr = categoriaDescr; }


    // ============ ACESSO AO DAO ============
    private static ProdutoDAO getDAO() {
        return new ProdutoDAO();
    }

    public static List<Produto> listarTodos(Conexao c) {
        return getDAO().findAll(c);
    }

    public static Produto buscarPorId(Integer id, Conexao c) {
        return getDAO().findById(id, c);
    }

    public boolean salvar(Conexao c) {
        if (prodDescr == null || prodDescr.trim().isEmpty())
            throw new IllegalArgumentException("Descrição é obrigatória");

        if (categoriaCod == null)
            throw new IllegalArgumentException("Categoria é obrigatória");

        prodDescr = prodDescr.trim().toUpperCase();
        return getDAO().save(this, c);
    }

    public static boolean excluir(Integer id, Conexao c) {
        return getDAO().delete(id, c);
    }
}
