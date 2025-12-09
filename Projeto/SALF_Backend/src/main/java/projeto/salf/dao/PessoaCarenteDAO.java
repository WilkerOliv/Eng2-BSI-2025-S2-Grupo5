package projeto.salf.dao;

import projeto.salf.model.PessoaCarente;
import projeto.salf.controller.bd.Conexao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PessoaCarenteDAO {
    private final Conexao conexao;

    public PessoaCarenteDAO(Conexao conexao) {
        this.conexao = conexao;
    }

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
        // ALTERAÇÃO: usar regex correta para remover tudo que não é dígito (\D) + trim, compatível com PostgreSQL
        String sql = "select pc_cpf, pc_nome, pc_data_nasc, pc_telefone, rua, bairro, cidade, uf, cep " +
                "  from pessoa_carente " +
                " where regexp_replace(trim(pc_cpf), '\\\\D', '', 'g') = regexp_replace(trim(?), '\\\\D', '', 'g')";
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
        // ALTERAÇÃO: tratar pc_data_nasc nula para evitar NPE no Date.valueOf(...)
        Date dataNasc = (p.getPcDataNasc() != null) ? Date.valueOf(p.getPcDataNasc()) : null;

        if (findById(p.getPcCpf()) == null) {
            String sql = "insert into pessoa_carente(pc_cpf, pc_nome, pc_data_nasc, pc_telefone, rua, bairro, cidade, uf, cep) values (?,?,?,?,?,?,?,?,?)";
            return conexao.manipular(sql, p.getPcCpf(), p.getPcNome(), dataNasc, p.getPcTelefone(), p.getRua(), p.getBairro(), p.getCidade(), p.getUf(), p.getCep());
        } else {
            String sql = "update pessoa_carente set pc_nome=?, pc_data_nasc=?, pc_telefone=?, rua=?, bairro=?, cidade=?, uf=?, cep=? where pc_cpf=?";
            return conexao.manipular(sql, p.getPcNome(), dataNasc, p.getPcTelefone(), p.getRua(), p.getBairro(), p.getCidade(), p.getUf(), p.getCep(), p.getPcCpf());
        }
    }

    public boolean deleteById(String cpf) {
        String sql = "delete from pessoa_carente where pc_cpf = ?";
        return conexao.manipular(sql, cpf);
    }
}
