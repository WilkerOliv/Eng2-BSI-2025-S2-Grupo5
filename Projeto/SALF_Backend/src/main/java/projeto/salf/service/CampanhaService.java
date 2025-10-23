package projeto.salf.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto.salf.model.Campanha;
import projeto.salf.repository.CampanhaRepository;

import java.util.List;

@Service
public class CampanhaService {

    private final CampanhaRepository repo;

    public CampanhaService(CampanhaRepository repo) {
        this.repo = repo;
    }

    public List<Campanha> listarTodos() {
        return repo.findAll();
    }

    public Campanha buscarPorId(Integer id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    public Campanha salvar(Campanha c) {
        // regra simples: se totalArrecadado vier null, zera
        if (c.getCampanhaTotalArrecadado() == null) {
            c.setCampanhaTotalArrecadado(0.0);
        }
        return repo.save(c);
    }

    @Transactional
    public void excluir(Integer id) {
        repo.deleteById(id);
    }

    @Transactional
    public Campanha finalizar(Integer id, Double totalArrecadado) {
        Campanha c = repo.findById(id).orElse(null);
        if (c == null) return null;
        c.setCampanhaTotalArrecadado(totalArrecadado != null ? totalArrecadado : 0.0);
        return repo.save(c);
    }
}
