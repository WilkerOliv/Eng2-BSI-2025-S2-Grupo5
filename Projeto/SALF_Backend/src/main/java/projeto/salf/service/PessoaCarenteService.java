package projeto.salf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto.salf.model.PessoaCarente;
import projeto.salf.repository.PessoaCarenteRepository;

import java.util.List;

@Service
public class PessoaCarenteService {
    @Autowired
    private PessoaCarenteRepository repository;

    public List<PessoaCarente> listarTodas() { return repository.findAll(); }
    public PessoaCarente salvar(PessoaCarente p) { return repository.save(p); }
    public void excluir(String cpf) { repository.deleteById(cpf); }
}