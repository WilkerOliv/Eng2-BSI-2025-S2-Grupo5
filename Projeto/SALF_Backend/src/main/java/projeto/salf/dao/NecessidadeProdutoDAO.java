package projeto.salf.dao;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.model.NecessidadeProduto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NecessidadeProdutoDAO {

    private final Conexao conexao;

    public NecessidadeProdutoDAO(Conexao conexao) {
        this.conexao = conexao;
    }

    //  Lista todas as necessidades, com nome da pessoa e descrição do produto.
    public List<NecessidadeProduto> findAll() {
        String sql =
                "select n.pessoa_carente_pc_cpf, n.produto_prod_cod, n.data, " +
                        "       n.quantidade, n.observacao, " +
                        "       pc.pc_nome, " +
                        "       p.prod_descr, p.categoria_produto_cat_cod " +
                        "  from necessidade_produto n " +
                        "  join pessoa_carente pc on pc.pc_cpf = n.pessoa_carente_pc_cpf " +
                        "  join produto p on p.prod_cod = n.produto_prod_cod " +
                        " order by n.data desc, pc.pc_nome";

        List<NecessidadeProduto> lista = new ArrayList<>();
        for (Map<String, Object> row : conexao.consultar(sql)) {
            lista.add(mapRow(row));
        }
        return lista;
    }

    // Lista necessidades por CPF
    public List<NecessidadeProduto> findByPessoa(String cpf) {
        String sql =
                "select n.pessoa_carente_pc_cpf, n.produto_prod_cod, n.data, " +
                        "       n.quantidade, n.observacao, " +
                        "       pc.pc_nome, " +
                        "       p.prod_descr, p.categoria_produto_cat_cod " +
                        "  from necessidade_produto n " +
                        "  join pessoa_carente pc on pc.pc_cpf = n.pessoa_carente_pc_cpf " +
                        "  join produto p on p.prod_cod = n.produto_prod_cod " +
                        " where n.pessoa_carente_pc_cpf = ? " +
                        " order by n.data desc, p.prod_descr";

        List<NecessidadeProduto> lista = new ArrayList<>();
        for (Map<String, Object> row : conexao.consultar(sql, cpf)) {
            lista.add(mapRow(row));
        }
        return lista;
    }

    // Busca por termo aplicado a:
    public List<NecessidadeProduto> search(String termo) {
        String like = "%" + termo + "%";
        String sql =
                "select n.pessoa_carente_pc_cpf, n.produto_prod_cod, n.data, " +
                        "       n.quantidade, n.observacao, " +
                        "       pc.pc_nome, " +
                        "       p.prod_descr, p.categoria_produto_cat_cod " +
                        "  from necessidade_produto n " +
                        "  join pessoa_carente pc on pc.pc_cpf = n.pessoa_carente_pc_cpf " +
                        "  join produto p on p.prod_cod = n.produto_prod_cod " +
                        " where n.pessoa_carente_pc_cpf ILIKE ? " +
                        "    or pc.pc_nome ILIKE ? " +
                        "    or p.prod_descr ILIKE ? " +
                        "    or n.observacao ILIKE ? " +
                        " order by n.data desc, pc.pc_nome";

        List<NecessidadeProduto> lista = new ArrayList<>();
        for (Map<String, Object> row : conexao.consultar(sql, like, like, like, like)) {
            lista.add(mapRow(row));
        }
        return lista;
    }


    public boolean save(NecessidadeProduto n) {
        String sqlVer =
                "select 1 from necessidade_produto " +
                        " where pessoa_carente_pc_cpf = ? " +
                        "   and produto_prod_cod = ?";

        boolean existe = conexao.existe(sqlVer,
                n.getPessoaCarentePcCpf(),
                n.getProdutoProdCod());

        if (!existe) {
            String sql =
                    "insert into necessidade_produto " +
                            "  (pessoa_carente_pc_cpf, produto_prod_cod, data, quantidade, observacao) " +
                            "values (?, ?, ?, ?, ?)";
            return conexao.manipular(sql,
                    n.getPessoaCarentePcCpf(),
                    n.getProdutoProdCod(),
                    Date.valueOf(n.getData()),
                    n.getQuantidade(),
                    n.getObservacao());
        } else {
            String sql =
                    "update necessidade_produto " +
                            "   set data = ?, quantidade = ?, observacao = ? " +
                            " where pessoa_carente_pc_cpf = ? " +
                            "   and produto_prod_cod = ?";
            return conexao.manipular(sql,
                    Date.valueOf(n.getData()),
                    n.getQuantidade(),
                    n.getObservacao(),
                    n.getPessoaCarentePcCpf(),
                    n.getProdutoProdCod());
        }
    }

    public boolean delete(String cpf, Integer prodCod) {
        String sql =
                "delete from necessidade_produto " +
                        " where pessoa_carente_pc_cpf = ? " +
                        "   and produto_prod_cod = ?";
        return conexao.manipular(sql, cpf, prodCod);
    }

    private NecessidadeProduto mapRow(Map<String, Object> row) {
        NecessidadeProduto n = new NecessidadeProduto();
        n.setPessoaCarentePcCpf((String) row.get("pessoa_carente_pc_cpf"));
        n.setProdutoProdCod((Integer) row.get("produto_prod_cod"));

        Object data = row.get("data");
        if (data instanceof java.sql.Date) {
            n.setData(((java.sql.Date) data).toLocalDate());
        } else if (data instanceof java.time.LocalDate) {
            n.setData((java.time.LocalDate) data);
        }

        n.setQuantidade((Integer) row.get("quantidade"));
        n.setObservacao((String) row.get("observacao"));
        n.setPessoaNome((String) row.get("pc_nome"));
        n.setProdutoDescr((String) row.get("prod_descr"));
        n.setCategoriaProdCod((Integer) row.get("categoria_produto_cat_cod"));
        return n;
    }
}
