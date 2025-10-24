package projeto.salf.dao;

import projeto.salf.model.PessoaCarente;
import projeto.salf.controller.bd.SingletonDB;
import projeto.salf.controller.bd.Conexao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PessoaCarenteDAO {
    private final Conexao conexao = SingletonDB.getConexao();

    public List<PessoaCarente> findAll() {
        String sql = "select pc_cpf, pc_nome, pc_data_nasc, pc_telefone, rua, bairro, cidade, uf, cep from pessoa_carente";
        List<PessoaCarente> lista = new ArrayList<>();
        for (Map<String,Object> r : conexao.consultar(sql)) {
            PessoaCarente p = new PessoaCarente();
            p.setPcCpf((String) r.get("pc_cpf"));
            p.setPcNome((String) r.get("pc_nome"));
            Date d = (Date) r.get("pc_data_nasc");
            if (d != null) p.setPcDataNasc(d.toLocalDate());
            p.setPcTelefone((String) r.get("pc_telefone"));
            p.setRua((String) r.get("rua"));
            p.setBairro((String) r.get("bairro"));
            p.setCidade((String) r.get("cidade"));
            p.setUf((String) r.get("uf"));
            p.setCep((String) r.get("cep"));
            lista.add(p);
        }
        return lista;
    }

    public PessoaCarente findById(String cpf) {
        String sql = "select pc_cpf, pc_nome, pc_data_nasc, pc_telefone, rua, bairro, cidade, uf, cep from pessoa_carente where pc_cpf = ?";
        var rows = conexao.consultar(sql, cpf);
        if (rows.isEmpty()) return null;
        Map<String,Object> r = rows.get(0);
        PessoaCarente p = new PessoaCarente();
        p.setPcCpf((String) r.get("pc_cpf"));
        p.setPcNome((String) r.get("pc_nome"));
        Date d = (Date) r.get("pc_data_nasc");
        if (d != null) p.setPcDataNasc(d.toLocalDate());
        p.setPcTelefone((String) r.get("pc_telefone"));
        p.setRua((String) r.get("rua"));
        p.setBairro((String) r.get("bairro"));
        p.setCidade((String) r.get("cidade"));
        p.setUf((String) r.get("uf"));
        p.setCep((String) r.get("cep"));
        return p;
    }

    public boolean save(PessoaCarente p) {
        if (findById(p.getPcCpf()) == null) {
            String sql = "insert into pessoa_carente(pc_cpf, pc_nome, pc_data_nasc, pc_telefone, rua, bairro, cidade, uf, cep) values (?,?,?,?,?,?,?,?,?)";
            return conexao.manipular(sql, p.getPcCpf(), p.getPcNome(), Date.valueOf(p.getPcDataNasc()), p.getPcTelefone(), p.getRua(), p.getBairro(), p.getCidade(), p.getUf(), p.getCep());
        } else {
            String sql = "update pessoa_carente set pc_nome=?, pc_data_nasc=?, pc_telefone=?, rua=?, bairro=?, cidade=?, uf=?, cep=? where pc_cpf=?";
            return conexao.manipular(sql, p.getPcNome(), Date.valueOf(p.getPcDataNasc()), p.getPcTelefone(), p.getRua(), p.getBairro(), p.getCidade(), p.getUf(), p.getCep(), p.getPcCpf());
        }
    }

    public boolean deleteById(String cpf) {
        String sql = "delete from pessoa_carente where pc_cpf = ?";
        return conexao.manipular(sql, cpf);
    }
}
