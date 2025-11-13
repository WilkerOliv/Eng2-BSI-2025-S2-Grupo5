package projeto.salf.model;

import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.controller.bd.Conexao;
import projeto.salf.dao.CategoriaProdutoDAO;

public class CategoriaProduto {

    private Integer catCod;
    private String catDescr;

    public Integer getCatCod() { return catCod; }
    public void setCatCod(Integer catCod) { this.catCod = catCod; }

    public String getCatDescr() { return catDescr; }
    public void setCatDescr(String catDescr) { this.catDescr = catDescr; }

    // ===================== ACESSO A DADOS =====================

    private static CategoriaProdutoDAO getDAO() {
        Conexao c = SingletonDB.getConexao();
        return new CategoriaProdutoDAO(c);
    }

    public static CategoriaProduto buscarPorId(Integer id) {
        return getDAO().findById(id);
    }

    public static java.util.List<CategoriaProduto> listarTodas() {
        return getDAO().findAll();
    }

    public boolean salvar() {
        if (catDescr == null || catDescr.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição da categoria é obrigatória.");
        }
        catDescr = catDescr.trim().toUpperCase();
        return getDAO().save(this);
    }

    public static boolean excluir(Integer id) {
        return getDAO().deleteById(id);
    }
}
