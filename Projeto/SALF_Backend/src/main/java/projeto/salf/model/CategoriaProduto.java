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
        return new CategoriaProdutoDAO();
    }

    public static CategoriaProduto buscarPorId(Integer id, Conexao c) {
        return getDAO().findById(id, c);
    }

    public static java.util.List<CategoriaProduto> listarTodas(Conexao c) {
        return getDAO().findAll(c);
    }

    public boolean salvar(Conexao c) {
        if (catDescr == null || catDescr.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição da categoria é obrigatória.");
        }
        catDescr = catDescr.trim().toUpperCase();
        return getDAO().save(this, c);
    }

    public static boolean excluir(Integer id, Conexao c) {
        return getDAO().deleteById(id, c);
    }
}
