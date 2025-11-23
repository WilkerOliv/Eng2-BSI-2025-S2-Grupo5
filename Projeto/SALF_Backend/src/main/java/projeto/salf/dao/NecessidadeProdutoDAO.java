package projeto.salf.dao;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.model.NecessidadeProduto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NecessidadeProdutoDAO {


    public NecessidadeProdutoDAO() {
    }

    public Integer criarLista(NecessidadeProduto.ListaDTO dto, Conexao c) {

        Integer necId = ((Number) c.consultarValorUnico(
                "SELECT nextval('seq_nec_id')")).intValue();

        for (NecessidadeProduto.ItemDTO item : dto.itens) {
            c.manipular("""
                INSERT INTO necessidade_produto
                (nec_id, pessoa_carente_pc_cpf, data, observacao, produto_prod_cod, quantidade)
                VALUES (?, ?, ?, ?, ?, ?)
            """, necId, dto.cpf, Date.valueOf(dto.data), dto.observacao,
                    item.produtoCod, item.quantidade);
        }

        return necId;
    }

    public List<NecessidadeProduto> listarAgrupado(Conexao c) {

        String sql = """
            SELECT DISTINCT nec_id, pessoa_carente_pc_cpf, data, observacao,
                   pc.pc_nome
            FROM necessidade_produto n
            JOIN pessoa_carente pc ON pc.pc_cpf = n.pessoa_carente_pc_cpf
            ORDER BY data DESC;
        """;

        List<NecessidadeProduto> lista = new ArrayList<>();

        for (Map<String,Object> row : c.consultar(sql)) {
            NecessidadeProduto n = new NecessidadeProduto();
            n.setNecId((Integer) row.get("nec_id"));
            n.setPessoaCpf((String) row.get("pessoa_carente_pc_cpf"));
            n.setPessoaNome((String) row.get("pc_nome"));

            var data = row.get("data");
            if (data instanceof java.sql.Date d) n.setData(d.toLocalDate());

            n.setObservacao((String) row.get("observacao"));
            lista.add(n);
        }

        return lista;
    }

    public List<NecessidadeProduto> listarItens(Integer necId, Conexao c) {

        String sql = """
        SELECT n.produto_prod_cod,
               p.prod_descr,
               n.quantidade,
               c.cat_descr AS categoria,
               n.data,
               n.observacao
        FROM necessidade_produto n
        JOIN produto p
          ON p.prod_cod = n.produto_prod_cod
        JOIN categoria_produto c
          ON c.cat_cod = p.categoria_produto_cat_cod
        WHERE n.nec_id = ?
        ORDER BY p.prod_descr
    """;

        List<NecessidadeProduto> lista = new ArrayList<>();

        for (Map<String,Object> row : c.consultar(sql, necId)) {
            NecessidadeProduto n = new NecessidadeProduto();
            n.setProdutoCod((Integer) row.get("produto_prod_cod"));
            n.setProdutoDescr((String) row.get("prod_descr"));
            n.setQuantidade((Integer) row.get("quantidade"));
            n.setCategoriaNome((String) row.get("categoria")); // alias da coluna
            lista.add(n);
        }

        return lista;
    }


    public void atualizarLista(Integer necId, NecessidadeProduto.ListaDTO dto, Conexao c) {

        // limpa itens antigos
        c.manipular("DELETE FROM necessidade_produto WHERE nec_id = ?", necId);

        // insere itens novos
        for (NecessidadeProduto.ItemDTO item : dto.itens) {
            c.manipular("""
                INSERT INTO necessidade_produto
                (nec_id, pessoa_carente_pc_cpf, data, observacao, produto_prod_cod, quantidade)
                VALUES (?, ?, ?, ?, ?, ?)
            """, necId, dto.cpf, Date.valueOf(dto.data), dto.observacao,
                    item.produtoCod, item.quantidade);
        }
    }

    public void excluirLista(Integer necId, Conexao c) {
        c.manipular("DELETE FROM necessidade_produto WHERE nec_id = ?", necId);
    }

    public void excluirItem(Integer necId, Integer produtoCod, Conexao c) {
        c.manipular("""
            DELETE FROM necessidade_produto
            WHERE nec_id = ? AND produto_prod_cod = ?
        """, necId, produtoCod);
    }
}
