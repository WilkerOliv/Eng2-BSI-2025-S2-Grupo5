package projeto.salf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto.salf.model.NecessidadeCesta;
import projeto.salf.model.NecessidadeCestaId;
import projeto.salf.repository.NecessidadeCestaRepository;

import java.util.List;

@Service
public class NecessidadeCestaService {
    @Autowired
    private NecessidadeCestaRepository repository;

    public List<NecessidadeCesta> listarTodas() { return repository.findAll(); }
    public NecessidadeCesta salvar(NecessidadeCesta n) { return repository.save(n); }
    public void excluir(NecessidadeCestaId id) { repository.deleteById(id); }
}