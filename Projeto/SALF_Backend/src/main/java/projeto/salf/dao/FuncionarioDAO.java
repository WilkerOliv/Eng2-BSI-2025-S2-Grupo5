package projeto.salf.dao;

import projeto.salf.controller.bd.Conexao;
import projeto.salf.model.Funcionario;

import java.util.List;
import java.util.Map;

public class FuncionarioDAO {

    private final Conexao conexao;

    public FuncionarioDAO(Conexao conexao) {
        this.conexao = conexao;
    }

    public List<Map<String, Object>> listarTodos() {
        String sql = """
            SELECT func_cpf,
                   func_nome,
                   func_senha,
                   func_email,
                   func_telefone,
                   tipo_acesso,
                   data_admissao,
                   data_demissao,
                   rua,
                   bairro,
                   cidade,
                   username,
                   uf,
                   cep,
                   cargo
              FROM funcionario
             ORDER BY func_nome
        """;
        return conexao.consultar(sql);
    }

    public Map<String, Object> buscarPorCpf(String cpf) {
        String sql = """
            SELECT func_cpf,
                   func_nome,
                   func_senha,
                   func_email,
                   func_telefone,
                   tipo_acesso,
                   data_admissao,
                   data_demissao,
                   rua,
                   bairro,
                   cidade,
                   username,
                   uf,
                   cep,
                   cargo
              FROM funcionario
             WHERE func_cpf = ?
        """;
        List<Map<String, Object>> res = conexao.consultar(sql, cpf);
        return res.isEmpty() ? null : res.get(0);
    }

    public boolean inserir(Funcionario f) {
        String sql = """
            INSERT INTO funcionario
            (func_cpf, func_nome, func_senha, func_email, func_telefone,
             tipo_acesso, data_admissao, data_demissao,
             rua, bairro, cidade, username, uf, cep, cargo)
            VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
        """;

        return conexao.manipular(sql,
                f.getFuncCpf(),
                f.getFuncNome(),
                f.getFuncSenha(),
                f.getFuncEmail(),
                f.getFuncTelefone(),
                f.getTipoAcesso(),
                f.getDataAdmissao(),
                f.getDataDemissao(),
                f.getRua(),
                f.getBairro(),
                f.getCidade(),
                f.getUsername(),
                f.getUf(),
                f.getCep(),
                f.getCargo()
        );
    }

    public boolean atualizar(Funcionario f) {
        String sql = """
            UPDATE funcionario SET
                func_nome      = ?,
                func_senha     = ?,
                func_email     = ?,
                func_telefone  = ?,
                tipo_acesso    = ?,
                data_admissao  = ?,
                data_demissao  = ?,
                rua            = ?,
                bairro         = ?,
                cidade         = ?,
                username       = ?,
                uf             = ?,
                cep            = ?,
                cargo          = ?
             WHERE func_cpf     = ?
        """;

        return conexao.manipular(sql,
                f.getFuncNome(),
                f.getFuncSenha(),
                f.getFuncEmail(),
                f.getFuncTelefone(),
                f.getTipoAcesso(),
                f.getDataAdmissao(),
                f.getDataDemissao(),
                f.getRua(),
                f.getBairro(),
                f.getCidade(),
                f.getUsername(),
                f.getUf(),
                f.getCep(),
                f.getCargo(),
                f.getFuncCpf()
        );
    }

    public boolean excluir(String cpf) {
        String sql = "DELETE FROM funcionario WHERE func_cpf = ?";
        return conexao.manipular(sql, cpf);
    }

    public Funcionario buscarPorEmail(String email) {

        String sql = """
        SELECT func_cpf,
               func_nome,
               func_senha,
               func_email,
               func_telefone,
               tipo_acesso,
               data_admissao,
               data_demissao,
               rua,
               bairro,
               cidade,
               username,
               uf,
               cep,
               cargo
          FROM funcionario
         WHERE LOWER(func_email) = LOWER(?)
    """;

        List<Map<String, Object>> res = conexao.consultar(sql, email);

        if (res.isEmpty()) return null;

        Map<String, Object> row = res.get(0);
        Funcionario f = new Funcionario();

        f.setFuncCpf((String) row.get("func_cpf"));
        f.setFuncNome((String) row.get("func_nome"));
        f.setFuncSenha((String) row.get("func_senha"));
        f.setFuncEmail((String) row.get("func_email"));
        f.setFuncTelefone((String) row.get("func_telefone"));
        f.setTipoAcesso((Integer) row.get("tipo_acesso"));

        f.setDataAdmissao((java.sql.Date) row.get("data_admissao"));
        f.setDataDemissao((java.sql.Date) row.get("data_demissao"));

        f.setRua((String) row.get("rua"));
        f.setBairro((String) row.get("bairro"));
        f.setCidade((String) row.get("cidade"));
        f.setUsername((String) row.get("username"));
        f.setUf((String) row.get("uf"));
        f.setCep((String) row.get("cep"));
        f.setCargo((String) row.get("cargo"));

        return f;
    }

    public Funcionario buscarPorUsername(String username) {

        String sql = """
            SELECT func_cpf,
                   func_nome,
                   func_senha,
                   func_email,
                   func_telefone,
                   tipo_acesso,
                   data_admissao,
                   data_demissao,
                   rua,
                   bairro,
                   cidade,
                   username,
                   uf,
                   cep,
                   cargo
              FROM funcionario
             WHERE LOWER(username) = LOWER(?)
        """;

        List<Map<String, Object>> res = conexao.consultar(sql, username);

        if (res.isEmpty()) return null;

        Map<String, Object> row = res.get(0);
        Funcionario f = new Funcionario();

        f.setFuncCpf((String) row.get("func_cpf"));
        f.setFuncNome((String) row.get("func_nome"));
        f.setFuncSenha((String) row.get("func_senha"));
        f.setFuncEmail((String) row.get("func_email"));
        f.setFuncTelefone((String) row.get("func_telefone"));
        if (row.get("tipo_acesso") instanceof Number n) {
            f.setTipoAcesso(n.intValue());
        }
        f.setRua((String) row.get("rua"));
        f.setBairro((String) row.get("bairro"));
        f.setCidade((String) row.get("cidade"));
        f.setUsername((String) row.get("username"));
        f.setUf((String) row.get("uf"));
        f.setCep((String) row.get("cep"));
        f.setCargo((String) row.get("cargo"));

        return f;
    }

    public int contarAdmins() {
        String sql = "SELECT COUNT(*) AS qtd FROM funcionario WHERE tipo_acesso = 1";
        List<Map<String, Object>> res = conexao.consultar(sql);
        if (res.isEmpty()) return 0;

        Object v = res.get(0).get("qtd");
        if (v instanceof Number n) {
            return n.intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(v));
        } catch (Exception e) {
            return 0;
        }
    }
}
