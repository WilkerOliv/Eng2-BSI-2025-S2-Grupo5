package projeto.salf.dao;

import projeto.salf.model.ListaCompra;
import projeto.salf.controller.bd.Conexao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListaCompraDAO {
    private final Conexao conexao;

    public ListaCompraDAO(Conexao conexao) {
        this.conexao = conexao;
    }

    public List<ListaCompra> findAll() {
        String sql = "select lc_cod, funcionario_func_cpf, data_criacao, descricao, status_atendimento from lista_compra";
        List<ListaCompra> lista = new ArrayList<>();
        for (Map<String,Object> r : conexao.consultar(sql)) {
            ListaCompra l = new ListaCompra();
            l.setLcCod((Integer) r.get("lc_cod"));
            l.setFuncionarioCpf((String) r.get("funcionario_func_cpf"));
            Date d = (Date) r.get("data_criacao");
            if (d != null) l.setDataCriacao(d.toLocalDate());
            l.setDescricao((String) r.get("descricao"));
            l.setStatusAtendimento((Integer) r.get("status_atendimento"));
            lista.add(l);
        }
        return lista;
    }

    public ListaCompra findById(Integer id) {
        String sql = "select lc_cod, funcionario_func_cpf, data_criacao, descricao, status_atendimento from lista_compra where lc_cod = ?";
        var rows = conexao.consultar(sql, id);
        if (rows.isEmpty()) return null;
        Map<String,Object> r = rows.get(0);
        ListaCompra l = new ListaCompra();
        l.setLcCod((Integer) r.get("lc_cod"));
        l.setFuncionarioCpf((String) r.get("funcionario_func_cpf"));
        Date d = (Date) r.get("data_criacao");
        if (d != null) l.setDataCriacao(d.toLocalDate());
        l.setDescricao((String) r.get("descricao"));
        l.setStatusAtendimento((Integer) r.get("status_atendimento"));
        return l;
    }

    public boolean save(ListaCompra l) {
        if (l.getLcCod() == null) {
            String sql = "insert into lista_compra(funcionario_func_cpf, data_criacao, descricao, status_atendimento) values (?, ?, ?, ?)";
            return conexao.manipular(sql, l.getFuncionarioCpf(), Date.valueOf(l.getDataCriacao()), l.getDescricao(), l.getStatusAtendimento());
        } else {
            String sql = "update lista_compra set funcionario_func_cpf = ?, data_criacao = ?, descricao = ?, status_atendimento = ? where lc_cod = ?";
            return conexao.manipular(sql, l.getFuncionarioCpf(), Date.valueOf(l.getDataCriacao()), l.getDescricao(), l.getStatusAtendimento(), l.getLcCod());
        }
    }

    public boolean deleteById(Integer id) {
        String sql = "delete from lista_compra where lc_cod = ?";
        return conexao.manipular(sql, id);
    }
}
