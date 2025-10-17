package projeto.salf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto.salf.model.ItensLista;
import projeto.salf.model.ItensListaId;
import projeto.salf.repository.ItensListaRepository;

import java.util.List;

@Service
public class ItensListaService {
    @Autowired
    private ItensListaRepository repository;

    public List<ItensLista> listarTodos() { return repository.findAll(); }
    public ItensLista salvar(ItensLista i) { return repository.save(i); }
    public void excluir(ItensListaId id) { repository.deleteById(id); }
}