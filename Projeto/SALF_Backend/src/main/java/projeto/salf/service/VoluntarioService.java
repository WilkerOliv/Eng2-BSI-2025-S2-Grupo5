package projeto.salf.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto.salf.model.Voluntario;
import projeto.salf.repository.VoluntarioRepository;

import java.util.List;

@Service
public class VoluntarioService {

    private final VoluntarioRepository repo;

    public VoluntarioService(VoluntarioRepository repo) {
        this.repo = repo;
    }

    public List<Voluntario> listarTodos() {
        return repo.findAll();
    }

    public Voluntario buscarPorCpf(String cpf) {
        return repo.findById(cpf).orElse(null);
    }

    @Transactional
    public Voluntario salvar(Voluntario v) {
        return repo.save(v);
    }

    @Transactional
    public void excluir(String cpf) {
        repo.deleteById(cpf);
    }
}
