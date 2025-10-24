package projeto.salf.service;

import projeto.salf.model.PessoaCarente;
import projeto.salf.dao.PessoaCarenteDAO;

import java.util.List;

public class PessoaCarenteService {
    private final PessoaCarenteDAO dao = new PessoaCarenteDAO();

    public List<PessoaCarente> listarTodas() { return dao.findAll(); }
    public PessoaCarente salvar(PessoaCarente p) {
        dao.save(p);
        return p;
    }
    public void excluir(String cpf) { dao.deleteById(cpf); }
}
