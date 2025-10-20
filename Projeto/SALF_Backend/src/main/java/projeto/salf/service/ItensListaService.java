package projeto.salf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto.salf.model.ItensDaLista;
import projeto.salf.model.ItensDaListaId;
import projeto.salf.repository.ItensDaListaRepository;

import java.util.List;

@Service
public class ItensListaService {
    @Autowired
    private ItensDaListaRepository repository;

    public List<ItensDaLista> listarTodos() { return repository.findAll(); }
    public ItensDaLista salvar(ItensDaLista i) { return repository.save(i); }
    public void excluir(ItensDaListaId id) { repository.deleteById(id); }
}