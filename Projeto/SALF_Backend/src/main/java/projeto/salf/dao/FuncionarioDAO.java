package projeto.salf.dao;

import projeto.salf.model.Funcionario;
import projeto.salf.controller.bd.Conexao;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FuncionarioDAO {
    private final Conexao conexao;

    public FuncionarioDAO(Conexao conexao) {
        this.conexao = conexao;
    }

    /**
     * Busca básica por email. Observação: usa a conexão 'raw' para prepared statements
     * porque a classe Conexao fornece accesso a Connection via getConnect().
     */
    public Funcionario buscaFuncEmail(String email) {
        String SQL = "SELECT func_nome, func_email, func_senha FROM funcionario WHERE func_email = ?";
        try (Connection con = conexao.getConnect();
             PreparedStatement stmt = con.prepareStatement(SQL)) {

            stmt.setString(1, email == null ? "" : email.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Funcionario func = new Funcionario();
                    func.setFuncNome(rs.getString("func_nome"));
                    func.setFuncEmail(rs.getString("func_email"));
                    func.setFuncSenha(rs.getString("func_senha"));
                    return func;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Funcionario> findAll() {
        String sql = "select func_cpf, func_nome, func_senha, func_email, func_telefone from funcionario";
        List<Funcionario> lista = new ArrayList<>();
        for (Map<String,Object> row : conexao.consultar(sql)) {
            Funcionario f = new Funcionario();
            f.setFuncCpf((String)row.get("func_cpf"));
            f.setFuncNome((String)row.get("func_nome"));
            f.setFuncSenha((String)row.get("func_senha"));
            f.setFuncEmail((String)row.get("func_email"));
            f.setFuncTelefone((String)row.get("func_telefone"));
            lista.add(f);
        }
        return lista;
    }

    public Funcionario findById(String cpf) {
        String sql = "select func_cpf, func_nome, func_senha, func_email, func_telefone from funcionario where func_cpf = ?";
        List<Map<String,Object>> res = conexao.consultar(sql, cpf);
        if (res.isEmpty()) return null;
        Map<String,Object> row = res.get(0);
        Funcionario f = new Funcionario();
        f.setFuncCpf((String)row.get("func_cpf"));
        f.setFuncNome((String)row.get("func_nome"));
        f.setFuncSenha((String)row.get("func_senha"));
        f.setFuncEmail((String)row.get("func_email"));
        f.setFuncTelefone((String)row.get("func_telefone"));
        return f;
    }

    public boolean save(Funcionario f) {
        if (findById(f.getFuncCpf()) == null) {
            String sql = "insert into funcionario(func_cpf, func_nome, func_senha, func_email, func_telefone) values (?,?,?,?,?)";
            return conexao.manipular(sql, f.getFuncCpf(), f.getFuncNome(), f.getFuncSenha(), f.getFuncEmail(), f.getFuncTelefone());
        } else {
            String sql = "update funcionario set func_nome=?, func_senha=?, func_email=?, func_telefone=? where func_cpf=?";
            return conexao.manipular(sql, f.getFuncNome(), f.getFuncSenha(), f.getFuncEmail(), f.getFuncTelefone(), f.getFuncCpf());
        }
    }

    public boolean deleteById(String cpf) {
        String sql = "delete from funcionario where func_cpf = ?";
        return conexao.manipular(sql, cpf);
    }
}
