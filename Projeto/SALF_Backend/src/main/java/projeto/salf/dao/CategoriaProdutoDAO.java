package projeto.salf.dao;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.model.CategoriaProduto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoriaProdutoDAO {
    private final Conexao conexao;

    public CategoriaProdutoDAO(Conexao conexao) {
        this.conexao = conexao;
    }

    public List<CategoriaProduto> findAll() {
        String sql = "SELECT cat_cod, cat_descr FROM categoria_produto";
        List<CategoriaProduto> lista = new ArrayList<>();

        for (Map<String, Object> r : conexao.consultar(sql)) {
            CategoriaProduto c = new CategoriaProduto();
            c.setCatCod((Integer) r.get("cat_cod"));
            c.setCatDescr((String) r.get("cat_descr"));
            lista.add(c);
        }
        return lista;
    }

    public boolean save(CategoriaProduto c) {
        if (c.getCatCod() == null) {
            String sql = "INSERT INTO categoria_produto (cat_descr) VALUES (?)";
            return conexao.manipular(sql, c.getCatDescr());
        } else {
            String sql = "UPDATE categoria_produto SET cat_descr = ? WHERE cat_cod = ?";
            return conexao.manipular(sql, c.getCatDescr(), c.getCatCod());
        }
    }

    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM categoria_produto WHERE cat_cod = ?";
        return conexao.manipular(sql, id);
    }
}
