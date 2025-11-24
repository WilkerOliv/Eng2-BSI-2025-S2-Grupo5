package projeto.salf.dao;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.model.Produto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProdutoDAO {

    public List<Produto> findAll(Conexao c) {
        String sql = """
            SELECT p.prod_cod, p.prod_descr, p.categoria_produto_cat_cod,
                   cat.cat_descr AS categoria
            FROM produto p
            JOIN categoria_produto cat
                ON cat.cat_cod = p.categoria_produto_cat_cod
            ORDER BY p.prod_descr;
        """;

        List<Produto> lista = new ArrayList<>();
        for (Map<String,Object> r : c.consultar(sql)) {

            Produto p = new Produto();
            p.setProdCod((Integer) r.get("prod_cod"));
            p.setProdDescr((String) r.get("prod_descr"));
            p.setCategoriaCod((Integer) r.get("categoria_produto_cat_cod"));
            p.setCategoriaDescr((String) r.get("categoria"));

            lista.add(p);
        }
        return lista;
    }

    public Produto findById(Integer id, Conexao c) {
        String sql = """
            SELECT p.prod_cod, p.prod_descr, p.categoria_produto_cat_cod,
                   cat.cat_descr AS categoria
            FROM produto p
            JOIN categoria_produto cat
                ON cat.cat_cod = p.categoria_produto_cat_cod
            WHERE prod_cod = ?
        """;

        List<Map<String,Object>> res = c.consultar(sql, id);
        if (res.isEmpty()) return null;

        Map<String,Object> r = res.get(0);
        Produto p = new Produto();
        p.setProdCod(id);
        p.setProdDescr((String) r.get("prod_descr"));
        p.setCategoriaCod((Integer) r.get("categoria_produto_cat_cod"));
        p.setCategoriaDescr((String) r.get("categoria"));

        return p;
    }

    public boolean save(Produto p, Conexao c) {
        if (p.getProdCod() == null) {
            // inserir
            return c.manipular("""
                INSERT INTO produto (prod_descr, categoria_produto_cat_cod)
                VALUES (?, ?)
            """, p.getProdDescr(), p.getCategoriaCod());
        } else {
            // atualizar
            return c.manipular("""
                UPDATE produto
                SET prod_descr = ?, categoria_produto_cat_cod = ?
                WHERE prod_cod = ?
            """, p.getProdDescr(), p.getCategoriaCod(), p.getProdCod());
        }
    }

    public boolean delete(Integer id, Conexao c) {
        return c.manipular("DELETE FROM produto WHERE prod_cod = ?", id);
    }
}
