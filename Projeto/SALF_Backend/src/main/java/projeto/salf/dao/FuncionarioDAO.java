package projeto.salf.dao;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.model.Funcionario;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DAO de Funcionário.
 * NÃO abre conexão; apenas usa a Conexao recebida.
 */
public class FuncionarioDAO {

    private final Conexao conexao;

    public FuncionarioDAO(Conexao conexao) {
        this.conexao = conexao;
    }

    public List<Funcionario> findAll() {
        String sql = "select func_cpf, func_nome, func_email, func_telefone " +
                "from funcionario " +
                "order by func_nome";
        List<Funcionario> lista = new ArrayList<>();
        for (Map<String, Object> row : conexao.consultar(sql)) {
            lista.add(mapRow(row));
        }
        return lista;
    }

    public Funcionario findByCpf(String cpf) {
        String sql = "select func_cpf, func_nome, func_email, func_telefone " +
                "from funcionario where func_cpf = ?";
        List<Map<String, Object>> res = conexao.consultar(sql, cpf);
        if (res.isEmpty()) return null;
        return mapRow(res.get(0));
    }

    public List<Funcionario> searchByCpfOrNome(String termo) {
        String like = "%" + termo + "%";
        String sql = "select func_cpf, func_nome, func_email, func_telefone " +
                "from funcionario " +
                "where func_cpf ILIKE ? or func_nome ILIKE ? " +
                "order by func_nome";
        List<Funcionario> lista = new ArrayList<>();
        for (Map<String, Object> row : conexao.consultar(sql, like, like)) {
            lista.add(mapRow(row));
        }
        return lista;
    }

    private Funcionario mapRow(Map<String, Object> row) {
        Funcionario f = new Funcionario();
        f.setFuncCpf((String) row.get("func_cpf"));
        f.setFuncNome((String) row.get("func_nome"));
        f.setFuncEmail((String) row.get("func_email"));
        f.setFuncTelefone((String) row.get("func_telefone"));
        return f;
    }
}
