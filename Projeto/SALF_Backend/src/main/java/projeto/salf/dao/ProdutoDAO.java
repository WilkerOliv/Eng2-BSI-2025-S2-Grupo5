package projeto.salf.dao;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.model.Produto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProdutoDAO {

    private final Conexao conexao;

    public ProdutoDAO(Conexao conexao) {
        this.conexao = conexao;
    }

    public List<Produto> findAll() {
        String sql = "select prod_cod, prod_descr, categoria_produto_cat_cod " +
                "from produto " +
                "order by prod_descr";
        List<Produto> lista = new ArrayList<>();
        for (Map<String, Object> row : conexao.consultar(sql)) {
            lista.add(mapRow(row));
        }
        return lista;
    }

    public Produto findById(Integer id) {
        String sql = "select prod_cod, prod_descr, categoria_produto_cat_cod " +
                "from produto where prod_cod = ?";
        List<Map<String, Object>> res = conexao.consultar(sql, id);
        if (res.isEmpty()) return null;
        return mapRow(res.get(0));
    }

    public List<Produto> searchByDescricao(String termo) {
        String like = "%" + termo + "%";
        String sql = "select prod_cod, prod_descr, categoria_produto_cat_cod " +
                "from produto " +
                "where prod_descr ILIKE ? " +
                "order by prod_descr";
        List<Produto> lista = new ArrayList<>();
        for (Map<String, Object> row : conexao.consultar(sql, like)) {
            lista.add(mapRow(row));
        }
        return lista;
    }

    public List<Produto> searchByCategoriaAndDescricao(Integer catCod, String termo) {
        String sql;
        List<Map<String, Object>> res;

        if (termo == null || termo.trim().isEmpty()) {
            sql = "select prod_cod, prod_descr, categoria_produto_cat_cod " +
                    "from produto " +
                    "where categoria_produto_cat_cod = ? " +
                    "order by prod_descr";
            res = conexao.consultar(sql, catCod);
        } else {
            String like = "%" + termo + "%";
            sql = "select prod_cod, prod_descr, categoria_produto_cat_cod " +
                    "from produto " +
                    "where categoria_produto_cat_cod = ? " +
                    "  and prod_descr ILIKE ? " +
                    "order by prod_descr";
            res = conexao.consultar(sql, catCod, like);
        }

        List<Produto> lista = new ArrayList<>();
        for (Map<String, Object> row : res) {
            lista.add(mapRow(row));
        }
        return lista;
    }

    private Produto mapRow(Map<String, Object> row) {
        Produto p = new Produto();
        p.setProdCod((Integer) row.get("prod_cod"));
        p.setProdDescr((String) row.get("prod_descr"));
        p.setCategoriaProdCod((Integer) row.get("categoria_produto_cat_cod"));
        return p;
    }
}
