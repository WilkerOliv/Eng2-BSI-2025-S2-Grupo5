package projeto.salf.dao;

import projeto.salf.model.NecessidadeProduto;
import projeto.salf.model.NecessidadeProdutoId;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.controller.bd.Conexao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NecessidadeProdutoDAO {
    private final Conexao conexao = SingletonDB.getConexao();

    public List<NecessidadeProduto> findAll() {
        String sql = "select pessoa_cpf as pessoa_cpf, produto_id as produto_id, data, quantidade, observacao from necessidade_produto";
        List<NecessidadeProduto> lista = new ArrayList<>();
        for (Map<String,Object> r : conexao.consultar(sql)) {
            NecessidadeProduto n = new NecessidadeProduto();
            n.setPessoaCpf((String) r.get("pessoa_cpf"));
            Object prodObj = r.get("produto_id");
            if (prodObj instanceof Integer) n.setProdutoId((Integer) prodObj);
            Date d = (Date) r.get("data");
            if (d != null) n.setData(d.toLocalDate());
            n.setQuantidade((Integer) r.get("quantidade"));
            n.setObservacao((String) r.get("observacao"));
            lista.add(n);
        }
        return lista;
    }

    public boolean save(NecessidadeProduto n) {
        // upsert: if exists -> update, else insert
        String existsSql = "select 1 from necessidade_produto where pessoa_cpf = ? and produto_id = ?";
        var ex = conexao.consultar(existsSql, n.getPessoaCpf(), n.getProdutoId());
        if (ex.isEmpty()) {
            String sql = "insert into necessidade_produto(pessoa_cpf, produto_id, data, quantidade, observacao) values (?, ?, ?, ?, ?)";
            return conexao.manipular(sql, n.getPessoaCpf(), n.getProdutoId(), Date.valueOf(n.getData()), n.getQuantidade(), n.getObservacao());
        } else {
            String sql = "update necessidade_produto set data = ?, quantidade = ?, observacao = ? where pessoa_cpf = ? and produto_id = ?";
            return conexao.manipular(sql, Date.valueOf(n.getData()), n.getQuantidade(), n.getObservacao(), n.getPessoaCpf(), n.getProdutoId());
        }
    }

    public boolean deleteById(NecessidadeProdutoId id) {
        String sql = "delete from necessidade_produto where pessoa_cpf = ? and produto_id = ?";
        return conexao.manipular(sql, id.getPessoaCpf(), id.getProdutoId());
    }
}
