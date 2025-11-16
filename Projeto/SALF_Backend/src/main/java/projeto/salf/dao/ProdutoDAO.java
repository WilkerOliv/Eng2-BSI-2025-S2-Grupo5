package projeto.salf.dao;

import projeto.salf.model.Produto;
import projeto.salf.controller.bd.Conexao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProdutoDAO {
    private final Conexao conexao;

    public ProdutoDAO(Conexao conexao) {
        this.conexao = conexao;
    }

    public List<Produto> findAll() {
        String sql = "select prod_cod, prod_descr, categoria_produto_cat_cod from produto";
        List<Produto> lista = new ArrayList<>();
        for (Map<String,Object> r : conexao.consultar(sql)) {
            Produto p = new Produto();
            p.setProdCod((Integer) r.get("prod_cod"));
            p.setProdDescr((String) r.get("prod_descr"));
            p.setCategoriaProdCod((Integer) r.get("categoria_produto_cat_cod"));
            lista.add(p);
        }
        return lista;
    }

    public Produto findById(Integer id) {
        String sql = "select prod_cod, prod_descr, categoria_produto_cat_cod from produto where prod_cod = ?";
        var rows = conexao.consultar(sql, id);
        if (rows.isEmpty()) return null;
        Map<String,Object> r = rows.get(0);
        Produto p = new Produto();
        p.setProdCod((Integer) r.get("prod_cod"));
        p.setProdDescr((String) r.get("prod_descr"));
        p.setCategoriaProdCod((Integer) r.get("categoria_produto_cat_cod"));
        return p;
    }

    public boolean save(Produto p) {
        if (p.getProdCod() == null) {
            String sql = "insert into produto(prod_descr, categoria_produto_cat_cod) values (?, ?)";
            return conexao.manipular(sql, p.getProdDescr(), p.getCategoriaProdCod());
        } else {
            String sql = "update produto set prod_descr = ?, categoria_produto_cat_cod = ? where prod_cod = ?";
            return conexao.manipular(sql, p.getProdDescr(), p.getCategoriaProdCod(), p.getProdCod());
        }
    }

    public boolean deleteById(Integer id) {
        String sql = "delete from produto where prod_cod = ?";
        return conexao.manipular(sql, id);
    }
}
