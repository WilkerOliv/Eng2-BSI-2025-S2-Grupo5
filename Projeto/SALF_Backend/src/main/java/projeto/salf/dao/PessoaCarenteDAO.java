package projeto.salf.dao;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.model.PessoaCarente;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PessoaCarenteDAO {

    private final Conexao conexao;

    public PessoaCarenteDAO(Conexao conexao) {
        this.conexao = conexao;
    }

    public List<PessoaCarente> findAll() {
        String sql = "select pc_cpf, pc_nome, pc_telefone " +
                "from pessoa_carente " +
                "order by pc_nome";
        List<PessoaCarente> lista = new ArrayList<>();
        for (Map<String, Object> row : conexao.consultar(sql)) {
            lista.add(mapRow(row));
        }
        return lista;
    }

    public PessoaCarente findByCpf(String cpf) {
        String sql = "select pc_cpf, pc_nome, pc_telefone " +
                "from pessoa_carente where pc_cpf = ?";
        List<Map<String, Object>> res = conexao.consultar(sql, cpf);
        if (res.isEmpty()) return null;
        return mapRow(res.get(0));
    }

    public List<PessoaCarente> searchByCpfOrNome(String termo) {
        String like = "%" + termo + "%";
        String sql = "select pc_cpf, pc_nome, pc_telefone " +
                "from pessoa_carente " +
                "where pc_cpf ILIKE ? or pc_nome ILIKE ? " +
                "order by pc_nome";
        List<PessoaCarente> lista = new ArrayList<>();
        for (Map<String, Object> row : conexao.consultar(sql, like, like)) {
            lista.add(mapRow(row));
        }
        return lista;
    }

    private PessoaCarente mapRow(Map<String, Object> row) {
        PessoaCarente p = new PessoaCarente();
        p.setPcCpf((String) row.get("pc_cpf"));
        p.setPcNome((String) row.get("pc_nome"));
        p.setPcTelefone((String) row.get("pc_telefone"));
        return p;
    }
}
