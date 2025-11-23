package projeto.salf.dao;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.model.ItensDaLista;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItensDaListaDAO {

    public ItensDaListaDAO() {
    }

    // Lista itens de uma lista, com dados do produto.
    public List<ItensDaLista> findByLista(Integer lcCod, Conexao conexao) {
        String sql =
                "select i.produto_prod_cod, i.lista_compra_lc_cod, i.lista_compra_funcionario_func_cpf, " +
                        "       i.quantidade, p.prod_descr, p.categoria_produto_cat_cod " +
                        "  from itens_da_lista i " +
                        "  join produto p on p.prod_cod = i.produto_prod_cod " +
                        " where i.lista_compra_lc_cod = ? " +
                        " order by p.prod_descr";

        List<ItensDaLista> lista = new ArrayList<>();
        for (Map<String, Object> row : conexao.consultar(sql, lcCod)) {
            lista.add(mapRow(row));
        }
        return lista;
    }


    public boolean save(ItensDaLista item, Conexao conexao) {
        String sqlVer =
                "select 1 from itens_da_lista " +
                        " where produto_prod_cod = ? " +
                        "   and lista_compra_lc_cod = ? " +
                        "   and lista_compra_funcionario_func_cpf = ?";

        boolean existe = conexao.existe(sqlVer,
                item.getProdutoProdCod(),
                item.getListaCompraLcCod(),
                item.getListaCompraFuncionarioFuncCpf());

        if (!existe) {
            String sql =
                    "insert into itens_da_lista " +
                            "  (produto_prod_cod, lista_compra_lc_cod, lista_compra_funcionario_func_cpf, quantidade) " +
                            "values (?, ?, ?, ?)";
            return conexao.manipular(sql,
                    item.getProdutoProdCod(),
                    item.getListaCompraLcCod(),
                    item.getListaCompraFuncionarioFuncCpf(),
                    item.getQuantidade());
        } else {
            String sql =
                    "update itens_da_lista " +
                            "   set quantidade = ? " +
                            " where produto_prod_cod = ? " +
                            "   and lista_compra_lc_cod = ? " +
                            "   and lista_compra_funcionario_func_cpf = ?";
            return conexao.manipular(sql,
                    item.getQuantidade(),
                    item.getProdutoProdCod(),
                    item.getListaCompraLcCod(),
                    item.getListaCompraFuncionarioFuncCpf());
        }
    }

    public boolean deleteItem(Integer lcCod, Integer prodCod, Conexao conexao) {
        String sql =
                "delete from itens_da_lista " +
                        " where lista_compra_lc_cod = ? " +
                        "   and produto_prod_cod = ?";
        return conexao.manipular(sql, lcCod, prodCod);
    }

    public boolean deleteByLista(Integer lcCod, Conexao conexao) {
        String sql =
                "delete from itens_da_lista " +
                        " where lista_compra_lc_cod = ?";
        return conexao.manipular(sql, lcCod);
    }

    private ItensDaLista mapRow(Map<String, Object> row) {
        ItensDaLista i = new ItensDaLista();
        i.setProdutoProdCod((Integer) row.get("produto_prod_cod"));
        i.setListaCompraLcCod((Integer) row.get("lista_compra_lc_cod"));
        i.setListaCompraFuncionarioFuncCpf((String) row.get("lista_compra_funcionario_func_cpf"));
        i.setQuantidade((Integer) row.get("quantidade"));
        i.setProdutoDescr((String) row.get("prod_descr"));
        i.setCategoriaProdCod((Integer) row.get("categoria_produto_cat_cod"));
        return i;
    }
}
