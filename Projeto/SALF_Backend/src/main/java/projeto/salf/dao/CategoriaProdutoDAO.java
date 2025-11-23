package projeto.salf.dao;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.model.CategoriaProduto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoriaProdutoDAO {

    public CategoriaProdutoDAO() {
    }

    public List<CategoriaProduto> findAll(Conexao conexao) {
        String sql = "select cat_cod, cat_descr from categoria_produto order by cat_descr";
        List<CategoriaProduto> lista = new ArrayList<>();
        for (Map<String, Object> r : conexao.consultar(sql)) {
            CategoriaProduto c = new CategoriaProduto();
            c.setCatCod((Integer) r.get("cat_cod"));
            c.setCatDescr((String) r.get("cat_descr"));
            lista.add(c);
        }
        return lista;
    }

    public CategoriaProduto findById(Integer id,  Conexao conexao) {
        String sql = "select cat_cod, cat_descr from categoria_produto where cat_cod = ?";
        List<Map<String, Object>> res = conexao.consultar(sql, id);
        if (res.isEmpty()) return null;
        Map<String, Object> r = res.get(0);
        CategoriaProduto c = new CategoriaProduto();
        c.setCatCod((Integer) r.get("cat_cod"));
        c.setCatDescr((String) r.get("cat_descr"));
        return c;
    }

    public boolean save(CategoriaProduto c,  Conexao conexao) {
        if (c.getCatCod() == null) {
            String sql = "insert into categoria_produto(cat_descr) values (?)";
            return conexao.manipular(sql, c.getCatDescr());
        } else {
            String sql = "update categoria_produto set cat_descr = ? where cat_cod = ?";
            return conexao.manipular(sql, c.getCatDescr(), c.getCatCod());
        }
    }

    public boolean deleteById(Integer id,  Conexao conexao) {
        String sql = "delete from categoria_produto where cat_cod = ?";
        return conexao.manipular(sql, id);
    }
}
