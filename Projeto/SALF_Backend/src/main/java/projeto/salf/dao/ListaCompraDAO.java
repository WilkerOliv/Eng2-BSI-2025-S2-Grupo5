package projeto.salf.dao;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.model.ListaCompra;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListaCompraDAO {


    public ListaCompraDAO() {
    }

    public List<ListaCompra> findAll(Conexao conexao) {
        String sql =
                "select lc.lc_cod, lc.funcionario_func_cpf, lc.data_criacao, " +
                        "       lc.descricao, lc.status_atendimento, f.func_nome " +
                        "  from lista_compra lc " +
                        "  join funcionario f on f.func_cpf = lc.funcionario_func_cpf " +
                        " order by lc.data_criacao desc, lc.lc_cod desc";

        List<ListaCompra> lista = new ArrayList<>();
        for (Map<String, Object> row : conexao.consultar(sql)) {
            lista.add(mapRow(row));
        }
        return lista;
    }

    public ListaCompra findById(Integer id, Conexao conexao) {
        String sql =
                "select lc.lc_cod, lc.funcionario_func_cpf, lc.data_criacao, " +
                        "       lc.descricao, lc.status_atendimento, f.func_nome " +
                        "  from lista_compra lc " +
                        "  join funcionario f on f.func_cpf = lc.funcionario_func_cpf " +
                        " where lc.lc_cod = ?";
        List<Map<String, Object>> res = conexao.consultar(sql, id);
        if (res.isEmpty()) return null;
        return mapRow(res.get(0));
    }

    public List<ListaCompra> searchByDescricao(String termo, Conexao conexao) {
        String like = "%" + termo + "%";
        String sql =
                "select lc.lc_cod, lc.funcionario_func_cpf, lc.data_criacao, " +
                        "       lc.descricao, lc.status_atendimento, f.func_nome " +
                        "  from lista_compra lc " +
                        "  join funcionario f on f.func_cpf = lc.funcionario_func_cpf " +
                        " where lc.descricao ILIKE ? " +
                        " order by lc.data_criacao desc, lc.lc_cod desc";
        List<ListaCompra> lista = new ArrayList<>();
        for (Map<String, Object> row : conexao.consultar(sql, like)) {
            lista.add(mapRow(row));
        }
        return lista;
    }

    public List<ListaCompra> searchByCpf(String termo, Conexao conexao) {
        String like = "%" + termo + "%";
        String sql =
                "select lc.lc_cod, lc.funcionario_func_cpf, lc.data_criacao, " +
                        "       lc.descricao, lc.status_atendimento, f.func_nome " +
                        "  from lista_compra lc " +
                        "  join funcionario f on f.func_cpf = lc.funcionario_func_cpf " +
                        " where lc.funcionario_func_cpf ILIKE ? " +
                        " order by lc.data_criacao desc, lc.lc_cod desc";
        List<ListaCompra> lista = new ArrayList<>();
        for (Map<String, Object> row : conexao.consultar(sql, like)) {
            lista.add(mapRow(row));
        }
        return lista;
    }

    public List<ListaCompra> searchByNomeFuncionario(String termo, Conexao conexao) {
        String like = "%" + termo + "%";
        String sql =
                "select lc.lc_cod, lc.funcionario_func_cpf, lc.data_criacao, " +
                        "       lc.descricao, lc.status_atendimento, f.func_nome " +
                        "  from lista_compra lc " +
                        "  join funcionario f on f.func_cpf = lc.funcionario_func_cpf " +
                        " where f.func_nome ILIKE ? " +
                        " order by lc.data_criacao desc, lc.lc_cod desc";
        List<ListaCompra> lista = new ArrayList<>();
        for (Map<String, Object> row : conexao.consultar(sql, like)) {
            lista.add(mapRow(row));
        }
        return lista;
    }

    public boolean save(ListaCompra l, Conexao conexao) {
        if (l.getLcCod() == null) {
            String sql =
                    "insert into lista_compra " +
                            "  (funcionario_func_cpf, data_criacao, descricao, status_atendimento) " +
                            "values (?, ?, ?, ?)";
            boolean ok = conexao.manipular(sql,
                    l.getFuncionarioFuncCpf(),
                    Date.valueOf(l.getDataCriacao()),
                    l.getDescricao(),
                    l.getStatusAtendimento());
            if (!ok) return false;

            // pega o id gerado
            Object val = conexao.consultarValorUnico(
                    "select currval(pg_get_serial_sequence('lista_compra', 'lc_cod'))");
            if (val instanceof Number) {
                l.setLcCod(((Number) val).intValue());
            }
            return true;
        } else {
            String sql =
                    "update lista_compra " +
                            "   set funcionario_func_cpf = ?, " +
                            "       data_criacao = ?, " +
                            "       descricao = ?, " +
                            "       status_atendimento = ? " +
                            " where lc_cod = ?";
            return conexao.manipular(sql,
                    l.getFuncionarioFuncCpf(),
                    Date.valueOf(l.getDataCriacao()),
                    l.getDescricao(),
                    l.getStatusAtendimento(),
                    l.getLcCod());
        }
    }

    public boolean deleteById(Integer id, Conexao conexao) {
        String sql = "delete from lista_compra where lc_cod = ?";
        return conexao.manipular(sql, id);
    }

    private ListaCompra mapRow(Map<String, Object> row) {
        ListaCompra l = new ListaCompra();
        l.setLcCod((Integer) row.get("lc_cod"));
        l.setFuncionarioFuncCpf((String) row.get("funcionario_func_cpf"));

        Object data = row.get("data_criacao");
        if (data instanceof java.sql.Date) {
            l.setDataCriacao(((java.sql.Date) data).toLocalDate());
        } else if (data instanceof java.time.LocalDate) {
            l.setDataCriacao((java.time.LocalDate) data);
        }

        l.setDescricao((String) row.get("descricao"));
        l.setStatusAtendimento((Integer) row.get("status_atendimento"));
        l.setFuncNome((String) row.get("func_nome"));
        return l;
    }
}
